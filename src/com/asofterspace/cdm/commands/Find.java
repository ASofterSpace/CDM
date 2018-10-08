package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.CommandCtrl;
import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.cdm.CdmCtrl;
import com.asofterspace.toolbox.cdm.CdmNode;
import com.asofterspace.toolbox.coders.ConversionException;
import com.asofterspace.toolbox.coders.UuidEncoderDecoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Find implements Command {

	private final String HELP_FIND = "find [-u <uuid>] [-n <name>] [-t <type>] [-x <xmltag>] <cdmPath> .. finds an element in the CDM";


	@Override
	public String getName() {
		return "find";
	}

	@Override
	public void execute() {

		CommandCtrl.useArgMapWithOnePath();

		if (CommandCtrl.getPathArg() == null) {
			System.err.println("You called  cdm find  but did not specify a CDM path to open - please do.");
			System.exit(4);
		}

		// TODO :: if this is just one file (e.g. toLowerCase() ends on .cdm) then actually just load that one file instead!
		CommandCtrl.loadCdm(false);

		CdmCtrl cdmCtrl = CommandCtrl.getCdmCtrl();
		
		Set<CdmNode> nodesFound = new HashSet<>();

		// TODO :: add another switch that allows searching only for elements that have this AND that instead of this OR that
		// (right now, we search e.g. for elements with this name OR that tag, but maybe someone wants to search for elements
		// that have this name AND that tag!)

		Map<String, String> arguments = CommandCtrl.getArgumentMap();

		// find by UUID
		if (arguments.containsKey("-u")) {
			String uuid = arguments.get("-u");
			try {
				uuid = UuidEncoderDecoder.ensureUUIDisEcore(uuid);
			} catch (ConversionException e) {
				System.err.println(e.getMessage());
				System.exit(11);
			}
			nodesFound.addAll(cdmCtrl.findByUuid(uuid));
		}
		
		// find by name
		if (arguments.containsKey("-n")) {
			nodesFound.addAll(cdmCtrl.findByName(arguments.get("-n")));
		}
		
		// find by type
		if (arguments.containsKey("-t")) {
			nodesFound.addAll(cdmCtrl.findByType(arguments.get("-t")));
		}
		
		// find by xml tag
		if (arguments.containsKey("-x")) {
			nodesFound.addAll(cdmCtrl.findByXmlTag(arguments.get("-x")));
		}
		
		if (nodesFound.size() == 0) {
			System.out.println("No entities have been found, sorry.");
			return;
		}
		
		if (nodesFound.size() == 1) {
			System.out.println("1 entity has been found:");
		} else {
			System.out.println(nodesFound.size() + " entities have been found:");
		}
		
		for (CdmNode node : nodesFound) {
			System.out.println("");
			node.print();
		}
	}

	@Override
	public String getShortHelp() {
		return HELP_FIND;
	}

	@Override
	public List<String> getLongHelp() {

		List<String> result = new ArrayList<>();

		result.add(HELP_FIND);
		result.add("");
		result.add("  -u UUID .. if specified, find an element by its UUID");
		result.add("  -n name .. if specified, find an element by its name");
		result.add("  -t type .. if specified, find an element by its xsi type");
		result.add("  -x xmltag .. if specified, find an element by its xml tag");

		return result;
	}
}

