package com.timtom.commands;

import java.util.Scanner;

public class ExitCommand extends Command
{

	public ExitCommand()
	{
		super("Do Nothing, Quit");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(Scanner scanner)
	{
		return "EXITLOOP";
	}

}
