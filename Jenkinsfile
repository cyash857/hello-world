pipeline {
    agent any

    environment {
        DATE = new Date().format('yy.M')
        TAG = "${DATE}.${BUILD_NUMBER}"
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn dependency:purge-local-repository'
            }
        }
         stage ('Test') {
	     steps {
	         sh 'mvn surefire:test'
	     }	     
        }
	 stage('SonarQube analysis') {
             steps {
                withSonarQubeEnv('sonarqube') {
                    sh "${scannerHome}/bin/sonar-scanner"
                }
             }
        }
        stage("Quality gate") {
             steps {
                waitForQualityGate abortPipeline: true
             }
        }
	 stage ('OWASP Dependency Check') {
	     steps {
		 sh 'mvn clean install org.owasp:dependency-check-maven:check -Ddependency-check-format=XML'
		 sh 'aws s3 cp target/dependency-check-report.html s3://pocbucketreport/owaspReport/'    
             }
        }
        stage('Docker Build') {
            steps {
                script {
                    docker.build("default-docker-local/myapp:${TAG}")
                }
            }
        }
	stage('Pushing Docker Image to Jfrog Artifactory') {
            steps {
                script {
                    docker.withRegistry('https://cyash857.jfrog.io/', 'artifactory-credential') {
                        docker.image("default-docker-local/myapp:${TAG}").push()
                        docker.image("default-docker-local/myapp:${TAG}").push("latest")
                    }
                }
            }
        }
        stage('Deploy'){
            steps {
                sh "docker stop myapp | true"
                sh "docker rm myapp | true"
                sh "docker run --name myapp -d -p 80:8080 cyash857.jfrog.io/default-docker-local/myapp:${TAG}"
            }
        }
    }
}
