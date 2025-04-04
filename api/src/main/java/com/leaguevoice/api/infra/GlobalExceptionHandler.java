package com.leaguevoice.api.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Value("${api.error.include-exception}")
    private boolean printStackTrace;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request){
        final String errorMessage = "Unknown Error occured";
        log.error(errorMessage, exception);

        return buildErrorResponse(
                exception,
                errorMessage,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus status,
            WebRequest request
    ){
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);

        if(this.printStackTrace){
            errorResponse.setStackTrace(Arrays.toString(exception.getStackTrace()));
        }

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(Exception exception, WebRequest request){
        final String errorMessage = "Not found exception";
        log.error(errorMessage, exception);

        return buildErrorResponse(
                exception,
                errorMessage,
                HttpStatus.NOT_FOUND,
                request
        );
    }
}
