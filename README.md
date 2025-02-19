# System Test for Project

This repository contains the configuration and scripts required to run **system tests** for the project using JUnit and
Maven. The tests are designed to verify the system's functionality in different environments using configurable
parameters such as environment, tenant and user credentials.


## Configuration

### System Properties

This system test requires several properties to be passed as system properties (`-D` arguments) during execution:

| Property      | Description                 | Example     |
|---------------|-----------------------------|-------------|
| `environment` | The environment             | `develop`   |
| `tenant`      | The name of the tenant      | `harbor`    |
| `user`        | Username for authentication | `adminuser` |
| `password`    | Password for authentication | `********`  |

You can configure these properties in your IntelliJ IDEA or directly in Maven commands.

## Running the Tests

You can run the system tests using IntelliJ IDEA or via Maven on the command line or Github Actions.

### Running in IntelliJ

1. **Open Run/Debug Configurations**:
    - Go to `Run > Edit Configurations...` in IntelliJ.

2. **Create/Modify JUnit Run Configuration**:
    - Select **JUnit** from the list and specify the test class to run.
    - In the **VM options** field, add the required system properties:
      ```bash
      -Denvironment=yourEnvironment -Dtenant=yourTenant -Dusername=yourUser -Dpassword=yourPassword
      ```


4. **Run the Test**:
    - Select the configuration and run the test using the **Run** button in IntelliJ.

### Running with Maven

You can also run the tests directly using Maven, which picks up the `pom.xml` configuration.

1. **Run Tests with Maven**:
   ```bash
   mvn clean test -Denvironment=yourEnvironment -Dtenant=yourTenant -Dusername=yourUser -Dpassword=yourPassword
   ```  

### Running with Github Actions
For GitHub Actions, go to Actions -> workflow -> Run workflow -> Fill in all parameters -> Run workflow.