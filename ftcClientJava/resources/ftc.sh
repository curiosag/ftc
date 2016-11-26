#!/bin/sh

# the proper way to pass parameters to java taken from
# http://stackoverflow.com/a/4772856/1428369
# $@ expands to the remaining arguments for the shell script, and putting quotes around it causes the arguments to be individually quoted

java -jar ftcClientJava-0.1.jar "$@"
