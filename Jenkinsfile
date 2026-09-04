pipeline {
    agent any

    options {
        skipDefaultCheckout()
        disableConcurrentBuilds()
    }

    parameters {
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Browser used for UI test execution'
        )

        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browser in headless mode'
        )

        choice(
            name: 'PROFILE',
            choices: ['Regression', 'Smoke'],
            description: 'Test suite to execute'
        )
    }

    stages {

    stage('Checkout') {
        steps {
            checkout scm
        }
    }

    stage('Verify') {
        steps {
            bat 'git status'
            bat 'mvn -version'
            bat 'docker --version'
            bat 'docker compose version'
        }
    }

    stage('Checkout Toolshop') {
        steps {
            dir('toolshop-app') {
                deleteDir()

                git branch: 'main',
                    url: 'https://github.com/testsmith-io/practice-software-testing.git'
            }
        }
    }
    
    stage('Start Toolshop') {
    	steps {
        	dir('toolshop-app') {
            	bat 'docker compose up -d --build'
            	bat 'docker compose ps -a'
        }
    }
}
}
}