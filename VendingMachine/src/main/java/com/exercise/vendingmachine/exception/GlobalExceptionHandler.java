package com.exercise.vendingmachine.exception;

import com.exercise.vendingmachine.config.FilterConfig;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorObject> entityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        ErrorObject message = new ErrorObject(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        LOG.error(ex.getMessage());
        return new ResponseEntity<ErrorObject>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorObject> accessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorObject message = new ErrorObject(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        LOG.error(ex.getMessage());
        return new ResponseEntity<ErrorObject>(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorObject> usernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ErrorObject message = new ErrorObject(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        LOG.error(ex.getMessage());
        return new ResponseEntity<ErrorObject>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorObject message = new ErrorObject(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        LOG.error(ex.getMessage());
        return new ResponseEntity<ErrorObject>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
