#!/bin/bash
SRC="../../ftc_workspace/ftcClientJava/target/"
DEPJAR=$SRC"dependency-jars/"
DEPSOURCES=$SRC"dependency-sources/"

DST="./lib/"

cp -u $SRC"ftcClientJava-0.1.jar" $DST
cp -u $SRC"ftcClientJava-0.1-sources.jar" $DST

for f in $( ls $DEPJAR ); do
	cp -u $DEPJAR$f $DST$f
done

for f in $( ls $DEPSOURCES ); do
	cp -u $DEPSOURCES$f $DST$f
done

 


