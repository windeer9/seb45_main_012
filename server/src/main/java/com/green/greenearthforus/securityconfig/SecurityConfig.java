package com.green.greenearthforus.securityconfig;

import com.green.greenearthforus.exception.BusinessLogicException;
import com.green.greenearthforus.exception.ExceptionCode;
import com.green.greenearthforus.login.handler.UserAccessDeniedHandler;
import com.green.greenearthforus.login.util.UserAuthenticationEntryPoint;
import com.green.greenearthforus.login.util.CustomAuthorityUtils;
import com.green.greenearthforus.login.filter.JwtAuthenticationFilter;
import com.green.greenearthforus.login.jwttoken.JwtTokenizer;
import com.green.greenearthforus.login.filter.JwtVerificationFilter;
import com.green.greenearthforus.user.controller.UserController;
import com.green.greenearthforus.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig{
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;

    private final UserRepository userRepository;

    public SecurityConfig(JwtTokenizer jwtTokenizer,
                          CustomAuthorityUtils authorityUtils,
                          UserRepository userRepository){
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new UserAuthenticationEntryPoint(jwtTokenizer))
                .accessDeniedHandler(new UserAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                                .antMatchers("/auth/login").permitAll()
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/user").permitAll()
//                        .antMatchers(HttpMethod.GET, "/user/**").hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.PATCH, "/user/**").hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.DELETE, "/user/**").hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.POST, "/post/**").hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.PATCH, "/post/**").hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.DELETE, "/post/**").hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.POST, "/comment/**").hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.PATCH, "/comment/**").hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.DELETE, "/comment/**").hasAnyRole("ADMIN", "USER")
                        .antMatchers("/vote/**").hasAnyRole("ADMIN", "USER")
                        .antMatchers("/calendar/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://greenearthforus.s3-website.ap-northeast-2.amazonaws.com"));
        configuration.setAllowedOriginPatterns(List.of("http://greenearthforus.**"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept"));
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Refresh");
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Refresh");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity>{
        @Override
        public void configure(HttpSecurity builder){
            AuthenticationManager aUthenticationManager = builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter =
                    new JwtAuthenticationFilter(aUthenticationManager, jwtTokenizer);
            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils, userRepository, jwtAuthenticationFilter);

            builder.addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
        }
    }

    public String regenerateAccessTokenUsingRefreshToken(HttpServletRequest request){
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        if(isAccessTokenExpired(request)){
            if(request.getHeader("Refresh") != null && !request.getHeader("Refresh").isEmpty()){
                // refresh토큰으로 만료된 access토큰을 재발급하는 로직
                Claims claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
                String neoAccessToken = generateNewAccessTokenUsingRefreshToken(request.getHeader("Refresh"), base64EncodedSecretKey, claims);
                if(neoAccessToken != null){
                    return neoAccessToken;
                }
            } else{
                Claims claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
                return jwtTokenizer.generateAccessToken(claims, claims.getSubject(), jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes()), base64EncodedSecretKey);
            }
        }
        return new BusinessLogicException(ExceptionCode.USER_FORBIDDEN).toString();
    }


    public boolean isAccessTokenExpired(HttpServletRequest request){
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        long expirationTime = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody().getExpiration().getTime();
        long currentTime = System.currentTimeMillis();

        return expirationTime < currentTime;
    }

    public String generateNewAccessTokenUsingRefreshToken(String refreshToken, String key, Claims accessTokenClaims) {
//        Key currentKey = jwtTokenizer.getKeyFromBase64EncodedKey(key);
//
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(currentKey)
//                .build()
//                .parseClaimsJws(refreshToken)
//                .getBody();
//
//        String username = claims.getSubject();
        String subject = jwtTokenizer.getClaims(refreshToken, key).getBody().getSubject();

        return jwtTokenizer.generateAccessToken(accessTokenClaims, subject, jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes()), key);
    }

}
