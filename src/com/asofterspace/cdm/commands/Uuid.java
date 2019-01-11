/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.CommandCtrl;
import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.coders.ConversionException;
import com.asofterspace.toolbox.coders.UuidEncoderDecoder;
import com.asofterspace.toolbox.coders.UuidEncoderDecoder.UuidKind;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Uuid implements Command {

	private final String HELP_UUID = "uuid [-k <kind>] [<uuid>] .. generates or converts a UUID";


	@Override
	public String getName() {
		return "uuid";
	}

	@Override
	public void execute() {

		CommandCtrl.useArgMapWithOnePath();

		String kind = "-";

		Map<String, String> arguments = CommandCtrl.getArgumentMap();
		
		if (arguments.containsKey("-k")) {
			kind = arguments.get("-k");
		}

		String pathArg = CommandCtrl.getPathArg();
		
		// if no argument is given...
		if (pathArg == null) {
			// ... generate a UUID
			
			switch (kind.toLowerCase()) {
				case "java":
				case "-":
					System.out.println(UuidEncoderDecoder.generateJavaUUID());
					return;
				case "ecore":
				case "emf":
					System.out.println(UuidEncoderDecoder.generateEcoreUUID());
					return;
				default:
					System.err.println("A UUID in the format '" + kind +
						"' cannot be created, as the format is not known... sorry!");
					System.exit(10);
			}
		}
		
		// on the other hand, if an argument is given... convert!
	
		UuidKind currentKind = UuidEncoderDecoder.detectUUIDkind(pathArg);
	
		switch (kind.toLowerCase()) {
			case "java":
			case "-":
				try {
					System.out.println(UuidEncoderDecoder.ensureUUIDisJava(pathArg));
				} catch (ConversionException e) {
					System.err.println(e.getMessage());
					System.exit(11);
				}
				break;
			case "ecore":
			case "emf":
				try {
					System.out.println(UuidEncoderDecoder.ensureUUIDisEcore(pathArg));
				} catch (ConversionException e) {
					System.err.println(e.getMessage());
					System.exit(11);
				}
				break;
			default:
				System.err.println("Your UUID cannot be converted into the format '" + kind +
					"', as the format is not known... sorry!");
				System.exit(10);
		}
	}

	@Override
	public String getShortHelp() {
		return HELP_UUID;
	}

	@Override
	public List<String> getLongHelp() {

		List<String> result = new ArrayList<>();

		result.add(HELP_UUID);
		result.add("");
		result.add("  Available UUID kinds for -k are:");
		result.add("    java .. default: generate a Java UUID");
		result.add("    ecore .. generate an EMF / Ecore UUID as used inside CDM files");
		result.add("    emf .. same as ecore ");
		result.add("");
		result.add("If no <uuid> argument is given, a new random UUID is generated.");
		result.add("If a <uuid> arguments is given, the given UUID is converted to a UUID of the specified kind.");

		return result;
	}
}
