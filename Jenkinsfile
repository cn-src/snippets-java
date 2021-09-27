pipeline {
    agent any

    tools {
        jdk "Java8"
    }

    stages {
        stage('Build') {
            steps {
                git 'https://github.com/cn-src/snippets-java.git'
                sh "./gradlew build"
            }
        }
    }
}