package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.CommandCtrl;
import com.asofterspace.cdm.interfaces.Command;

import java.util.ArrayList;
import java.util.List;


public class Print implements Command {

	private final String HELP_PRINT = "print <cdmPath> .. prints the contents of the CDM as XML";


	@Override
	public String getName() {
		return "print";
	}

	@Override
	public void execute() {

		CommandCtrl.useArgMapWithOnePath();

		if (CommandCtrl.getPathArg() == null) {
			System.err.println("You called  cdm print  but did not specify a CDM path to open - please do.");
			System.exit(4);
		}

		// TODO :: if this is just one file (e.g. toLowerCase() ends on .cdm) then actually just load that one file instead!
		CommandCtrl.loadCdm(CommandCtrl.getPathArg(), false, CommandCtrl.getCdmCtrl(), false);

		CommandCtrl.getCdmCtrl().debugPrintAll();
	}

	@Override
	public String getShortHelp() {
		return HELP_PRINT;
	}

	@Override
	public List<String> getLongHelp() {

		List<String> result = new ArrayList<>();

		result.add(HELP_PRINT);
		result.add("This is mostly used for debug purposes, to quickly see whether a (small!) EMF binary CDM was understood and correctly parsed or not.");
		result.add("Think of this as a shorthand for converting the CDM to its XML representation and printing the resulting file contents to the console.");

		return result;
	}
}

