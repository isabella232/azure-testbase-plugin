package io.jenkins.plugins.azuretestbase.dm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.jenkins.plugins.azuretestbase.dm.emuntype.CreatedByType;
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
public class SystemData {
    private String createdBy;
    private CreatedByType createdByType;
    // a timestamp
    private String createdAt;
    private String lastModifiedBy;
    private CreatedByType lastModifiedByType;
    // a timestamp
    private String lastModifiedAt;
}
