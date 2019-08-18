#!/bin/bash

VERSION=

mvn install:install-file -Dfile=YOUR_JAR.jar -DgroupId=YOUR_GROUP_ID -DartifactId=YOUR_ARTIFACT_ID -Dversion=${VERSION} -Dpackaging=jar -DlocalRepositoryPath=/var/www/html/mavenRepository

