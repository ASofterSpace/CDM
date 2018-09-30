# cdm

**Class:** EGS-CC Tools

**Target Language:** Java 8 64-Bit

**Minimum Supported Language:** Java 7 32-Bit

**Platform:** Windows / Linux

A commandline tool for modifying EGS-CC CDMs

## Manual

The official manual for this tool can be found [here](http://asofterspace.com/cdm/manual.pdf).

## Setup

1. Ensure you have a JDK (Java Development Kit) of Java 7 or higher on your machine, ideally a 64-Bit version:

```
java -version
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