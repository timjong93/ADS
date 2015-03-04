package com.timtom;

import java.util.Scanner;

import com.timtom.commands.CommandList;
import com.timtom.commands.ExitCommand;
import com.timtom.commands.FindRecipe;
import com.timtom.commands.InsertRecipe;

/**
 * Created by Tim on 12-2-2015.
 */
public class App
{

	private Scanner scanner;
	private CommandList commandList;

	public App()
	{
		scanner = new Scanner(System.in);
		commandList = new CommandList();
		commandList.addCommand(new FindRecipe());
		commandList.addCommand(new InsertRecipe());

		// Must be last
		commandList.addCommand(new ExitCommand());
		execute();

		DatabaseHelper.getDatabaseHelper();

	}

	public void printWelcomeText()
	{
		System.out.println("Welcome to the Recepies System.");
	}

	public void execute()
	{
		printWelcomeText();
		while (true)
		{
			Object result = commandList.ExecuteCommand(scanner);

			if (result instanceof String && ((String) result).equals("EXITLOOP"))
			{
				break;
			}
		}
	}
}
