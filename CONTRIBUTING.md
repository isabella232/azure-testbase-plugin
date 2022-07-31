# Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.opensource.microsoft.com.

When you submit a pull request, a CLA bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., status check, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.

## Prerequisites

1. [JDK](https://www.oracle.com/java/technologies/downloads/) , the version of which should be greater than or equal to 8, and can not be Open JDK.
2. The latest version of [maven](https://www.oracle.com/java/technologies/downloads/).

## Project structure

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
            |   testdata-demo.properties           -- demo test data used for unit test, you need to fill the values and rename it to testdata.properties or testdata-df.properties
            |   ...                                -- other test data
```

## Build and Test

1. Clone project and change the work folder.

   ```shell
   git clone git@ssh.dev.azure.com:v3/jujiang/FHL-Hackathon/fhl.jenkins
   
   cd fhl.jenkins
   ```

2. Verify the project. This step is recommended but not mandatory.

   ```shell
   # verify the project in prod env
   mvn clean verify
   # or
   mvn clean verify -Denv=prod
   
   # verify the project in dogfood env
   mvn clean verify -Denv=dogfood
   
   # verify the project without executing unit test
   mvn clean verify -DskipTests
   
   # verify the project without generate test classes. The parameters should be "-Dmaven.test.skip=true" in powershell because '.' is a recognized symbol in powershell command.
   mvn clean verify -Dmaven.test.skip=true
   ```

3. Run the unit test. If you want to run TestBase api test, you need to fill the `src/test/resources/testdata-demo.properties` and copy it to `src/test/resources/testdata-properties`. In dog-food env, this file is `src/test/resources/testdata-df.properties`.

   ```shell
   mvn clean test
   
   # run TestBase api test in prod env
   mvn clean test -Dapitest=true
   # or
   mvn clean test -Dapitest=true -Denv=prod
   
   # run TestBase api test in dog-food env
   mvn clean test -Dapitest=true -Denv=dogfood
   ```

4. Run the temporary Jenkins server, and open `localhost:8080/jenkins/` in your browser.

   ```shell
   # run in prod env
   mvn clean hpi:run
   # or
   mvn clean hpi:run -Denv=prod
   
   # run in dog-food env
   mvn clean hpi:run -Denv=dogfood
   
   # or run on specified port
   mvn hpi:run "-Djetty.port=8081"
   ```

5. Package the Jenkins plugin to an HPI file.

   ```
   mvn clean package
   ```

You can also look up other lifecycle provided by hpi plugin at [maven-hpi-plugin](https://jenkinsci.github.io/maven-hpi-plugin/plugin-info.html).

## Upgrade maven parent

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
