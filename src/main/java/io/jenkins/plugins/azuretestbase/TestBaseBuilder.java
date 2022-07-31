package io.jenkins.plugins.azuretestbase;

import hudson.Launcher;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import io.jenkins.plugins.azuretestbase.Messages;
import io.jenkins.plugins.azuretestbase.dm.Command;
import io.jenkins.plugins.azuretestbase.dm.FileUploadUrl;
import io.jenkins.plugins.azuretestbase.dm.FileUploadUrlBody;
import io.jenkins.plugins.azuretestbase.dm.Package;
import io.jenkins.plugins.azuretestbase.dm.Properties;
import io.jenkins.plugins.azuretestbase.dm.TargetOS;
import io.jenkins.plugins.azuretestbase.dm.TestBaseConfiguration;
import io.jenkins.plugins.azuretestbase.dm.TestBaseTest;
import io.jenkins.plugins.azuretestbase.dm.emuntype.ActionType;
import io.jenkins.plugins.azuretestbase.dm.emuntype.ContentType;
import io.jenkins.plugins.azuretestbase.dm.emuntype.TestType;
import io.jenkins.plugins.azuretestbase.http.AzureManagementAPI;
import io.jenkins.plugins.azuretestbase.http.AzureManagementResult;
import io.jenkins.plugins.azuretestbase.http.Utils;
import io.jenkins.plugins.azuretestbase.json.JsonConvertion;
import hudson.model.AbstractProject;
import hudson.model.FreeStyleProject;
import hudson.model.Item;
import hudson.model.Queue;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.queue.Tasks;
import hudson.security.ACL;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;

import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.UUID;

import jenkins.model.Jenkins;
// import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;

import org.acegisecurity.Authentication;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.kohsuke.stapler.DataBoundSetter;

public class TestBaseBuilder extends Builder implements SimpleBuildStep {

    // client secret
    private String credentialsId;
    // use configuration file or use options specfied in Jenkins UI
    private boolean useConfigurationFile;
    private String configurationFilePath;
    private TestBaseOptions testBaseOptions;

    private static final String defaultConfigurationFilePath = "TestBase.json";


    @DataBoundConstructor
    public TestBaseBuilder() {}

