@echo off

echo "Pulling the latest changes..."

git pull

cd ..\..

IF NOT EXIST .\Toolbox-Java\ (
	git clone https://github.com/ASofterSpace/Toolbox-Java.git
)

cd Toolbox-Java

git pull

cd ..\cdm\windows
