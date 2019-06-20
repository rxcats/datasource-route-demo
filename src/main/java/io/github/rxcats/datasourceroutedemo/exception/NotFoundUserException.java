package io.github.rxcats.datasourceroutedemo.exception;

public class NotFoundUserException extends RuntimeException {
    private static final long serialVersionUID = 8705654570407443081L;

    public NotFoundUserException(String message) {
        super(message);
    }
}
