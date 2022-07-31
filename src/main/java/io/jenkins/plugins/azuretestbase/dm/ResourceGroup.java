package io.jenkins.plugins.azuretestbase.dm;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class ResourceGroup {
    private String id;
    private String location;
    private String managedBy;
    private String name;
    private ResourceGroupProperties properties;
    private Map<String, String> tags;
    private String type;
}
