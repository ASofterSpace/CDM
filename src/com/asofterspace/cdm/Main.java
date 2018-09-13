package com.asofterspace.cdm;

import com.asofterspace.toolbox.Utils;
import com.asofterspace.toolbox.web.JSON;

public class Main {

	public final static String PROGRAM_TITLE = "cdm commandline tool";
	public final static String VERSION_NUMBER = "0.0.0.1(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "13. September 2018";

	public static void main(String[] args) {
	
		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		// if we were called without arguments...
		if (args.length < 1) {
			// ... tell everyone that this is basically nonsense!
			showHelp();
			System.exit(1);
		}
		
		// check all the arguments
		for (int i = 0; i < args.length; i++) {
			switch (args[i].toLowerCase()) {
				case "-h":
				case "--help":
					showHelp();
					break;
				case "-v":
				case "--version":
					showVersion();
					break;
				default:
					System.out.println("Sorry, I did not understand the argument '" + args[i] + "' - call  cmd --help  to get a list of possible commands.");
			}
		}
		
		// all is shiny! all is good! exit code 0!
		System.exit(0);
	}
	
	private static void showHelp() {
		System.out.println("Welcome to the " + Utils.getFullProgramIdentifier() + "! :)");
		System.out.println("");
		System.out.println("Possible commands:");
		System.out.println("-v | --version .. shows the version of the " + PROGRAM_TITLE);
		System.out.println("-h | --help .. shows this help");
	}
	
	private static void showVersion() {
		System.out.println(Utils.getFullProgramIdentifierWithDate());
	}

}
