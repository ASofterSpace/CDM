/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.CommandCtrl;
import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.cdm.CdmMonitoringControlElement;
import com.asofterspace.toolbox.cdm.CdmNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Root implements Command {

	private final String HELP_ROOT = "root [-n <name>] [-d <destinationCdmPath>] <cdmPath> .. shows the root of the MCM tree";


	@Override
	public String getName() {
		return "root";
	}

	@Override
	public void execute() {

		CommandCtrl.useArgMapWithOnePath();

		if (CommandCtrl.getPathArg() == null) {
			System.err.println("You called  cdm root  but did not specify a CDM path of the CDM for which the root should be accessed - please do.");
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
		
		String setName = "-";
		String destinationPath = "-";

		Map<String, String> arguments = CommandCtrl.getArgumentMap();
		
		if (arguments.containsKey("-n")) {
			setName = arguments.get("-n");
		}
		
		if (arguments.containsKey("-d")) {
			destinationPath = arguments.get("-d");
		}
		
		if ("-".equals(setName)) {
		
			if (!"-".equals(destinationPath)) {
				// if they want us to save even though nothing changed... oookay xD
				CommandCtrl.saveToDestinationPath(destinationPath);
			}
		
		} else {
		
			for (CdmNode node : roots) {
				node.setName(setName);
			}
			
			CommandCtrl.saveToDestinationPath(destinationPath);

			// in this case, do not print out the result!
			System.out.println("Changed the root name to " + setName + "!");
			return;
		}
		
		if (roots.size() == 1) {
			roots.iterator().next().print();
			return;
		}
		
		System.out.println(roots.size() + " different root nodes have been found in this CDM!");
		System.out.println("Here they are:");
		
		for (CdmNode node : roots) {
			System.out.println("");
			node.print();
		}
	}

	@Override
	public String getShortHelp() {
		return HELP_ROOT;
	}

	@Override
	public List<String> getLongHelp() {

		List<String> result = new ArrayList<>();

		result.add(HELP_ROOT);
		result.add("  If a name is selected with -n, the root element will be renamed to this name.");
		result.add("  If no destination CDM path is selected using -d, then the CDM that is opened will be overwritten in-place in case the name is changed.");

		return result;
	}
}
