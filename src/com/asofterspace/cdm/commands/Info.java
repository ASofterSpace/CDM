package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.CommandCtrl;
import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.cdm.CdmCtrl;

import java.util.List;


public class Info implements Command {

	private final String HELP_INFO = "info <cdmPath> .. shows information about the CDM";


	@Override
	public String getName() {
		return "info";
	}

	@Override
	public void execute() {

		CommandCtrl.useArgMapWithOnePath();

		if (CommandCtrl.getPathArg() == null) {
			System.err.println("You called  cdm info  but did not specify a CDM path of the CDM for which information should be shown - please do.");
			System.exit(4);
		}

		// TODO :: if this is just one file (e.g. toLowerCase() ends on .cdm) then actually just load that one file instead!
		CommandCtrl.loadCdm(false);

		String cdmVersion = CommandCtrl.getCdmCtrl().getCdmVersion();
		String cdmPrefix = CommandCtrl.getCdmCtrl().getCdmVersionPrefix();
		System.out.println("CDM version: " + cdmVersion);
		System.out.println("CDM version prefix: " + cdmPrefix);
		System.out.println("CDM compatible with EGS-CC release: " + CdmCtrl.getCompatWithEGSCCstr(cdmVersion, cdmPrefix));
		System.out.println("CDM compatible with RTF Framework CDM editor version: " + CdmCtrl.getCompatWithMCDEstr(cdmVersion, cdmPrefix));
	}

	@Override
	public String getShortHelp() {
		return HELP_INFO;
	}

	@Override
	public List<String> getLongHelp() {
		return null;
	}
}

