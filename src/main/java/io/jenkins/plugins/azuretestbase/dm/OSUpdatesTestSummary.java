package io.jenkins.plugins.azuretestbase.dm;

import java.util.List;

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
public class OSUpdatesTestSummary {
    private ExecutionStatus executionStatus;
    private Grade grade;
    private List<OSUpdateTestSummary> osUpdateTestSummaries;
    private String testRunTime;
    private TestStatus testStatus;
}
