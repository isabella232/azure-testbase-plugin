package io.jenkins.plugins.azuretestbase.dm.emuntype;

public enum PackageStatus {
    Deleted, 
    Error, 
    PreValidationCheckPass,
    Ready,
    Registered,
    Unknown,
    ValidatingPackage,
    ValidationLongerThanUsual,
    VerifyingPackage;
}
