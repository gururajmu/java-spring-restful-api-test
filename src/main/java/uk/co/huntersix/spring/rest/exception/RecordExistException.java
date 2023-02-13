package uk.co.huntersix.spring.rest.exception;

public class RecordExistException extends RuntimeException {
    public RecordExistException(String msg) {
        super(msg);
    }
}
