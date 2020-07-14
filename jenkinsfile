#!/usr/bin/env groovy 

node {
      stage("Build Started") {
      }
	  
	    stage("Checkout") {
        checkout scm
      }
      
      //Build project and export jacoco reports.
      stage("Build Project") {
        bat "mvn clean verify package"
      }
          
      //Deploy in SonarQube.
      stage("Code Quality Analysis") {
        withSonarQubeEnv() {
          bat "mvn sonar:sonar"
        }
      }

      stage("Build Succeed") {
      }
  
}