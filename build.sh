#!/bin/bash

mvn install
./copySources.sh
cd org.cg.eclipse.plugins.ftc
./copyDependencies.sh
