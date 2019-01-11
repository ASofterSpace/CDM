/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.cdm.commands;

import com.asofterspace.cdm.CommandCtrl;
import com.asofterspace.cdm.interfaces.Command;
import com.asofterspace.toolbox.Utils;

import java.util.List;


public class Help implements Command {

	private final String HELP_HELP = "help [<command>] .. shows the help, optionally detailed help for a specific command";


	@Override
	public String getName() {
		return "help";
	}

	@Override
	public void execute() {

		CommandCtrl.useArgMapWithOnePath();

		if (CommandCtrl.getPathArg() == null) {

			System.out.println("Welcome to the " + Utils.getFullProgramIdentifier() + "! :)");
			System.out.println("");
			System.out.println("Available commands:");
			System.out.println("");

			List<Command> commands = CommandCtrl.getRegisteredCommands();

			for (Command command : commands) {
				// ignore commands that do not want to be shown in the shortlist
				if (command.getShortHelp() != null) {
					System.out.println("* " + command.getShortHelp());
				}
			}

		} else {

			Command command = CommandCtrl.getCommandByName(CommandCtrl.getPathArg().toLowerCase());

			if (command == null) {
				System.err.println("Whoopsie! I do not actually know the command '" + CommandCtrl.getPathArg() +
						"', so I cannot offer any help with it...");
				System.exit(20);
			}

			List<String> helpStrs = command.getLongHelp();

			if (helpStrs == null) {

				if (command.getShortHelp() == null) {
					System.out.println("For this command, no further help is available.");
				} else {
					System.out.println(command.getShortHelp());
				}

			} else {

				for (String helpStr : helpStrs) {
					System.out.println(helpStr);
				}
			}
		}
	}

	@Override
	public String getShortHelp() {
		return HELP_HELP;
	}

	@Override
	public List<String> getLongHelp() {
		return null;
	}
}
