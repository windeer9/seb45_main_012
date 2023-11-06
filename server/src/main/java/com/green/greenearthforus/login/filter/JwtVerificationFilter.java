package com.green.greenearthforus.login.filter;

import com.green.greenearthforus.login.util.CustomAuthorityUtils;
import com.green.greenearthforus.login.jwttoken.JwtTokenizer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.Authentication;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import io.jsonwebtoken.Jwts;

public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer,
                                 CustomAuthorityUtils customAuthorityUtils){
        this.authorityUtils = customAuthorityUtils;
        this.jwtTokenizer = jwtTokenizer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try{
            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims, request, response);
        } catch(Exception e){
//            request.setAttribute("exception", e);
            if(isAccessTokenExpired(request)) {
                if (request.getHeader("Refresh") != null && !request.getHeader("Refresh").isEmpty()) {
                    if (!isRefreshTokenExpired(request)) {
                        // refresh토큰으로 만료된 access토큰을 재발급하는 로직
                        String jws = request.getHeader("Refresh");
                        String base64EncodedSecretKey = jwtTokenizer.key();
                        Claims accessClaims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
                        String neoAccessToken = generateNewAccessTokenUsingRefreshToken(request.getHeader("Refresh"), base64EncodedSecretKey, accessClaims);
                        if (neoAccessToken != null) {
                            response.setHeader("Authorization", "Bearer " + neoAccessToken);
                        }
                    }
                }
            }
        }



        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return authorization == null || !authorization.startsWith("Bearer ");
    }

    private Map<String, Object> verifyJws(HttpServletRequest request){
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

//        String jws = "";
//        String refresh = "";

//        if(request.getHeader("Authorization") != null && !request.getHeader("Authorization").isEmpty()) {
//            jws = request.getHeader("Authorization").replace("Bearer ", "");
//            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
//            String base64EncodedSecretKey = jwtTokenizer.key();


//            if (isAccessTokenExpired(request)) {
//                if (request.getHeader("Refresh") != null && !request.getHeader("Refresh").isEmpty()) {
//                    if (isRefreshTokenExpired(request)) {
//                        Claims accessClaims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
//                        jws = jwtTokenizer.generateAccessToken(accessClaims, accessClaims.getSubject(), jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes()), base64EncodedSecretKey);
//
//                        refresh = jwtTokenizer.generateRefreshToken(accessClaims.getSubject(), jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes()), base64EncodedSecretKey);
//                        response.setHeader("Authorization", "Bearer " + jws);
//                        response.setHeader("Refresh", refresh);
//                    } else {
//                        // refresh토큰으로 만료된 access토큰을 재발급하는 로직
//                        Claims accessClaims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
//                        String neoAccessToken = generateNewAccessTokenUsingRefreshToken(request.getHeader("Refresh"), base64EncodedSecretKey, accessClaims);
//                        if (neoAccessToken != null) {
//                            response.setHeader("Authorization", "Bearer " + neoAccessToken);
//                        }
//                    }
//                } else {
//                   Claims accessClaims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
//                    jws = jwtTokenizer.generateAccessToken(accessClaims, accessClaims.getSubject(), jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes()), base64EncodedSecretKey);
//                    refresh = jwtTokenizer.generateRefreshToken(accessClaims.getSubject(), jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes()), base64EncodedSecretKey);
//                    response.setHeader("Authorization", "Bearer " + jws);
//                    response.setHeader("Refresh", refresh);
//                }
//            }
//        }

        return jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
    }

    private void setAuthenticationToContext(Map<String, Object> claims, HttpServletRequest request, HttpServletResponse response){
        String userId= (String) claims.get("userUseId");
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities(List.of((String) claims.get("roles")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    public boolean isAccessTokenExpired(HttpServletRequest request){
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        long expirationTime = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody().getExpiration().getTime();
        long currentTime = System.currentTimeMillis();

        return expirationTime < currentTime;
    }

    public boolean isRefreshTokenExpired(HttpServletRequest request){
        String jws = request.getHeader("Refresh");
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
