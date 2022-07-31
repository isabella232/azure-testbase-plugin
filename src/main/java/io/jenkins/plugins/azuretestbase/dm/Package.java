package io.jenkins.plugins.azuretestbase.dm;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFilter;
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
@JsonFilter("Package")
public class Package {
    // mandatory
    @JsonInclude(value = Include.NON_NULL)
    private String location;
    @JsonInclude(value = Include.NON_NULL)
    private Properties properties;

    // optional
    @JsonInclude(value = Include.NON_NULL)
    private Map<String, String> tags;

    // the fields defined below are only used for the response data when putting a package
    @JsonInclude(value = Include.NON_NULL)
    private String id;
    @JsonInclude(value = Include.NON_NULL)
    private String name;
    @JsonInclude(value = Include.NON_NULL)
    private String type;
    @JsonInclude(value = Include.NON_NULL)
    private String etag;
    @JsonInclude(value = Include.NON_NULL)
    private SystemData systemData;
}
