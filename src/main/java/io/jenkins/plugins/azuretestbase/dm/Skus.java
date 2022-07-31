package io.jenkins.plugins.azuretestbase.dm;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.jenkins.plugins.azuretestbase.dm.emuntype.Tier;
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
public class Skus {
    private String resourceType;
    private String name;
    private Tier tier;
    private List<Capability> capabilities;
    private List<String> locations;
}
