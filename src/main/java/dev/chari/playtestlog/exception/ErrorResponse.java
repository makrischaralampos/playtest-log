package dev.chari.playtestlog.exception;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
    Instant timestamp,
    int status,
    String error,
    String message,
    List<FieldError> fieldErrors
) {
    public record FieldError(String field, String message) {}

    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(Instant.now(), status, error, message, null);
    }

    public static ErrorResponse validationFailure(
        Map<String, String> fieldErrors
    ) {
        List<FieldError> errors = fieldErrors
            .entrySet()
            .stream()
            .map(e -> new FieldError(e.getKey(), e.getValue()))
            .toList();
        return new ErrorResponse(
            Instant.now(),
            400,
            "Bad Request",
            "Validation failed",
            errors
        );
    }
}
