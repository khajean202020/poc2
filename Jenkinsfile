def projectDirName = "broker-modernisation-services-automation"
def projectTestBuildDir = "${projectDirName}"
def JDK = "jdk-11.0.24+9"
properties([parameters([
[$class      : 'ChoiceParameter',
                             choiceType  : 'PT_SINGLE_SELECT',
                             description : 'Select a test suite from dropdown List',
                             filterLength: 0,
                             filterable  : false,
                             name        : 'TestNGsuiteXmlFile',
                             randomName  : 'none',

                             script      : [$class        : 'GroovyScript',
                                            fallbackScript: [classpath: [],sandbox: true, script: "return['Could not get test module name']"],
                                            script        : [classpath: [], sandbox: true, script: "return ['','testng']"]
                             ]
                            ],

])])

pipeline {
agent any
  options {
    timestamps()
  }


  stages {

     stage('Check Build Parameter Values') {
               steps {
               script{

           		   println  "is TestNGsuiteXmlFile parameter value not null ? : "
           		   println  "${params.TestNGsuiteXmlFile}".toString()!=null
           		   println  "is TestNGsuiteXmlFile parameter value empty ? : " + "${params.TestNGsuiteXmlFile}".toString().isEmpty()


               }
               }
           }
    stage('List Java Version') {
      steps {
      withMaven(maven : 'apache-maven-3.6.3') {
        bat "java -version"
      }
      }
    }
    stage('Execute tests') {
      steps {


                script{
                 withMaven(maven : 'apache-maven-3.6.3') {
                    if("${params.TestNGsuiteXmlFile}".toString().isEmpty()){
                        echo " test suite value not selected from dropdown list.. No test cases executed !!! "
                    }

                    else{
                        echo "Test execution started"


                        bat "./mvnw clean test -DsuiteXmlFile=${params.TestNGsuiteXmlFile}"


                    }
                    }

         }
      }
    }
  }

post {
        always {
            publishHTML (target: [
                  allowMissing: false,
                  alwaysLinkToLastBuild: false,
                  keepAll: true,
                  reportDir: 'reports',
                  reportFiles: '*.html',
                  reportName: "RCov Report"
                ])
        }
    }

}