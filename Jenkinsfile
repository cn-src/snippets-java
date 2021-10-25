pipeline {
    agent any
    stages {

        stage('build-Java11') {
            tools {
                    jdk "Java11"
            }
            steps {
                sh "./gradlew clean build"
            }
        }
    }
}