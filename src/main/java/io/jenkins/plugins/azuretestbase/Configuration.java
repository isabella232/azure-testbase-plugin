package io.jenkins.plugins.azuretestbase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.Getter;

// the singleton pattern
public class Configuration {
    protected static final String propertyFile = "/config.properties";
    protected static final String propertyFileDf = "/config-df.properties";

    // use these object to synchronize, because propertyFile and propertyFileDf may be in the constant pool
    private static final Object propertyFileLock = new Object();
    private static final Object propertyFileDfLock = new Object();

    // forbid to reorder command
    private static volatile Configuration instance;
    private static volatile Configuration instanceDf;
    
    @Getter
    private final String authorityHost;
    @Getter
    private final String baseUrl;
    @Getter
    private final String scope;
    @Getter
    private final String testBaseApiVersion;
    @Getter
    private final String resourceManagementApiVersion;
    @Getter
    private final String portalBaseUrl;
    @Getter
    private final String portalHostUser;
    @Getter
    private final Env configurationEnv;


    public static enum Env {
        Prod,
        DogFood
    }


    public static Env getEnv() {
        String env = System.getProperty("env");
        if(env == null || env.equalsIgnoreCase("prod"))
            return Env.Prod;
        else if(env.equalsIgnoreCase("dog-food") || env.equalsIgnoreCase("dogfood") || env.equalsIgnoreCase("df"))
            return Env.DogFood;
        throw new RuntimeException("Unkown property env=" + env);
    }


    public static Configuration getInstance() throws IOException {
        if(getEnv() == Env.DogFood)
            return getInstanceDf();
        return getInstanceProd();
    }


    private static Configuration getInstanceProd() throws IOException {
        if(instance == null) {
            synchronized(propertyFileLock) {
                if(instance == null)
                    instance = new Configuration(propertyFile, Env.Prod);
            }
        }
        return instance;
    }


    private static Configuration getInstanceDf() throws IOException {
        if(instanceDf == null) {
            synchronized(propertyFileDfLock) {
                if(instanceDf == null)
                    instanceDf = new Configuration(propertyFileDf, Env.DogFood);
            }
        }
        return instanceDf;
    }


    protected Configuration(String filePath, Env configurationEnv) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(filePath);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } finally {
            inputStream.close();
        }

        this.configurationEnv = configurationEnv;
        this.authorityHost = properties.getProperty("authorityHost");
        this.baseUrl = properties.getProperty("baseUrl");
        this.scope = properties.getProperty("scope");
        this.testBaseApiVersion = properties.getProperty("testBaseApiVersion");
        this.resourceManagementApiVersion = properties.getProperty("resourceManagementApiVersion");
        this.portalBaseUrl = properties.getProperty("portalBaseUrl");
        this.portalHostUser = properties.getProperty("portalHostUser");
    }
}
