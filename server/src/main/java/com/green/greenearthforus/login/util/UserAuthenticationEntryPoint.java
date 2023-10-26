package com.green.greenearthforus.login.util;

import com.green.greenearthforus.login.error.ErrorResponder;
import com.green.greenearthforus.login.jwttoken.JwtTokenizer;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private JwtTokenizer jwtTokenizer;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException{
            Exception exception = (Exception) request.getAttribute("exception");

        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        if(isAccessTokenExpired(request)){
            if(request.getHeader("Refresh") != null && !request.getHeader("Refresh").isEmpty()){
                if(isRefreshTokenExpired(request)) {
                    Claims accessClaims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
                    jws = jwtTokenizer.generateAccessToken(accessClaims, accessClaims.getSubject(), jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes()), base64EncodedSecretKey);
                    String refresh = jwtTokenizer.generateRefreshToken(accessClaims.getSubject(), jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes()), base64EncodedSecretKey);
                    response.setHeader("Authorization", "Bearer " + jws);
                    response.setHeader("Refresh", refresh);
                }else{
                    // refresh토큰으로 만료된 access토큰을 재발급하는 로직
                    Claims accessClaims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
                    String neoAccessToken = generateNewAccessTokenUsingRefreshToken(request.getHeader("Refresh"), base64EncodedSecretKey, accessClaims);
                    if (neoAccessToken != null) {
                        response.setHeader("Authorization", "Bearer " + neoAccessToken);
                    }
                }
            } else{
                Claims accessClaims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
                jws = jwtTokenizer.generateAccessToken(accessClaims, accessClaims.getSubject(), jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes()), base64EncodedSecretKey);
                String refresh = jwtTokenizer.generateRefreshToken(accessClaims.getSubject(), jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes()), base64EncodedSecretKey);
                response.setHeader("Authorization", "Bearer "+jws);
                response.setHeader("Refresh", refresh);
            }
        }

        ErrorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED);
            logExceptionMessage(authenticationException, exception);
    }

    private void logExceptionMessage(AuthenticationException authException, Exception exception){
        String message = exception != null ? exception.getMessage() : authException.getMessage();
        log.warn("Unauthorized error happend: {}", message + " contain new token.");
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
