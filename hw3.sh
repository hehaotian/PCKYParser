#!/bin/sh

javac *.java
java build_pcfg $1 $2
# java pcky $2 $3 $4