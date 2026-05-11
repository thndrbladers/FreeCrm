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
                bat 'mvn clean test -DsuiteXmlFile=feature.xml'
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