# cdm

**Class:** EGS-CC Tools

**Target Language:** Java 8 64-Bit

**Minimum Supported Language:** Java 7 32-Bit

**Platform:** Windows / Linux

A commandline tool for modifying EGS-CC CDMs

## Manual

The official manual for this tool can be found [here](http://asofterspace.com/cdm/manual.pdf).

## Default Setup

To set up the cdm commandline tool you can just download a zip file, call a setup script, and it should start working.

In particular, you can download the latest zip from [here](http://asofterspace.com/cdm/cdm.zip).

To run the cdm commandline tool on your computer, you need to install Java on it.
(Any JRE or JVM of Java 7 or higher will be sufficient; for using very large CDM files, a 64-Bit version will be necessary.)

To check that you have Java installed, you can type in a terminal:

```
java -version
```

Once Java is found, unzip the cdm commandline tool file, e.g. under Linux by typing:

```
unzip cdm.zip
```

Finally, to register the cdm command with your terminal, call under Windows:

```
cd cdm\windows
install_from_zip.bat
```

Or under Linux:

```
cd cdm/linux
./install_from_zip.sh
```

The `cdm` command will now be available in the current terminal session.

Especially under Windows, it might be necessary to restart the machine before it also becomes available in other terminal sessions.


## Setup using Manual Build

1. Ensure you have a JDK (Java Development Kit) of Java 7 or higher on your machine, ideally a 64-Bit version, and that `javac` is on your current `PATH`:

```
java -version
```

and

```
javac -version
```

2. Clone this repository onto your machine:

```
git clone https://github.com/ASofterSpace/cdm.git
```

3. Start the build of the latest cdm commandline tool version by calling under Windows:

```
cd cdm\windows
install_latest.bat
```

Or under Linux:

```
cd cdm/linux
./install_latest.sh
```

The `cdm` command will now be available in the current terminal session.

Especially under Windows, it might be necessary to restart the machine before it also becomes available in other terminal sessions.

### Creating a Release

Should you want to create a zip release of what you just built, then you have to get your hands on a Java 7 `rt.jar` (such that the released zip can be created in a way compatible with Java 7 and above.)
Once you have it, put this file at the location:

```
cdm/other/java7_rt.jar
```

Then, under Linux you can call:

```
./release.sh
```

(from the same directory in which you also executed the `./install_latest.sh` command.)

## Run

To start up the cdm commandline tool after it has been built, you can call:

```
cdm help
```

This should work both under Windows (where `cdm` is expanded to `cdm.bat` automagically) and Linux (where the `cdm` shell script is executed.)

## License and Support

We at A Softer Space really love the Unlicense, which pretty much allows anyone to do anything with this source code.
For more info, see the file UNLICENSE.

If you desperately need to use this source code under a different license, [contact us](mailto:moya@asofterspace.com) - I am sure we can figure something out.

Finally, if you like this tool and would be happy for it to be developed further, for bugs that you find to be fixed, and even for new features that you have in mind to be added, then just let us know and we will offer you a service contract at such great conditions that you cannot possibly say no to it. :)