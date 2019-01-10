package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.Utils;

import java.util.List;


public class VersionForZip implements Command {

	@Override
	public String getName() {
		return "version_for_zip";
	}

	@Override
	public void execute() {
		System.out.println("version " + Utils.getVersionNumber());
	}

	@Override
	public String getShortHelp() {
		return null;
	}

	@Override
	public List<String> getLongHelp() {
		return null;
	}
}

