package dev.chari.playtestlog.exception;

public class SessionClosedException extends RuntimeException {

    public SessionClosedException(String message) {
        super(message);
    }
}
