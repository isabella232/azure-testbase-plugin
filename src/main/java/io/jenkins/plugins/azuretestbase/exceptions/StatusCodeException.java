package io.jenkins.plugins.azuretestbase.exceptions;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.jenkins.plugins.azuretestbase.dm.ErrorContent;
import lombok.Getter;

public class StatusCodeException extends TestBaseException {
    @Getter
    private int statusCode;
    @Getter
    private ErrorContent errorContent;
    @Getter
    private String errorMessage;

    
    // using statusCode and ErrorContent
    public StatusCodeException(int statusCode, @NonNull ErrorContent errorContent) {
        super();
        this.statusCode = statusCode;
        this.errorContent = errorContent;
    }


    // using statusCode and errorMessage
    public StatusCodeException(int statusCode, @NonNull String errorMessage) {
        super();
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }


    // only using statusCode
    public StatusCodeException(int statusCode) {
        super();
        this.statusCode = statusCode;
    }


    // using statusCode and ErrorContent and cause
    public StatusCodeException(int statusCode, @NonNull ErrorContent errorContent, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
        this.errorContent = errorContent;
    }


    // using statusCode and errorMessage and cause
    public StatusCodeException(int statusCode, @NonNull String errorMessage, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }


    // using statusCode and cause
    public StatusCodeException(int statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }


    @Override
    @NonNull
    public String toString() {
        if(errorContent != null)
            return String.format("statusCode = %s, errorContent = %s", 
                statusCode, errorContent.toString());
        if(errorMessage != null)
            return String.format("statusCode = %s, errorMessage = %s", errorMessage);
        return String.format("statusCode = %s", statusCode);
    }
}
