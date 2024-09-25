Alcohol Duty Performance Tests
Link: https://github.com/hmrc/alcohol-duty-performance-tests

Performance test suite for the `Alcohol Duty Returns`, using [performance-test-runner](https://github.com/hmrc/performance-test-runner) under the hood.

### 1. Services to run (for local testing)
Start the docker desktop application (and make sure the mongodb is running on the docker)
Start `Alhocol Duty` services as follows:
sm2 --start ALCOHOL_DUTY_ALL
To enable test only endpoint for local testing, stop ALCOHOL_DUTY_FRONTEND & ALCOHOL_DUTY_RETURNS from SM2 services and run these services locally with an argument as below:
sbt "run -Dapplication.router=testOnlyDoNotUseInAppConf.Routes"

### 2. To run smoke tests
Run below command in terminal: 
sbt -Dperftest.runSmokeTest=true -DrunLocal=true gatling:test

### 3. To run full tests
Run below command in terminal:
sbt -DrunLocal=true gatling:test

### 3. To run full tests in staging
Go to Jenkins Performance Tests Job on Staging
Click on 'Build with parameters'
Enter branch name (leave blank if the tests needs to run from the main branch)
Set below parameters:
Load: 100
ramp_up: 1
constant_rate: 8
Then click 'Build'

### 5. Logging
The default log level for all HTTP requests is set to `WARN`. Configure [logback.xml](src/test/resources/logback.xml) to update this if required.

### 6. WARNING
Do **NOT** run a full performance test against staging from your local machine. Please [implement a new performance test job](https://confluence.tools.tax.service.gov.uk/display/DTRG/Practical+guide+to+performance+testing+a+digital+service#Practicalguidetoperformancetestingadigitalservice-SettingupabuildonJenkinstorunagainsttheStagingenvironment) and execute your job from the dashboard in [Performance Jenkins](https://performance.tools.staging.tax.service.gov.uk).

### 7. License
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
