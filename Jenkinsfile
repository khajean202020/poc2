def projectDirName = "broker-modernisation-services-automation"
def projectTestBuildDir = "${projectDirName}"
def JDK = "jdk-11.0.24+9"
properties([parameters([
[$class      : 'ChoiceParameter',
                             choiceType  : 'PT_SINGLE_SELECT',
                             description : 'Select test module from dropdown List',
                             filterLength: 0,
                             filterable  : false,
                             name        : 'ServiceName',
                             randomName  : 'none',

                             script      : [$class        : 'GroovyScript',
                                            fallbackScript: [classpath: [],sandbox: true, script: "return['Could not get test module name']"],
                                            script        : [classpath: [], sandbox: true, script: "return ['','file-transfer-service','aims-vector-service','pnr-transformer','start-workflow-execution-wrapper', 'pnr-profile-feed-processing','pnr-mongo-feed-service', 'pnr-edw-feed-service','pnr-plusgrade-feed-service','ssim-tdp-export-service', 'ssim-ftp-service','pnr-responsys-feed-service', 'fics-msg-service', 'fics-inform-service','fics-mongo-feed-service', 'fics-messaging-engine','fics-deicing-feed-service', 'astral-recon-service','plusgrade-subscriber-service-NEW','trendtours-group-names-service','email-integration-service']"]
                             ]
                            ],
[$class              : 'CascadeChoiceParameter',
                             choiceType          : 'PT_SINGLE_SELECT',
                             description         : 'Select AWS environment from the dropdown list',
                             filterLength        : 0,
                             filterable          : false,
                             name                : 'AWSEnvironment',
                             randomName          : 'none1',
                             referencedParameters: 'ServiceName',
                             script              : [$class        : 'GroovyScript',
                                                    fallbackScript: [classpath: [], sandbox: true, script: "return [\"**Could not get environment from AWS Environment **\"]"],
                                                    script        : [classpath: [], sandbox: true, script: '''
            				if (ServiceName.equals('file-transfer-service')){
                                         return['awssandbox','awsnonprod','awsuat']
                                        }
                                        else if(ServiceName.equals('email-integration-service')){
                                              return['awssandbox','awsnonprod','awsuat','awsstress','awsstg','awsprd']
                                        }
                                        else{
                                            return ['Select test module from dropdown']
                                        }
                                    ''' ]
                             ]
                            ],
[$class              : 'CascadeChoiceParameter',
                             choiceType          : 'PT_SINGLE_SELECT',
                             description         : 'Jenkins Access I AM Role Account',
                             filterLength        : 0,
                             filterable          : false,
                             name                : 'IAMRoleAccount',
                             randomName          : 'none2',
                             referencedParameters: 'AWSEnvironment',
                             script              : [$class        : 'GroovyScript',
                                                    fallbackScript: [classpath: [], sandbox: true, script: "return [\"Account Information Not Available\"]"],
                                                    script        : [classpath: [], sandbox: true, script: '''
            				if (AWSEnvironment.equals('awssandbox')){
                                   return['973954063285']
                            }
                            else if(AWSEnvironment.equals('awsnonprod')){
                                   return['952894771269']
                            }

                            else{
                                    return ['Jenkins Access IAM Role Account Not Available']
                            }
                            ''' ]
                                                         ]
                                                        ],
[$class              : 'CascadeChoiceParameter',
                             choiceType          : 'PT_SINGLE_SELECT',
                             description         : 'Jenkins Access I AM Role',
                             filterLength        : 0,
                             filterable          : false,
                             name                : 'IAMRole',
                             randomName          : 'none3',
                             referencedParameters: 'AWSEnvironment',
                             script              : [$class        : 'GroovyScript',
                                                    fallbackScript: [classpath: [], sandbox: true, script: "return [\"Role Information Not Available\"]"],
                                                    script        : [classpath: [], sandbox: true, script: '''
            				if (AWSEnvironment.equals('awssandbox')){
                                         return['']
                                        }
                                        else if(AWSEnvironment.equals('awsnonprod')){
                                            return['devops-tools-dev-broker-jenkins-access']
                                        }
                                        else{
                                            return ['Jenkins Access IAM Role Not Available']
                                        }
                                    ''' ]
                             ]
                            ],
[$class              : 'CascadeChoiceParameter',
                             choiceType          : 'PT_SINGLE_SELECT',
                             description         : 'Select a test suite from dropdown List',
                             filterLength        : 0,
                             filterable          : false,
                             name                : 'TestNGsuiteXmlFile',
                             randomName          : 'none4',
                             referencedParameters: 'AWSEnvironment,ServiceName',
                             script              : [$class        : 'GroovyScript',
                                                    fallbackScript: [classpath: [], sandbox: true, script: "return [\"**Unknown test suite **\"]"],
                                                    script        : [classpath: [], sandbox: true, script: '''
            				if (ServiceName.equals('file-transfer-service')){
                                         return['runDefaultTests','runAllTests','runFileFtpInToInvokeFileTransferService','runFileTransferConfigurationLambdaTests','runFileTransferLambda_AccelyaToEI_Tests','runFileTransferService_AccelyaToEI_PositiveTests','runFileTransferService_AccelyaToEI_NegativeTests','runSmokeTests','runFileTransferService_EIToAccelya_PositiveTests','runFileTransferService_EIToAccelya_NegativeTests','runFileTransferLambda_EIToAccelya_Tests']
                                        }
                                        else if(ServiceName.equals('email-integration-service')){
                                               return['runEmailIntegrationLambdaPositiveTests','runEmailIntegrationLambdaNegativeTests','runEmailIntegrationSQSPositiveTests','runEmailIntegrationSNSPositiveTests']
                                        }
                                        else{
                                            return ['Select test module from dropdown List']
                                        }
                                    ''' ]
                             ]
                            ],
[$class: 'StringParameterDefinition', name: 'JiraKey', trim:"true", defaultValue: "",description: '(Optional parameter). Please enter the jira key of your project to import test results into jira zephyr cycle summary. (e.g - PAT).If value not specified, test results do not get imported into zephyr cycle summary'],
[$class: 'StringParameterDefinition', name: 'JiraZephyrReleaseVersion',trim:"true", defaultValue: "",description: '(Optional parameter). Please Specify Specify Zephyr Jira Release Version - if left blank version will be set as unscheduled'],
[$class: 'StringParameterDefinition', name: 'JiraZephyrCycleName',trim:"true", defaultValue: "",description: '(Optional parameter). Please Specify Zephyr Jira Test Cycle Name e.g Independent Release EI.2054 - AWS FICS SNS module, Independent Release EI24.1971 - AWS File Transfer improvements etc.. (Patriots Jira Zephyr Cycle Summary URL :https://eiaerlingus.atlassian.net/projects/PAT?selectedItem=com.thed.zephyr.je__cycle-summary)'],
])])

