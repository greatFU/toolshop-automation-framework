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

	stage('Wait for Composer') {
    	steps {
        	dir('toolshop-app') {
            	bat 'docker compose wait composer'
            	bat 'docker compose ps -a'
        }
    }
}
	stage('Prepare Database') {
    	steps {
        	dir('toolshop-app') {
            	bat 'docker compose exec -T laravel-api php artisan migrate:fresh --seed'
        }
    }
}
	stage('Wait for Application') {
    	steps {
        	bat '''
        	@echo off
        	for /L %%i in (1,1,60) do (
            	curl.exe -fsS http://localhost:8091/brands >nul 2>&1 && curl.exe -fsS http://localhost:4200 >nul 2>&1 && (
                	echo Toolshop is ready
                	exit /b 0
            )

            echo Waiting for Toolshop... %%i/60
            timeout /t 2 /nobreak >nul
        )

        echo Toolshop did not become ready
        exit /b 1
        '''
    }
}
}
}