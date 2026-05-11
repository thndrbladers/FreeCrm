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
                bat 'mvn clean test -Denv=dev -DsuiteXmlFile=testng/features/${env.BRANCH_NAME}.xml'
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