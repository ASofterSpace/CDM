/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.cdm;

import com.asofterspace.cdm.commands.Create;
import com.asofterspace.cdm.commands.Convert;
import com.asofterspace.cdm.commands.Compare;
import com.asofterspace.cdm.commands.Validate;
import com.asofterspace.cdm.commands.Info;
import com.asofterspace.cdm.commands.Root;
import com.asofterspace.cdm.commands.Tree;
import com.asofterspace.cdm.commands.Find;
import com.asofterspace.cdm.commands.Print;
import com.asofterspace.cdm.commands.Uuid;
import com.asofterspace.cdm.commands.Version;
import com.asofterspace.cdm.commands.VersionForZip;
import com.asofterspace.cdm.commands.Help;
import com.asofterspace.toolbox.Utils;


public class Main {

	public final static String PROGRAM_TITLE = "cdm commandline tool";
	public final static String VERSION_NUMBER = "0.0.1.6beta(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "13. September 2018 - 12. January 2019";


	public static void main(String[] args) {

		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		// Utils.debuglog("Starting up...");

		CommandCtrl.register(new Create());
		CommandCtrl.register(new Convert());
		CommandCtrl.register(new Compare());
		CommandCtrl.register(new Validate());
		CommandCtrl.register(new Info());
		CommandCtrl.register(new Root());
		CommandCtrl.register(new Tree());
		CommandCtrl.register(new Find());
		CommandCtrl.register(new Print());
		CommandCtrl.register(new Uuid());
		CommandCtrl.register(new Version());
		CommandCtrl.register(new VersionForZip());
		CommandCtrl.register(new Help());

		// TODO :: add interactive command to have an interactive session...

		// TODO :: add list commands, e.g. list parameters, list activities, list scripts, ...

		// TODO :: add command to automagically fix problems, e.g. when there is a link to a UUID that is in a
		// different file than the link indicates, but we can fix it because the UUID is - wait for it - unique :D

		// TODO :: add command to read a particular script, that is, get the content of that script and print it to system out

		CommandCtrl.executeCommand(args);

		// Utils.debuglog("Done!");

		// all is shiny! all is good! exit code 0!
		System.exit(0);
	}

}
