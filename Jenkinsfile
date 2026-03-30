pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'SathishToken', url: 'https://github.com/SSANDHI/SELENIUM_AUTOMATION.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Run Tests') {
            steps {
                sh 'mvn test -DsuiteXmlFile=testng.xml'
            }
        }
    }
    post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }
    }
}