    @DataBoundSetter
    public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
    }
    @DataBoundSetter
    public void setUseConfigurationFile(boolean useConfigurationFile) {
        this.useConfigurationFile = useConfigurationFile;
    }
    @DataBoundSetter
    public void setConfigurationFilePath(String configurationFilePath) {
        this.configurationFilePath = configurationFilePath;
    }
    @DataBoundSetter
    public void setTestBaseOptions(TestBaseOptions testBaseOptions) {
        this.testBaseOptions = testBaseOptions;
    }


    public String getCredentialsId() {
        return credentialsId;
    }
    public boolean isUseConfigurationFile() {
        return useConfigurationFile;
    }
    public String getConfigurationFilePath() {
        return configurationFilePath;
    }
    public TestBaseOptions getTestBaseOptions() {
        return testBaseOptions;
    }


    // find the file with the same name as targetName; this func will only search file, not folder
    private FilePath getFile(FilePath workspace, @NonNull String targetName) throws IOException, InterruptedException {
        targetName = targetName.trim();
        Path targetPath = Paths.get(targetName);
        List<String> targetDirs = new ArrayList<>();
        Spliterator<Path> sp = targetPath.spliterator().trySplit();
        sp.forEachRemaining(val -> {
            targetDirs.add(val.toString());
        });
        return getFileRecur(workspace, targetDirs, 0);
    }


    private FilePath getFileRecur(FilePath workspace, @NonNull List<String> targetDirs, int index) throws IOException, InterruptedException  {
        if(!workspace.isDirectory())
            return null;
        if(index >= targetDirs.size() || targetDirs.get(index).isEmpty())
            return null;
        List<FilePath> fileList = workspace.list();
        for(FilePath filePath : fileList) {
            if(filePath.getName().equals(targetDirs.get(index))) {
                if(index == targetDirs.size() - 1) {
                    if(!filePath.isDirectory())
                        return filePath;
                } else {
                    if(filePath.isDirectory())
                        return getFileRecur(filePath, targetDirs, index + 1);
                }
            }
        }
        return null;
    }


    private TestBaseConfiguration getTestBaseConfiguration() {
        // construct out of box test and functional test
        List<TestBaseTest> tests = new ArrayList<>();
        // out of box test
        if(testBaseOptions.isOutOfBoxTest()) {
            List<Command> commands = new ArrayList<>();
            String installScript = testBaseOptions.getInstallScript();
            String uninstallScript = testBaseOptions.getUninstallScript();
            String launchScript = testBaseOptions.getLaunchScript();
            String closeScript = testBaseOptions.getCloseScript();
            if(installScript != null && !installScript.isEmpty()) {
                Command command = new Command();
                command.setAction(ActionType.Install);
                command.setName("install");
                command.setContentType(ContentType.Path);
                command.setContent(installScript);
                // whether to restart after install
                command.setRestartAfter(testBaseOptions.isRestartAfterInstall());
                commands.add(command);
            }
            if(uninstallScript != null && !uninstallScript.isEmpty()) {
                Command command = new Command();
                command.setAction(ActionType.Uninstall);
                command.setName("uninstall");
                command.setContentType(ContentType.Path);
                command.setContent(uninstallScript);
                commands.add(command);
            }
            if(launchScript != null && !launchScript.isEmpty()) {
                Command command = new Command();
                command.setAction(ActionType.Launch);
                command.setName("launch");
                command.setContentType(ContentType.Path);
                command.setContent(launchScript);
                commands.add(command);
            }
            if(closeScript != null && !closeScript.isEmpty()) {
                Command command = new Command();
                command.setAction(ActionType.Close);
                command.setName("close");
                command.setContentType(ContentType.Path);
                command.setContent(closeScript);
                commands.add(command);
            }
            TestBaseTest test = new TestBaseTest();
            test.setTestType(TestType.OutOfBoxTest);
            test.setCommands(commands);
            tests.add(test);
        }
        // functional test
        if(testBaseOptions.isFunctionalTest()) {
            List<FunctionalTest> functionalTestList = testBaseOptions.getFunctionalTestList();
            List<Command> commands = new ArrayList<>();
            if(functionalTestList != null) {
                for(int i = 0; i < functionalTestList.size(); i++) {
                    FunctionalTest functionalTest = functionalTestList.get(i);
                    Command command = new Command();
                    command.setAction(ActionType.Custom);
                    command.setName("costom" + i);
                    command.setContentType(ContentType.Path);
                    command.setContent(functionalTest.getContent());
                    command.setApplyUpdateBefore(functionalTest.isApplyUpdateBefore());
                    command.setRestartAfter(functionalTest.isRestartAfter());
                    commands.add(command);
                }
            }
            TestBaseTest test = new TestBaseTest();
            test.setTestType(TestType.FunctionalTest);
            test.setCommands(commands);
            tests.add(test);
        }

        // construct test matrix
        List<TargetOS> targetOSList = new ArrayList<>();
        if(testBaseOptions.isSecurityUpdates()) {
            TargetOS securityUpdates = new TargetOS();
            securityUpdates.setOsUpdateType("Security updates");
            if(testBaseOptions.getSecurityUpdatesTargetOSs() != null)
                securityUpdates.setTargetOSs(new ArrayList<>(testBaseOptions.getSecurityUpdatesTargetOSs()));
            targetOSList.add(securityUpdates);
        }
        if(testBaseOptions.isFeatureUpdates()) {
            TargetOS featureUpdates = new TargetOS();
            featureUpdates.setOsUpdateType("Feature updates");
            if(testBaseOptions.getFeatureUpdatesBaselineOSs() != null)
                featureUpdates.setBaselineOSs(new ArrayList<>(testBaseOptions.getFeatureUpdatesBaselineOSs()));
            if(testBaseOptions.getFeatureUpdatesTargetOSs() != null)
                featureUpdates.setTargetOSs(new ArrayList<>(testBaseOptions.getFeatureUpdatesTargetOSs()));
            targetOSList.add(featureUpdates);
        }

        // construct properties
        Properties properties = new Properties();
        properties.setApplicationName(testBaseOptions.getApplicationName());
        properties.setVersion(testBaseOptions.getVersion());
        properties.setFlightingRing(testBaseOptions.getFlightingRing());
        properties.setTests(tests);
        properties.setTargetOSList(targetOSList);

        // construct package
        Package packageProp = new Package();
        packageProp.setLocation("global");
        packageProp.setProperties(properties);

        return new TestBaseConfiguration(
            testBaseOptions.getTenantId(),
            testBaseOptions.getClientId(),
            "",
            testBaseOptions.getSubscriptionId(),
            testBaseOptions.getResourceGroup(),
            testBaseOptions.getTestBaseAccount(),
            testBaseOptions.getArtifact(),
            packageProp
        );
    }


    // check if fileName is suffix with one of allowedSuffix
    private boolean withSuffix(@NonNull String fileName, @NonNull String[] allowedSuffix) {
        for(String suffix : allowedSuffix) {
            if(fileName.length() >= suffix.length() && fileName.substring(fileName.length() - suffix.length(), fileName.length()).equals(suffix))
                return true;
        }
        return false;
    }


    @Override
    public void perform(Run<?, ?> run, FilePath workspace, EnvVars env, Launcher launcher, TaskListener listener) 
        throws JsonMappingException, JsonProcessingException, InterruptedException, IOException {
        
        listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_PluginStart());

        // get plugin configuration
        Configuration configuration = Configuration.getInstance();

        listener.getLogger().println(
            String.format(
                Messages.TestBaseBuilder_DescriptorImpl_infos_Environment(),
                configuration.getConfigurationEnv()
            )
        );

        listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_LoadConfiguration());

        // load client secret
        if(credentialsId == null || credentialsId.isEmpty()) {
            listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_CredentialsEmpty());
            run.setResult(Result.FAILURE);
            return;
        }
        StringCredentials credential = CredentialsProvider.findCredentialById(credentialsId, StringCredentials.class, run);
        if(credential == null) {
            listener.getLogger().println(
                String.format(
                    Messages.TestBaseBuilder_DescriptorImpl_infos_CredentialsNotFound(), 
                    credentialsId
                )
            );
            run.setResult(Result.FAILURE);
            return;
        }

        // other configuration
        TestBaseConfiguration testBaseConfiguration;
        if(useConfigurationFile) {
            // get configuration file and convert to JsonObj
            String finalConfigurationFilePath = defaultConfigurationFilePath;
            if(configurationFilePath != null && !configurationFilePath.isEmpty()) 
                finalConfigurationFilePath = configurationFilePath;

            // format validation
            String[] allowedSuffix = {".json", ".JSON"};
            if(!withSuffix(finalConfigurationFilePath, allowedSuffix)) {
                listener.getLogger().println(
                    String.format(
                        Messages.TestBaseBuilder_DescriptorImpl_infos_ConfigurationFormatError(), 
                        finalConfigurationFilePath
                    )
                );
                run.setResult(Result.FAILURE);
                return;
            }

            FilePath configurationFile = getFile(workspace, finalConfigurationFilePath);
            // file not found
            if(configurationFile == null) {
                listener.getLogger().println(
                    String.format(
                        Messages.TestBaseBuilder_DescriptorImpl_infos_FileNotFound(), 
                        finalConfigurationFilePath
                    )
                );
                run.setResult(Result.FAILURE);
                return;
            }
            testBaseConfiguration = JsonConvertion.jsonToObj(configurationFile.readToString(), TestBaseConfiguration.class);
        } else {
            if(testBaseOptions == null)
                setTestBaseOptions(new TestBaseOptions());
            testBaseConfiguration = getTestBaseConfiguration();
        }

        // set client secret
        testBaseConfiguration.setClientSecret(credential.getSecret().getPlainText());
        
        // mandatory parameters, which should never be null or empty
        if(testBaseConfiguration.getTenantId() == null || testBaseConfiguration.getTenantId().isEmpty()) {
            listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_TenantIdEmpty());
            run.setResult(Result.FAILURE);
            return;
        }
        if(testBaseConfiguration.getClientId() == null || testBaseConfiguration.getClientId().isEmpty()) {
            listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_ClientIdEmpty());
            run.setResult(Result.FAILURE);
            return;
        }
        if(testBaseConfiguration.getSubscriptionId() == null || testBaseConfiguration.getSubscriptionId().isEmpty()) {
            listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_SubscriptionIdEmpty());
            run.setResult(Result.FAILURE);
            return;
        }
        if(testBaseConfiguration.getResourceGroup() == null || testBaseConfiguration.getResourceGroup().isEmpty()) {
            listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_ResourceGroupEmpty());
            run.setResult(Result.FAILURE);
            return;
        }
        if(testBaseConfiguration.getTestBaseAccount() == null || testBaseConfiguration.getTestBaseAccount().isEmpty()) {
            listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_TestBaseAccountEmpty());
            run.setResult(Result.FAILURE);
            return;
        }
        if(testBaseConfiguration.getArtifact() == null || testBaseConfiguration.getArtifact().isEmpty()) {
            listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_ArtifactEmpty());
            run.setResult(Result.FAILURE);
            return;
        }
        if(testBaseConfiguration.getPackageProp() == null) {
            listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_PackageEmpty());
            run.setResult(Result.FAILURE);
            return;
        }
        if(testBaseConfiguration.getPackageProp().getProperties() == null) {
            listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_PropertiesEmpty());
            run.setResult(Result.FAILURE);
            return;
        }
        if(testBaseConfiguration.getPackageProp().getProperties().getVersion() == null || testBaseConfiguration.getPackageProp().getProperties().getApplicationName().isEmpty()) {
            listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_ApplicationNameEmpty());
            run.setResult(Result.FAILURE);
            return;
        }
        if(testBaseConfiguration.getPackageProp().getProperties().getVersion() == null || testBaseConfiguration.getPackageProp().getProperties().getVersion().isEmpty()) {
            listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_ApplicationVersionEmpty());
            run.setResult(Result.FAILURE);
            return;
        }

        // format validation
        String[] allowedSuffix = {".zip"};
        if(!withSuffix(testBaseConfiguration.getArtifact(), allowedSuffix)) {
            listener.getLogger().println(
                String.format(
                    Messages.TestBaseBuilder_DescriptorImpl_infos_ArtifactFormatError(), 
                    testBaseConfiguration.getArtifact()
                )
            );
            run.setResult(Result.FAILURE);
            return;
        }
        FilePath uploadFilePath = getFile(workspace, testBaseConfiguration.getArtifact());
        if(uploadFilePath == null) {
            listener.getLogger().println(
                String.format(
                    Messages.TestBaseBuilder_DescriptorImpl_infos_FileNotFound(), 
                    testBaseConfiguration.getArtifact()
                )
            );
            run.setResult(Result.FAILURE);
            return;
        }

        Package packageProp = testBaseConfiguration.getPackageProp();

        listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_AccountAuthentication());

        // create an AzureManagementAPI
        AzureManagementAPI azureManagementAPI = new AzureManagementAPI(
            configuration.getBaseUrl(),
            testBaseConfiguration.getTenantId(), 
            testBaseConfiguration.getClientId(), 
            testBaseConfiguration.getClientSecret(),
            configuration.getAuthorityHost(),
            configuration.getScope());

        listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_GetUploadUrl());

        // get upload url
        Path packagePath = Paths.get(testBaseConfiguration.getArtifact());
        Path blobNamePath = packagePath.getFileName();
        if(blobNamePath == null) {
            listener.getLogger().println(
                String.format(
                    Messages.TestBaseBuilder_DescriptorImpl_infos_FileNameNotFound(), 
                    testBaseConfiguration.getArtifact()
                )
            );
            run.setResult(Result.FAILURE);
            return;
        }
        FileUploadUrlBody fileUploadUrlBody = new FileUploadUrlBody(blobNamePath.toString());
        AzureManagementResult<FileUploadUrl> fileUploadUrlResult = azureManagementAPI.getFileUploadUrl(
            testBaseConfiguration.getSubscriptionId(), 
            testBaseConfiguration.getResourceGroup(), 
            testBaseConfiguration.getTestBaseAccount(), 
            configuration.getTestBaseApiVersion(),
            fileUploadUrlBody);

        listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_UploadPackage());

        // upload a package
        byte[] putStream = Utils.readAllBytes(uploadFilePath.read());
        azureManagementAPI.uploadFile(fileUploadUrlResult.getEntity().getUploadUrl(), putStream);

        listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_CreatePackage());

        // blobPath parameter is a entire url containing protocol and host, not the 'blobPath' value returned by getUploadUrl
        String blobPath = fileUploadUrlResult.getEntity().getUploadUrl().split("\\?")[0];
        packageProp.getProperties().setBlobPath(blobPath);
        String packageName = packageProp.getProperties().getApplicationName() + "-" + packageProp.getProperties().getVersion();
        AzureManagementResult<Package> packageResult = azureManagementAPI.createPackage(
            testBaseConfiguration.getSubscriptionId(), 
            testBaseConfiguration.getResourceGroup(), 
            testBaseConfiguration.getTestBaseAccount(), 
            packageName, 
            configuration.getTestBaseApiVersion(),
            packageProp);

        listener.getLogger().println(
            String.format(
                Messages.TestBaseBuilder_DescriptorImpl_infos_CreateSuccessfully(), 
                packageResult.getEntity().getName()
            )
        );

        // add side bar
        run.addAction(new TestBaseAction(testBaseConfiguration));

        listener.getLogger().println(Messages.TestBaseBuilder_DescriptorImpl_infos_PluginEnd());
    }

    
    @Symbol("testBase")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public FormValidation doCheckConfigurationFilePath(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.ok(Messages.TestBaseBuilder_DescriptorImpl_infos_EmptyConfigurationFile());
            return FormValidation.ok(String.format(Messages.TestBaseBuilder_DescriptorImpl_infos_ContainsConfigurationFile(), value));
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return Messages.TestBaseBuilder_DescriptorImpl_infos_DisplayName();
        }

        @Override
        public String getHelpFile() {
            return "/plugin/azure-testbase/help.html";
        }


        // fill select options of credentials
        public ListBoxModel doFillCredentialsIdItems(@AncestorInPath Item project, @QueryParameter String credentialsId) {
            StandardListBoxModel result = new StandardListBoxModel();
            if (project == null) {
                if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
                    return result.includeCurrentValue(credentialsId);
                }
            } else {
                if (!project.hasPermission(Item.EXTENDED_READ) && !project.hasPermission(CredentialsProvider.USE_ITEM)) {
                    return result.includeCurrentValue(credentialsId);
                }
            }
            if (project == null) {
                /* Construct a fake project, suppress the deprecation warning because the
                    * replacement for the deprecated API isn't accessible in this context. */
                @SuppressWarnings("deprecation")
                Item fakeProject = new FreeStyleProject(Jenkins.get(), "fake-" + UUID.randomUUID().toString());
                project = fakeProject;
            }

            // the replacement of getAuthenticationOf and SYSTEM is not applicable for StandardListBoxModel.includeAs
            @SuppressWarnings("deprecation")
            Authentication auth = project instanceof Queue.Task ? Tasks.getAuthenticationOf((Queue.Task) project) : ACL.SYSTEM;
            return result
                .includeEmptyValue()
                .includeAs(auth, project, StringCredentials.class)
                .includeCurrentValue(credentialsId);
        }
    }
}
