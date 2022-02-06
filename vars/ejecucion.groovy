def call(){
  pipeline {
      agent any
      environment {
          NEXUS_USER         = credentials('NEXUS-USER')
          NEXUS_PASSWORD     = credentials('NEXUS-PASS')
          GIT_REPO_NAME      = GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
      }
      stages {
          stage("Pipeline"){
              steps {
                  script{
                    //def ejecucion = load 'maven.groovy'
                    sh "echo '1: '${env.GIT_BRANCH}"
                    sh "echo '2: '${GIT_BRANCH}"
                    String[] str
                    str        = GIT_BRANCH.split('/')
                    rama = str[1]
                    if (rama.contains('feature') ) {
                        sh "echo 'rama feature IC'"
                        ic.call()
                    }
                    else {
                        sh "echo 'rama 2' ${str[1]}  ' DC'"
                        dc.call()
                    }
                  }
              }
              post{
                success{
                    slackSend color: 'good', message: "[Ignacio] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack'
                }
                failure{
                    slackSend color: 'danger', message: "[Ignacio] [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.TAREA}]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack'
                }
            }
          }
      }
  }
}
return this;
