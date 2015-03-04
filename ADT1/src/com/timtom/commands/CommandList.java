package com.timtom.commands;

import java.util.ArrayList;
import java.util.Scanner;

public class CommandList
{

	ArrayList<Command> commands = new ArrayList<Command>();

	public CommandList()
	{
		// TODO Auto-generated constructor stub
	}

	public void addCommand(Command c)
	{
		commands.add(c);
	}

	public void printMenu()
	{
		System.out.println("-------------------------------------");
		System.out.println("|Please make a choice from the menu |");
		for (int i = 0; i < commands.size(); i++)
		{
			System.out.println(String.format("|%2d).\t%s", i + 1, commands.get(i).getDescription()));
		}
	}

	public Object ExecuteCommand(Scanner scanner)
	{
		printMenu();
		int i = scanner.nextInt();
		if (i - 1 < commands.size())
		{
			return commands.get(i - 1).execute(scanner);
		} else
		{
			System.err.println("invalid choice");
		}

		return null;
	}
}
