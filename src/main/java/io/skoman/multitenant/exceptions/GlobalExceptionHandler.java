package io.skoman.multitenant.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception){

        ProblemDetail errorDetail = null;

        // TODO send this stack trace to an observability tool
        exception.printStackTrace();

        switch (exception) {
            case BadCredentialsException ex -> {
                errorDetail = getProblemDetail(ex, 401, "The username or password is incorrect");
            }
            case AccountStatusException ex -> {
                errorDetail = getProblemDetail(ex, 403, "The account is locked");
            }
            case AccessDeniedException ex -> {
                errorDetail = getProblemDetail(ex, 403, "You are not authorized to access this resource");
            }
            case SignatureException ex -> {
                errorDetail = getProblemDetail(ex, 403, "The JWT signature is invalid");
            }
            case ExpiredJwtException ex -> {
                errorDetail = getProblemDetail(ex, 403, "The JWT token has expired");
            }
            default -> {
                errorDetail = getProblemDetail(exception, 500, "Unknown internal server error");
            }
        }

        return errorDetail;
    }

    private static ProblemDetail getProblemDetail(Exception ex, int codeError, String message) {
        final String PROPERTY_INDEX = "description";
        ProblemDetail errorDetail;
        errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(codeError), ex.getMessage());
        errorDetail.setProperty(PROPERTY_INDEX, message);
        return errorDetail;
    }
}
