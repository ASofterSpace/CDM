#!/bin/bash

# adjust path to access CDM from anywhere
export PATH=$PATH:`dirname "$0"`/..

# add autocompletion
_autocomplete_cdm()
{
	local proposal
	local all_commands
	all_commands="compare convert create find help info interactive root tree uuid validate version"

	if [[ $COMP_CWORD -eq 1 ]]; then
		# complete the command argument
		proposal="${all_commands}"
	else
		if [[ $COMP_CWORD -eq 2 ]] && [[ ${COMP_WORDS[1]} == "help" ]]; then
			# complete the first help argument
			proposal="${all_commands}"
		else
			# for everything else, just do regular completion based on the filesystem...
			proposal=$(ls)
		fi
	fi

	local typed
	typed="${COMP_WORDS[COMP_CWORD]}"
	COMPREPLY=( $(compgen -W "${proposal}" -- ${typed}) )
	return 0;
}

complete -F _autocomplete_cdm cdm

