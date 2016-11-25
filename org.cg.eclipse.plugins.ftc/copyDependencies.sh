#!/bin/bash

function feedback {
	if [[ -z $1 || -z $2 ]]
	then
		exit 1
	fi

	if [[ ! -e $1 || $1 -nt $2 ]] # destination does not exists OR src is newer than dest
	then
		echo "copying $1"
	fi
}


SRC="../ftcClientJava/target/"
DEPJAR=$SRC"dependency-jars/"
DEPSOURCES=$SRC"dependency-sources/"

DST="./lib/"

echo "copying dependencies from $SRC to $DST"

cp -u $SRC"ftcClientJava-0.1.jar" $DST
cp -u $SRC"ftcClientJava-0.1-sources.jar" $DST

for f in $( ls $DEPJAR ); do
	feedback $DEPJAR$f $DST$f
	cp -u $DEPJAR$f $DST$f
done

for f in $( ls $DEPSOURCES ); do
	feedback $DEPJAR$f $DST$f
	cp -u $DEPSOURCES$f $DST$f
done

 


