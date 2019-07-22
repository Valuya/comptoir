
pipeline {

    parameters {
        booleanParam(name: 'SKIP_TESTS', defaultValue: false, description: 'Skip tests')
        string(name: 'ALT_DEPLOYMENT_REPOSITORY', defaultValue: '', description: 'Alternative deployment repo')
    }
    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    stages {
        stage ('Build') {
            agent {
                docker {
                    image 'maven:3-alpine'
                    label 'be.valuya.jenkins.build=true'
                    args  '--network comptoir-dev-napo-db-net'
                }
            }
            environment {
                NAPO_DB_PASSWORD = credentials('comptoir-dev-napo-db-root-password')
            }
            steps {
               withMaven(maven: 'maven', mavenSettingsConfig: 'nexus-mvn-settings') {
                    sh '''
                        mvn \
                        -Ddb.napo.url=jdbc:mariadb://db-napo:10101/napo \
                        -Ddb.napo.username=root \
                        -Ddb.napo.password="$NAPO_DB_PASSWORD" \
                        -Ddb.napo.schema=napo \
                        -Pcomptoir-thorntails
                        clean package deploy
                    '''
               }
            }
        }
    }
}
