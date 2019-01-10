#!/bin/bash

echo "Re-building with target Java 7 (such that the compiled .class files will be compatible with as many JVMs as possible)..."

cd ../src

# build build build!
javac -encoding utf8 -d ../bin -bootclasspath ../other/java7_rt.jar -source 1.7 -target 1.7 @sourcefiles.list

cd ..



echo "Creating the release file cdm.zip..."

mkdir release
cd release

mkdir cdm

# copy the main files
cp -R ../bin cdm
cp ../UNLICENSE cdm
cp ../cdm cdm
cp ../cdm.bat cdm

# copy the linux and windows files
cd cdm
mkdir linux
mkdir windows
cp ../../linux/install_from_zip.sh linux
cp ../../linux/setup.sh linux
cp ../../linux/cdm_bashrc.sh linux
cp ../../windows/install_from_zip.bat windows
cp ../../windows/setup.bat windows
cd ..

# convert \n to \r\n for the Windows files!
cd cdm
awk 1 ORS='\r\n' cdm.bat > rn
mv rn cdm.bat
cd windows
awk 1 ORS='\r\n' install_from_zip.bat > rn
mv rn install_from_zip.bat
awk 1 ORS='\r\n' setup.bat > rn
mv rn setup.bat
cd ..
cd ..

# create a version tag right in the zip file
cd cdm
version=$(./cdm version_for_zip)
echo "$version" > "$version"
cd ..

# zip it all up
zip -rq cdm.zip cdm

mv cdm.zip ..

cd ..
rm -rf release

echo "The file cdm.zip has been created in $(pwd)"

cd linux

