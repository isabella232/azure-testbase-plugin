package io.jenkins.plugins.azuretestbase.dm;

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
public class TestSummaryResource {
    private String id;
    private String name;
    private String type;
    private SystemData systemData;
    private TestSummaryProperties properties;
}
