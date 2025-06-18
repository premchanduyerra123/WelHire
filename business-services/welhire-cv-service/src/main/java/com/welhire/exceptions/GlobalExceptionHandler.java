package com.welhire.exceptions;


import com.welhire.shared.dto.wrapper.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ParsedCvNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleParsedCvNotFound(ParsedCvNotFoundException ex) {
        ErrorResponse body = new ErrorResponse(
                ex.getMessage(),
                ex.getCode(),
                null                           // details (stack trace, etc.) – omit or fill if needed
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /* --- Optional: catch-all for uncaught RuntimeExceptions --- */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        ErrorResponse body = new ErrorResponse(
                "Internal server error",
                "INTERNAL_ERROR",
                ex.getMessage()               // DON’T expose stack traces in prod
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}

