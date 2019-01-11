/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.cdm;

import com.asofterspace.cdm.commands.Help;
import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.cdm.CdmCtrl;
import com.asofterspace.toolbox.cdm.CdmNode;
import com.asofterspace.toolbox.cdm.exceptions.AttemptingEmfException;
import com.asofterspace.toolbox.cdm.exceptions.CdmLoadingException;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.Utils;
import com.asofterspace.toolbox.utils.ProgressIndicator;
import com.asofterspace.toolbox.utils.NoOpProgressIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CommandCtrl {

	// the list of registered command instances
	private static List<Command> commandList = new ArrayList<>();

	// everything related to arguments
	private static String[] mainArgs;
	private static String firstarg;
	private static Map<String, String> argumentMap;
	private static List<String> argumentList;
	private static String pathArg;
	private static String otherPathArg;

	// the default controller (however, commands can also use their own)
	private static CdmCtrl cdmCtrl;


	public static void register(Command command) {
		commandList.add(command);
	}

	public static void executeCommand(String[] args) {
	
		mainArgs = args;

		// create one default CDM controller - we might create more later (e.g. when comparing two CDMs),
		// but one will be plenty for now, thank you very much ;)
		cdmCtrl = new CdmCtrl();

		// if we were called without arguments...
		if (mainArgs.length < 1) {
			// ... tell everyone that this is basically nonsense!
			Help help = new Help();
			help.execute();
			System.exit(1);
		}
		
		// get the first arg...
		firstarg = mainArgs[0];
		
		String firstargCompare = firstarg.trim().toLowerCase();
		
		while (firstargCompare.startsWith("-")) {
			firstargCompare = firstargCompare.substring(1);
		}

		Command calledCommand = getCommandByName(firstargCompare);

		if (calledCommand == null) {
			System.err.println("Sorry, I did not understand the argument '" + args[0] + "' - call  cdm help  to get a list of possible commands.");
			System.exit(2);
		}

		calledCommand.execute();

		// Utils.debuglog("Done!");

		// all is shiny! all is good! exit code 0!
		System.exit(0);
	}
	
	// use a map of arguments, e.g. -u uuid -n name ..., together with at most one path in the end
	public static void useArgMapWithOnePath() {
		
		// ... get a map of all modifiers ...
		argumentMap = new HashMap<String, String>();
		
		// we start at 1 (as 0 is already the firstarg), and go up to < mainArgs.length - 1,
		// as we want to be strictly less than mainArgs.length, but one less because that is
		// already the lastarg, if there is one...
		for (int i = 1; i < mainArgs.length - 1; i++) {
			if (mainArgs[i].startsWith("-")) {
				argumentMap.put(mainArgs[i].toLowerCase(), mainArgs[i+1]);
				i++;
			} else {
				System.err.println("The argument '" + mainArgs[i] + "' was not understood - please check  cdm help " + firstarg);
				System.exit(4);
			}
		}
		
		// ... get the last argument
		if (mainArgs.length > 1) {
			// in the case of  cdm uuid -k ecore  we have no last arg, as we have an odd amount of arguments!
			if (mainArgs.length % 2 == 0) {
				pathArg = mainArgs[mainArgs.length - 1];
			}
		}
	}
	
	// use a list of arguments, e.g. -u -n ..., together with at most one path in the end
	public static void useArgListWithOnePath() {
		
		// ... get a list of all modifiers ...
		argumentList = new ArrayList<String>();
		
		// we start at 1 (as 0 is already the firstarg), and go up to < mainArgs.length - 1,
		// as we want to be strictly less than mainArgs.length, but one less because that is
		// already the lastarg, if there is one
		for (int i = 1; i < mainArgs.length - 1; i++) {
			if (mainArgs[i].startsWith("-")) {
				argumentList.add(mainArgs[i].toLowerCase());
				i++;
			} else {
				System.err.println("The argument '" + mainArgs[i] + "' was not understood - please check  cdm help " + firstarg);
				System.exit(4);
			}
		}
		
		// ... get the last argument
		if (mainArgs.length > 1) {
			pathArg = mainArgs[mainArgs.length - 1];
		}
	}

	// use a list of arguments, e.g. -u -n ..., together with at most two paths in the end
	public static void useArgListWithTwoPaths() {
		
		// ... get a list of all modifiers ...
		argumentList = new ArrayList<String>();
		
		// we start at 1 (as 0 is already the firstarg), and go up to < mainArgs.length - 2,
		// as we want to be strictly less than mainArgs.length, but two less because those are
		// already the lastargs, if they are there
		for (int i = 1; i < mainArgs.length - 2; i++) {
			if (mainArgs[i].startsWith("-")) {
				argumentList.add(mainArgs[i].toLowerCase());
				i++;
			} else {
				System.err.println("The argument '" + mainArgs[i] + "' was not understood - please check  cdm help " + firstarg);
				System.exit(4);
			}
		}
		
		// ... get the last arguments
		if (mainArgs.length > 1) {
			pathArg = mainArgs[mainArgs.length - 1];
		}
		if (mainArgs.length > 2) {
			otherPathArg = mainArgs[mainArgs.length - 2];
		}
	}

	public static void loadCdm() {
		loadCdm(true);
	}

	public static void loadCdm(boolean loadFullModel) {
		loadCdm(pathArg, loadFullModel, cdmCtrl, true);
	}

	public static void loadCdm(String cdmPath, boolean loadFullModel, CdmCtrl cdmCtrlToLoadInto, boolean exitOnProblem) {

		Directory cdmDir = new Directory(cdmPath);
		ProgressIndicator noProgress = new NoOpProgressIndicator();

		try {
			if (loadFullModel) {
				cdmCtrlToLoadInto.loadCdmDirectory(cdmDir, noProgress);
			} else {
				cdmCtrlToLoadInto.loadCdmDirectoryFaster(cdmDir, noProgress);
			}
		} catch (AttemptingEmfException | CdmLoadingException e) {
			System.err.println(e.getMessage());
			if (exitOnProblem) {
				System.exit(3);
			}
		}
	}

	public static void saveToDestinationPath(String destinationPath) {
	
		// now actually save the result
		if ((destinationPath == null) || ("-".equals(destinationPath))) {

			// overwrite the original with the new result
			// TODO :: do not ignore the target format once we have more than XML available!
			cdmCtrl.save();

		} else {

			Directory destDir = new Directory(destinationPath);

			// complain if the directory is not empty, unless the source and destination are the same
			if (!pathArg.equals(destinationPath)) {
				Boolean isEmpty = destDir.isEmpty();
				if ((isEmpty == null) || !isEmpty) {
					System.err.println("The specified destination directory is not empty - please save the conversion result into an empty directory!");
					System.exit(9);
				}
			}

			// save the result to the new destination path
			// TODO :: do not ignore the target format once we have more than XML available!
			cdmCtrl.saveTo(destDir);
		}
	}

	public static CdmCtrl getCdmCtrl() {
		return cdmCtrl;
	}

	public static Map<String, String> getArgumentMap() {
		return argumentMap;
	}

	public static List<String> getArgumentList() {
		return argumentList;
	}

	public static String getPathArg() {
		return pathArg;
	}

	public static String getOtherPathArg() {
		return otherPathArg;
	}

	public static List<Command> getRegisteredCommands() {
		return commandList;
	}

	public static Command getCommandByName(String name) {

		name = name.toLowerCase();

		for (Command command : commandList) {
			if (command.getName().toLowerCase().equals(name)) {
				return command;
			}
		}

		return null;
	}

}
