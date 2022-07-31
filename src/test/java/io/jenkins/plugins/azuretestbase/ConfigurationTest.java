package io.jenkins.plugins.azuretestbase;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@EnabledIfSystemProperty(named = "apitest", matches = "(?i)true")
public class ConfigurationTest {
    @Test
    public void readConfigurationTest() throws Exception {
        Configuration configuration = Configuration.getInstance();

        assertNotNull(configuration);

        assertNotNull(configuration.getAuthorityHost());
        assertNotNull(configuration.getBaseUrl());
        assertNotNull(configuration.getResourceManagementApiVersion());
        assertNotNull(configuration.getScope());
        assertNotNull(configuration.getTestBaseApiVersion());
        assertNotNull(configuration.getPortalBaseUrl());
        assertNotNull(configuration.getPortalHostUser());
    }

    
    @Test
    public void readMoreConfigurationTest() throws Exception {
        MoreConfiguration moreConfiguration = MoreConfiguration.getInstance();

        assertNotNull(moreConfiguration);

        assertNotNull(moreConfiguration.getAuthorityHost());
        assertNotNull(moreConfiguration.getBaseUrl());
        assertNotNull(moreConfiguration.getResourceManagementApiVersion());
        assertNotNull(moreConfiguration.getScope());
        assertNotNull(moreConfiguration.getTestBaseApiVersion());
        assertNotNull(moreConfiguration.getPortalBaseUrl());
        assertNotNull(moreConfiguration.getPortalHostUser());
        assertNotNull(moreConfiguration.getClientId());
        assertNotNull(moreConfiguration.getClientSecret());
        assertNotNull(moreConfiguration.getResourceGroupName());
        assertNotNull(moreConfiguration.getSubscriptionId());
        assertNotNull(moreConfiguration.getTenantId());
        assertNotNull(moreConfiguration.getTestBaseAccountName());
    }
}
