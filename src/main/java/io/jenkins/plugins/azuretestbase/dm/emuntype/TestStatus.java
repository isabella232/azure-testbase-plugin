package io.jenkins.plugins.azuretestbase.dm.emuntype;

public enum TestStatus {
    None, 
    TestExecutionInProgress, 
    DataProcessing, 
    TestFailure, 
    UpdateFailure, 
    TestAndUpdateFailure, 
    InfrastructureFailure, 
    Completed
}
