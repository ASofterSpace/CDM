package com.asofterspace.cdm.interfaces;

import java.util.List;


/**
 * This represents a single command in the cdm commandline tool.
 */
public interface Command {

	/**
	 * Get the name of this command.
	 */
	String getName();

	/**
	 * This is called when the command is actually being executed.
	 */
	void execute();

	/**
	 * Get the short help text for this command (one line, to be shown together with others.)
	 */
	String getShortHelp();

	/**
	 * Get the long help text for this command (allll the details - and all the lines - that you want.)
	 * Return null here if there is no long help text - in that case, the short help text will be used instead.
	 */
	List<String> getLongHelp();
}

