#!/bin/bash
mvn clean install
rm codenapper.jar
cp target/*.jar codenapper.jar
