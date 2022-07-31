package io.jenkins.plugins.azuretestbase.dm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.jenkins.plugins.azuretestbase.dm.emuntype.ActionType;
import io.jenkins.plugins.azuretestbase.dm.emuntype.ContentType;
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
public class Command {
    // mandatory
    @JsonInclude(value = Include.NON_NULL)
    private String name;
    @JsonInclude(value = Include.NON_NULL)
    private ActionType action;
    @JsonInclude(value = Include.NON_NULL)
    private ContentType contentType;
    @JsonInclude(value = Include.NON_NULL)
    private String content;

    // optional
    @JsonInclude(value = Include.NON_NULL)
    private Boolean runElevated;
    @JsonInclude(value = Include.NON_NULL)
    private Boolean restartAfter;
    @JsonInclude(value = Include.NON_NULL)
    private Integer maxRunTime;
    @JsonInclude(value = Include.NON_NULL)
    private Boolean runAsInteractive;
    @JsonInclude(value = Include.NON_NULL)
    private Boolean alwaysRun;
    @JsonInclude(value = Include.NON_NULL)
    private Boolean applyUpdateBefore;
}
