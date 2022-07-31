package io.jenkins.plugins.azuretestbase.dm;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class TargetOS {
    // mandatory
    @JsonInclude(value = Include.NON_NULL)
    private String osUpdateType;
    @JsonInclude(value = Include.NON_NULL)
    private List<String> targetOSs;
    
    // optional
    @JsonInclude(value = Include.NON_NULL)
    private List<String> baselineOSs;
}
