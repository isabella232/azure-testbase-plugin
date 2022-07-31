package io.jenkins.plugins.azuretestbase.dm;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.jenkins.plugins.azuretestbase.dm.emuntype.TestType;
import io.jenkins.plugins.azuretestbase.dm.emuntype.ValidationRunResult;
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
@JsonFilter("TestBaseTest")
public class TestBaseTest {
    // mandatory
    @JsonInclude(value = Include.NON_NULL)
    private TestType testType;
    @JsonInclude(value = Include.NON_NULL)
    private List<Command> commands;

    // optional
    @JsonInclude(value = Include.NON_NULL)
    private Boolean isActive;

    // the fields defined below are only used for the response data when putting a package
    @JsonInclude(value = Include.NON_NULL)
    private ValidationRunResult validationRunStatus;
    @JsonInclude(value = Include.NON_NULL)
    private String validationResultId;
}
