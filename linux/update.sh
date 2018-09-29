#!/bin/bash

echo "Pulling the latest changes..."

git pull

cd ../..

if [[ ! -d ./Toolbox-Java ]]; then
	git clone https://github.com/ASofterSpace/Toolbox-Java.git
fi

cd Toolbox-Java

git pull

cd ../cdm/linux
