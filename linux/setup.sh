#!/bin/bash

echo "Registering in .bashrc if not already done..."

bashrcLine="source $(pwd)/cdm_bashrc.sh"

grep -qF -- "$bashrcLine" ~/.bashrc || echo "$bashrcLine" >> ~/.bashrc

# also adjust the path right now, not just after a terminal restart
./cdm_bashrc.sh


echo "Building the cdm commandline tool..."

./build.sh

