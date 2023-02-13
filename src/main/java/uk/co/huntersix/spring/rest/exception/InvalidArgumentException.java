package uk.co.huntersix.spring.rest.exception;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String msg) {
        super(msg);
    }
}
