package com.bookstore.security;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * This customises the exceptions for REST responses
 *
 * @author Edson Cruz
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * If the Exception is annotated by {@link ResponseStatus} this will create the response based on
     * annotation values. Otherwise, the response will be <code>BAD_REQUEST</code> and data from his own Exception
     *
     * @param ex error
     * @return ProblemDetail customised
     */
    @ExceptionHandler
    public ProblemDetail handleSecurityException(Exception ex) {

        if (ex.getClass().isAnnotationPresent(ResponseStatus.class)) {
            HttpStatus httpStatus = ex.getClass().getAnnotation(ResponseStatus.class).value();
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, ex.getMessage());
            problemDetail.setTitle(httpStatus.getReasonPhrase());
            problemDetail.setType(URI.create(ex.getClass().getSimpleName()));

            return problemDetail;
        } else if (ex instanceof ConstraintViolationException constraintViolationException) {
            StringBuilder errors = new StringBuilder();

            constraintViolationException.getConstraintViolations().forEach((e) -> errors.append(e.getMessage()).append(" | "));

            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors.toString());
            problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
            problemDetail.setType(URI.create(ex.getClass().getSimpleName()));

            return problemDetail;
        } else {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
            problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
            problemDetail.setType(URI.create(ex.getClass().getSimpleName()));

            return problemDetail;
        }
    }
}