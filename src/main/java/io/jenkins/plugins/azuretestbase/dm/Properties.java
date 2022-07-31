package io.jenkins.plugins.azuretestbase.dm;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.jenkins.plugins.azuretestbase.dm.emuntype.PackageStatus;
import io.jenkins.plugins.azuretestbase.dm.emuntype.ProvisioningState;
import io.jenkins.plugins.azuretestbase.dm.emuntype.TestType;
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
@JsonFilter("Properties")
public class Properties {
    // mandatory
    @JsonInclude(value = Include.NON_NULL)
    private String applicationName;
    @JsonInclude(value = Include.NON_NULL)
    private String version;
    @JsonInclude(value = Include.NON_NULL)
    private List<TargetOS> targetOSList;
    @JsonInclude(value = Include.NON_NULL)
    private String flightingRing;
    @JsonInclude(value = Include.NON_NULL)
    private String blobPath;
    @JsonInclude(value = Include.NON_NULL)
    private List<TestBaseTest> tests;

    // the fields defined below are only used for the response data when putting a package
    @JsonInclude(value = Include.NON_NULL)
    private ProvisioningState provisioningState;
    @JsonInclude(value = Include.NON_NULL)
    private List<TestType> testTypes;
    @JsonInclude(value = Include.NON_NULL)
    private PackageStatus packageStatus;
    // a timestamp
    @JsonInclude(value = Include.NON_NULL)
    private String lastModifiedTime;
    @JsonInclude(value = Include.NON_NULL)
    private Boolean isEnabled;
    @JsonInclude(value = Include.NON_NULL)
    private List<ValidationResult> validationResults;
}
