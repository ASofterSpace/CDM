package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.Utils;

import java.util.List;


public class Version implements Command {

	@Override
	public String getName() {
		return "version";
	}

	@Override
	public void execute() {
		System.out.println(Utils.getFullProgramIdentifierWithDate());
	}

	@Override
	public String getShortHelp() {
		return "version .. shows the version of the " + Utils.getProgramTitle();
	}

	@Override
	public List<String> getLongHelp() {
		return null;
	}
}

