package io.jenkins.plugins.azuretestbase;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Label;
import io.jenkins.plugins.azuretestbase.TestBaseBuilder;

import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class TestBaseBuilderTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    private final String configurationFilePath = "TestBase.json";

    @Test
    public void testConfigRoundtrip() throws Exception {
        FreeStyleProject project = jenkins.createFreeStyleProject();
        TestBaseBuilder tbb = new TestBaseBuilder();
        project.getBuildersList().add(tbb);
        project = jenkins.configRoundtrip(project);

        TestBaseBuilder idealTbb = new TestBaseBuilder();
        idealTbb.setConfigurationFilePath("");
        idealTbb.setCredentialsId("");
        TestBaseOptions testBaseOptions = new TestBaseOptions();
        List<FunctionalTest> functionTestList = new ArrayList<>();
        functionTestList.add(new FunctionalTest(""));
        testBaseOptions.setApplicationName("");
        testBaseOptions.setArtifact("");
        testBaseOptions.setClientId("");
        testBaseOptions.setCloseScript("");
        testBaseOptions.setFeatureUpdates(false);
        testBaseOptions.setFeatureUpdatesBaselineOSs(new ArrayList<>());
        testBaseOptions.setFeatureUpdatesTargetOSs(new ArrayList<>());
        testBaseOptions.setFlightingRing("Insider Beta Channel");
        testBaseOptions.setFunctionalTest(false);
        testBaseOptions.setFunctionalTestList(functionTestList);
        testBaseOptions.setInstallScript("");
        testBaseOptions.setLaunchScript("");
        testBaseOptions.setOutOfBoxTest(true);
        testBaseOptions.setResourceGroup("");
        testBaseOptions.setRestartAfterInstall(false);
        testBaseOptions.setSecurityUpdates(true);
        testBaseOptions.setSecurityUpdatesTargetOSs(new ArrayList<>());
        testBaseOptions.setSubscriptionId("");
        testBaseOptions.setTenantId("");
        testBaseOptions.setTestBaseAccount("");
        testBaseOptions.setUninstallScript("");
        testBaseOptions.setVersion("");
        idealTbb.setTestBaseOptions(testBaseOptions);

        jenkins.assertEqualDataBoundBeans(idealTbb, project.getBuildersList().get(0));
    }
    

    @Test
    public void testBuildUseConfigurationFile() throws Exception {
        FreeStyleProject project = jenkins.createFreeStyleProject();
        TestBaseBuilder tbb = new TestBaseBuilder();
        project.getBuildersList().add(tbb);

        // do not test for the time being
        // FreeStyleBuild build = jenkins.buildAndAssertSuccess(project);
        // jenkins.assertLogContains("Hello, " + name, build);
    }

    @Test
    public void testBuildUseSpecifiedOptions() throws Exception {

        FreeStyleProject project = jenkins.createFreeStyleProject();
        TestBaseBuilder tbb = new TestBaseBuilder();
        tbb.setConfigurationFilePath(configurationFilePath);
        project.getBuildersList().add(tbb);

        // do not test for the time being
        // FreeStyleBuild build = jenkins.buildAndAssertSuccess(project);
        // jenkins.assertLogContains("Bonjour, " + name, build);
    }

    @Test
    public void testScriptedPipeline() throws Exception {
        String agentLabel = "my-agent";
        jenkins.createOnlineSlave(Label.get(agentLabel));
        WorkflowJob job = jenkins.createProject(WorkflowJob.class, "test-scripted-pipeline");
        String pipelineScript
                = "node {\n"
                + "  useConfigurationFile: true, configurationFilePath: '" + configurationFilePath + "'\n"
                + "}";
        job.setDefinition(new CpsFlowDefinition(pipelineScript, true));
        
        // do not test for the time being
        // WorkflowRun completedBuild = jenkins.assertBuildStatusSuccess(job.scheduleBuild2(0));
        // String expectedString = "Hello, " + name + "!";
        // jenkins.assertLogContains(expectedString, completedBuild);
    }

}