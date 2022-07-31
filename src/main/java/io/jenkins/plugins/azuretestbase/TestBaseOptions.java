package io.jenkins.plugins.azuretestbase;

import java.io.Serializable;
import java.util.List;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import lombok.ToString;

@ToString
public class TestBaseOptions  extends AbstractDescribableImpl<TestBaseOptions> implements Serializable {

    // service principal
    private String tenantId;
    private String clientId;

    // comstom options
    private String subscriptionId;
    private String resourceGroup;
    private String testBaseAccount;
    private String artifact;
    private String applicationName;
    private String version;

    // out of box test
    private boolean outOfBoxTest;
    private String installScript;
    private String launchScript;
    private String closeScript;
    private String uninstallScript;
    private boolean restartAfterInstall;

    // functional test
    private boolean functionalTest;
    private List<FunctionalTest> functionalTestList;

    // security updates
    private boolean securityUpdates;
    private List<String> securityUpdatesTargetOSs;

    // feature updates
    private boolean featureUpdates;
    private String flightingRing;
    private List<String> featureUpdatesTargetOSs;
    private List<String> featureUpdatesBaselineOSs;


    @DataBoundConstructor
    public TestBaseOptions() {}

    @DataBoundSetter
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    @DataBoundSetter
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    @DataBoundSetter
    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
    @DataBoundSetter
    public void setResourceGroup(String resourceGroup) {
        this.resourceGroup = resourceGroup;
    }
    @DataBoundSetter
    public void setTestBaseAccount(String testBaseAccount) {
        this.testBaseAccount = testBaseAccount;
    }
    @DataBoundSetter
    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }
    @DataBoundSetter
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
    @DataBoundSetter
    public void setVersion(String version) {
        this.version = version;
    }
    @DataBoundSetter
    public void setOutOfBoxTest(boolean outOfBoxTest) {
        this.outOfBoxTest = outOfBoxTest;
    }
    @DataBoundSetter
    public void setInstallScript(String installScript) {
        this.installScript = installScript;
    }
    @DataBoundSetter
    public void setLaunchScript(String launchScript) {
        this.launchScript = launchScript;
    }
    @DataBoundSetter
    public void setCloseScript(String closeScript) {
        this.closeScript = closeScript;
    }    
    @DataBoundSetter
    public void setUninstallScript(String uninstallScript) {
        this.uninstallScript = uninstallScript;
    }
    @DataBoundSetter
    public void setRestartAfterInstall(boolean restartAfterInstall) {
        this.restartAfterInstall = restartAfterInstall;
    }
    @DataBoundSetter
    public void setFunctionalTest(boolean functionalTest) {
        this.functionalTest = functionalTest;
    }
    @DataBoundSetter
    public void setFunctionalTestList(List<FunctionalTest> functionalTestList) {
        this.functionalTestList = functionalTestList;
    }
    @DataBoundSetter
    public void setSecurityUpdates(boolean securityUpdates) {
        this.securityUpdates = securityUpdates;
    }
    @DataBoundSetter
    public void setSecurityUpdatesTargetOSs(List<String> securityUpdatesTargetOSs) {
        this.securityUpdatesTargetOSs = securityUpdatesTargetOSs;
    }
    @DataBoundSetter
    public void setFeatureUpdates(boolean featureUpdates) {
        this.featureUpdates = featureUpdates;
    }
    @DataBoundSetter
    public void setFlightingRing(String flightingRing) {
        this.flightingRing = flightingRing;
    }
    @DataBoundSetter
    public void setFeatureUpdatesTargetOSs(List<String> featureUpdatesTargetOSs) {
        this.featureUpdatesTargetOSs = featureUpdatesTargetOSs;
    }
    @DataBoundSetter
    public void setFeatureUpdatesBaselineOSs(List<String> featureUpdatesBaselineOSs) {
        this.featureUpdatesBaselineOSs = featureUpdatesBaselineOSs;
    }


    public String getTenantId() {
        return tenantId;
    }
    public String getClientId() {
        return clientId;
    }
    public String getSubscriptionId() {
        return subscriptionId;
    }
    public String getResourceGroup() {
        return resourceGroup;
    }
    public String getTestBaseAccount() {
        return testBaseAccount;
    }
    public String getArtifact() {
        return artifact;
    }
    public String getApplicationName() {
        return applicationName;
    }
    public String getVersion() {
        return version;
    }
    public boolean isOutOfBoxTest() {
        return outOfBoxTest;
    }
    public String getInstallScript() {
        return installScript;
    }
    public String getLaunchScript() {
        return launchScript;
    }
    public String getCloseScript() {
        return closeScript;
    }
    public String getUninstallScript() {
        return uninstallScript;
    }
    public boolean isRestartAfterInstall() {
        return restartAfterInstall;
    }
    public boolean isFunctionalTest() {
        return functionalTest;
    }
    public List<FunctionalTest> getFunctionalTestList() {
        return functionalTestList;
    }
    public boolean isSecurityUpdates() {
        return securityUpdates;
    }
    public List<String> getSecurityUpdatesTargetOSs() {
        return securityUpdatesTargetOSs;
    }
    public boolean isFeatureUpdates() {
        return featureUpdates;
    }
    public String getFlightingRing() {
        return flightingRing;
    }
    public List<String> getFeatureUpdatesTargetOSs() {
        return featureUpdatesTargetOSs;
    }
    public List<String> getFeatureUpdatesBaselineOSs() {
        return featureUpdatesBaselineOSs;
    }

    @Symbol("testBaseOptions")
    @Extension
    public static class DescriptorImpl extends Descriptor<TestBaseOptions> {
        @NonNull
        public String getDisplayName() {
            return "";
        }


        // mandatory options
        public FormValidation doCheckTenantId(@QueryParameter String value) {
            if(value == null || value.equals(""))
                return FormValidation.error(Messages.TestBaseOptions_DescriptorImpl_infos_NotEmpty());
            return FormValidation.ok();
        }
        public FormValidation doCheckClientId(@QueryParameter String value) {
            if(value == null || value.equals(""))
                return FormValidation.error(Messages.TestBaseOptions_DescriptorImpl_infos_NotEmpty());
            return FormValidation.ok();
        }
        public FormValidation doCheckSubscriptionId(@QueryParameter String value) {
            if(value == null || value.equals(""))
                return FormValidation.error(Messages.TestBaseOptions_DescriptorImpl_infos_NotEmpty());
            return FormValidation.ok();
        }
        public FormValidation doCheckResourceGroup(@QueryParameter String value) {
            if(value == null || value.equals(""))
                return FormValidation.error(Messages.TestBaseOptions_DescriptorImpl_infos_NotEmpty());
            return FormValidation.ok();
        }
        public FormValidation doCheckTestBaseAccount(@QueryParameter String value) {
            if(value == null || value.equals(""))
                return FormValidation.error(Messages.TestBaseOptions_DescriptorImpl_infos_NotEmpty());
            return FormValidation.ok();
        }
        public FormValidation doCheckArtifact(@QueryParameter String value) {
            if(value == null || value.equals(""))
                return FormValidation.error(Messages.TestBaseOptions_DescriptorImpl_infos_NotEmpty());
            return FormValidation.ok();
        }
        public FormValidation doCheckApplicationName(@QueryParameter String value) {
            if(value == null || value.equals(""))
                return FormValidation.error(Messages.TestBaseOptions_DescriptorImpl_infos_NotEmpty());
            return FormValidation.ok();
        }
        public FormValidation doCheckVersion(@QueryParameter String value) {
            if(value == null || value.equals(""))
                return FormValidation.error(Messages.TestBaseOptions_DescriptorImpl_infos_NotEmpty());
            return FormValidation.ok();
        }


        // either or validation, and I don't know why these validation are invalid
        public FormValidation doCheckOutOfBoxTest(@QueryParameter boolean value, @QueryParameter boolean functionalTest) {
            if(value == false && functionalTest == false)
                return FormValidation.error(Messages.TestBaseOptions_DescriptorImpl_infos_OutOfBoxTestOrFunctionalTest());
            return FormValidation.ok();
        }
        public FormValidation doCheckFunctionalTest(@QueryParameter boolean value, @QueryParameter boolean outOfBoxTest) {
            if(value == false && outOfBoxTest == false)
                return FormValidation.error(Messages.TestBaseOptions_DescriptorImpl_infos_OutOfBoxTestOrFunctionalTest());
            return FormValidation.ok();
        }
        public FormValidation doCheckFeatureUpdates(@QueryParameter boolean value, @QueryParameter boolean securityUpdates) {
            if(value == false && securityUpdates == false)
                return FormValidation.error(Messages.TestBaseOptions_DescriptorImpl_infos_SecurityUpdatesOrFeatureUpdates());
            return FormValidation.ok();
        }
        public FormValidation doCheckSecurityUpdates(@QueryParameter boolean value, @QueryParameter boolean featureUpdates) {
            if(value == false && featureUpdates == false)
                return FormValidation.error(Messages.TestBaseOptions_DescriptorImpl_infos_SecurityUpdatesOrFeatureUpdates());
            return FormValidation.ok();
        }


        // conditional mandatory
        public FormValidation doCheckFlightingRing(@QueryParameter String value, @QueryParameter boolean featureUpdates) {
            if(featureUpdates && (value == null || value.equals("")))
                return FormValidation.error(Messages.TestBaseOptions_DescriptorImpl_infos_NotEmpty());
            return FormValidation.ok();
        }


        // hard-coded
        public ListBoxModel doFillFlightingRingItems() {
            ListBoxModel r = new ListBoxModel();
            r.add("Insider Beta Channel", "Insider Beta Channel");
            return r;
        }


        // hard-coded
        public ListBoxModel doFillSecurityUpdatesTargetOSsItems() {
            ListBoxModel r = new ListBoxModel();
            String[] osList = {
                "Windows Server 2022 - Server Core",
                "Windows Server 2022",
                "Windows Server 2019 - Server Core",
                "Windows Server 2019",
                "Windows Server 2016 - Server Core",
                "Windows Server 2016",
                "Windows 11 21H2",
                "Windows 10 21H2",
                "Windows 10 21H1",
                "Windows 10 20H2",
                "All Future OS Updates"
            };
            for(String s : osList) {
                r.add(s, s);
            }
            return r;
        }


        // hard-coded
        public ListBoxModel doFillFeatureUpdatesTargetOSsItems() {
            ListBoxModel r = new ListBoxModel();
            String[] osList = {
                "Windows 11 22H2",
                "Windows 11 21H2",
                "Windows 10 21H2"
            };
            for(String s : osList) {
                r.add(s, s);
            }
            return r;
        }


        // hard-coded
        public ListBoxModel doFillFeatureUpdatesBaselineOSsItems() {
            ListBoxModel r = new ListBoxModel();
            String[] osList = {
                "Windows 11 22H2",
                "Windows 11 21H2",
                "Windows 10 21H2"
            };
            for(String s : osList) {
                r.add(s, s);
            }
            return r;
        }
    }

    private static final long serialVersionUID = 1L;
}
