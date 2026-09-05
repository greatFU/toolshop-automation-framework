def composeStarted = false
def reportsCleaned = false

pipeline {
    // Requires a Windows agent with Maven, browsers and Docker available.
    agent any

    options {
        skipDefaultCheckout()
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'],
            description: 'Browser used for UI test execution')
        booleanParam(name: 'HEADLESS', defaultValue: true,
            description: 'Run browser in headless mode')
        choice(name: 'PROFILE', choices: ['Regression', 'Smoke'],
            description: 'Test suite to execute')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                // Only delete generated output within this Jenkins workspace.
                dir('reports') { deleteDir() }
                dir('screenshots') { deleteDir() }
                dir('target/surefire-reports') { deleteDir() }
                script { reportsCleaned = true }
            }
        }

        stage('Verify') {
            steps {
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
                    // A failed startup may still leave containers running.
                    script { composeStarted = true }
                    bat 'docker compose up -d --build'
                }
            }
        }

        stage('Wait for Composer') {
            options {
                timeout(time: 10, unit: 'MINUTES')
            }
            steps {
                dir('toolshop-app') {
                    script {
                        int composerExitCode = bat(
                            script: 'docker compose wait composer', returnStatus: true)
                        if (composerExitCode != 0) {
                            error("Composer failed with exit code ${composerExitCode}")
                        }
                    }
                }
            }
        }

        stage('Prepare Database') {
            steps {
                dir('toolshop-app') {
                    // Destructive reset of the dedicated test application's database.
                    bat 'docker compose exec -T laravel-api php artisan migrate:fresh --seed'
                }
            }
        }

        stage('Wait for Application') {
            steps {
                bat '''
                    @echo off
                    for /L %%i in (1,1,60) do (
                        curl.exe -fsS --connect-timeout 2 --max-time 5 http://localhost:8091/brands >nul 2>&1 && curl.exe -fsS --connect-timeout 2 --max-time 5 http://localhost:4200 >nul 2>&1 && (
                            echo Toolshop is ready
                            exit /b 0
                        )
                        echo Waiting for Toolshop... %%i/60
                        powershell -NoProfile -Command "Start-Sleep -Seconds 2"
                    )
                    echo Toolshop did not become ready
                    exit /b 1
                '''
            }
        }

        stage('Run UI Tests') {
            steps {
                bat """
                    mvn clean test ^
                        -P${params.PROFILE} ^
                        -Dbrowser=${params.BROWSER} ^
                        -Dheadless=${params.HEADLESS} ^
                        -DbaseUrl=http://localhost:4200
                """
            }
        }
    }

    post {
        always {
            script {
                try {
                    if (composeStarted && currentBuild.currentResult != 'SUCCESS') {
                        // Diagnostics must not replace the original failure.
                        catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                            timeout(time: 2, unit: 'MINUTES') {
                                dir('toolshop-app') {
                                    bat(script: 'docker compose ps -a', returnStatus: true)
                                    bat(script: 'docker compose logs --no-color --tail=200', returnStatus: true)
                                }
                            }
                        }
                    }
                    // Do not publish stale output after a checkout/cleanup failure.
                    if (reportsCleaned) {
                        try {
                            junit(testResults: 'target/surefire-reports/TEST-*.xml',
                                allowEmptyResults: true)
                        } finally {
                            archiveArtifacts(
                                artifacts: 'reports/**, target/surefire-reports/**, screenshots/**',
                                allowEmptyArchive: true)
                        }
                    }
                } finally {
                    if (composeStarted) {
                        // Run even when reporting fails; do not remove volumes.
                        catchError(buildResult: 'FAILURE', catchInterruptions: false) {
                            timeout(time: 2, unit: 'MINUTES') {
                                dir('toolshop-app') {
                                    bat 'docker compose down'
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
