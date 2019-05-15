node {
	agent {
        docker { image 'node:7-alpine' }
    }
    stage('Clone source') {
        checkout scm
    }
    
    stage('what the result') {
    	sh "pwd"
    	sh "ls -alh"
    }
    
    stage('compile the code') {
       sh "mvn clean install"
    }
}
