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
	public final static String VERSION_NUMBER = "0.0.0.5(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "13. September 2018 - 18. September 2018";

	public static void main(String[] args) {

		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		// if we were called without arguments...
		if (args.length < 1) {
			// ... tell everyone that this is basically nonsense!
			showHelp(null);
			System.exit(1);
		}

		// check all the arguments
		String lowarg = args[0].toLowerCase();
		switch (lowarg) {
			case "create":
				if (args.length > 3) {
					createCdm(args[1], args[2], args[3]);
				} else {
					System.err.println("You called  cdm create  which should be followed by a template, a version and a path, but did not specify these arguments.");
					System.exit(4);
				}
				break;
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
				} else if (args.length > 3) {
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
			case "uuid":
				if (args.length > 3) {
					uuid(args[1], args[2], args[3]);
				} else if (args.length > 2) {
					uuid(args[1], args[2], null);
				} else if (args.length > 1) {
					uuid(args[1], null, null);
				} else {
					System.err.println("You called  cdm uuid  which should be followed by an instruction on what to do with the UUID - e.g. generate a UUID, convert a UUID to a different format, etc. - but you did not supply that instruction.");
					System.exit(4);
				}
				break;
			case "help":
				if (args.length > 1) {
					showHelp(args[1]);
				} else {
					showHelp(null);
				}
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

	private static void createCdm(String template, String targetVersionAndPrefix, String path) {

		if ("-".equals(template)) {
			template = CdmCtrl.getTemplates().get(0);
		}

		String[] verPrefArgs = targetVersionAndPrefix.split(":");
		String version;
		String prefix;

		if (verPrefArgs.length > 1) {
			prefix = verPrefArgs[0];
			version = verPrefArgs[1];
		} else {
			prefix = "-";
			version = verPrefArgs[0];
		}

		if ("-".equals(version)) {
			version = CdmCtrl.getHighestKnownCdmVersion();
		}

		// if no prefix is specified, take the correct one automagically!
		if ("-".equals(prefix)) {
			prefix = CdmCtrl.getPrefixForVersion(version);
			if (prefix == null) {
				System.err.println("I do not know which prefix is associated with CDM version " + version + ".");
				System.err.println("Please explicitly specify a prefix, e.g. call  cdm create (...) prefix:" + version + " (...)");
				System.exit(7);
			}
		}

		try {
			CdmCtrl.createNewCdm(path, version, prefix, template);

		} catch (AttemptingEmfException | CdmLoadingException e) {
			System.err.println(e.getMessage());
			System.exit(8);
		}
		
		System.out.println("The new CDM has been created!");
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
					System.err.println("Please explicitly specify a prefix (even '-' would be enough to keep the current one), e.g. call  cdm convert -:" + toVersion + " (...)");
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

			Directory destDir = new Directory(destinationPath);

			// complain if the directory is not empty, unless the source and destination are the same
			if (!originPath.equals(destinationPath)) {
				Boolean isEmpty = destDir.isEmpty();
				if ((isEmpty == null) || !isEmpty) {
					System.err.println("The specified destination directory is not empty - please save the conversion result into an empty directory!");
					System.exit(9);
				}
			}

			// save the result to the new destination path
			// TODO :: do not ignore the target format once we have more than XML available!
			CdmCtrl.saveTo(destDir);
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

	private static void uuid(String action, String argument1, String argument2) {

		switch (action.toLowerCase()) {

			case "generate":
				if ((argument1 == null) || "-".equals(argument1)) {
					argument1 = "default";
				}

				switch (argument1.toLowerCase()) {
					case "java":
					case "default":
						System.out.println(Utils.generateJavaUUID());
						return;
					case "ecore":
					case "emf":
						System.out.println(Utils.generateEcoreUUID());
						return;
					default:
						System.err.println("A UUID in the format '" + argument1 +
							"' cannot be created, as the format is not known... sorry!");
						System.exit(10);
				}
				break;

			case "convert":
				if ((argument1 == null) || "-".equals(argument1)) {
					argument1 = "default";
				}

				if (argument2 == null) {
					System.err.println("You called  cdm uuid convert " + argument1 +
						"  which should usually be followed by the UUID to convert, " +
						"but you left it out, so there is nothing to convert.");
					System.exit(4);
				}

				switch (argument1.toLowerCase()) {
					case "java":
					case "default":
						System.out.println(Utils.convertEcoreUUIDtoJava(argument2));
						return;
					case "ecore":
					case "emf":
						System.out.println(Utils.convertJavaUUIDtoEcore(argument2));
						return;
					default:
						System.err.println(argument2 + " cannot be converted to the format '" + argument2 +
							"', as the format is not known... sorry!");
						System.exit(10);
				}

				break;

			default:
				System.err.println("You called  cdm uuid " + action +
						"  but this action cannot be understood.");
				System.exit(11);
		}
	}

	private static void showHelp(String command) {

		final String HELP_CREATE = "create <template> <[targetVersionPrefix:]targetVersion> <path> .. creates a new CDM of the specified version at the specified path, using one of the available templates for it";
		final String HELP_INFO = "info <path> .. shows information about the CDM lying at the path";
		final String HELP_CONVERT = "convert <[targetVersionPrefix:]targetVersion> <targetFormat> <sourcePath> [<destinationPath>] .. converts the CDM lying at the source path to the target version (optionally including a custom prefix, by default using the correct one) and format and saves it in the destination path, or overwrites the CDM on disk if no destination path is given";
		final String HELP_VALIDATE = "validate <path> .. validates the CDM lying at the path";
		final String HELP_UUID = "uuid <action> <type> [<argument>] .. performs a UUID action";
		final String HELP_VERSION = "version .. shows the version of the " + PROGRAM_TITLE;
		final String HELP_HELP = "help [<command>] .. shows the help, optionally detailed help for a specific command";

		if (command == null) {
			System.out.println("Welcome to the " + Utils.getFullProgramIdentifier() + "! :)");
			System.out.println("");
			System.out.println("Available commands:");
			System.out.println("");
			System.out.println("* " + HELP_CREATE);
			System.out.println("* " + HELP_INFO);
			System.out.println("* " + HELP_CONVERT);
			System.out.println("* " + HELP_VALIDATE);
			System.out.println("* " + HELP_UUID);
			System.out.println("* " + HELP_VERSION);
			System.out.println("* " + HELP_HELP);
			// TODO :: add list commands, e.g. list parameters, list activities, list scripts, ...
			// TODO :: add command to show the MCM tree
			// TODO :: add command to read a particular script, that is, get the content of that script and print it to system out
			// TODO :: add command to rename the root node (e.g. to mcmRoot for simpler starting with default config)
		} else {

			switch (command.toLowerCase()) {

				case "create":
					System.out.println(HELP_CREATE);
					System.out.println("");
					System.out.println("  available templates:");
					List<String> templates = CdmCtrl.getTemplates();
					List<String> templatesShort = CdmCtrl.getTemplatesShort();
					for (int i = 0; i < templates.size(); i++) {
						System.out.println("    " + templatesShort.get(i) + " .. " + templates.get(i));
					}
					System.out.println("    - .. default template (" + templates.get(0) + ")");
					System.out.println("");
					System.out.println("  supported versions:");
					for (String ver : CdmCtrl.getKnownCdmVersions()) {
						System.out.println("    " + ver);
					}
					System.out.println("    - .. highest available version (" + CdmCtrl.getHighestKnownCdmVersion() + ")");
					break;

				case "info":
					System.out.println(HELP_INFO);
					break;

				case "convert":
					System.out.println(HELP_CONVERT);
					System.out.println("");
					System.out.println("  supported target versions:");
					for (String ver : CdmCtrl.getKnownCdmVersions()) {
						System.out.println("    " + ver);
					}
					System.out.println("    - .. keep the current version");
					System.out.println("");
					System.out.println("  supported target formats:");
					System.out.println("    xml .. human-readable CDM format based on XML");
					System.out.println("    - .. keep the current format");
					// TODO :: also add EMF, JSON, CSV, ZIP, etc.
					break;

				case "validate":
					System.out.println(HELP_VALIDATE);
					break;

				case "uuid":
					System.out.println(HELP_UUID);
					System.out.println("");
					System.out.println("  available actions:");
					System.out.println("    generate [<format>] .. generate a Java or EMF UUID, depending on the chosen format");
					System.out.println("    convert <newFormat> <uuid> .. converts a Java UUID to EMF or the other way around, depending on the chosen format");
					break;

				case "version":
					System.out.println(HELP_VERSION);
					break;

				case "help":
					System.out.println(HELP_HELP);
					break;

				default:
					System.err.println("Whoopsie! I do not actually know the command '" + command +
						"', so I cannot offer any help with it...");
			}
		}
	}

	private static void showVersion() {
		System.out.println(Utils.getFullProgramIdentifierWithDate());
	}

}
