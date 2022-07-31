package io.jenkins.plugins.azuretestbase.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.jenkins.plugins.azuretestbase.dm.ErrorContent;
import io.jenkins.plugins.azuretestbase.dm.FileUploadUrl;
import io.jenkins.plugins.azuretestbase.dm.FileUploadUrlBody;
import io.jenkins.plugins.azuretestbase.dm.Package;
import io.jenkins.plugins.azuretestbase.dm.Page;
import io.jenkins.plugins.azuretestbase.dm.ResourceGroup;
import io.jenkins.plugins.azuretestbase.dm.Skus;
import io.jenkins.plugins.azuretestbase.dm.TestSummaryResource;
import io.jenkins.plugins.azuretestbase.exceptions.HeaderException;
import io.jenkins.plugins.azuretestbase.exceptions.StatusCodeException;
import io.jenkins.plugins.azuretestbase.exceptions.TestBaseException;
import io.jenkins.plugins.azuretestbase.json.JsonConvertion;
import lombok.Getter;
import reactor.core.publisher.Mono;

public class AzureManagementAPI {
    @NonNull
    private final HttpClientWrapper clientWrapper;

    @Getter
    private String accessToken;

    @NonNull
    @Getter
    private final String baseUrl;

    
    // Service principal authentication
    @CheckForNull
    private ClientSecretCredential clientSecretCredential = null;
    private String[] scopes;


    // initialize using access token to access TestBase restful API
    public AzureManagementAPI(@NonNull String baseUrl, @NonNull String accessToken) {
        clientWrapper = new HttpClientWrapper();
        this.baseUrl = baseUrl;
        this.accessToken = accessToken;
    }


    // initialze using clientId and clientSecret
    public AzureManagementAPI(@NonNull String baseUrl, 
        @NonNull String tenantId, 
        @NonNull String clientId, 
        @NonNull String clientSecret, 
        String authorityHost,
        String... scopes) {

        clientWrapper = new HttpClientWrapper();
        this.baseUrl = baseUrl;
        this.scopes = scopes;
        ClientSecretCredentialBuilder clientSecretCredentialBuilder = new ClientSecretCredentialBuilder()
            .clientId(clientId)
            .clientSecret(clientSecret)
            .tenantId(tenantId);

        if(authorityHost != null)
            clientSecretCredentialBuilder = clientSecretCredentialBuilder.authorityHost(authorityHost);
        
        this.clientSecretCredential = clientSecretCredentialBuilder.build();
        updateAccessToken();
    }


    // update accesstoken
    private void updateAccessToken() {
        if(clientSecretCredential == null || scopes == null)
            throw new TestBaseException("update accesstoken without initializing clientId, clientSecret or scopes");
        Mono<AccessToken> mono = clientSecretCredential.getToken(new TokenRequestContext().addScopes(scopes));
        if(mono == null)
            throw new TestBaseException("update accesstoken failed");
        AccessToken at = mono.block();
        if(at == null)
            throw new TestBaseException("update accesstoken failed");
        this.accessToken = at.getToken();
    }


    // throw StatusCodeException if statusCode is not in idealStatusCode
    private void raiseStatusCode(int statusCode, String content, int... idealStatusCode) {
        boolean legalStatusCode = false;
        if(idealStatusCode == null || idealStatusCode.length <= 0)
            idealStatusCode = new int[]{200};
        for(int oneCode : idealStatusCode)
            if(statusCode == oneCode) {
                legalStatusCode = true;
                break;
            }
            
        ErrorContent errorContent;
        if(!legalStatusCode) {
            try {
                errorContent = JsonConvertion.jsonToObj(content, ErrorContent.class);
                throw new StatusCodeException(statusCode, errorContent);
            } catch(JsonProcessingException e) {
                throw new StatusCodeException(statusCode, e);
            }
        }
    }

    
    // read content from response and convert it to json entity and return the entity
    private <T> T extractJsonFormatEntity(HttpResponse response, TypeReference<T> t, int... idealStatusCode) 
        throws JsonMappingException, JsonProcessingException, IOException {

        Header header = response.getEntity().getContentType();
        String idealHeaderValue = "application/json";
        int statusCode = response.getStatusLine().getStatusCode();
        InputStream input = response.getEntity().getContent();
        Charset charset = clientWrapper.getCharset();
        String content = new String(Utils.readAllBytes(input), charset);

        // judge if content-type is application/json
        if(header == null)
            throw new HeaderException(statusCode, null, idealHeaderValue, content);
        if(!header.getValue().contains(idealHeaderValue))
            throw new HeaderException(statusCode, header, idealHeaderValue, content);

        // check the status code
        raiseStatusCode(statusCode, content, idealStatusCode);
        T entity = JsonConvertion.jsonToObj(content, t);

        return entity;
    }


