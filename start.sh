#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo $DIR
date >> "$DIR/server.log"
(java -jar "$DIR/target/codenapper-1.0.1-RELEASE.jar" &)
