pipeline {
    agent any
     tools {
            jdk "Java8"
        }
    stages {
        stage('build') {
            steps {
                sh "./gradlew clean build"
            }
        }
    }
}