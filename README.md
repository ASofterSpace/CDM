# cdm

**Class:** EGS-CC Tools

**Language:** Java

**Platform:** Windows / Linux

A commandline tool for modifying EGS-CC CDMs

## Setup

Download our Toolbox-Java (which is a separate project here on github) into an adjacent directory on your hard drive.

Start the build by calling under Windows:

```
build.bat
```

Or under Linux:

```
build.sh
```

You then probably will also want to adjust your `PATH` variable, to add the path to the repository on your hard drive. (You don't really have to do this, but if you do not, then you always need to manually go to the cdm directory to execute the `cdm` command, and you might get tired of that. ^^)

Under Windows, assuming you have just called

```
C:\foo\bar\cdm\build.bat
```

you can go to the Control Panel, and in there System > Advanced System Settings > Environment Variables.
Then edit the `Path` environment variable and add in this case `C:\foo\bar\cdm`.
Finally, you might have to restart the machine for the change to take effect.

Under Linux, assuming you have just called

```
/foo/bar/cdm/build.sh
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

This should work both under Windows (where cdm is expanded to cdm.bat automagically) and Linux (where the cdm shell script is executed.)

## License

We at A Softer Space really love the Unlicense, which pretty much allows anyone to do anything with this source code.
For more info, see the file UNLICENSE.

If you desperately need to use this source code under a different license, [contact us](mailto:moya@asofterspace.com) - I am sure we can figure something out.