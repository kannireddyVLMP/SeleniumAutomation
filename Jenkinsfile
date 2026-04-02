pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn test'
            }
        }
    }

    post {
        always {
            // Archive screenshots (assuming they’re saved in /screenshots folder)
            archiveArtifacts artifacts: 'screenshots/**/*.png', fingerprint: true

            // Archive Extent report files (HTML, JSON, etc.)
            archiveArtifacts artifacts: 'reports/**/*.html', fingerprint: true
            archiveArtifacts artifacts: 'reports/**/*.json', fingerprint: true

            // Publish Extent HTML report (requires HTML Publisher plugin)
            publishHTML([
                reportDir: 'reports',
                reportFiles: 'ExtentReport.html',
                reportName: 'Extent Report'
            ])
        }

        failure {
            echo 'Build failed. Check logs and screenshots in artifacts.'
        }

        success {
            echo 'Build succeeded. Reports published.'
        }
    }
}
