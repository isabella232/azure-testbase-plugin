package io.jenkins.plugins.azuretestbase.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import io.jenkins.plugins.azuretestbase.MoreConfiguration;
import io.jenkins.plugins.azuretestbase.dm.FileUploadUrl;
import io.jenkins.plugins.azuretestbase.dm.FileUploadUrlBody;
import io.jenkins.plugins.azuretestbase.dm.Package;
import io.jenkins.plugins.azuretestbase.dm.Page;
import io.jenkins.plugins.azuretestbase.dm.ResourceGroup;
import io.jenkins.plugins.azuretestbase.dm.Skus;
import io.jenkins.plugins.azuretestbase.exceptions.StatusCodeException;
import io.jenkins.plugins.azuretestbase.json.JsonConvertion;

@TestInstance(Lifecycle.PER_CLASS)
@EnabledIfSystemProperty(named = "apitest", matches = "(?i)true")
public class AzureManagementAPITest {

    private MoreConfiguration moreConfiguration;

    // All tests use this pre-packaged file
    private String blobPath = "java-demo.zip";

    @BeforeAll
    public void initializeConfiguration() throws IOException{
        moreConfiguration = MoreConfiguration.getInstance();
    }


    public String getFileName(String filePath) {
        Path path = Paths.get(filePath);
        return path.getFileName().toString();
    }


    @Test
    public void testSkus() throws IOException {
        AzureManagementAPI testBase = new AzureManagementAPI(
            moreConfiguration.getBaseUrl(), 
            moreConfiguration.getTenantId(), 
            moreConfiguration.getClientId(), 
            moreConfiguration.getClientSecret(), 
            moreConfiguration.getAuthorityHost(), 
            moreConfiguration.getScope());
        AzureManagementResult<Page<Skus>> pageResult = testBase.skus(moreConfiguration.getSubscriptionId(), moreConfiguration.getTestBaseApiVersion());
        assertNotNull(pageResult.getEntity());
        System.out.println(pageResult);
    }


    @Test
    public void testGetFileUploadUrl() throws IOException {
        AzureManagementAPI testBase = new AzureManagementAPI(
            moreConfiguration.getBaseUrl(), 
            moreConfiguration.getTenantId(), 
            moreConfiguration.getClientId(), 
            moreConfiguration.getClientSecret(), 
            moreConfiguration.getAuthorityHost(), 
            moreConfiguration.getScope());
        FileUploadUrlBody fileUploadUrlBody = new FileUploadUrlBody(getFileName(blobPath));
        AzureManagementResult<FileUploadUrl> fileUploadUrlResult = testBase.getFileUploadUrl(
            moreConfiguration.getSubscriptionId(), 
            moreConfiguration.getResourceGroupName(), 
            moreConfiguration.getTestBaseAccountName(), 
            moreConfiguration.getTestBaseApiVersion(), 
            fileUploadUrlBody);
        assertNotNull(fileUploadUrlResult.getEntity());
        System.out.println(fileUploadUrlResult);
    }


