package com.timtom.commands;

import java.util.Scanner;

/**
 * Created by Tim on 12-2-2015.
 */
public class Command
{

	private String Description;

	public Command(String description)
	{
		Description = description;
	}

	public String getDescription()
	{
		return Description;
	}

	public Object execute(Scanner scanner)
	{
		System.err.println("Not Yet Implemented");
		return null;
	}
}
