package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.CommandCtrl;
import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.cdm.CdmCtrl;
import com.asofterspace.toolbox.cdm.exceptions.AttemptingEmfException;
import com.asofterspace.toolbox.cdm.exceptions.CdmLoadingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Create implements Command {

	private final String HELP_CREATE = "create [-t <template>] [-f <format>] [-p <versionPrefix>] [-v <version>] <cdmPath> .. creates a new CDM";

	@Override
	public String getName() {
		return "create";
	}

	@Override
	public void execute() {

		CommandCtrl.useArgMapWithOnePath();

		if (CommandCtrl.getPathArg() == null) {
			System.err.println("You called  cdm create  but did not specify a CDM path at which the CDM should be created - please do.");
			System.exit(4);
		}

		// read out the given arguments
		String template = "-";
		String format = "-";
		String prefix = "-";
		String version = "-";

		Map<String, String> arguments = CommandCtrl.getArgumentMap();
		
		if (arguments.containsKey("-t")) {
			template = arguments.get("-t");
		}
		
		if (arguments.containsKey("-f")) {
			format = arguments.get("-f");
		}
		
		if (arguments.containsKey("-p")) {
			prefix = arguments.get("-p");
		}
		
		if (arguments.containsKey("-v")) {
			version = arguments.get("-v");
		}
		
		// replace defaults
		if ("-".equals(template)) {
			template = CdmCtrl.getTemplates().get(0);
		}

		if ("-".equals(format)) {
			format = "xml";
		}

		if ("-".equals(version)) {
			version = CdmCtrl.getHighestKnownCdmVersion();
		}

		// if no prefix is specified, take the correct one automagically!
		if ("-".equals(prefix)) {
			prefix = CdmCtrl.getPrefixForVersion(version);
			if (prefix == null) {
				System.err.println("I do not know which prefix is associated with CDM version " + version + ".");
				System.err.println("Please explicitly specify a prefix, e.g. call  cdm create (...) prefix:" + version + " (...)");
				System.exit(7);
			}
		}

		// actually perform the work
		try {
			// TODO :: do not ignore the format ;)
			CdmCtrl cdmCtrl = new CdmCtrl();
			cdmCtrl.createNewCdm(CommandCtrl.getPathArg(), version, prefix, template);
			
		} catch (AttemptingEmfException | CdmLoadingException e) {
			System.err.println(e.getMessage());
			System.exit(8);
		}
		
		System.out.println("The new CDM has been created!");
	}

	@Override
	public String getShortHelp() {
		return HELP_CREATE;
	}

	@Override
	public List<String> getLongHelp() {

		List<String> result = new ArrayList<>();

		result.add(HELP_CREATE);
		result.add("");

		result.add("  Available templates for -t are:");
		List<String> templates = CdmCtrl.getTemplates();
		List<String> templatesShort = CdmCtrl.getTemplatesShort();
		for (int i = 0; i < templates.size(); i++) {
			result.add("    " + templatesShort.get(i) + " .. " + templates.get(i));
		}
		result.add("    - .. default template: " + templatesShort.get(0));
		result.add("");

		result.add("  Supported target formats for -f are:");
		result.add("    xml .. human-readable CDM format based on XML");
		result.add("    - .. default: xml");
		// TODO :: also add EMF, JSON, CSV, ZIP, etc.
		result.add("");

		result.add("  The version prefix for -p is automatically selected based on the version; only select it manually if you really have to.");
		result.add("");

		result.add("  Supported versions for -v are:");
		for (String ver : CdmCtrl.getKnownCdmVersions()) {
			result.add("    " + ver);
		}
		result.add("    - .. default: highest available version (" + CdmCtrl.getHighestKnownCdmVersion() + ")");

		return result;
	}
}

