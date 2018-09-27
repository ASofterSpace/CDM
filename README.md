# cdm

**Class:** EGS-CC Tools

**Target Language:** Java 8 64-Bit

**Minimum Supported Language:** Java 7 32-Bit

**Platform:** Windows / Linux

A commandline tool for modifying EGS-CC CDMs

## Setup

1. Ensure you have a JDK (Java Development Kit) of Java 7 or higher on your machine, ideally a 64-Bit version:

```
java -version
```

2. Clone this repository onto your machine:

```
git clone https://github.com/ASofterSpace/cdm.git
cd cdm
```

3. Start the build of the latest cdm commandline tool version by calling under Windows:

```
build_latest.bat
```

Or under Linux:

```
./build_latest.sh
```

4. You probably will also want to adjust your `PATH` variable, to add the path to the repository on your hard drive. (You don't really have to do this, but if you do not, then you always need to manually go to the cdm directory to execute the `cdm` command, and you might get tired of that. ^^)

Under Windows, assuming you have just called

```
C:\foo\bar\cdm\build_latest.bat
```

you can go to the Control Panel, and in there System > Advanced System Settings > Environment Variables.
Then edit the `Path` environment variable and add in this case `C:\foo\bar\cdm`.
Finally, you might have to restart the machine for the change to take effect.

Under Linux, assuming you have just called

```
/foo/bar/cdm/build_latest.sh
```

you can edit the path variable by entering

```
export PATH=$PATH:/foo/bar/cdm/
```

As this change will get lost upon restart, you might want to put that line also into your `~/.bashrc` file.

## Run

To start up the cdm commandline tool after it has been built, you can call:

```
cdm help
```

This should work both under Windows (where `cdm` is expanded to `cdm.bat` automagically) and Linux (where the `cdm` shell script is executed.)

## License

We at A Softer Space really love the Unlicense, which pretty much allows anyone to do anything with this source code.
For more info, see the file UNLICENSE.

If you desperately need to use this source code under a different license, [contact us](mailto:moya@asofterspace.com) - I am sure we can figure something out.
