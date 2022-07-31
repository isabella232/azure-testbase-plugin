package io.jenkins.plugins.azuretestbase.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import io.jenkins.plugins.azuretestbase.dm.ErrorContent;
import io.jenkins.plugins.azuretestbase.dm.Package;
import io.jenkins.plugins.azuretestbase.dm.Page;
import io.jenkins.plugins.azuretestbase.dm.Skus;
import io.jenkins.plugins.azuretestbase.dm.TestBaseConfiguration;
import io.jenkins.plugins.azuretestbase.dm.TestSummaryResource;

public class JsonConvertionTest {

    @Test
    public void testSimpleConvertion() {
        String jsonStr = "{\"value\": [{\"resourceType\": \"string\", \"name\": \"string\", \"tier\": \"Standard\", \"capabilities\": [{\"name\": \"string\", \"value\": \"string\"}], \"locations\": [\"string\"]}], \"nextLink\": \"string\"}";
        String target = "{\"nextLink\":\"string\",\"value\":[{\"resourceType\":\"string\",\"name\":\"string\",\"tier\":\"Standard\",\"capabilities\":[{\"name\":\"string\",\"value\":\"string\"}],\"locations\":[\"string\"]}]}";
        try {
            Page<Skus> expectedResult = JsonConvertion.jsonToObj(jsonStr, new TypeReference<Page<Skus>>() {});
            String convertedStr = JsonConvertion.objToJson(expectedResult);
            assertEquals(target, convertedStr);
            Page<Skus> result = JsonConvertion.jsonToObj(jsonStr, Page.class, Skus.class);
            convertedStr = JsonConvertion.objToJson(result);
            assertEquals(expectedResult, result);
            assertEquals(target, convertedStr);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            assertTrue(true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(true);
        }
    }

    
    @Test
    public void testConvertionLackParameter() {
        String jsonStr = "{\"value\": [{\"name\": \"string\", \"tier\": \"Standard\", \"capabilities\": [{\"name\": \"string\"}], \"locations\": [\"string\"]}]}";
        String target = "{\"nextLink\":null,\"value\":[{\"resourceType\":null,\"name\":\"string\",\"tier\":\"Standard\",\"capabilities\":[{\"name\":\"string\",\"value\":null}],\"locations\":[\"string\"]}]}";
        try {
            Page<Skus> expectedResult = JsonConvertion.jsonToObj(jsonStr, new TypeReference<Page<Skus>>() {});
            String convertedStr = JsonConvertion.objToJson(expectedResult);
            assertEquals(target, convertedStr);
            Page<Skus> result = JsonConvertion.jsonToObj(jsonStr, Page.class, Skus.class);
            convertedStr = JsonConvertion.objToJson(result);
            assertEquals(expectedResult, result);
            assertEquals(target, convertedStr);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            assertTrue(true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(true);
        }
    }


    @Test
    public void testConvertionSpareParameter() {
        String jsonStr = "{\"value\": [{\"resourceType\": \"string\", \"name\": \"string\", \"tier\": \"Standard\", \"capabilities\": [{\"name\": \"string\", \"value\": \"string\"}], \"locations\": [\"string\"]}], \"nextLink\": \"string\", \"foo\": \"bar\"}";
        String target = "{\"nextLink\":\"string\",\"value\":[{\"resourceType\":\"string\",\"name\":\"string\",\"tier\":\"Standard\",\"capabilities\":[{\"name\":\"string\",\"value\":\"string\"}],\"locations\":[\"string\"]}]}";
        try {
            Page<Skus> expectedResult = JsonConvertion.jsonToObj(jsonStr, new TypeReference<Page<Skus>>() {});
            String convertedStr = JsonConvertion.objToJson(expectedResult);
            assertEquals(target, convertedStr);
            Page<Skus> result = JsonConvertion.jsonToObj(jsonStr, Page.class, Skus.class);
            convertedStr = JsonConvertion.objToJson(result);
            assertEquals(expectedResult, result);
            assertEquals(target, convertedStr);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            assertTrue(true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(true);
        }
    }


    @Test
    public void testConvertionSpareMoreParameter() {
        String jsonStr = "{\"value\": [{\"else\": \"noting\", \"resourceType\": \"string\", \"name\": \"string\", \"tier\": \"Standard\", \"capabilities\": [{\"name\": \"string\", \"value\": \"string\", \"tem\": \"nothing\"}], \"locations\": [\"string\"]}], \"nextLink\": \"string\", \"foo\": \"bar\"}";
        String target = "{\"nextLink\":\"string\",\"value\":[{\"resourceType\":\"string\",\"name\":\"string\",\"tier\":\"Standard\",\"capabilities\":[{\"name\":\"string\",\"value\":\"string\"}],\"locations\":[\"string\"]}]}";
        try {
            Page<Skus> expectedResult = JsonConvertion.jsonToObj(jsonStr, new TypeReference<Page<Skus>>() {});
            String convertedStr = JsonConvertion.objToJson(expectedResult);
            assertEquals(target, convertedStr);
            Page<Skus> result = JsonConvertion.jsonToObj(jsonStr, Page.class, Skus.class);
            convertedStr = JsonConvertion.objToJson(result);
            assertEquals(expectedResult, result);
            assertEquals(target, convertedStr);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            assertTrue(true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(true);
        }
    }


    @Test
    public void testConvertionViaClass() {
        String jsonStr = "{\"error\": {\"code\": \"string\", \"message\": \"string\", \"target\": \"string\", \"details\": [{\"code\": \"string\", \"message\": \"string\"}]}}";
        String target = "{\"error\":{\"code\":\"string\",\"message\":\"string\",\"target\":\"string\",\"details\":[{\"code\":\"string\",\"message\":\"string\",\"target\":null,\"details\":null}]}}";
        try {
            ErrorContent expectedResult = JsonConvertion.jsonToObj(jsonStr, new TypeReference<ErrorContent>() {});
            String convertedStr = JsonConvertion.objToJson(expectedResult);
            assertEquals(target, convertedStr);
            ErrorContent result = JsonConvertion.jsonToObj(jsonStr, ErrorContent.class);
            convertedStr = JsonConvertion.objToJson(result);
            assertEquals(expectedResult, result);
            assertEquals(target, convertedStr);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            assertTrue(true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(true);
        }
    }


    @Test
    public void testOmitSerialize() {
        String jsonStr = "{\"tags\": {\"\u4f60\u7684\u540d\u5b57\": \"\u597d\u770b\"}, \"location\": \"global\", \"properties\": {\"applicationName\": \"\", \"version\": \"\", \"targetOSList\": [{\"osUpdateType\": \"Security updates\", \"targetOSs\": [\"Windows 11 21H2\", \"Windows 10 21H2\"]}, {\"osUpdateType\": \"Feature updates\", \"targetOSs\": [\"Windows 10 21H2\"]}], \"flightingRing\": \"Insider Beta Channel\", \"blobPath\": \"\", \"tests\": [{\"commands\": [{\"action\": \"Install\", \"alwaysRun\": false, \"applyUpdateBefore\": false, \"content\": \"scripts/outofbox/install.ps1\", \"contentType\": \"Path\", \"maxRunTime\": 0, \"name\": \"install\", \"restartAfter\": true, \"runAsInteractive\": false, \"runElevated\": false}, {\"action\": \"Launch\", \"alwaysRun\": false, \"applyUpdateBefore\": false, \"content\": \"scripts/outofbox/launch.ps1\", \"contentType\": \"Path\", \"maxRunTime\": 0, \"name\": \"launch\", \"restartAfter\": false, \"runAsInteractive\": false, \"runElevated\": false}, {\"action\": \"Close\", \"alwaysRun\": false, \"applyUpdateBefore\": false, \"content\": \"scripts/outofbox/close.ps1\", \"contentType\": \"Path\", \"maxRunTime\": 0, \"name\": \"close\", \"restartAfter\": false, \"runAsInteractive\": false, \"runElevated\": false}, {\"action\": \"Uninstall\", \"alwaysRun\": false, \"applyUpdateBefore\": false, \"content\": \"scripts/outofbox/uninstall.ps1\", \"contentType\": \"Path\", \"maxRunTime\": 0, \"name\": \"uninstall\", \"restartAfter\": false, \"runAsInteractive\": false, \"runElevated\": false}], \"isActive\": true, \"testType\": \"OutOfBoxTest\"}, {\"commands\": [{\"action\": \"Custom\", \"alwaysRun\": false, \"applyUpdateBefore\": true, \"content\": \"scripts/functional/script1.ps1\", \"contentType\": \"Path\", \"maxRunTime\": 0, \"name\": \"script1\", \"restartAfter\": true, \"runAsInteractive\": false, \"runElevated\": false}, {\"action\": \"Custom\", \"alwaysRun\": false, \"applyUpdateBefore\": false, \"content\": \"scripts/functional/script2.ps1\", \"contentType\": \"Path\", \"maxRunTime\": 0, \"name\": \"script2\", \"restartAfter\": true, \"runAsInteractive\": false, \"runElevated\": false}], \"isActive\": true, \"testType\": \"FunctionalTest\"}]}}";
        String target = "{\"location\":\"global\",\"properties\":{\"applicationName\":\"\",\"version\":\"\",\"targetOSList\":[{\"osUpdateType\":\"Security updates\",\"targetOSs\":[\"Windows 11 21H2\",\"Windows 10 21H2\"]},{\"osUpdateType\":\"Feature updates\",\"targetOSs\":[\"Windows 10 21H2\"]}],\"flightingRing\":\"Insider Beta Channel\",\"blobPath\":\"\",\"tests\":[{\"testType\":\"OutOfBoxTest\",\"commands\":[{\"name\":\"install\",\"action\":\"Install\",\"contentType\":\"Path\",\"content\":\"scripts/outofbox/install.ps1\",\"runElevated\":false,\"restartAfter\":true,\"maxRunTime\":0,\"runAsInteractive\":false,\"alwaysRun\":false,\"applyUpdateBefore\":false},{\"name\":\"launch\",\"action\":\"Launch\",\"contentType\":\"Path\",\"content\":\"scripts/outofbox/launch.ps1\",\"runElevated\":false,\"restartAfter\":false,\"maxRunTime\":0,\"runAsInteractive\":false,\"alwaysRun\":false,\"applyUpdateBefore\":false},{\"name\":\"close\",\"action\":\"Close\",\"contentType\":\"Path\",\"content\":\"scripts/outofbox/close.ps1\",\"runElevated\":false,\"restartAfter\":false,\"maxRunTime\":0,\"runAsInteractive\":false,\"alwaysRun\":false,\"applyUpdateBefore\":false},{\"name\":\"uninstall\",\"action\":\"Uninstall\",\"contentType\":\"Path\",\"content\":\"scripts/outofbox/uninstall.ps1\",\"runElevated\":false,\"restartAfter\":false,\"maxRunTime\":0,\"runAsInteractive\":false,\"alwaysRun\":false,\"applyUpdateBefore\":false}],\"isActive\":true},{\"testType\":\"FunctionalTest\",\"commands\":[{\"name\":\"script1\",\"action\":\"Custom\",\"contentType\":\"Path\",\"content\":\"scripts/functional/script1.ps1\",\"runElevated\":false,\"restartAfter\":true,\"maxRunTime\":0,\"runAsInteractive\":false,\"alwaysRun\":false,\"applyUpdateBefore\":true},{\"name\":\"script2\",\"action\":\"Custom\",\"contentType\":\"Path\",\"content\":\"scripts/functional/script2.ps1\",\"runElevated\":false,\"restartAfter\":true,\"maxRunTime\":0,\"runAsInteractive\":false,\"alwaysRun\":false,\"applyUpdateBefore\":false}],\"isActive\":true}]},\"tags\":{\"你的名字\":\"好看\"}}";
        try {
            Package expectedResult = JsonConvertion.jsonToObj(jsonStr, Package.class);
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("Properties", SimpleBeanPropertyFilter.filterOutAllExcept(
                "applicationName",
                "version",
                "targetOSList",
                "flightingRing",
                "blobPath",
                "tests"
            ));
            filterProvider.addFilter("TestBaseTest", SimpleBeanPropertyFilter.filterOutAllExcept(
                "testType",
                "isActive",
                "commands"
            ));
            filterProvider.addFilter("Package", SimpleBeanPropertyFilter.filterOutAllExcept(
                "tags",
                "location",
                "properties"
            ));
            filterProvider.setFailOnUnknownId(false);
            String convertedStr = JsonConvertion.objToJson(expectedResult, filterProvider);
            assertEquals(target, convertedStr);
            Package result = JsonConvertion.jsonToObj(jsonStr, Package.class);
            assertEquals(expectedResult, result);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            assertTrue(true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(true);
        }
    }


    @Test
    public void testNoOmitSerialize() {
        String jsonStr = "{\"tags\": {\"\u4f60\u7684\u540d\u5b57\": \"\u597d\u770b\"}, \"location\": \"global\", \"properties\": {\"applicationName\": \"\", \"version\": \"\", \"targetOSList\": [{\"osUpdateType\": \"Security updates\", \"targetOSs\": [\"Windows 11 21H2\", \"Windows 10 21H2\"]}, {\"osUpdateType\": \"Feature updates\", \"targetOSs\": [\"Windows 10 21H2\"]}], \"flightingRing\": \"Insider Beta Channel\", \"blobPath\": \"\", \"tests\": [{\"commands\": [{\"action\": \"Install\", \"alwaysRun\": false, \"applyUpdateBefore\": false, \"content\": \"scripts/outofbox/install.ps1\", \"contentType\": \"Path\", \"maxRunTime\": 0, \"name\": \"install\", \"restartAfter\": true, \"runAsInteractive\": false, \"runElevated\": false}, {\"action\": \"Launch\", \"alwaysRun\": false, \"applyUpdateBefore\": false, \"content\": \"scripts/outofbox/launch.ps1\", \"contentType\": \"Path\", \"maxRunTime\": 0, \"name\": \"launch\", \"restartAfter\": false, \"runAsInteractive\": false, \"runElevated\": false}, {\"action\": \"Close\", \"alwaysRun\": false, \"applyUpdateBefore\": false, \"content\": \"scripts/outofbox/close.ps1\", \"contentType\": \"Path\", \"maxRunTime\": 0, \"name\": \"close\", \"restartAfter\": false, \"runAsInteractive\": false, \"runElevated\": false}, {\"action\": \"Uninstall\", \"alwaysRun\": false, \"applyUpdateBefore\": false, \"content\": \"scripts/outofbox/uninstall.ps1\", \"contentType\": \"Path\", \"maxRunTime\": 0, \"name\": \"uninstall\", \"restartAfter\": false, \"runAsInteractive\": false, \"runElevated\": false}], \"isActive\": true, \"testType\": \"OutOfBoxTest\"}, {\"commands\": [{\"action\": \"Custom\", \"alwaysRun\": false, \"applyUpdateBefore\": true, \"content\": \"scripts/functional/script1.ps1\", \"contentType\": \"Path\", \"maxRunTime\": 0, \"name\": \"script1\", \"restartAfter\": true, \"runAsInteractive\": false, \"runElevated\": false}, {\"action\": \"Custom\", \"alwaysRun\": false, \"applyUpdateBefore\": false, \"content\": \"scripts/functional/script2.ps1\", \"contentType\": \"Path\", \"maxRunTime\": 0, \"name\": \"script2\", \"restartAfter\": true, \"runAsInteractive\": false, \"runElevated\": false}], \"isActive\": true, \"testType\": \"FunctionalTest\"}]}}";
        String target = "{\"location\":\"global\",\"properties\":{\"applicationName\":\"\",\"version\":\"\",\"targetOSList\":[{\"osUpdateType\":\"Security updates\",\"targetOSs\":[\"Windows 11 21H2\",\"Windows 10 21H2\"]},{\"osUpdateType\":\"Feature updates\",\"targetOSs\":[\"Windows 10 21H2\"]}],\"flightingRing\":\"Insider Beta Channel\",\"blobPath\":\"\",\"tests\":[{\"testType\":\"OutOfBoxTest\",\"commands\":[{\"name\":\"install\",\"action\":\"Install\",\"contentType\":\"Path\",\"content\":\"scripts/outofbox/install.ps1\",\"runElevated\":false,\"restartAfter\":true,\"maxRunTime\":0,\"runAsInteractive\":false,\"alwaysRun\":false,\"applyUpdateBefore\":false},{\"name\":\"launch\",\"action\":\"Launch\",\"contentType\":\"Path\",\"content\":\"scripts/outofbox/launch.ps1\",\"runElevated\":false,\"restartAfter\":false,\"maxRunTime\":0,\"runAsInteractive\":false,\"alwaysRun\":false,\"applyUpdateBefore\":false},{\"name\":\"close\",\"action\":\"Close\",\"contentType\":\"Path\",\"content\":\"scripts/outofbox/close.ps1\",\"runElevated\":false,\"restartAfter\":false,\"maxRunTime\":0,\"runAsInteractive\":false,\"alwaysRun\":false,\"applyUpdateBefore\":false},{\"name\":\"uninstall\",\"action\":\"Uninstall\",\"contentType\":\"Path\",\"content\":\"scripts/outofbox/uninstall.ps1\",\"runElevated\":false,\"restartAfter\":false,\"maxRunTime\":0,\"runAsInteractive\":false,\"alwaysRun\":false,\"applyUpdateBefore\":false}],\"isActive\":true},{\"testType\":\"FunctionalTest\",\"commands\":[{\"name\":\"script1\",\"action\":\"Custom\",\"contentType\":\"Path\",\"content\":\"scripts/functional/script1.ps1\",\"runElevated\":false,\"restartAfter\":true,\"maxRunTime\":0,\"runAsInteractive\":false,\"alwaysRun\":false,\"applyUpdateBefore\":true},{\"name\":\"script2\",\"action\":\"Custom\",\"contentType\":\"Path\",\"content\":\"scripts/functional/script2.ps1\",\"runElevated\":false,\"restartAfter\":true,\"maxRunTime\":0,\"runAsInteractive\":false,\"alwaysRun\":false,\"applyUpdateBefore\":false}],\"isActive\":true}]},\"tags\":{\"你的名字\":\"好看\"}}";
        try {
            Package expectedResult = JsonConvertion.jsonToObj(jsonStr, Package.class);
            String convertedStr = JsonConvertion.objToJson(expectedResult);
            assertEquals(target, convertedStr);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            assertTrue(true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(true);
        }
    }


    @Test
    public void testEmptySerialization() {
        String jsonStr = "{\"package\":{}}";
        String target = "{\"tenantId\":null,\"clientId\":null,\"clientSecret\":null,\"subscriptionId\":null,\"resourceGroup\":null,\"testBaseAccount\":null,\"artifact\":null,\"package\":{}}";
        try {
            TestBaseConfiguration expectedResult = JsonConvertion.jsonToObj(jsonStr, TestBaseConfiguration.class);
            String convertedStr = JsonConvertion.objToJson(expectedResult);
            assertEquals(target, convertedStr);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            assertTrue(true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(true);
        }
    }


    @Test
    public void testTestSummaryResource() {
        String jsonStr = "{\"value\":[{\"id\":\"/subscriptions/9fabf001-9c1f-4aab-9451-e431da271956/resourceGroups/bingoz/providers/Microsoft.TestBase/testBaseAccounts/good_account/testSummaries/TestSummary-ca257fad-9636-35b3-98fe-de776e6663ca\",\"name\":\"TestSummary-ca257fad-9636-35b3-98fe-de776e6663ca\",\"type\":\"testBaseAccounts/testSummaries\",\"properties\":{\"testSummaryId\":\"ca257fad-9636-35b3-98fe-de776e6663ca\",\"packageId\":\"/SUBSCRIPTIONS/9FABF001-9C1F-4AAB-9451-E431DA271956/RESOURCEGROUPS/BINGOZ/PROVIDERS/MICROSOFT.TESTBASE/TESTBASEACCOUNTS/GOOD_ACCOUNT/Packages/JAVA-DEMO-TEST-1.1\",\"applicationName\":\"java-demo-test\",\"applicationVersion\":\"1.1\",\"executionStatus\":\"Completed\",\"grade\":\"Fail\",\"testStatus\":\"TestFailure\",\"testRunTime\":\"2022-07-13T02:45:53.8560186Z\",\"featureUpdatesTestSummary\":null,\"securityUpdatesTestSummary\":{\"executionStatus\":\"Completed\",\"grade\":\"Fail\",\"testStatus\":\"TestFailure\",\"testRunTime\":\"2022-07-13T02:45:53.8560186Z\",\"osUpdateTestSummaries\":[{\"osName\":\"Windows 10 20H2\",\"releaseName\":\"2022.06 B\",\"buildRevision\":\"1766\",\"releaseVersionDate\":\"2022-06-07T19:38:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1766\",\"executionStatus\":\"Completed\",\"testStatus\":\"TestFailure\",\"grade\":\"Fail\",\"testRunTime\":\"2022-07-11T09:12:43.4738526Z\"},{\"osName\":\"Windows 10 21H1\",\"releaseName\":\"2022.06 B\",\"buildRevision\":\"1766\",\"releaseVersionDate\":\"2022-06-07T19:38:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1766\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-11T09:12:00.3588103Z\"},{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.06 B\",\"buildRevision\":\"1766\",\"releaseVersionDate\":\"2022-06-07T19:38:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1766\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-12T23:15:45.8266116Z\"},{\"osName\":\"Windows 10 21H1\",\"releaseName\":\"2022.06 B\",\"buildRevision\":\"1766\",\"releaseVersionDate\":\"2022-06-07T19:38:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1766\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-11T09:10:18.6180865Z\"},{\"osName\":\"Windows 10 20H2\",\"releaseName\":\"2022.06 B\",\"buildRevision\":\"1766\",\"releaseVersionDate\":\"2022-06-07T19:38:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1766\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-11T09:12:13.0350227Z\"},{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.06 B\",\"buildRevision\":\"1766\",\"releaseVersionDate\":\"2022-06-07T19:38:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1766\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T02:45:53.8560186Z\"}]},\"packageTags\":{}}},{\"id\":\"/subscriptions/9fabf001-9c1f-4aab-9451-e431da271956/resourceGroups/bingoz/providers/Microsoft.TestBase/testBaseAccounts/good_account/testSummaries/TestSummary-6c40a1ae-40c2-36d5-93f2-8ad098afadf8\",\"name\":\"TestSummary-6c40a1ae-40c2-36d5-93f2-8ad098afadf8\",\"type\":\"testBaseAccounts/testSummaries\",\"properties\":{\"testSummaryId\":\"6c40a1ae-40c2-36d5-93f2-8ad098afadf8\",\"packageId\":\"/SUBSCRIPTIONS/9FABF001-9C1F-4AAB-9451-E431DA271956/RESOURCEGROUPS/BINGOZ/PROVIDERS/MICROSOFT.TESTBASE/TESTBASEACCOUNTS/GOOD_ACCOUNT/Packages/JAVA-DEMO-TEST-1.2\",\"applicationName\":\"java-demo-test\",\"applicationVersion\":\"1.2\",\"executionStatus\":\"Completed\",\"grade\":\"Pass\",\"testStatus\":\"Completed\",\"testRunTime\":\"2022-07-13T21:33:34.6668761Z\",\"featureUpdatesTestSummary\":null,\"securityUpdatesTestSummary\":{\"executionStatus\":\"Completed\",\"grade\":\"Pass\",\"testStatus\":\"Completed\",\"testRunTime\":\"2022-07-13T21:33:34.6668761Z\",\"osUpdateTestSummaries\":[{\"osName\":\"Windows 10 20H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T19:43:32.5376432Z\"},{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T19:32:42.5735039Z\"},{\"osName\":\"Windows 10 21H1\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T21:22:00.3715848Z\"},{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T12:52:56.9900253Z\"},{\"osName\":\"Windows 10 21H1\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T20:35:58.0270360Z\"},{\"osName\":\"Windows 10 20H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T21:33:34.6668761Z\"}]},\"packageTags\":{}}},{\"id\":\"/subscriptions/9fabf001-9c1f-4aab-9451-e431da271956/resourceGroups/bingoz/providers/Microsoft.TestBase/testBaseAccounts/good_account/testSummaries/TestSummary-35fdc2b6-9074-3ea1-be45-0261cbba3d10\",\"name\":\"TestSummary-35fdc2b6-9074-3ea1-be45-0261cbba3d10\",\"type\":\"testBaseAccounts/testSummaries\",\"properties\":{\"testSummaryId\":\"35fdc2b6-9074-3ea1-be45-0261cbba3d10\",\"packageId\":\"/SUBSCRIPTIONS/9FABF001-9C1F-4AAB-9451-E431DA271956/RESOURCEGROUPS/BINGOZ/PROVIDERS/MICROSOFT.TESTBASE/TESTBASEACCOUNTS/GOOD_ACCOUNT/Packages/JAVA-DEMO-TEST-1.4\",\"applicationName\":\"java-demo-test\",\"applicationVersion\":\"1.4\",\"executionStatus\":\"Completed\",\"grade\":\"Pass\",\"testStatus\":\"Completed\",\"testRunTime\":\"2022-07-13T13:43:12.6201515Z\",\"featureUpdatesTestSummary\":null,\"securityUpdatesTestSummary\":{\"executionStatus\":\"Completed\",\"grade\":\"Pass\",\"testStatus\":\"Completed\",\"testRunTime\":\"2022-07-13T13:43:12.6201515Z\",\"osUpdateTestSummaries\":[{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T12:52:36.8338190Z\"},{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T13:43:12.6201515Z\"}]},\"packageTags\":{}}},{\"id\":\"/subscriptions/9fabf001-9c1f-4aab-9451-e431da271956/resourceGroups/bingoz/providers/Microsoft.TestBase/testBaseAccounts/good_account/testSummaries/TestSummary-54440bf8-ca7e-3a8b-8379-16b78e61c125\",\"name\":\"TestSummary-54440bf8-ca7e-3a8b-8379-16b78e61c125\",\"type\":\"testBaseAccounts/testSummaries\",\"properties\":{\"testSummaryId\":\"54440bf8-ca7e-3a8b-8379-16b78e61c125\",\"packageId\":\"/SUBSCRIPTIONS/9FABF001-9C1F-4AAB-9451-E431DA271956/RESOURCEGROUPS/BINGOZ/PROVIDERS/MICROSOFT.TESTBASE/TESTBASEACCOUNTS/GOOD_ACCOUNT/Packages/JAVA-DEMO-TEST-1.3\",\"applicationName\":\"java-demo-test\",\"applicationVersion\":\"1.3\",\"executionStatus\":\"Completed\",\"grade\":\"Pass\",\"testStatus\":\"Completed\",\"testRunTime\":\"2022-07-13T13:02:12.3995345Z\",\"featureUpdatesTestSummary\":null,\"securityUpdatesTestSummary\":{\"executionStatus\":\"Completed\",\"grade\":\"Pass\",\"testStatus\":\"Completed\",\"testRunTime\":\"2022-07-13T13:02:12.3995345Z\",\"osUpdateTestSummaries\":[{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T13:02:12.3995345Z\"},{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T12:54:05.2055804Z\"}]},\"packageTags\":{}}},{\"id\":\"/subscriptions/9fabf001-9c1f-4aab-9451-e431da271956/resourceGroups/bingoz/providers/Microsoft.TestBase/testBaseAccounts/good_account/testSummaries/TestSummary-3a5a61be-ed68-3b32-a5a1-819ec4653461\",\"name\":\"TestSummary-3a5a61be-ed68-3b32-a5a1-819ec4653461\",\"type\":\"testBaseAccounts/testSummaries\",\"properties\":{\"testSummaryId\":\"3a5a61be-ed68-3b32-a5a1-819ec4653461\",\"packageId\":\"/SUBSCRIPTIONS/9FABF001-9C1F-4AAB-9451-E431DA271956/RESOURCEGROUPS/BINGOZ/PROVIDERS/MICROSOFT.TESTBASE/TESTBASEACCOUNTS/GOOD_ACCOUNT/Packages/JAVA-DEMO-TEST-1.6\",\"applicationName\":\"java-demo-test\",\"applicationVersion\":\"1.6\",\"executionStatus\":\"Completed\",\"grade\":\"Pass\",\"testStatus\":\"Completed\",\"testRunTime\":\"2022-07-13T13:51:23.9072338Z\",\"featureUpdatesTestSummary\":null,\"securityUpdatesTestSummary\":{\"executionStatus\":\"Completed\",\"grade\":\"Pass\",\"testStatus\":\"Completed\",\"testRunTime\":\"2022-07-13T13:51:23.9072338Z\",\"osUpdateTestSummaries\":[{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T13:37:18.9736349Z\"},{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T13:51:23.9072338Z\"}]},\"packageTags\":{}}},{\"id\":\"/subscriptions/9fabf001-9c1f-4aab-9451-e431da271956/resourceGroups/bingoz/providers/Microsoft.TestBase/testBaseAccounts/good_account/testSummaries/TestSummary-26f2b009-c23e-3dac-a26d-138e36a55587\",\"name\":\"TestSummary-26f2b009-c23e-3dac-a26d-138e36a55587\",\"type\":\"testBaseAccounts/testSummaries\",\"properties\":{\"testSummaryId\":\"26f2b009-c23e-3dac-a26d-138e36a55587\",\"packageId\":\"/SUBSCRIPTIONS/9FABF001-9C1F-4AAB-9451-E431DA271956/RESOURCEGROUPS/BINGOZ/PROVIDERS/MICROSOFT.TESTBASE/TESTBASEACCOUNTS/GOOD_ACCOUNT/Packages/JAVA-DEMO-TEST-1.5\",\"applicationName\":\"java-demo-test\",\"applicationVersion\":\"1.5\",\"executionStatus\":\"Completed\",\"grade\":\"Fail\",\"testStatus\":\"TestFailure\",\"testRunTime\":\"2022-07-13T12:56:46.7048207Z\",\"featureUpdatesTestSummary\":null,\"securityUpdatesTestSummary\":{\"executionStatus\":\"Completed\",\"grade\":\"Fail\",\"testStatus\":\"TestFailure\",\"testRunTime\":\"2022-07-13T12:56:46.7048207Z\",\"osUpdateTestSummaries\":[{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"TestFailure\",\"grade\":\"Fail\",\"testRunTime\":\"2022-07-13T12:56:46.7048207Z\"},{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-13T12:56:01.6443036Z\"}]},\"packageTags\":{}}},{\"id\":\"/subscriptions/9fabf001-9c1f-4aab-9451-e431da271956/resourceGroups/bingoz/providers/Microsoft.TestBase/testBaseAccounts/good_account/testSummaries/TestSummary-da593e53-fcb7-3650-962a-9b67ce87d80c\",\"name\":\"TestSummary-da593e53-fcb7-3650-962a-9b67ce87d80c\",\"type\":\"testBaseAccounts/testSummaries\",\"properties\":{\"testSummaryId\":\"da593e53-fcb7-3650-962a-9b67ce87d80c\",\"packageId\":\"/SUBSCRIPTIONS/9FABF001-9C1F-4AAB-9451-E431DA271956/RESOURCEGROUPS/BINGOZ/PROVIDERS/MICROSOFT.TESTBASE/TESTBASEACCOUNTS/GOOD_ACCOUNT/Packages/JAVA-DEMO-TEST-1.7\",\"applicationName\":\"java-demo-test\",\"applicationVersion\":\"1.7\",\"executionStatus\":\"Failed\",\"grade\":\"NotAvailable\",\"testStatus\":\"InfrastructureFailure\",\"testRunTime\":\"2022-07-14T10:12:00.0998678Z\",\"featureUpdatesTestSummary\":null,\"securityUpdatesTestSummary\":{\"executionStatus\":\"Failed\",\"grade\":\"NotAvailable\",\"testStatus\":\"InfrastructureFailure\",\"testRunTime\":\"2022-07-14T10:12:00.0998678Z\",\"osUpdateTestSummaries\":[{\"osName\":\"Windows 11 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"795\",\"releaseVersionDate\":\"2022-06-27T17:32:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"22000.795\",\"executionStatus\":\"Failed\",\"testStatus\":\"InfrastructureFailure\",\"grade\":\"NotAvailable\",\"testRunTime\":\"2022-07-14T10:12:00.0998678Z\"},{\"osName\":\"Windows 10 21H1\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"TestFailure\",\"grade\":\"Fail\",\"testRunTime\":\"2022-07-14T09:02:28.2905224Z\"},{\"osName\":\"Windows 10 21H1\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-14T08:47:00.1350581Z\"},{\"osName\":\"Windows 10 20H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-14T08:49:21.3652154Z\"},{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-14T06:43:34.7957623Z\"},{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-14T07:13:34.4002193Z\"},{\"osName\":\"Windows 10 20H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-14T08:43:53.6726006Z\"},{\"osName\":\"Windows 11 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"795\",\"releaseVersionDate\":\"2022-06-27T17:32:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"22000.795\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-14T09:02:28.2916849Z\"}]},\"packageTags\":{}}},{\"id\":\"/subscriptions/9fabf001-9c1f-4aab-9451-e431da271956/resourceGroups/bingoz/providers/Microsoft.TestBase/testBaseAccounts/good_account/testSummaries/TestSummary-3878b019-235b-397d-aae6-ea057530881c\",\"name\":\"TestSummary-3878b019-235b-397d-aae6-ea057530881c\",\"type\":\"testBaseAccounts/testSummaries\",\"properties\":{\"testSummaryId\":\"3878b019-235b-397d-aae6-ea057530881c\",\"packageId\":\"/SUBSCRIPTIONS/9FABF001-9C1F-4AAB-9451-E431DA271956/RESOURCEGROUPS/BINGOZ/PROVIDERS/MICROSOFT.TESTBASE/TESTBASEACCOUNTS/GOOD_ACCOUNT/Packages/JAVA-DEMO-TEST-1.8\",\"applicationName\":\"java-demo-test\",\"applicationVersion\":\"1.8\",\"executionStatus\":\"InProgress\",\"grade\":\"NotAvailable\",\"testStatus\":\"TestExecutionInProgress\",\"testRunTime\":\"2022-07-15T03:07:21.3797508Z\",\"featureUpdatesTestSummary\":null,\"securityUpdatesTestSummary\":{\"executionStatus\":\"InProgress\",\"grade\":\"NotAvailable\",\"testStatus\":\"TestExecutionInProgress\",\"testRunTime\":\"2022-07-15T03:07:21.3797508Z\",\"osUpdateTestSummaries\":[{\"osName\":\"Windows 11 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"795\",\"releaseVersionDate\":\"2022-06-27T17:32:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"22000.795\",\"executionStatus\":\"InProgress\",\"testStatus\":\"TestExecutionInProgress\",\"grade\":\"NotAvailable\",\"testRunTime\":null},{\"osName\":\"Windows 10 20H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"InProgress\",\"testStatus\":\"TestExecutionInProgress\",\"grade\":\"NotAvailable\",\"testRunTime\":null},{\"osName\":\"Windows 10 20H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"InProgress\",\"testStatus\":\"TestExecutionInProgress\",\"grade\":\"NotAvailable\",\"testRunTime\":null},{\"osName\":\"Windows 10 21H1\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"InProgress\",\"testStatus\":\"TestExecutionInProgress\",\"grade\":\"NotAvailable\",\"testRunTime\":null},{\"osName\":\"Windows 10 21H1\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"InProgress\",\"testStatus\":\"TestExecutionInProgress\",\"grade\":\"NotAvailable\",\"testRunTime\":null},{\"osName\":\"Windows 11 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"795\",\"releaseVersionDate\":\"2022-06-27T17:32:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"22000.795\",\"executionStatus\":\"InProgress\",\"testStatus\":\"TestExecutionInProgress\",\"grade\":\"NotAvailable\",\"testRunTime\":null},{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"FunctionalTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"TestFailure\",\"grade\":\"Fail\",\"testRunTime\":\"2022-07-15T03:07:21.3797508Z\"},{\"osName\":\"Windows 10 21H2\",\"releaseName\":\"2022.07 B\",\"buildRevision\":\"1826\",\"releaseVersionDate\":\"2022-06-28T22:50:00.0000000Z\",\"flightingRing\":\"\",\"testType\":\"OutOfBoxTest\",\"buildVersion\":\"19041.1826\",\"executionStatus\":\"Completed\",\"testStatus\":\"Completed\",\"grade\":\"Pass\",\"testRunTime\":\"2022-07-15T02:51:26.0671880Z\"}]},\"packageTags\":{}}}]}";
        try {
            Page<TestSummaryResource> expectedResult = JsonConvertion.jsonToObj(jsonStr, new TypeReference<Page<TestSummaryResource>>() {});
            String convertedStr = JsonConvertion.objToJson(expectedResult);
            assertNotNull(expectedResult.getValue());
            for(TestSummaryResource testSummaryResource : expectedResult.getValue())
                assertNotNull(testSummaryResource.getProperties());
        } catch (JsonMappingException e) {
            e.printStackTrace();
            assertTrue(true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertTrue(true);
        }
    }
}
