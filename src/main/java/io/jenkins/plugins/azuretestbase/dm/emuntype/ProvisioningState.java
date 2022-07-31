package io.jenkins.plugins.azuretestbase.dm.emuntype;

public enum ProvisioningState {
    Cancelled,
    Creating,
    Deleting,
    Failed,
    Succeeded,
    Updating,
    // This value is not listed in the document of ProvisioningState model, but it may be included in response json.
    Accepted;
}
