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
            // Archive screenshots (on failures)
            archiveArtifacts artifacts: 'screenshots/**/*.png', fingerprint: true

            // Archive Extent report files
            archiveArtifacts artifacts: 'reports/**/*.html', fingerprint: true
            archiveArtifacts artifacts: 'reports/**/*.json', fingerprint: true

            // Archive log4j2 logs
            archiveArtifacts artifacts: 'logs/**/*.log', fingerprint: true

            // Publish Extent HTML report (requires HTML Publisher plugin)
            publishHTML([
                reportDir: 'reports',
                reportFiles: 'ExtentReport.html',
                reportName: 'Extent Report'
            ])

            // Publish JUnit/TestNG results (for trend graph)
            junit 'target/surefire-reports/*.xml'
        }

        failure {
            echo 'Build failed. Check logs, screenshots, and reports in artifacts.'
        }

        success {
            echo 'Build succeeded. Reports published.'
        }
    }
}
