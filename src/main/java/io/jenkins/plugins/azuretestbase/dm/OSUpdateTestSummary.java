package io.jenkins.plugins.azuretestbase.dm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.jenkins.plugins.azuretestbase.dm.emuntype.ExecutionStatus;
import io.jenkins.plugins.azuretestbase.dm.emuntype.Grade;
import io.jenkins.plugins.azuretestbase.dm.emuntype.TestStatus;
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
public class OSUpdateTestSummary {
    private String buildRevision;
    private String buildVersion;
    private ExecutionStatus executionStatus;
    private String flightingRing;
    private Grade grade;
    private String osName;
    private String releaseName;
    private String releaseVersionDate;
    private String testRunTime;
    private TestStatus testStatus;
    private String testType;
}
