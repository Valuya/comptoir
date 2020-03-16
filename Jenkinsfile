
pipeline {
    agent any
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
                    image 'maven'
                    args  '--network comptoir-dev-napo-db-net'
                }
            }
            environment {
                NAPO_DB_PASSWORD = credentials('comptoir-dev-napo-db-root-password')
            }
            steps {
               withMaven(maven: 'maven', mavenSettingsConfig: 'nexus-mvn-settings') {
                    sh '''
                        $MVN_CMD \
                        -Ddb.napo.url=jdbc:mariadb://db-napo/napo \
                        -Ddb.napo.username=root \
                        -Ddb.napo.password="$NAPO_DB_PASSWORD" \
                        -Ddb.napo.schema=napo \
                        -Pcomptoir-thorntails \
                        clean package deploy
                    '''
               }
               stash includes: 'comptoir-thorntail/Dockerfile,comptoir-thorntail/target/comptoir-thorntail.jar', name: 'thorntail-jar'
               stash includes: 'docker/**,comptoir-ear/target/comptoir-ear.ear', name: 'docker-ear'
            }
        }
        stage ('Build docker image') {
            steps {
                unstash 'thorntail-jar'
                sh '''
                    export COMMIT="$(git rev-parse --short HEAD)"
                    docker build \
                      --tag comptoir-thorntail-jenkins-build:${COMMIT} \
                      -f comptoir-thorntail/Dockerfile \
                      .
                    docker tag comptoir-thorntail-jenkins-build:${COMMIT} docker.valuya.be/comptoir-thorntail:latest
                    docker push docker.valuya.be/comptoir-thorntail:latest
                '''
            }
        }
    }
}
