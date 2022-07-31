package io.jenkins.plugins.azuretestbase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.Getter;

public class MoreConfiguration extends Configuration {
    private static final String morePropertyFile = "/testdata.properties";
    private static final String morePropertyFileDf = "/testdata-df.properties";

    // use these object to synchronize, because morePropertyFile and morePropertyFileDf may be in the constant pool
    private static final Object morePropertyFileLock = new Object();
    private static final Object morePropertyFileDfLock = new Object();

    // forbid to reorder command
    private static volatile MoreConfiguration instance;
    private static volatile MoreConfiguration instanceDf;

    @Getter
    private String subscriptionId;
    @Getter
    private String resourceGroupName;
    @Getter
    private String testBaseAccountName;
    @Getter
    private String clientId;
    @Getter
    private String tenantId;
    @Getter
    private String clientSecret;


    public static MoreConfiguration getInstance() throws IOException {
        if(getEnv() == Env.DogFood)
            return getInstanceDf();
        return getInstanceProd();
    }


    private static MoreConfiguration getInstanceProd() throws IOException {
        if(instance == null) {
            synchronized(morePropertyFileLock) {
                if(instance == null)
                    instance = new MoreConfiguration(propertyFile, morePropertyFile, Env.Prod);
            }
        }
        return instance;
    }


    private static MoreConfiguration getInstanceDf() throws IOException {
        if(instanceDf == null) {
            synchronized(morePropertyFileDfLock) {
                if(instanceDf == null)
                    instanceDf = new MoreConfiguration(propertyFileDf, morePropertyFileDf, Env.DogFood);
            }
        }
        return instanceDf;
    }


    private MoreConfiguration(String filePath, String moreFilePath, Env configurationEnv) throws IOException {
        super(filePath, configurationEnv);

        InputStream inputStream = this.getClass().getResourceAsStream(moreFilePath);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } finally {
            inputStream.close();
        }

        this.subscriptionId = properties.getProperty("subscriptionId");
        this.resourceGroupName = properties.getProperty("resourceGroupName");
        this.testBaseAccountName = properties.getProperty("testBaseAccountName");
        this.clientId = properties.getProperty("clientId");
        this.tenantId = properties.getProperty("tenantId");
        this.clientSecret = properties.getProperty("clientSecret");
    }
}
