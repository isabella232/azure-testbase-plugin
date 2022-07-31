package io.jenkins.plugins.azuretestbase.exceptions;

import org.apache.http.Header;

import edu.umd.cs.findbugs.annotations.NonNull;
import lombok.Getter;

public class HeaderException extends TestBaseException {
    @Getter
    private Header actualHeader;
    @Getter
    private String idealHeaderValue;
    @Getter
    private String responseContent;
    @Getter
    private int statusCode;


    // using Header and content and cause
    public HeaderException(@NonNull int statusCode, Header actualHeader, @NonNull String idealHeaderValue, String responseContent, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
        this.actualHeader = actualHeader;
        this.idealHeaderValue = idealHeaderValue;
        this.responseContent = responseContent;
    }


    // using Header and content
    public HeaderException(@NonNull int statusCode, Header actualHeader, @NonNull String idealHeaderValue, String responseContent) {
        super();
        this.statusCode = statusCode;
        this.actualHeader = actualHeader;
        this.idealHeaderValue = idealHeaderValue;
        this.responseContent = responseContent;
    }


    // using Header and cause
    public HeaderException(@NonNull int statusCode, Header actualHeader, @NonNull String idealHeaderValue, Throwable cause) {
        this(statusCode, actualHeader, idealHeaderValue, null, cause);
    }


    // using Header
    public HeaderException(@NonNull int statusCode, Header actualHeader, @NonNull String idealHeaderValue) {
        this(statusCode, actualHeader, idealHeaderValue, (String)null);
    }


    @Override
    public String toString() {
        if(actualHeader == null)
            return String.format("statusCode = %d, idealHeaderValue is %s, but actual headerValue is null. response content = %s", 
                statusCode,
                idealHeaderValue,
                responseContent);
        return String.format("statusCode = %d, idealHeaderValue of %s is %s, but headerValue header is %s. response content = %s", 
            statusCode,
            idealHeaderValue, 
            actualHeader.getName(), 
            actualHeader.getValue(),
            responseContent);
    }
}
