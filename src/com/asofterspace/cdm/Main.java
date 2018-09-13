package com.asofterspace.cdm;

import com.asofterspace.toolbox.cdm.CdmCtrl;
import com.asofterspace.toolbox.cdm.exceptions.AttemptingEmfException;
import com.asofterspace.toolbox.cdm.exceptions.CdmLoadingException;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.Utils;
import com.asofterspace.toolbox.utils.ProgressIndicator;
import com.asofterspace.toolbox.utils.NoOpProgressIndicator;
import com.asofterspace.toolbox.web.JSON;

public class Main {

	public final static String PROGRAM_TITLE = "cdm commandline tool";
	public final static String VERSION_NUMBER = "0.0.0.2(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
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
		String lowarg = args[0].toLowerCase();
		switch (lowarg) {
			case "info":
				if (args.length > 1) {
					showInfo(args[1]);
				} else {
					System.err.println("You called  cdm info  which should be followed by a CDM path, but did not specify a CDM path.");
					System.exit(4);
				}
				break;
			case "help":
				showHelp();
				break;
			case "version":
				showVersion();
				break;
			default:
				System.err.println("Sorry, I did not understand the argument '" + args[0] + "' - call  cdm help  to get a list of possible commands.");
				System.exit(2);
		}
		
		// all is shiny! all is good! exit code 0!
		System.exit(0);
	}
	
	private static void loadCdm(String pathArg) {
		
		Directory cdmDir = new Directory(pathArg);
		ProgressIndicator noProgress = new NoOpProgressIndicator();
		
		try {
			CdmCtrl.loadCdmDirectory(cdmDir, noProgress);
		} catch (AttemptingEmfException | CdmLoadingException e) {
			System.err.println(e.getMessage());
			System.exit(3);
		}
	}

	private static void showInfo(String pathArg) {

		// TODO :: if this is just one file (e.g. toLowerCase() ends on .cdm) then actually just load that one file instead!
		loadCdm(pathArg);

		String cdmVersion = CdmCtrl.getCdmVersion();
		String cdmPrefix = CdmCtrl.getCdmVersionPrefix();
		System.out.println("CDM version: " + cdmVersion);
		System.out.println("CDM version prefix: " + cdmPrefix);
		System.out.println("CDM compatible with EGS-CC release: " + CdmCtrl.getCompatWithEGSCCstr(cdmVersion, cdmPrefix));
		System.out.println("CDM compatible with RTF Framework CDM editor version: " + CdmCtrl.getCompatWithMCDEstr(cdmVersion, cdmPrefix));
	}
	
	private static void showHelp() {
		System.out.println("Welcome to the " + Utils.getFullProgramIdentifier() + "! :)");
		System.out.println("");
		System.out.println("Possible commands:");
		System.out.println("info <path> .. shows information about the CDM lying at the path");
		System.out.println("version .. shows the version of the " + PROGRAM_TITLE);
		System.out.println("help .. shows this help");
	}
	
	private static void showVersion() {
		System.out.println(Utils.getFullProgramIdentifierWithDate());
	}

}
