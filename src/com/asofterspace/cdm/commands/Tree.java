/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.CommandCtrl;
import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.cdm.CdmMonitoringControlElement;
import com.asofterspace.toolbox.coders.UuidEncoderDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Tree implements Command {

	// TODO :: optionally specify to show only MCEs (like now), or also parameters, also activities, also events, ...
	private final String HELP_TREE = "tree [-u] <cdmPath> .. shows the MCM tree";


	@Override
	public String getName() {
		return "tree";
	}

	@Override
	public void execute() {

		CommandCtrl.useArgListWithOnePath();

		if (CommandCtrl.getPathArg() == null) {
			System.err.println("You called  cdm tree  but did not specify a CDM path of the CDM for which the tree should be accessed - please do.");
			System.exit(4);
		}

		// TODO :: if this is just one file (e.g. toLowerCase() ends on .cdm) then actually just load that one file instead!
		// (however, not sure if the tree can be constructed from just one file... ah well, it will work out somehow ^^)
		CommandCtrl.loadCdm();
		
		Set<CdmMonitoringControlElement> roots = CommandCtrl.getCdmCtrl().getAllMcmTreeRoots();
		
		if (roots.size() < 1) {
			System.err.println("The CDM that you specified does not seem to contain an MCM tree!");
			System.exit(12);
		}
		
		boolean isFirst = true;

		List<String> argumentList = CommandCtrl.getArgumentList();

		for (CdmMonitoringControlElement root : roots) {

			if (!isFirst) {
				System.out.println("");
			}

			recursivelyShowTree(root, "", argumentList.contains("-u"));

			isFirst = false;
		}
	}

	private static void recursivelyShowTree(CdmMonitoringControlElement mce, String prefix, boolean showUuid) {
		
		String curline = prefix + mce.getName();
		
		if (showUuid) {
			curline += " [" + UuidEncoderDecoder.convertEcoreUUIDtoJava(mce.getId()) + "]";
		}
		
		System.out.println(curline);
		
		for (CdmMonitoringControlElement child : mce.getSubElements()) {
			recursivelyShowTree(child, "  " + prefix, showUuid);
		}
	}

	@Override
	public String getShortHelp() {
		return HELP_TREE;
	}

	@Override
	public List<String> getLongHelp() {

		List<String> result = new ArrayList<>();

		result.add(HELP_TREE);
		result.add("  Optional modifiers:");
		result.add("    -u .. also show the UUID of each element in the tree");
		// TODO :: -a to show activities, -p to show parameters, -e to show events, -* to show all (but check if that is a problem in bash!)

		return result;
	}
}
