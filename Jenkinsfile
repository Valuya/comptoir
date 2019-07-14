#!groovy

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
            steps {
               withMaven(maven: 'maven', mavenSettingsConfig: 'db-napo-settings-xml') {
                    sh "mvn -DskipTests=${params.SKIP_TESTS} clean compile install"
               }
            }
        }
        stage ('Publish') {
            steps {
                script {
                    env.MVN_ARGS=""
                    if (params.ALT_DEPLOYMENT_REPOSITORY != '') {
                        env.MVN_ARGS="-DaltDeploymentRepository=${params.ALT_DEPLOYMENT_REPOSITORY}"
                    }
                }
                withMaven(maven: 'maven', mavenSettingsConfig: 'db-napo-settings-xml',
                          mavenOpts: '-DskipTests=true') {
                    sh "mvn deploy $MVN_ARGS"
                }
            }
        }
    }
}
