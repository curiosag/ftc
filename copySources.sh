#!/bin/bash   

target="./ftcClientJava/target/dependency-sources/"

echo "copying sources to $target"

cp ./JavaCommon/target/JavaCommon-0.1-sources.jar $target
cp ./ftcConnector/target/ftcConnector-0.1-sources.jar $target
cp ./ftcParser/target/ftcParser-0.1-sources.jar $target
cp ./ftcShared/target/ftcShared-0.1-sources.jar $target
cp ./ftcSql/target/ftcSql-0.1-sources.jar $target
cp ./ftcClientJava/target/ftcClientJava-0.1-sources.jar $target
