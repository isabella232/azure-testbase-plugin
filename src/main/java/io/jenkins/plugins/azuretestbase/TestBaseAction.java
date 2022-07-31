package io.jenkins.plugins.azuretestbase;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import hudson.model.Run;
import io.jenkins.plugins.azuretestbase.dm.Package;
import io.jenkins.plugins.azuretestbase.dm.Page;
import io.jenkins.plugins.azuretestbase.dm.TestBaseConfiguration;
import io.jenkins.plugins.azuretestbase.dm.TestSummaryResource;
import io.jenkins.plugins.azuretestbase.http.AzureManagementAPI;
import io.jenkins.plugins.azuretestbase.http.AzureManagementResult;
import jenkins.model.RunAction2;

public class TestBaseAction implements RunAction2 {

    @Override
    public String getIconFileName() {
        return "/plugin/azure-testbase/logo.png";
    }

    @Override
    public String getDisplayName() {
        return "TestBase";
    }

    @Override
    public String getUrlName() {
        return "testbase";
    }

    private TestBaseConfiguration testBaseConfiguration;

    public TestBaseAction(TestBaseConfiguration testBaseConfiguration) {
        this.testBaseConfiguration = testBaseConfiguration;
    }

    public TestBaseConfiguration getTestBaseConfiguration() {
        return this.testBaseConfiguration;
    }

    private Run<?, ?> run;

    @Override
    public void onAttached(Run<?, ?> r) {
        this.run = r;
    }

    @Override
    public void onLoad(Run<?, ?> r) {
        this.run = r;    
    }

    public Run<?, ?> getRun() {
        return this.run;
    }


    // get package info
    public Package getPackage() throws IOException {
        Configuration configuration = Configuration.getInstance();
        AzureManagementAPI azureManagementAPI = new AzureManagementAPI(
            configuration.getBaseUrl(),
            testBaseConfiguration.getTenantId(), 
            testBaseConfiguration.getClientId(), 
            testBaseConfiguration.getClientSecret(),
            configuration.getAuthorityHost(),
            configuration.getScope());

        AzureManagementResult<Package> packageResult = azureManagementAPI.getPackage(
            testBaseConfiguration.getSubscriptionId(), 
            testBaseConfiguration.getResourceGroup(), 
            testBaseConfiguration.getTestBaseAccount(), 
            testBaseConfiguration.getPackageProp().getProperties().getApplicationName() + "-" + testBaseConfiguration.getPackageProp().getProperties().getVersion(), 
            configuration.getTestBaseApiVersion());

        return packageResult.getEntity();
    }


    // get test summaries
    public Page<TestSummaryResource> testSummaries() throws IOException {
        Configuration configuration = Configuration.getInstance();
        AzureManagementAPI azureManagementAPI = new AzureManagementAPI(
            configuration.getBaseUrl(),
            testBaseConfiguration.getTenantId(), 
            testBaseConfiguration.getClientId(), 
            testBaseConfiguration.getClientSecret(),
            configuration.getAuthorityHost(),
            configuration.getScope());

        AzureManagementResult<Page<TestSummaryResource>> pageResult = azureManagementAPI.testSummaries(
            testBaseConfiguration.getSubscriptionId(), 
            testBaseConfiguration.getResourceGroup(), 
            testBaseConfiguration.getTestBaseAccount(), 
            configuration.getTestBaseApiVersion());

        return pageResult.getEntity();
    }


    // get test summaries json info
    public String testSummariesJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // do not throw exception if there is no filter specified
        mapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String result = mapper.writeValueAsString(testSummaries());
        return result;
    }

    
    // get package json info
    public String getPackageJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // do not throw exception if there is no filter specified
        mapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String result = mapper.writeValueAsString(getPackage());
        return result;
    }


    // get package link
    public String getPackageLink() throws IOException {
        Configuration configuration = Configuration.getInstance();
        String linkPath = "%s/#@%s/resource/subscriptions/%s/resourceGroups/%s/providers/Microsoft.TestBase/testBaseAccounts/%s/packages/%s-%s/overview";
        return String.format(
            linkPath, 
            configuration.getPortalBaseUrl(), 
            configuration.getPortalHostUser(),
            testBaseConfiguration.getSubscriptionId(), 
            testBaseConfiguration.getResourceGroup(), 
            testBaseConfiguration.getTestBaseAccount(), 
            testBaseConfiguration.getPackageProp().getProperties().getApplicationName(), 
            testBaseConfiguration.getPackageProp().getProperties().getVersion());
    }


    // get test summary link
    public String getSummaryLink() throws IOException {
        Configuration configuration = Configuration.getInstance();
        String linkPath = "%s/#@%s/resource/subscriptions/%s/resourceGroups/%s/providers/Microsoft.TestBase/testBaseAccounts/%s/packages/%s-%s/test_summary";
        return String.format(
            linkPath, 
            configuration.getPortalBaseUrl(), 
            configuration.getPortalHostUser(),
            testBaseConfiguration.getSubscriptionId(), 
            testBaseConfiguration.getResourceGroup(), 
            testBaseConfiguration.getTestBaseAccount(), 
            testBaseConfiguration.getPackageProp().getProperties().getApplicationName(), 
            testBaseConfiguration.getPackageProp().getProperties().getVersion());
    }


    // update package info
    public void updatePackage() throws IOException {
        Package packageProp = getPackage();
        testBaseConfiguration.setPackageProp(packageProp);
    }


    public String configurationJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("TestBaseConfiguration", SimpleBeanPropertyFilter.serializeAllExcept("clientSecret"));
        filterProvider.addFilter("Properties", SimpleBeanPropertyFilter.serializeAllExcept("blobPath"));
        // do not throw exception if there is no filter specified
        filterProvider.setFailOnUnknownId(false);
        mapper.setFilterProvider(filterProvider);
        String result = mapper.writeValueAsString(testBaseConfiguration);
        return result;
    }
}