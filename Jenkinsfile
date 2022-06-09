pipeline {
  agent any
  
  stages {
    stage ('Build') {
      steps {
        sh 'mvn clean package'
     stage ('Deploy') {
      steps {
        script {
          deploy adapters: [tomcat9(credentialsId: 'tomcat_credential', path: '', url: 'http://13.232.4.127:8080')], contextPath: '/pipeline', onFailure: false, war: 'webapp/target/*.war'
      }1
    }
  }
}