    // generate mandatory headers of testbase api
    private Map<String, String> mandatoryHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        return headers;
    }


    // generate mandatory params of testbase api
    private Map<String, String> mandatoryParams(@NonNull String apiVersion) {
        Map<String, String> params = new HashMap<>();
        params.put("api-version", apiVersion);
        return params;
    }


    // request skus of TestBase
    public AzureManagementResult<Page<Skus>> skus(@NonNull String subscriptionId, @NonNull String apiVersion)
        throws JsonMappingException, JsonProcessingException, ClientProtocolException, IOException {
        
        // construct path
        String requestPath = "/subscriptions/%s/providers/Microsoft.TestBase/skus";
        requestPath = String.format(requestPath, subscriptionId);

        // construct params
        Map<String, String> params = mandatoryParams(apiVersion);

        // construct headers
        Map<String, String> headers = mandatoryHeaders();
        headers.put("Accept", "application/json");

        // get content
        HttpResponse response = clientWrapper.get(baseUrl + requestPath, params, headers);

        // get and return final json object
        Page<Skus> entity = extractJsonFormatEntity(response, new TypeReference<Page<Skus>>() {}, 200);
        AzureManagementResult<Page<Skus>> ret = new AzureManagementResult<>(response.getStatusLine().getStatusCode(), entity);
        return ret;
    }

    
    // request getFileUploadUrl of TestBase
    public AzureManagementResult<FileUploadUrl> getFileUploadUrl(@NonNull String subscriptionId, 
        @NonNull String resourceGroupName, 
        @NonNull String testBaseAccountName, 
        @NonNull String apiVersion, 
        @NonNull FileUploadUrlBody bodyEntity)
        throws JsonMappingException, JsonProcessingException, ClientProtocolException, IOException {
        
        // construct path
        String requestPath = "/subscriptions/%s/resourceGroups/%s/providers/Microsoft.TestBase/testBaseAccounts/%s/getFileUploadUrl";
        requestPath = String.format(requestPath, subscriptionId, resourceGroupName, testBaseAccountName);

        // construct params
        Map<String, String> params = mandatoryParams(apiVersion);
        
        // construct headers
        Map<String, String> headers = mandatoryHeaders();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");

        // get content
        String payload = JsonConvertion.objToJson(bodyEntity);
        HttpResponse response = clientWrapper.post(baseUrl + requestPath, params, headers, payload);

        // get and return final json object
        FileUploadUrl entity = extractJsonFormatEntity(response, new TypeReference<FileUploadUrl>() {}, 200);
        AzureManagementResult<FileUploadUrl> ret = new AzureManagementResult<>(response.getStatusLine().getStatusCode(), entity);
        return ret;
    }


    public void uploadFile(@NonNull String uploadUrl, File file)
        throws JsonMappingException, JsonProcessingException, ClientProtocolException, IOException {
        
        // the uploadUrl has contained the required params
        Map<String, String> params = null;
        
        // no need of authentication
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("x-ms-blob-type", "BlockBlob");

        // do it
        HttpResponse response = clientWrapper.put(uploadUrl, params, headers, file);
        int statusCode = response.getStatusLine().getStatusCode();
        String content = new String(Utils.readAllBytes(response.getEntity().getContent()), clientWrapper.getCharset());
        if(statusCode != 200 && statusCode != 201)
            throw new StatusCodeException(statusCode, content);
    }


    public void uploadFile(@NonNull String uploadUrl, byte[] bytes)
        throws JsonMappingException, JsonProcessingException, ClientProtocolException, IOException {
        
        // the uploadUrl has contained the required params
        Map<String, String> params = null;
        
        // no need of authentication
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("x-ms-blob-type", "BlockBlob");

        // do it
        HttpResponse response = clientWrapper.put(uploadUrl, params, headers, bytes);
        int statusCode = response.getStatusLine().getStatusCode();
        String content = new String(Utils.readAllBytes(response.getEntity().getContent()), clientWrapper.getCharset());
        if(statusCode != 200 && statusCode != 201)
            throw new StatusCodeException(statusCode, content);
    }


    // create a testbase package
    public AzureManagementResult<Package> createPackage(@NonNull String subscriptionId, 
        @NonNull String resourceGroupName, 
        @NonNull String testBaseAccountName, 
        @NonNull String packageName, 
        @NonNull String apiVersion, 
        @NonNull Package bodyEntity)
        throws JsonMappingException, JsonProcessingException, ClientProtocolException, IOException {

        // construct path
        String requestPath = "/subscriptions/%s/resourceGroups/%s/providers/Microsoft.TestBase/testBaseAccounts/%s/packages/%s";
        requestPath = String.format(requestPath, subscriptionId, resourceGroupName, testBaseAccountName, packageName);

        // construct params
        Map<String, String> params = mandatoryParams(apiVersion);
        
        // construct headers
        Map<String, String> headers = mandatoryHeaders();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");

        // only serialize request relevant fields
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("Package", SimpleBeanPropertyFilter.filterOutAllExcept(
            "tags",
            "location",
            "properties"
        ));
        filterProvider.addFilter("Properties", SimpleBeanPropertyFilter.filterOutAllExcept(
            "applicationName",
            "version",
            "targetOSList",
            "flightingRing",
            "blobPath",
            "tests",
            "isEnabled"
        ));
        filterProvider.addFilter("TestBaseTest", SimpleBeanPropertyFilter.filterOutAllExcept(
            "testType",
            "isActive",
            "commands"
        ));
        String payload = JsonConvertion.objToJson(bodyEntity, filterProvider);
        HttpResponse response = clientWrapper.put(baseUrl + requestPath, params, headers, payload);

        // get and return final json object
        Package entity = extractJsonFormatEntity(response, new TypeReference<Package>() {}, 200, 201);
        AzureManagementResult<Package> ret = new AzureManagementResult<>(response.getStatusLine().getStatusCode(), entity);
        return ret;
    }


    // get a testbase package
    public AzureManagementResult<Package> getPackage(@NonNull String subscriptionId, 
        @NonNull String resourceGroupName, 
        @NonNull String testBaseAccountName, 
        @NonNull String packageName, 
        @NonNull String apiVersion)
        throws JsonMappingException, JsonProcessingException, ClientProtocolException, IOException {

        // construct path
        String requestPath = "/subscriptions/%s/resourceGroups/%s/providers/Microsoft.TestBase/testBaseAccounts/%s/packages/%s";
        requestPath = String.format(requestPath, subscriptionId, resourceGroupName, testBaseAccountName, packageName);

        // construct params
        Map<String, String> params = mandatoryParams(apiVersion);
        
        // construct headers
        Map<String, String> headers = mandatoryHeaders();
        headers.put("Accept", "application/json");

        HttpResponse response = clientWrapper.get(baseUrl + requestPath, params, headers);

        // get and return final json object
        Package entity = extractJsonFormatEntity(response, new TypeReference<Package>() {}, 200);
        AzureManagementResult<Package> ret = new AzureManagementResult<>(response.getStatusLine().getStatusCode(), entity);
        return ret;
    }


    // get testSummaries under specified TestBase account
    public AzureManagementResult<Page<TestSummaryResource>> testSummaries(@NonNull String subscriptionId, 
        @NonNull String resourceGroupName, 
        @NonNull String testBaseAccountName, 
        @NonNull String apiVersion)
        throws JsonMappingException, JsonProcessingException, ClientProtocolException, IOException {

        // construct path
        String requestPath = "/subscriptions/%s/resourceGroups/%s/providers/Microsoft.TestBase/testBaseAccounts/%s/testSummaries";
        requestPath = String.format(requestPath, subscriptionId, resourceGroupName, testBaseAccountName);

        // construct params
        Map<String, String> params = mandatoryParams(apiVersion);
        
        // construct headers
        Map<String, String> headers = mandatoryHeaders();
        headers.put("Accept", "application/json");

        HttpResponse response = clientWrapper.get(baseUrl + requestPath, params, headers);

        // get and return final json object
        Page<TestSummaryResource> entity = extractJsonFormatEntity(response, new TypeReference<Page<TestSummaryResource>>() {}, 200);
        AzureManagementResult<Page<TestSummaryResource>> ret = new AzureManagementResult<>(response.getStatusLine().getStatusCode(), entity);
        return ret;
    }


    // get resource group list of a subscriptionId
    public AzureManagementResult<Page<ResourceGroup>> resourceGroupsList(@NonNull String subscriptionId, @NonNull String apiVersion)
        throws JsonMappingException, JsonProcessingException, ClientProtocolException, IOException {
        
        // construct path
        String requestPath = "/subscriptions/%s/resourcegroups";
        requestPath = String.format(requestPath, subscriptionId);

        // construct params
        Map<String, String> params = mandatoryParams(apiVersion);

        // construct headers
        Map<String, String> headers = mandatoryHeaders();
        headers.put("Accept", "application/json");

        // get content
        HttpResponse response = clientWrapper.get(baseUrl + requestPath, params, headers);

        // get and return final json object
        Page<ResourceGroup> entity = extractJsonFormatEntity(response, new TypeReference<Page<ResourceGroup>>() {}, 200);
        AzureManagementResult<Page<ResourceGroup>> ret = new AzureManagementResult<>(response.getStatusLine().getStatusCode(), entity);
        return ret;
    }
}
