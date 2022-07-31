package io.jenkins.plugins.azuretestbase.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AzureManagementResult<T> {
    private int statusCode;
    private T entity;
}
