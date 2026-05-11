pipeline {

    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Execute Tests') {

            steps {

                script {

                    def suitePath = bat(
                        script: """
                        @echo off
                        dir /s /b FeatureXmlsByReleases\\${env.BRANCH_NAME}.xml
                        """,
                        returnStdout: true
                    ).trim()

                    bat "mvn clean test -Denv=dev -DsuiteXmlFile=\"${suitePath}\""
                }
            }
        }
    }

    post {

        success {
            echo 'Execution Passed'
        }

        failure {
            echo 'Execution Failed'
        }

        always {
            echo 'Execution Completed'
        }
    }
}