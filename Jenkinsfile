pipeline {
    agent any

    environment {
        DATE = new Date().format('yy.M')
        TAG = "${DATE}.${BUILD_NUMBER}"
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
         stage ('Test') {
	     steps {
	         sh 'mvn surefire:test'
	     }	     
        }
	 stage ('OWASP Dependency Check') {
	     steps {
		 sh 'mvn clean install org.owasp:dependency-check-maven:check -Ddependency-check-format=XML'
             }
        }
        stage('Docker Build') {
            steps {
                script {
                    docker.build("default-docker-local/hello-world:${TAG}")
                }
            }
        }
	stage('Pushing Docker Image to Jfrog Artifactory') {
            steps {
                script {
                    docker.withRegistry('https://cyash857.jfrog.io/', 'artifactory-credential') {
                        docker.image("default-docker-local/hello-world:${TAG}").push()
                        docker.image("default-docker-local/hello-world:${TAG}").push("latest")
                    }
                }
            }
        }
        stage('Deploy'){
            steps {
                sh "docker stop hello-world | true"
                sh "docker run --name hello-world -d -p 9004:80 cyash857.jfrog.io/default-docker-local/hello-world:${TAG}"
            }
        }
    }
}
