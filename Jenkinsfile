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
            // Publish Extent HTML report (requires HTML Publisher plugin)
            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target',
                reportFiles: 'ExtentReport.html',
                reportName: 'Extent Report'
            ])

            // Publish JUnit/TestNG results (for trend graph)
            junit 'target/surefire-reports/*.xml'
        }

        failure {
            // Archive screenshots (on failures)
            archiveArtifacts artifacts: 'target/screenshots/**/*.png', fingerprint: true

            // Archive Extent report files (HTML + JSON)
            archiveArtifacts artifacts: 'target/*.html', fingerprint: true
            archiveArtifacts artifacts: 'target/*.json', fingerprint: true

            // Archive log4j2 logs
            archiveArtifacts artifacts: 'logs/**/*.log', fingerprint: true

            echo 'Build failed. Check logs, screenshots, and reports in artifacts.'
        }

        success {
            echo 'Build succeeded. Reports published.'
        }
    }
}
