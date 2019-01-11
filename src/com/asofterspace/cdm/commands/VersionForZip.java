/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.Utils;

import java.util.ArrayList;
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
	
		List<String> result = new ArrayList<>();
		
		result.add("version_for_zip .. shows just the version identifier of the " + Utils.getProgramTitle());
		result.add("");
		result.add("This is an internal command used to get the current version when releasing a zip file such that the zip file can be adequately versioned.");
		result.add("For everyday purposes, you should use  cdm version  instead of  cdm version_for_zip");
		
		return result;
	}
}
