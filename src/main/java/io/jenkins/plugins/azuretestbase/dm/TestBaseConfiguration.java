package io.jenkins.plugins.azuretestbase.dm;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
@ToString
@JsonFilter("TestBaseConfiguration")
public class TestBaseConfiguration {
    // all are mandatory
    private String tenantId;
    private String clientId;
    private String clientSecret;
    private String subscriptionId;
    private String resourceGroup;
    private String testBaseAccount;
    private String artifact;
    @JsonProperty("package")
    private Package packageProp;
}
