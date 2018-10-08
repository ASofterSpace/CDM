#!/bin/bash

echo "Building the cdm commandline tool..."

cd ..

if [[ ! -d ../Toolbox-Java ]]; then
	echo "It looks like you did not yet get the Toolbox-Java project - please do so (and put it as a folder next to the CDM Script Editor folder.)"
	exit
fi

cd src/com/asofterspace

rm -rf toolbox

mkdir toolbox
cd toolbox

mkdir cdm
cd cdm
mkdir exceptions
cd ..
mkdir coders
mkdir io
mkdir utils

cd ../../../..

cp ../Toolbox-Java/src/com/asofterspace/toolbox/*.java src/com/asofterspace/toolbox
cp ../Toolbox-Java/src/com/asofterspace/toolbox/cdm/*.* src/com/asofterspace/toolbox/cdm
cp ../Toolbox-Java/src/com/asofterspace/toolbox/cdm/exceptions/*.* src/com/asofterspace/toolbox/cdm/exceptions
cp ../Toolbox-Java/src/com/asofterspace/toolbox/coders/*.* src/com/asofterspace/toolbox/coders
cp ../Toolbox-Java/src/com/asofterspace/toolbox/io/*.* src/com/asofterspace/toolbox/io
cp ../Toolbox-Java/src/com/asofterspace/toolbox/utils/*.* src/com/asofterspace/toolbox/utils

rm -rf bin

mkdir bin

cd src

find . -name "*.java" > sourcefiles.list

javac -encoding utf8 -d ../bin @sourcefiles.list

cd ..

echo "Build executed!"

cd linux

