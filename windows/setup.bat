@echo off

echo "Registering in Windows PATH variable if not already done..."

REM  get the a softer space cdm directory
set ASSCDM=%~dp0..
set ASSCDMFIND=%ASSCDM:\=\\%

REM  set the user PATH environment variable
echo "%PATH%" | findstr /i %ASSCDMFIND% >nul && echo "Already registered in environment variable" || setx PATH "%PATH%;%ASSCDM%"

REM set the PATH environment variable also for the remainder of this cmd session
echo "%PATH%" | findstr /i %ASSCDMFIND% >nul && echo "Already registered in session variable" || set PATH=%PATH%;%ASSCDM%


echo "Building the cdm commandline tool..."

REM  "build.bat"