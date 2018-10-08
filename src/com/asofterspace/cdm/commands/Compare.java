package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.CommandCtrl;
import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.cdm.CdmCtrl;

import java.util.List;


public class Compare implements Command {

	private final String HELP_COMPARE = "compare <leftCdmPath> <rightCdmPath> .. compares the CDMs";


	@Override
	public String getName() {
		return "compare";
	}

	@Override
	public void execute() {

		CommandCtrl.useArgListWithTwoPaths();

		if (CommandCtrl.getPathArg() == null) {
			System.err.println("You called  cdm compare  but did not specify any CDM path of the CDMs that should be validated - please do.");
			System.exit(4);
		}
		
		if (CommandCtrl.getOtherPathArg() == null) {
			System.err.println("You called  cdm compare  but did not specify a second CDM path of the second CDM that should be compared to the first one - please do.");
			System.exit(4);
		}

		CdmCtrl cdmCtrl = new CdmCtrl();
		
		// TODO :: if this is just one file (e.g. toLowerCase() ends on .cdm) then actually just load that one file instead!
		CommandCtrl.loadCdm(CommandCtrl.getPathArg(), false, cdmCtrl, true);
		
		CdmCtrl otherCdmCtrl = new CdmCtrl();
		
		// TODO :: if this is just one file (e.g. toLowerCase() ends on .cdm) then actually just load that one file instead!
		CommandCtrl.loadCdm(CommandCtrl.getOtherPathArg(), false, otherCdmCtrl, true);
		
		List<String> differences = cdmCtrl.findDifferencesFrom(otherCdmCtrl);
		
		if (differences.size() < 1) {
			System.out.println("No differences have been found between the two CDMs!");
			return;
		}

		System.out.println("The following differences have been found:");
		for (String difference : differences) {
			System.out.println(difference);
		}
	}

	@Override
	public String getShortHelp() {
		return HELP_COMPARE;
	}

	@Override
	public List<String> getLongHelp() {
		return null;
	}
}

