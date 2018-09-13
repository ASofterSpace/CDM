IF NOT EXIST ..\Toolbox-Java\ (
	echo "It looks like you did not yet get the Toolbox-Java project - please do so (and put it as a folder next to the CDM Script Editor folder.)"
	EXIT
)

cd src\com\asofterspace

rd /s /q toolbox

md toolbox
cd toolbox

md cdm
cd cdm
md exceptions
cd ..
md io
md utils
md web

cd ..\..\..\..

copy "..\Toolbox-Java\src\com\asofterspace\toolbox\*.java" "src\com\asofterspace\toolbox"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\cdm\*.*" "src\com\asofterspace\toolbox\cdm"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\cdm\exceptions\*.*" "src\com\asofterspace\toolbox\cdm\exceptions"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\io\*.*" "src\com\asofterspace\toolbox\io"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\utils\*.*" "src\com\asofterspace\toolbox\utils"
copy "..\Toolbox-Java\src\com\asofterspace\toolbox\web\*.*" "src\com\asofterspace\toolbox\web"

rd /s /q bin

md bin

cd src

dir /s /B *.java > sourcefiles.list

javac -cp "../emf/*" -d ../bin @sourcefiles.list

cd ..

pause
