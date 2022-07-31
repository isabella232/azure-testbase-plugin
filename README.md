# Azure: TestBase

This plugin allows user to define steps in free-style jobs or pipleline jobs, which supports to onboard predefined packages to Azure TestBase for testing. After the end of the CI/CD process, you can view basic test result in the TestBase panel of build history subpage.

## Usage

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

## Samples

1. Onboard existing package to TestBase: [testbase-package-jenkins](https://github.com/BINGOGO123/testbase-package-jenkins)
2. Build, package and onboard a simple console application: [testbase-console-jenkins](https://github.com/BINGOGO123/testbase-console-jenkins)
3. Build, package and onboard a calculator application with UI: [testbase-calculator-jenkins](https://github.com/BINGOGO123/testbase-calculator-jenkins)

## Schema of configuration file

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

## Trademarks

This project may contain trademarks or logos for projects, products, or services. Authorized use of Microsoft 
trademarks or logos is subject to and must follow 
[Microsoft's Trademark & Brand Guidelines](https://www.microsoft.com/en-us/legal/intellectualproperty/trademarks/usage/general).
Use of Microsoft trademarks or logos in modified versions of this project must not cause confusion or imply Microsoft sponsorship.
Any use of third-party trademarks or logos are subject to those third-party's policies.

## Telemetry

azure-testbase-plugin collects usage data and sends it to Microsoft to help improve our products and services. Read our [privacy statement]([http://go.microsoft.com/fwlink/?LinkId=521839](https://nam06.safelinks.protection.outlook.com/?url=http%3A%2F%2Fgo.microsoft.com%2Ffwlink%2F%3FLinkId%3D521839&data=05|01|t-haibinzang%40microsoft.com|d036768adb7e44e005c508da6fa446c1|72f988bf86f141af91ab2d7cd011db47|1|0|637945048603172959|Unknown|TWFpbGZsb3d8eyJWIjoiMC4wLjAwMDAiLCJQIjoiV2luMzIiLCJBTiI6Ik1haWwiLCJXVCI6Mn0%3D|3000|||&sdata=Z58OX2lKCbNi%2BhCQ8eL7YQPZEvS3wNgRxUqOtpdcbnc%3D&reserved=0)) to learn more.

You can turn off usage data collection in Manage Jenkins -> Configure System -> Azure -> Help make Azure Jenkins plugins better by sending anonymous usage statistics to Azure Application Insights.

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

