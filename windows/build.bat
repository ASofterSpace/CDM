@echo off

echo "Building the cdm commandline tool..."

cd ..

IF NOT EXIST ..\Toolbox-Java\ (
	echo "It looks like you did not yet get the Toolbox-Java project - please do so (and put it as a folder next to the cdm folder.)"
	EXIT 1
)

cd src\com\asofterspace

rd /s /q toolbox

md toolbox
cd toolbox

md cdm
cd cdm
md exceptions
cd ..
md coders
md io
md utils

cd ..\..\..\..

copy "..\Toolbox-Java\src\com\asofterspace\toolbox\*.java" "src\com\asofterspace\toolbox"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\cdm\*.*" "src\com\asofterspace\toolbox\cdm"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\cdm\exceptions\*.*" "src\com\asofterspace\toolbox\cdm\exceptions"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\coders\*.*" "src\com\asofterspace\toolbox\coders"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\io\*.*" "src\com\asofterspace\toolbox\io"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\utils\*.*" "src\com\asofterspace\toolbox\utils"

rd /s /q bin

md bin

cd src

dir /s /B *.java > sourcefiles.list

echo Executing build...

javac -deprecation -Xlint:all -encoding utf8 -d ../bin @sourcefiles.list

pause
