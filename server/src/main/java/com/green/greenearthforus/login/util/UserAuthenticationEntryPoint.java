package com.green.greenearthforus.login.util;

import com.green.greenearthforus.login.error.ErrorResponder;
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
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException{
            Exception exception = (Exception) request.getAttribute("exception");
            ErrorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED);

            logExceptionMessage(authenticationException, exception);
    }

    private void logExceptionMessage(AuthenticationException authException, Exception exception){
        String message = exception != null ? exception.getMessage() : authException.getMessage();
        log.warn("Unauthorized error happend: {}", message);
    }

}
