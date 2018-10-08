package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.CommandCtrl;
import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.cdm.CdmCtrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Convert implements Command {

	private final String HELP_CONVERT = "convert [-f <format>] [-p <versionPrefix>] [-v <version>] [-d <destinationCdmPath>] <cdmPath> .. converts the CDM";


	@Override
	public String getName() {
		return "convert";
	}

	/**
	 * Open the CDM in originPath and convert it to the specified version (possibly including a prefix separated with a colon) and format.
	 * If destination path is null, overwrite the CDM in place.
	 * If destination path is given, store the conversion result there.
	 */
	@Override
	public void execute() {

		CommandCtrl.useArgMapWithOnePath();

		if (CommandCtrl.getPathArg() == null) {
			System.err.println("You called  cdm create  but did not specify a CDM path at which the CDM should be created - please do.");
			System.exit(4);
		}

		// read out the given arguments
		String template = "-";
		String format = "-";
		String toPrefix = "-";
		String toVersion = "-";
		String destinationPath = "-";

		Map<String, String> arguments = CommandCtrl.getArgumentMap();
		
		if (arguments.containsKey("-t")) {
			template = arguments.get("-t");
		}
		
		if (arguments.containsKey("-f")) {
			format = arguments.get("-f");
		}
		
		if (arguments.containsKey("-p")) {
			toPrefix = arguments.get("-p");
		}
		
		if (arguments.containsKey("-v")) {
			toVersion = arguments.get("-v");
		}
		
		if (arguments.containsKey("-d")) {
			destinationPath = arguments.get("-d");
		}
		
		// replace defaults
		String conversionTargetStr = "";

		// first check if the target format selected actually can be done (and if not, complain immediately!)
		if (!"-".equals(format)) {
			format = format.toUpperCase();

			switch (format) {
				case "XML":
					break;
				default:
					System.err.println("Sorry, the target format " + format + " is not yet supported!");
					System.exit(5);
			}

			conversionTargetStr = format + " format";
		}

		// now attempt to load the CDM from the origin path; we here need the full model, as the conversion
		// actually in rare but existing cases needs to cross-reference things and be surprisingly smart...
		CommandCtrl.loadCdm();

		// do the conversion to a different version (and possibly prefix)
		if ("-".equals(toVersion)) {
			toVersion = null;
		}

		if ("-".equals(toPrefix)) {
			toPrefix = null;
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

		if ((toPrefix != null) || (toVersion != null)) {
			CommandCtrl.getCdmCtrl().convertTo(toVersion, toPrefix);

			if ("".equals(conversionTargetStr)) {
				conversionTargetStr = "CDM version " + CommandCtrl.getCdmCtrl().getCdmVersion();
			} else {
				conversionTargetStr += " and CDM version " + CommandCtrl.getCdmCtrl().getCdmVersion();
			}
		}
		
		CommandCtrl.saveToDestinationPath(destinationPath);

		if ("".equals(conversionTargetStr)) {
			System.out.println("No conversion done - as I was told to keep both version and format the same! :)");
		} else {
			System.out.println("Conversion to " + conversionTargetStr + " done!");
		}
	}

	@Override
	public String getShortHelp() {
		return HELP_CONVERT;
	}

	@Override
	public List<String> getLongHelp() {

		List<String> result = new ArrayList<>();

		result.add(HELP_CONVERT);
		result.add("");
		result.add("  Supported target formats for -f are:");
		result.add("    xml .. human-readable CDM format based on XML");
		result.add("    - .. default: keep the current format");
		// TODO :: also add EMF, JSON, CSV, ZIP, etc.
		result.add("");
		result.add("  The version prefix for -p is automatically selected based on the version; only select it manually if you really have to.");
		result.add("");
		result.add("  Supported target versions for -v are:");
		for (String ver : CdmCtrl.getKnownCdmVersions()) {
			result.add("    " + ver);
		}
		result.add("    - .. default: keep the current version");
		result.add("");
		result.add("  If no destination CDM path is selected using -d, then the CDM that is opened will be overwritten in-place.");

		return result;
	}
}

