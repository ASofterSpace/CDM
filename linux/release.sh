#!/bin/bash

echo "Re-building with target Java 7 (such that the compiled .class files will be compatible with as many JVMs as possible)..."

cd ../src

javac -encoding utf8 -d ../bin -bootclasspath ../other/java7_rt.jar -source 1.7 -target 1.7 @sourcefiles.list

cd ..



echo "Creating the release file cdm.zip..."

mkdir release
cd release

mkdir cdm

cp -R ../bin cdm
cp ../UNLICENSE cdm
cp ../cdm cdm
cp ../cdm.bat cdm

cd cdm
mkdir linux
mkdir windows
cp ../../linux/install_from_zip.sh linux
cp ../../linux/setup.sh linux
cp ../../linux/cdm_bashrc.sh linux
cp ../../windows/install_from_zip.bat windows
cp ../../windows/setup.bat windows
cd ..

zip -rq cdm.zip cdm

mv cdm.zip ..

cd ..
rm -rf release

echo "The file cdm.zip has been created in $(pwd)"

cd linux

