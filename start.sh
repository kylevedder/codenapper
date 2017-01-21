#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo $DIR
date >> "$DIR/server.log"
if [ -z ${SOURCEPATH+x} ];
then
    echo "SOURCEPATH is unset, please set it.";
else
    (java -jar "$DIR/codenapper.jar" --source-path="$SOURCEPATH" &)
fi

