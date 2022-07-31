# Project

> This repo has been populated by an initial template to help get you started. Please
> make sure to update the content to build a great experience for community-building.

As the maintainer of this project, please make a few updates:

- Improving this README.MD file to provide a great experience
- Updating SUPPORT.MD with content about this project's support experience
- Understanding the security reporting process in SECURITY.MD
- Remove this section from the README

## Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.opensource.microsoft.com.

When you submit a pull request, a CLA bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., status check, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.

## Trademarks

This project may contain trademarks or logos for projects, products, or services. Authorized use of Microsoft 
trademarks or logos is subject to and must follow 
[Microsoft's Trademark & Brand Guidelines](https://www.microsoft.com/en-us/legal/intellectualproperty/trademarks/usage/general).
Use of Microsoft trademarks or logos in modified versions of this project must not cause confusion or imply Microsoft sponsorship.
Any use of third-party trademarks or logos are subject to those third-party's policies.

## Azure: TestBase

## Introduction

This plugin allows user to define steps in free-style jobs or pipleline jobs, which supports to onboard predefined packages to Azure TestBase for testing. After the end of the CI/CD process, you can view basic test result in the TestBase panel of build history subpage.

## For developer

### Prerequisites

1. [JDK](https://www.oracle.com/java/technologies/downloads/) , the version of which should be greater than or equal to 8, and can not be Open JDK.
2. The latest version of [maven](https://www.oracle.com/java/technologies/downloads/).

### Project structure

```
fhl.jenkins
|
|   Jenkinsfile                                    -- pipeline configuration file used in Jenkins
|   pom.xml                                        -- maven configuration file
|   TestBase-schema.json                           -- schema of TestBase configuration
|
└───src 
    │
    └───main
    |   |
    │   └───java
    |   |   |
    |   |   └───io.jenkins.plugins.azuretestbase
    |   |       |
    |   |       |   Configuration.java             -- module for loading configuration
    |   |       |   FunctionalTest.java            -- storage class of functional test options
    |   |       |   TestBaseAction.java            -- storage class of TestBase test result view
    |   |       |   TestBaseBuilder.java           -- extension of freestyle job step and pipeline job step
    |   |       |   TestBaseOptions.java           -- storage class of TestBase freestyle options
    |   |       |
    |   |       └───dm                             -- data model of TestBase request and response
    |   |       └───exceptions                     -- custom exceptions
    |   |       └───http                           -- wrapper of httpcore to access TestBase API
    |   |       └───json                           -- wrapper of jackson convertion
    |   |
    |   └───resources
    |   |   |
    |   |   |   config.properties                  -- configuration of prod envirionment
    |   |   |   config-df.properties               -- configuration of dog-food envirionment
    |   |   |   index.jelly                        -- intructions of plugin
    |   |   |
    |   |   └───io.jenkins.plugins.azuretestbase
    |   |       |
    |   |       |   Messages.properties            -- static string which are displayed on the interface
    |   |       |
    |   |       └───FunctionalTest                 -- frontpage of functional test options
    |   |       └───TestBaseAction                 -- frontpage of TestBase test result view
    |   |       └───TestBaseBuilder                -- frontpage of TestBase freestyle step and pipeline step
    |   |       └───TestBaseOptions                -- frontpage of TestBase freestyle options
    |   |
    │   └───webapp                                 -- static resource used for displaying on the interface
    |
    └───test
        |
        └───java
        |   └───io.jenkins.plugins.azuretestbase   -- unit test, substructure is the same as 'main/java'
        |
        └───resources
            |
            |   testdata.properties                -- prod test data used for unit test
            |   testdata-df.properties             -- dog-food test data used for unit test
            |   ...                                -- other test data
```

### Build and Test

1. Clone project and change the work folder.

   ```shell
   git clone git@ssh.dev.azure.com:v3/jujiang/FHL-Hackathon/fhl.jenkins
   
   cd fhl.jenkins
   ```

2. Verify the project. This step is recommended but not mandatory.

   ```shell
   mvn clean verify
   
   # or verify the project without executing unit test
   mvn clean verify -DskipTests
   
   # or verify the project without generate test classes. The parameters should be "-Dmaven.test.skip=true" in powershell because '.' is a recognized symbol in powershell command.
   mvn clean verify -Dmaven.test.skip=true
   ```

3. Run the temporary Jenkins server, and open `localhost:8080/jenkins/` in your browser.

   ```shell
   mvn clean hpi:run
   
   # or run on specified port
   mvn hpi:run "-Djetty.port=8081"
   ```

4. Package the Jenkins plugin to an HPI file.

   ```
   mvn clean package
   ```

You can also look up other lifecycle provided by hpi plugin at [maven-hpi-plugin](https://jenkinsci.github.io/maven-hpi-plugin/plugin-info.html).

### Upgrade maven parent

It's recommended to upgrade the referenced parent version after the release of new version of the parent pom.

1. Add `settings.xml` to `~/.m2/`, the content reads as follows:

   ```xml
   <settings>
     <pluginGroups>
       <pluginGroup>org.jenkins-ci.tools</pluginGroup>
     </pluginGroups>
   
     <profiles>
       <!-- Give access to Jenkins plugins -->
       <profile>
         <id>jenkins</id>
         <activation>
           <activeByDefault>true</activeByDefault> <!-- change this to false, if you don't like to have it on per default -->
         </activation>
         <repositories>
           <repository>
             <id>repo.jenkins-ci.org</id>
             <url>https://repo.jenkins-ci.org/public/</url>
           </repository>
         </repositories>
         <pluginRepositories>
           <pluginRepository>
             <id>repo.jenkins-ci.org</id>
             <url>https://repo.jenkins-ci.org/public/</url>
           </pluginRepository>
         </pluginRepositories>
       </profile>
     </profiles>
     <mirrors>
       <mirror>
         <id>repo.jenkins-ci.org</id>
         <url>https://repo.jenkins-ci.org/public/</url>
         <mirrorOf>m.g.o-public</mirrorOf>
       </mirror>
     </mirrors>
   </settings>
   ```

2. Upgrade parent pom.

   ```shell
   mvn versions:update-parent
   ```

## For customer

### Usage

1. Install this plugin on your Jenkins server.

2. Go to Azure AAD and register an application.

   > prod: [Microsoft - Microsoft Azure](https://ms.portal.azure.com/#view/Microsoft_AAD_IAM/ActiveDirectoryMenuBlade/~/Overview)
   >
   > dog-food: [Microsoft Dogfood - Microsoft Azure (azure-test.net)](https://df.onecloud.azure-test.net/#view/Microsoft_AAD_IAM/ActiveDirectoryMenuBlade/~/Overview)

3. Add a client secret to the application and save this secret.

4. Add this application to the subscription which you want to consume on. To do this, you must be the admin of the subscription. If you are not, you need to ask the admin to to it.

5. Run the temporary Jenkins server or install the hpi file packaged in the steps above on your Jenkins server.

6. Install [jenkinsci/plain-credentials-plugin (github.com)](https://github.com/jenkinsci/plain-credentials-plugin), and create a secret text credential with the secret you saved before.

7. Create a freestyle job or pipeline job to onboard your package to TestBase automatically.

   * For pipeline job, you should add a Jenkins configuration file (such as `Jenkinsfile`) and a TestBase configuration file (such as `TestBase.json`) to your SCM.

     The basic content of `Jenkinsfile` are as follows, and the 'dfclientsecret' is the credential ID of the secret text credential you created in step 6.

     ```groovy
     pipeline {
         agent any
     
         stages {
             stage('onboard') {
                 steps {
                     testBase useConfigurationFile: true, configurationFilePath: 'TestBase.json', credentialsId: 'dfclientsecret'
                 }
             }
         }
     }
     ```

     The basic content of `TestBase.json` are as follows, and you can look up complete content and format requirements at [Schema of configuration file](#Schema of configuration file). You need to fill the value required in this file before adding it to SCM.

     ```json
     {
       "tenantId": "",
       "clientId": "",
       "subscriptionId": "",
       "resourceGroup": "",
       "testBaseAccount": "",
       "artifact": "",
       "package": {
         "location": "global",
         "properties": {
           "applicationName": "",
           "version": "",
           "targetOSList": [
             {
               "osUpdateType": "Security updates",
               "targetOSs": [
                 "Windows 11 21H2"
               ]
             },
             {
               "osUpdateType": "Feature updates",
               "targetOSs": [
                 "Windows 11 21H2"
               ]
             }
           ],
           "flightingRing": "Insider Beta Channel",
           "tests": [
             {
               "commands": [
                 {
                   "name": "install",
                   "action": "Install",
                   "contentType": "Path",
                   "content": "scripts/outofbox/install.ps1",
                   "restartAfter": true
                 },
                 {
                   "name": "launch",
                   "action": "Launch",
                   "contentType": "Path",
                   "content": "scripts/outofbox/launch.ps1"
                 },
                 {
                   "name": "close",
                   "action": "Close",
                   "contentType": "Path",
                   "content": "scripts/outofbox/close.ps1"
                 },
                 {
                   "name": "uninstall",
                   "action": "Uninstall",
                   "contentType": "Path",
                   "content": "scripts/outofbox/uninstall.ps1"
                 }
               ],
               "testType": "OutOfBoxTest"
             },
             {
               "commands": [
                 {
                   "name": "script1",
                   "action": "Custom",
                   "applyUpdateBefore": true,
                   "contentType": "Path",
                   "content": "scripts/functional/script1.ps1",
                   "restartAfter": true
                 },
                 {
                   "name": "script2",
                   "action": "Custom",
                   "contentType": "Path",
                   "content": "scripts/functional/script2.ps1",
                   "restartAfter": true
                 }
               ],
               "testType": "FunctionalTest"
             }
           ]
         }
       }
     }
     ```

     If you don't want to write a `TestBase.json`, you can use the ***pipeline syntax*** to generate a TestBase step with custom options. You should get into your pipeline job and click the ***pipeline syntax*** in the sidebar, and click the ***syntax generator*** in the sidebar, and choose the ***testBase: Onboard TestBase package*** in the sample step under the step section. You can choose the secret credential you created before as the service principal and customize various configuration of onboarding a TestBase package. Finally click the ***Generate pipeline script*** to generate the pipeline step with custom options. The `Jenkinsfile` without using a TestBase configuration file should be like this:

     ```groovy
     pipeline {
         agent any
     
         stages {
             stage('onboard') {
                 steps {
                     testBase configurationFilePath: '', credentialsId: 'dfclientsecret', testBaseOptions: testBaseOptions(applicationName: '', artifact: '', clientId: '', closeScript: 'scripts/outofbox/close.ps1', featureUpdates: true, featureUpdatesBaselineOSs: [], featureUpdatesTargetOSs: ['Windows 11 21H2'], flightingRing: 'Insider Beta Channel', functionalTestList: [functionalTest('')], installScript: 'scripts/outofbox/install.ps1', launchScript: 'scripts/outofbox/launch.ps1', outOfBoxTest: true, resourceGroup: '', restartAfterInstall: true, securityUpdates: true, securityUpdatesTargetOSs: ['Windows 11 21H2'], subscriptionId: '', tenantId: '', testBaseAccount: '', uninstallScript: 'scripts/outofbox/uninstall.ps1', version: ''), useConfigurationFile: false
                 }
             }
         }
     }
     ```

   * For freestyle job, you can use the TestBase configuration file in your SCM or use the options provided by plugin view to configure the onboard process of TestBase package. If you want to use the first method, you should choose the ***Use configuration path*** in the TestBase step and add a `TestBase.json` to your SCM. If you want to use the second method, you should choose the ***Use the options provided here*** in the TestBase step and complete other mandatory options that follow. 

8. Once you have successfully created the Jenkins job, you can click ***Build Now*** to start the job.

9. In the ***Console Output***, you can see the details of onboarding TestBase package, and after the end of job, you can view the basic test result in the TestBase panel.

### Samples

1. Onboard existing package to TestBase: [testbase-package-jenkins](https://github.com/BINGOGO123/testbase-package-jenkins)
2. Build, package and onboard a simple console application: [testbase-console-jenkins](https://github.com/BINGOGO123/testbase-console-jenkins)
3. Build, package and onboard a calculator application with UI: [testbase-calculator-jenkins](https://github.com/BINGOGO123/testbase-calculator-jenkins)

### Schema of configuration file

You can also look up the schema at [TestBase-schema.json](https://dev.azure.com/jujiang/FHL-Hackathon/_git/fhl.jenkins?path=TestBase-schema.json).

```json
{
  "$schema": "http://json-schema.org/draft-07/schema",
  "$id": "jenkins_testbase_configuration",
  "title": "Jenkins TestBase configuration",
  "description": "Jenkins TestBase plugin configuration schema.",
  "type": "object",
  "properties": {
    "tenantId": {
      "type": "string",
      "description": "Azure tenant ID."
    },
    "clientId": {
      "type": "string",
      "description": "Azure Active Directory application ID."
    },
    "subscriptionId": {
      "type": "string",
      "description": "Azure subscription ID"
    },
    "resourceGroup": {
      "type": "string",
      "description": "Azure resource group name"
    },
    "testBaseAccount": {
      "type": "string",
      "description": "Azure TestBase account name"
    },
    "artifact": {
      "type": "string",
      "description": "Path of your package for test"
    },
    "package": {
      "type": "object",
      "description": "The Test Base Package resource.",
      "properties": {
        "tags": {
          "type": "object",
          "description": "The tags of the resource.",
          "additionalProperties": {
            "type": "string"
          }
        },
        "location": {
          "type": "string",
          "description": "The geo-location where the resource lives."
        },
        "properties": {
          "description": "The properties of the Test Base Package.",
          "type": "object",
          "properties": {
            "applicationName": {
              "type": "string",
              "description": "Application name"
            },
            "version": {
              "type": "string",
              "description": "Application version"
            },
            "flightingRing": {
              "type": "string",
              "description": "The flighting ring for feature update."
            },
            "targetOSList": {
              "type": "array",
              "description": "Specifies the target OSs of specific OS Update types.",
              "items": {
                "type": "object",
                "description": "The information of the target OS to be tested.",
                "properties": {
                  "osUpdateType": {
                    "type": "string",
                    "description": "Specifies the OS update type to test against, e.g., 'Security updates' or 'Feature updates'."
                  },
                  "targetOSs": {
                    "type": "array",
                    "items": {
                      "type": "string"
                    },
                    "description": "Specifies the target OSs to be tested."
                  },
                  "baselineOSs": {
                    "type": "array",
                    "items": {
                      "type": "string"
                    },
                    "description": "Specifies the baseline OSs to be tested."
                  }
                },
                "required": [
                  "osUpdateType",
                  "targetOSs"
                ]
              }
            },
            "tests": {
              "type": "array",
              "description": "The detailed test information.",
              "items": {
                "description": "The definition of a Test.",
                "type": "object",
                "properties": {
                  "testType": {
                    "type": "string",
                    "description": "The type of the test.",
                    "enum": [
                      "OutOfBoxTest",
                      "FunctionalTest"
                    ]
                  },
                  "isActive": {
                    "type": "boolean",
                    "description": "Indicates if this test is active.It doesn't schedule test for not active Test."
                  },
                  "commands": {
                    "type": "array",
                    "description": "The commands used in the test.",
                    "items": {
                      "type": "object",
                      "description": "The command used in the test",
                      "properties": {
                        "name": {
                          "type": "string",
                          "description": "The name of the command."
                        },
                        "action": {
                          "type": "string",
                          "description": "The action of the command.",
                          "enum": [
                            "Install",
                            "Launch",
                            "Close",
                            "Uninstall",
                            "Custom"
                          ]
                        },
                        "contentType": {
                          "type": "string",
                          "description": "The type of command content.",
                          "enum": [
                            "Inline",
                            "File",
                            "Path"
                          ]
                        },
                        "content": {
                          "type": "string",
                          "description": "The content of the command. The content depends on source type."
                        },
                        "runElevated": {
                          "type": "boolean",
                          "description": "Specifies whether to run the command as administrator."
                        },
                        "restartAfter": {
                          "type": "boolean",
                          "description": "Specifies whether to restart the VM after the command executed."
                        },
                        "maxRunTime": {
                          "type": "integer",
                          "format": "int32",
                          "description": "Specifies the max run time of the command."
                        },
                        "runAsInteractive": {
                          "type": "boolean",
                          "description": "Specifies whether to run the command in interactive mode."
                        },
                        "alwaysRun": {
                          "type": "boolean",
                          "description": "Specifies whether to run the command even if a previous command is failed."
                        },
                        "applyUpdateBefore": {
                          "type": "boolean",
                          "description": "Specifies whether to apply update before the command."
                        }
                      },
                      "required": [
                        "name",
                        "action",
                        "contentType",
                        "content"
                      ]
                    }
                  }
                },
                "required": [
                  "testType",
                  "commands"
                ]
              }
            }
          },
          "required": [
            "applicationName",
            "version",
            "flightingRing",
            "targetOSList",
            "tests"
          ]
        }
      }
    }
  },
  "required": [
    "tenantId",
    "clientId",
    "subscriptionId",
    "resourceGroup",
    "testBaseAccount",
    "artifact",
    "package"
  ]
}
```

## Reference

1. User documentation: [Jenkins User Documentation](https://www.jenkins.io/doc/)

2. Developer documentation: [Plugin development (jenkins.io)](https://www.jenkins.io/doc/developer/plugin-development/)

3. Quick start of Jenkins plugin: [Preparing for Plugin Development (jenkins.io)](https://www.jenkins.io/doc/developer/tutorial/prepare/)

4. List of extension points: [Extensions Index (jenkins.io)](https://www.jenkins.io/doc/developer/extensions/)

5. Jenkins Java documentation: [Jenkins Javadoc](https://javadoc.jenkins.io/index.html)

6. SCM of Jenkins core and Jenkins plugin: [Jenkins (github.com)](https://github.com/jenkinsci)

7. Jelly documentation: [Jelly - Tag Documentation (apache.org)](https://commons.apache.org/proper/commons-jelly/tags.html)

8. Jelly tags defined in Jenkins core: [jenkins/core/src/main/resources/lib/form at 63f80114e99f6692812c3039407652592bdf36fe · jenkinsci/jenkins (github.com)](https://github.com/jenkinsci/jenkins/tree/63f80114e99f6692812c3039407652592bdf36fe/core/src/main/resources/lib/form)

9. Jenkins artifacts repo: https://repo.jenkins-ci.org/ui/

10. Jenkins community: [Jenkins - The leading open source automation server, Jenkins provides hundreds of plugins to support building, deploying and automating any project.](https://community.jenkins.io/)

11. Jenkins issues: [System Dashboard - Jenkins Jira](https://issues.jenkins.io/secure/Dashboard.jspa)

12. Jenkins account register: [Sign up | Jenkins](https://accounts.jenkins.io/signup)

13. Jenkins server delicated to build plugin: [Projects [Jenkins]](https://ci.jenkins.io/)

