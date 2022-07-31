package io.jenkins.plugins.azuretestbase.dm;

import java.util.Map;

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
public class TestSummaryProperties {
    private String testSummaryId;
    private String packageId;
    private String applicationName;
    private String applicationVersion;
    private ExecutionStatus executionStatus;
    private OSUpdatesTestSummary featureUpdatesTestSummary;
    private Grade grade;
    private OSUpdatesTestSummary securityUpdatesTestSummary;
    private String testRunTime;
    private TestStatus testStatus;
    private Map<String, String> packageTags;
}
