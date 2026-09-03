package dev.chari.playtestlog.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
        ResourceNotFoundException ex
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse.of(404, "Not Found", ex.getMessage())
        );
    }

    @ExceptionHandler(SessionClosedException.class)
    public ResponseEntity<ErrorResponse> handleSessionClosed(
        SessionClosedException ex
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse.of(409, "Conflict", ex.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult()
            .getFieldErrors()
            .forEach(fe ->
                fieldErrors.put(fe.getField(), fe.getDefaultMessage())
            );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse.validationFailure(fieldErrors)
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
        MethodArgumentTypeMismatchException ex
    ) {
        String message =
            "Invalid value '" +
            ex.getValue() +
            "' for parameter '" +
            ex.getName() +
            "'";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse.of(400, "Bad Request", message)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse.of(
                500,
                "Internal Server Error",
                "An unexpected error occurred"
            )
        );
    }
}
