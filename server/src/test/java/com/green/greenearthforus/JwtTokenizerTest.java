package com.green.greenearthforus;

import com.green.greenearthforus.login.jwttoken.JwtTokenizer;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtTokenizerTest {
    private static JwtTokenizer jwtTokenizer;
    private String secretKey;
    private String base64EncodedSecretKey;

    @BeforeAll
    public void init(){
        jwtTokenizer = new JwtTokenizer();
        secretKey = "testKey1234123412341234123412341234";
        base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(secretKey);
    }

    @Test
    public void encodeBase64SecretKeyTest(){
        System.out.println(base64EncodedSecretKey);
        assertThat(secretKey, is(new String(Decoders.BASE64.decode(base64EncodedSecretKey))));
    }

    @Test
    public void generateAccessTokenTest(){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1);
        claims.put("roles", List.of("USER"));

        String subject = "test access token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        Date expiration = calendar.getTime();

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        System.out.println(accessToken);
        assertThat(accessToken, notNullValue());
    }

    @Test
    public void generateRefreshTokenTest(){
        String subject = "test refresh token";
        Calendar calendar = Calendar.getInstance();
         calendar.add(Calendar.HOUR,24);
         Date expiration = calendar.getTime();

         String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        System.out.println(refreshToken);

        assertThat(refreshToken, notNullValue());
    }

    @DisplayName("dose not throw any Exception when jws verify")
    @Test
    public void verifySignatureTest(){
        String accessToken = getAccessToekn(Calendar.MINUTE,10);
        assertDoesNotThrow(()-> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));
    }

    @DisplayName("throw expiredJwtException when jws verify")
    @Test
    public void verifyExpirationTest() throws InterruptedException{
        String accessToken = getAccessToekn(Calendar.SECOND, 1);
        assertDoesNotThrow(() -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));

        TimeUnit.MILLISECONDS.sleep(1500);

        assertThrows(ExpiredJwtException.class, () -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));
    }

    @Test
    public void getClaimsTest(){
        String accessToken = getAccessToekn(Calendar.MINUTE, 10);
        Map<String, Object> claims = jwtTokenizer.getClaims(accessToken, base64EncodedSecretKey).getBody();

        assertThat(claims.get("userId"), is(1));
        assertThat(((List)claims.get("roles")).get(0), is("USER"));
    }


    private String getAccessToekn(int timeUnit, int timeAmount){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1);
        claims.put("roles", List.of("USER"));

        String subject = "test access token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit, timeAmount);
        Date expiration = calendar.getTime();
        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }
}