pipeline {
  options {
    timestamps()
  }
  tools {
   
  }
  agent {
    label 'jdk-17'
  }
  stages {
    stage('Init') {
        steps {
        sh 'set +x ;[ ! -f ./mvnw ] && { echo "Maven Wrapper not found!"; exit 1; } || [ ! -x ./mvnw ] && chmod +x ./mvnw ; exit 0'
              }
    }
     stage('Check Build Parameter Values') {
               steps {
               script{
                   echo "Build Parameter Values"
                   echo "AWSEnvironment : ${params.AWSEnvironment}"
           		   echo "ServiceName : ${params.ServiceName}"
           		   echo "TestNGsuiteXmlFile : ${params.TestNGsuiteXmlFile}"
           		   echo "Jira Key : ${params.JiraKey}"
           		   echo "Jira Zephyr Release Version : ${params.JiraZephyrReleaseVersion}"
                   echo "Jira Zephyr Cycle Name : ${params.JiraZephyrCycleName}"

           		   println  "is AWSEnvironment parameter value not null ? : "
           		   println  "${params.AWSEnvironment}".toString()!=null
           		   println  "is AWSEnvironment parameter value empty ? : " + "${params.AWSEnvironment}".toString().isEmpty()

           		   println  "is ServiceName parameter value not null ? : "
           		   println  "${params.ServiceName}".toString()!=null
           		   println  "is ServiceName parameter value empty ? : " + "${params.ServiceName}".toString().isEmpty()

           		   println  "is TestNGsuiteXmlFile parameter value not null ? : "
           		   println  "${params.TestNGsuiteXmlFile}".toString()!=null
           		   println  "is TestNGsuiteXmlFile parameter value empty ? : " + "${params.TestNGsuiteXmlFile}".toString().isEmpty()

                   println  "is Jira Key parameter value not null ? : "
                   println  "${params.JiraKey}".toString()!=null
                   println  "is Jira Key parameter value empty ? : " + "${params.JiraKey}".toString().isEmpty()

                   println  "is Jira Zephyr Cycle Name parameter value not null ? : "
                   println  "${params.JiraZephyrCycleName}".toString()!=null
                   println  "is Jira Zephyr Cycle Name value empty ? : " + "${params.JiraZephyrCycleName}".toString().isEmpty()

                   println  "is Jira Zephyr Release Version Name parameter value not null ? : "
                   println  "${params.JiraZephyrReleaseVersion}".toString()!=null
                   println  "is Jira Zephyr Release Version Name value empty ? : " + "${params.JiraZephyrReleaseVersion}".toString().isEmpty()
               }
               }
           }
    stage('List Java Version') {
      steps {
        sh "java -version"
      }
    }
    stage('Execute tests') {
      steps {
         withCredentials([
         usernamePassword(credentialsId: 'digital-jenkins-artifactory-creds', usernameVariable: 'ARTIFACTORY_USERNAME', passwordVariable: 'ARTIFACTORY_PASSWORD'),
         usernamePassword(credentialsId: 'digital-jenkins-artifactory-creds', usernameVariable: 'ORG_GRADLE_PROJECT_ARTIFACTORY_USERNAME',passwordVariable: 'ORG_GRADLE_PROJECT_ARTIFACTORY_PASSWORD'),
         string(credentialsId: 'zephyr_access_key', variable: 'ZEPHYR_ACCESS_KEY'),
         string(credentialsId: 'zephyr_secret_key', variable: 'ZEPHYR_SECRET_KEY'),
         string(credentialsId: 'zephyr_account_id', variable: 'ZEPHYR_ACCOUNT_ID'),
         string(credentialsId: 'auto_ci_jira_auth_token', variable: 'AUTO_CI_JIRA_AUTH_TOKEN')
         ]) {
            withEnv(["ARTIFACTORY_URL=${ARTIFACTORY_URL}/artifactory"]) {

                script{
                    if("${params.AWSEnvironment}".toString().isEmpty() || "${params.ServiceName}".toString().isEmpty() || "${params.TestNGsuiteXmlFile}".toString().isEmpty()){
                        echo " AWS environment value or service name or test suite value not selected from dropdown list.. No test cases executed !!! "
                    }
                    else if ("${params.AWSEnvironment}".toString().equalsIgnoreCase("awssandbox")){
                        echo "Test execution started in aws sandbox environment"
                        if("${params.JiraKey}".toString().isEmpty()){
                           sh "./mvnw clean test -P${params.AWSEnvironment} -pl ${params.ServiceName} -am -DsuiteXmlFile=${params.TestNGsuiteXmlFile} -s settings.xml  "
                        }
                        else{
                            sh "./mvnw clean test -P${params.AWSEnvironment} -pl ${params.ServiceName} -am -DsuiteXmlFile=${params.TestNGsuiteXmlFile} -s settings.xml -DjiraKey='${params.JiraKey}' -DjiraZephyrCycleName='${params.JiraZephyrCycleName}' -DjiraZephyrVersion=${params.JiraZephyrReleaseVersion} -DZEPHYR_ACCESS_KEY=${env.ZEPHYR_ACCESS_KEY} -DZEPHYR_SECRET_KEY=${env.ZEPHYR_SECRET_KEY} -DZEPHYR_ACCOUNT_ID=${env.ZEPHYR_ACCOUNT_ID} -DAUTO_CI_JIRA_AUTH_TOKEN=${env.AUTO_CI_JIRA_AUTH_TOKEN} "
                        }
                    }
                    else{
                        echo "Test execution started in ${params.AWSEnvironment} environment"
                        withAWS(
                            roleAccount: "${params.IAMRoleAccount}",
                            role: "${params.IAMRole}",
                            region: "eu-west-1"
                        ){
                           if("${params.JiraKey}".toString().isEmpty()){
                                 echo "Zephyr has not been started as no Jira key was supplied."
                                 sh "./mvnw clean test -P${params.AWSEnvironment} -pl ${params.ServiceName} -am -DsuiteXmlFile=${params.TestNGsuiteXmlFile} -s settings.xml "
                          }
                          else{
                                sh "./mvnw clean test -P${params.AWSEnvironment} -pl ${params.ServiceName} -am -DsuiteXmlFile=${params.TestNGsuiteXmlFile} -s settings.xml -DjiraKey='${params.JiraKey}' -DjiraZephyrCycleName='${params.JiraZephyrCycleName}' -DjiraZephyrVersion=${params.JiraZephyrReleaseVersion} -DZEPHYR_ACCESS_KEY=${env.ZEPHYR_ACCESS_KEY} -DZEPHYR_SECRET_KEY=${env.ZEPHYR_SECRET_KEY} -DZEPHYR_ACCOUNT_ID=${env.ZEPHYR_ACCOUNT_ID} -DAUTO_CI_JIRA_AUTH_TOKEN=${env.AUTO_CI_JIRA_AUTH_TOKEN} "
                          }
                        }
                    }
                }
            }
         }
      }
    }
  }
 post {
        always {
            echo "Looking for results at: ${env.WORKSPACE}/${projectTestBuildDir}/allure-results"
            echo "Service Name : "+params.get('ServiceName')
            script {
                allure commandline: 'Allure-2.14', jdk: "${JDK}", reportBuildPolicy: 'ALWAYS', results: [[path: "${projectTestBuildDir}/allure-results"]]
            }
        }
    }
}