    @Test
    public void testCreateFileObj() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(blobPath);
        System.out.println(url.toString());
        System.out.println(url.getPath());
        File file = new File(url.getPath());
        assertNotNull(file);
        System.out.println(file);
        System.out.println(file.length());
        System.out.println(file.exists());
        System.out.println(file.isFile());
        url = classLoader.getResource("package.json");
        System.out.println(url.openStream());
        String content = new String(Utils.readAllBytes(url.openStream()), StandardCharsets.UTF_8);
        System.out.println(content);
    }


    @Test
    public void testInvalidUploadUrl() throws IOException {
        String invalidUploadUrl;
        if(moreConfiguration.getConfigurationEnv() == MoreConfiguration.Env.DogFood)
            invalidUploadUrl = "https://tbwestusprodsa.blob.core.windows.net/0fa28bdd-fcab-4351-9fc5-89b965a77e96/temp/e254e204-7ec5-4dad-bc11-27d14841606b/curl-demo?sv=2019-07-07&sr=b&sig=uDFfuqireDE3sZsWU3Cz14Zad6ECJUKxvgADUJD3H30%3D&se=2022-06-23T05%3A32%3A02Z&sp=acw";
        else if(moreConfiguration.getConfigurationEnv() == MoreConfiguration.Env.Prod)
            invalidUploadUrl = "https://tbwestusprodsa.blob.core.windows.net/0fa28bdd-fcab-4351-9fc5-89b965a77e96/temp/e254e204-7ec5-4dad-bc11-27d14841606b/curl-demo?sv=2019-07-07&sr=b&sig=uDFfuqireDE3sZsWU3Cz14Zad6ECJUKxvgADUJD3H30%3D&se=2022-06-23T05%3A32%3A02Z&sp=acw";
        else {
            System.out.println("Skip test on unrecognized env=" + moreConfiguration.getConfigurationEnv());
            return;
        }
        AzureManagementAPI testBase = new AzureManagementAPI(
            moreConfiguration.getBaseUrl(), 
            moreConfiguration.getTenantId(), 
            moreConfiguration.getClientId(), 
            moreConfiguration.getClientSecret(), 
            moreConfiguration.getAuthorityHost(), 
            moreConfiguration.getScope());
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(blobPath);
        File file = new File(url.getPath());
        StatusCodeException statusCodeException = assertThrows(StatusCodeException.class, () -> {
            testBase.uploadFile(invalidUploadUrl, file);
        });
        assertEquals(403, statusCodeException.getStatusCode());
    }


    @Test
    public void testUploadFile() throws IOException {
        // get upload url
        AzureManagementAPI testBase = new AzureManagementAPI(
            moreConfiguration.getBaseUrl(), 
            moreConfiguration.getTenantId(), 
            moreConfiguration.getClientId(), 
            moreConfiguration.getClientSecret(), 
            moreConfiguration.getAuthorityHost(), 
            moreConfiguration.getScope());
        FileUploadUrlBody fileUploadUrlBody = new FileUploadUrlBody(getFileName(blobPath));
        AzureManagementResult<FileUploadUrl> fileUploadUrlResult = testBase.getFileUploadUrl(
            moreConfiguration.getSubscriptionId(), 
            moreConfiguration.getResourceGroupName(), 
            moreConfiguration.getTestBaseAccountName(), 
            moreConfiguration.getTestBaseApiVersion(), 
            fileUploadUrlBody);
        assertNotNull(fileUploadUrlResult.getEntity());
        assertNotNull(fileUploadUrlResult.getEntity().getUploadUrl());

        // upload a package
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(blobPath);
        File file = new File(url.getPath());
        testBase.uploadFile(fileUploadUrlResult.getEntity().getUploadUrl(), file);
    }


    public void createPackage(String packagePath, String blobPath) throws Exception {
        AzureManagementAPI testBase = new AzureManagementAPI(
            moreConfiguration.getBaseUrl(), 
            moreConfiguration.getTenantId(), 
            moreConfiguration.getClientId(), 
            moreConfiguration.getClientSecret(), 
            moreConfiguration.getAuthorityHost(), 
            moreConfiguration.getScope());

        Path blobPathObj = Paths.get(blobPath);
        
        // get upload url
        FileUploadUrlBody fileUploadUrlBody = new FileUploadUrlBody(blobPathObj.getFileName().toString());
        AzureManagementResult<FileUploadUrl> fileUploadUrlResult = testBase.getFileUploadUrl(
            moreConfiguration.getSubscriptionId(), 
            moreConfiguration.getResourceGroupName(), 
            moreConfiguration.getTestBaseAccountName(), 
            moreConfiguration.getTestBaseApiVersion(), 
            fileUploadUrlBody);
        assertNotNull(fileUploadUrlResult);
        assertNotNull(fileUploadUrlResult.getEntity().getUploadUrl());

        // upload a package
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(blobPath);
        File file = new File(url.getPath());
        testBase.uploadFile(fileUploadUrlResult.getEntity().getUploadUrl(), file);

        // create a package
        // read packageJson and construct package entity
        url = classLoader.getResource(packagePath);
        String jsonStr = new String(Utils.readAllBytes(url.openStream()), StandardCharsets.UTF_8);
        Package bodyEntity = JsonConvertion.jsonToObj(jsonStr, Package.class);
        // set some properties
        String actualApplicationName = bodyEntity.getProperties().getApplicationName() + "-" + System.currentTimeMillis();
        bodyEntity.getProperties().setApplicationName(actualApplicationName);
        // blobPath parameter is a entire url containing protocol and host, not the 'blobPath' value returned by getUploadUrl
        String uploadBlobPath = fileUploadUrlResult.getEntity().getUploadUrl().split("\\?")[0];
        bodyEntity.getProperties().setBlobPath(uploadBlobPath);
        String packageName = actualApplicationName + "-" + bodyEntity.getProperties().getVersion();
        AzureManagementResult<Package> packageResult = testBase.createPackage(
            moreConfiguration.getSubscriptionId(), 
            moreConfiguration.getResourceGroupName(), 
            moreConfiguration.getTestBaseAccountName(), 
            packageName,
            moreConfiguration.getTestBaseApiVersion(), 
            bodyEntity);
        assertNotNull(packageResult.getEntity());
        System.out.println(packageResult);
    }


    @Test
    public void testCreatePackage() throws Exception {
        createPackage("package.json", "java-demo.zip");
    }


    @Test
    public void testCreatePackageSimpleConfiguration() throws Exception {
        createPackage("package-simple.json", "java-demo.zip");
    }


    @Test
    public void testCreatePackageLossConfiguration() throws Exception {
        StatusCodeException statusCodeException = assertThrows(StatusCodeException.class, () -> {
            createPackage("package-loss.json", "java-demo.zip");
        });
        assertEquals(400, statusCodeException.getStatusCode());
        assertEquals("BadRequest", statusCodeException.getErrorContent().getError().getCode());
    }

    
    @Test
    public void testInvalidAccesstoken() throws IOException {
        String invalidAccessToken;
        if(moreConfiguration.getConfigurationEnv() == MoreConfiguration.Env.DogFood)
            invalidAccessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IlRGN1hTWFZ6ellOUDc3S0x5ZUZpamJsSmdVWSIsImtpZCI6IlRGN1hTWFZ6ellOUDc3S0x5ZUZpamJsSmdVWSJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLXBwZS5uZXQvZjY4NmQ0MjYtOGQxNi00MmRiLTgxYjctYWI1NzhlMTEwY2NkLyIsImlhdCI6MTY1NjU4MDgwMiwibmJmIjoxNjU2NTgwODAyLCJleHAiOjE2NTY1ODYxMDcsIl9jbGFpbV9uYW1lcyI6eyJncm91cHMiOiJzcmMxIn0sIl9jbGFpbV9zb3VyY2VzIjp7InNyYzEiOnsiZW5kcG9pbnQiOiJodHRwczovL2dyYXBoLnBwZS53aW5kb3dzLm5ldC9mNjg2ZDQyNi04ZDE2LTQyZGItODFiNy1hYjU3OGUxMTBjY2QvdXNlcnMvZDM0MDRmOWQtNGQ2MS00YWRiLWJmYmYtMmY0N2ZlMTg1NjA2L2dldE1lbWJlck9iamVjdHMifX0sImFjciI6IjEiLCJhaW8iOiJBVlFBcS84V0FBQUFaQWdzS3BoeXM3bXZTY3Z5Z29uTzhvcGdQbXJiV3lXb0o2aWF5MGdlOE4yYXFnUnVaQVBhb1RRUEJWV3RJUlZLRno1eGZtNmROVUNJS0hFWlNKRnB6bDN6MlV4Z2Q5UVpSbVRjU2pBaVhQQT0iLCJhbXIiOlsid2lhIiwibWZhIl0sImFwcGlkIjoiYzQ0YjQwODMtM2JiMC00OWMxLWI0N2QtOTc0ZTUzY2JkZjNjIiwiYXBwaWRhY3IiOiIwIiwiZmFtaWx5X25hbWUiOiJaYW5nIiwiZ2l2ZW5fbmFtZSI6IkhhaWJpbiIsImlwYWRkciI6IjE2Ny4yMjAuMjU1LjEwNCIsIm5hbWUiOiJIYWliaW4gWmFuZyIsIm9pZCI6ImQzNDA0ZjlkLTRkNjEtNGFkYi1iZmJmLTJmNDdmZTE4NTYwNiIsIm9ucHJlbV9zaWQiOiJTLTEtNS0yMS0yMTI3NTIxMTg0LTE2MDQwMTI5MjAtMTg4NzkyNzUyNy01ODAyMzEyNSIsInB1aWQiOiIxMDAzREZGRDAxNjMyMzNBIiwicmgiOiIwLkFBQUFKdFNHOWhhTjIwS0J0NnRYamhFTXpVWklmM2tBdXRkUHVrUGF3ZmoyTUJNQkFDVS4iLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJzdWIiOiJXMHZ2N2ZVbTBrSXY0ZkZHWFJiUDdlcHhKeTc2QUZ4SkJFX25YVUhSekVrIiwidGlkIjoiZjY4NmQ0MjYtOGQxNi00MmRiLTgxYjctYWI1NzhlMTEwY2NkIiwidW5pcXVlX25hbWUiOiJ0LWhhaWJpbnphbmdAbWljcm9zb2Z0LmNvbSIsInVwbiI6InQtaGFpYmluemFuZ0BtaWNyb3NvZnQuY29tIiwidXRpIjoiQURnYzlZc01YRWVKVzl0R21ZSWpBQSIsInZlciI6IjEuMCIsInhtc190Y2R0IjoxMjg0NjkwNjkzfQ.fMrPO-yCypLLekQ3mKsW6ejbL-6zUQ2n7LYs3r9xpOmKHerOuyML0PVBj0ju1gzO1QzFJ4tAGXeE40ipjOcu6G2m6brBgKxiJEqdNUnXr50AazVaz-s5QvyDuUzdXcX-0zpcW9r7NZ0kdiGaG4lcPTRFmJldvmOQtMKNTRSXgVMbDgx1P4X_j5DXp3AQ6L6adl5kT4_scDazc8UVwAoNnvaPSjNssQzie16bVMIJNsjyyhTKb8xURGBP2VSNMIcoHYJkxOizs7mYM4EkDL6FabPmiTVppFrGplmIkHxswzwReMeEskdTe4bLsLBTEroEiwH4wP-bOMwPd7pPj5Tcvg";
        else if(moreConfiguration.getConfigurationEnv() == MoreConfiguration.Env.Prod)
            invalidAccessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IjJaUXBKM1VwYmpBWVhZR2FYRUpsOGxWMFRPSSIsImtpZCI6IjJaUXBKM1VwYmpBWVhZR2FYRUpsOGxWMFRPSSJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC83MmY5ODhiZi04NmYxLTQxYWYtOTFhYi0yZDdjZDAxMWRiNDcvIiwiaWF0IjoxNjU2NDE3ODc3LCJuYmYiOjE2NTY0MTc4NzcsImV4cCI6MTY1NjQyMzQzNCwiX2NsYWltX25hbWVzIjp7Imdyb3VwcyI6InNyYzEifSwiX2NsYWltX3NvdXJjZXMiOnsic3JjMSI6eyJlbmRwb2ludCI6Imh0dHBzOi8vZ3JhcGgud2luZG93cy5uZXQvNzJmOTg4YmYtODZmMS00MWFmLTkxYWItMmQ3Y2QwMTFkYjQ3L3VzZXJzL2IzNDViMTYxLWIwMjEtNGEzOS1iMWEzLTE3NDRkZDQ0MzRkZC9nZXRNZW1iZXJPYmplY3RzIn19LCJhY3IiOiIxIiwiYWlvIjoiQVZRQXEvOFRBQUFBSkZ0bU9YZy95alZzVlpLaklWUU9IeUlRaE83QWNTaXRjY2FpcGJNTVlMSEh1ZlFmMGQ4YXQ0UlFkdEhkT25FMDc0RVB0UW1hWi9rejJObGs0Q25rakkyN1dPT0xtdFlBdWlDQ01Wb2Q0MGM9IiwiYW1yIjpbInJzYSIsIm1mYSJdLCJhcHBpZCI6ImM0NGI0MDgzLTNiYjAtNDljMS1iNDdkLTk3NGU1M2NiZGYzYyIsImFwcGlkYWNyIjoiMCIsImRldmljZWlkIjoiM2MwM2U3MGMtZWVkMS00NWNjLWIyYWYtMmRjMmRhMTcxYjUxIiwiZmFtaWx5X25hbWUiOiJaYW5nIiwiZ2l2ZW5fbmFtZSI6IkhhaWJpbiIsImlwYWRkciI6IjIyMy43OS4xMC45OCIsIm5hbWUiOiJIYWliaW4gWmFuZyIsIm9pZCI6ImIzNDViMTYxLWIwMjEtNGEzOS1iMWEzLTE3NDRkZDQ0MzRkZCIsIm9ucHJlbV9zaWQiOiJTLTEtNS0yMS0yMTI3NTIxMTg0LTE2MDQwMTI5MjAtMTg4NzkyNzUyNy01ODAyMzEyNSIsInB1aWQiOiIxMDAzMjAwMjAwMkUzRkE4IiwicmgiOiIwLkFSb0F2NGo1Y3ZHR3IwR1JxeTE4MEJIYlIwWklmM2tBdXRkUHVrUGF3ZmoyTUJNYUFIYy4iLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJzdWIiOiJ3YXB5ZkZDTHJoTEwxbzFsQXdYZVNaN0NzNndkT2VOWE5ESzlrd2lmTUVzIiwidGlkIjoiNzJmOTg4YmYtODZmMS00MWFmLTkxYWItMmQ3Y2QwMTFkYjQ3IiwidW5pcXVlX25hbWUiOiJ0LWhhaWJpbnphbmdAbWljcm9zb2Z0LmNvbSIsInVwbiI6InQtaGFpYmluemFuZ0BtaWNyb3NvZnQuY29tIiwidXRpIjoiSElaUmtrRXZla1M3ZzBnZFJ6dDlBUSIsInZlciI6IjEuMCIsInhtc190Y2R0IjoxMjg5MjQxNTQ3fQ.ShiGd1LExpfgTm1N9MmX1zAeW_m-5UBtcmnD-4AB4J70T3Sb6_WgKMAB54Y9ti28G6G8ZvVCJBvr_D1xPo0ngtB5Z7lNRAidvyhlHkfOxLkHx0FBgG66c4GUiTlnygRD7_ie6sw2oGYkccxwaoAZigTdOB1ghPFBZWl5lGr7zOz-WXqjAiTf6v2gPFkB-y-FU3QcRi5VvIHNFg-wiaD3CoIPwF-0lN3OUCb2x8_-Fwf3CGJxgE2_W1WFLE4-IyQz6tUykEsJI5D4cDTk1kFD6LSQ0rBzDHI8JUo37QAQc4y7G0VDZ_u7lGRLojJj7Ag4SeR-L9veWV2iGY1OxT63pw";
        else {
            System.out.println("Skip test on unrecognized env=" + moreConfiguration.getConfigurationEnv());
            return;
        }
        AzureManagementAPI testBase = new AzureManagementAPI(moreConfiguration.getBaseUrl(), invalidAccessToken);
        StatusCodeException statusCodeException = assertThrows(StatusCodeException.class, () -> {
            testBase.skus(moreConfiguration.getSubscriptionId(), moreConfiguration.getTestBaseApiVersion());
        });
        assertEquals(401, statusCodeException.getStatusCode());
        assertEquals("ExpiredAuthenticationToken", statusCodeException.getErrorContent().getError().getCode());
    }


    @Test
    public void testResourceGroupsList() throws IOException {
        AzureManagementAPI testBase = new AzureManagementAPI(
            moreConfiguration.getBaseUrl(), 
            moreConfiguration.getTenantId(), 
            moreConfiguration.getClientId(), 
            moreConfiguration.getClientSecret(), 
            moreConfiguration.getAuthorityHost(), 
            moreConfiguration.getScope());
        AzureManagementResult<Page<ResourceGroup>> pageResult = testBase.resourceGroupsList(
            moreConfiguration.getSubscriptionId(), 
            moreConfiguration.getResourceManagementApiVersion());
        assertNotNull(pageResult.getEntity());
        System.out.println(pageResult);
    }
}
