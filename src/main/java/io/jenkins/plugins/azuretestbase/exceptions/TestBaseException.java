package io.jenkins.plugins.azuretestbase.exceptions;

// The root of custom exceptions
public class TestBaseException extends RuntimeException {
    // fixed constructor
    public TestBaseException() {
        super();
    }

    public TestBaseException(String message) {
        super(message);
    }

    public TestBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestBaseException(Throwable cause) {
        super(cause);
    }
}
