package com.asofterspace.cdm;

import com.asofterspace.toolbox.cdm.CdmCtrl;
import com.asofterspace.toolbox.cdm.exceptions.AttemptingEmfException;
import com.asofterspace.toolbox.cdm.exceptions.CdmLoadingException;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.Utils;
import com.asofterspace.toolbox.utils.ProgressIndicator;
import com.asofterspace.toolbox.utils.NoOpProgressIndicator;
import com.asofterspace.toolbox.web.JSON;

import java.util.ArrayList;
import java.util.List;


public class Main {

	public final static String PROGRAM_TITLE = "cdm commandline tool";
	public final static String VERSION_NUMBER = "0.0.0.3(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "13. September 2018 - 14. September 2018";

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
			case "convert":
				if (args.length > 4) {
					convertCdm(args[1], args[2], args[3], args[4]);
				} else if (args.length > 2) {
					convertCdm(args[1], args[2], args[3], null);
				} else {
					System.err.println("You called  cdm convert  which should be followed by the target version, target format, a CDM source path, and optionally a CDM destination path, but did not specify these arguments.");
					System.exit(4);
				}
				break;
			case "validate":
				if (args.length > 1) {
					validate(args[1]);
				} else {
					System.err.println("You called  cdm validate  which should be followed by a CDM path, but did not specify a CDM path.");
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
	
	/**
	 * Open the CDM in originPath and convert it to the specified version (possibly including a prefix separated with a colon) and format.
	 * If destination path is null, overwrite the CDM in place.
	 * If destination path is given, store the conversion result there.
	 */
	private static void convertCdm(String targetVersionAndPrefix, String targetFormat, String originPath, String destinationPath) {
	
		String conversionTargetStr = "";
	
		// first check if the target format selected actually can be done (and if not, complain immediately!)
		if (!"-".equals(targetFormat)) {
			targetFormat = targetFormat.toUpperCase();
			
			switch (targetFormat) {
				case "XML":
					break;
				default:
					System.err.println("Sorry, the target format " + targetFormat + " is not yet supported!");
					System.exit(5);
			}
		
			conversionTargetStr = targetFormat + " format";
		}
		
		// now attempt to load the CDM from the origin path
		loadCdm(originPath);

		// do the conversion to a different version (and possibly prefix)
		if (!"-".equals(targetVersionAndPrefix)) {
			String[] verPrefArgs = targetVersionAndPrefix.split(":");
			String toVersion = null;
			String toPrefix = null;
			
			if (verPrefArgs.length > 1) {
				toPrefix = verPrefArgs[0];
				toVersion = verPrefArgs[1];
			} else {
				toVersion = verPrefArgs[0];
			}
			
			if ("-".equals(toVersion)) {
				toVersion = null;
			}
			
			// if no prefix is specified, take the correct one automagically!
			// (however, if the target version is null - so kept the same - then the prefix can also be null - also be kept the same)
			if ((toPrefix == null) && (toVersion != null)) {
				toPrefix = CdmCtrl.getPrefixForVersion(toVersion);
				if (toPrefix == null) {
					System.err.println("I do not know which prefix is associated with CDM version " + toVersion + ".");
					System.err.println("Please explicitly specify a prefix, e.g. call  cdm convert -:" + toVersion + " (...)");
					System.exit(7);
				}
			}
			
			// only check this now, after having checked for null, as when the user really does enter "-:...",
			// then we know they want to keep the original prefix and only change the version!
			if ("-".equals(toPrefix)) {
				toPrefix = null;
			}
			
			// we have to check again, as the user could have entered not just "-" (which would have triggered the outer if),
			// but also "-:-" (which we are guarding against here) to be especially funny... ;)
			if ((toPrefix != null) || (toVersion != null)) {
				CdmCtrl.convertTo(toVersion, toPrefix);
				
				if ("".equals(conversionTargetStr)) {
					conversionTargetStr = "CDM version " + CdmCtrl.getCdmVersion();
				} else {
					conversionTargetStr += " and CDM version " + CdmCtrl.getCdmVersion();
				}
			}
		}
		
		// now actually save the result
		if ((destinationPath == null) || ("-".equals(destinationPath))) {
		
			// overwrite the original with the new result
			// TODO :: do not ignore the target format once we have more than XML available!
			CdmCtrl.save();
		
		} else {
		
			// save the result to the new destination path
			// TODO :: do not ignore the target format once we have more than XML available!
			CdmCtrl.saveTo(new Directory(destinationPath));
		}
		
		if ("".equals(conversionTargetStr)) {
			System.out.println("No conversion done - as I was told to keep both version and format the same! :)");
		} else {
			System.out.println("Conversion to " + conversionTargetStr + " done!");
		}
	}
	
	private static void validate(String pathArg) {

		// TODO :: if this is just one file (e.g. toLowerCase() ends on .cdm) then actually just load that one file instead!
		loadCdm(pathArg);
		
		List<String> problems = new ArrayList<>();
		
		int problemAmount = CdmCtrl.checkValidity(problems);
		
		if (problemAmount > 0) {
			System.err.println("The CDM does not seem to be valid.");
			if (problemAmount == 1) {
				System.err.println("There is one problem:");
			} else {
				System.err.println("There are " + problemAmount + " problems:");
			}
			System.err.println("");
			for (String problem : problems) {
				System.err.println(problem);
			}
			System.exit(6);
		}
		
		System.out.println("The CDM looks valid to me!");
	}

	private static void showHelp() {
		System.out.println("Welcome to the " + Utils.getFullProgramIdentifier() + "! :)");
		System.out.println("");
		System.out.println("Available commands:");
		System.out.println("");
		System.out.println("info <path> .. shows information about the CDM lying at the path");
		System.out.println("");
		System.out.println("convert <[targetVersionPrefix:]targetVersion> <targetFormat> <sourcePath> [<destinationPath>] .. converts the CDM lying at the source path to the target version (optionally including a custom prefix, by default using the correct one) and format and saves it in the destination path, or overwrites the CDM on disk if no destination path is given");
		System.out.println("  supported target versions:");
		for (String ver : CdmCtrl.getKnownCdmVersions()) {
			System.out.println("    " + ver);
		}
		System.out.println("    - .. keep the current version");
		System.out.println("  supported target formats:");
		System.out.println("    xml .. human-readable CDM format based on XML");
		System.out.println("    - .. keep the current format");
		// TODO :: also add EMF, JSON, CSV, ZIP, etc.
		System.out.println("");
		System.out.println("validate <path> .. validates the CDM lying at the path");
		System.out.println("");
		System.out.println("version .. shows the version of the " + PROGRAM_TITLE);
		System.out.println("");
		System.out.println("help .. shows this help");
	}
	
	private static void showVersion() {
		System.out.println(Utils.getFullProgramIdentifierWithDate());
	}

}
