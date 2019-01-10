package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.CommandCtrl;
import com.asofterspace.cdm.interfaces.Command;

import java.util.ArrayList;
import java.util.List;


public class Validate implements Command {

	private final String HELP_VALIDATE = "validate <cdmPath> .. validates the CDM";


	@Override
	public String getName() {
		return "validate";
	}

	@Override
	public void execute() {

		CommandCtrl.useArgMapWithOnePath();

		if (CommandCtrl.getPathArg() == null) {
			System.err.println("You called  cdm validate  but did not specify a CDM path of the CDM that should be validated - please do.");
			System.exit(4);
		}

		// TODO :: if this is just one file (e.g. toLowerCase() ends on .cdm) then actually just load that one file instead!
		CommandCtrl.loadCdm();

		List<String> problems = new ArrayList<>();

		int problemAmount = CommandCtrl.getCdmCtrl().checkValidity(problems);

		if (problemAmount > 0) {
			System.err.println("The CDM does not seem to be valid.");
			System.err.println("");
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

	@Override
	public String getShortHelp() {
		return HELP_VALIDATE;
	}

	@Override
	public List<String> getLongHelp() {
		return null;
	}
}
