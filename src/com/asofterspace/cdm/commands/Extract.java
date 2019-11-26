/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.CommandCtrl;
import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.cdm.exceptions.CdmSavingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Extract implements Command {

	private final String HELP_PRINT = "extract [-f <filename>] [-d <destinationCdmPath>] <cdmPath> .. extracts a CDM file from the set of CDM files and puts it into a new destination along with all CDM files it depends on";


	@Override
	public String getName() {
		return "extract";
	}

	@Override
	public void execute() {

		CommandCtrl.useArgMapWithOnePath();

		if (CommandCtrl.getPathArg() == null) {
			System.err.println("You called  cdm extract  but did not specify a CDM path to open - please do.");
			System.exit(4);
		}

		String fromFile = "-";
		String toDestinationPath = "-";

		Map<String, String> arguments = CommandCtrl.getArgumentMap();

		if (arguments.containsKey("-f")) {
			fromFile = arguments.get("-f");
		} else {
			System.err.println("You called  cdm extract  but did not specify a filename to extract out of the CDM - please do.");
			System.exit(4);
		}

		if (arguments.containsKey("-d")) {
			toDestinationPath = arguments.get("-d");
		}

		// TODO :: if this is just one file (e.g. toLowerCase() ends on .cdm) then actually just load that one file instead!
		CommandCtrl.loadCdm(CommandCtrl.getPathArg(), false, CommandCtrl.getCdmCtrl(), false);

		try {
			// now actually perform the extraction!
			CommandCtrl.getCdmCtrl().extractFileTo(fromFile, toDestinationPath);
		} catch (CdmSavingException e) {
			System.err.println(e.getMessage());
			System.exit(8);
		}
	}

	@Override
	public String getShortHelp() {
		return HELP_PRINT;
	}

	@Override
	public List<String> getLongHelp() {

		List<String> result = new ArrayList<>();

		result.add(HELP_PRINT);
		result.add("This can be used to separate the control system part from the rest of the tailoring.");

		return result;
	}
}
