#!/bin/sh

javac *.java

# java build_pcfg data/parses.train trained.pcfg
java build_pcfg $1 $2

# java pcky trained.pcfg data/sents.test parses.hyp
# java pcky $2 $3 $4