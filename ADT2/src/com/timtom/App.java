package com.timtom;

import java.util.Scanner;

import com.timtom.commands.*;

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
		commandList.addCommand(new ShowAllRecipes());
        commandList.addCommand(new FindRecipeByIngredientsOR());
        commandList.addCommand(new FindRecipeByIngredientsAnd());
        commandList.addCommand(new FindRecipeByIngredientOrAndSort());
		commandList.addCommand(new InsertRecipe());
		commandList.addCommand(new InsertUser());
		commandList.addCommand(new AddComment());
		commandList.addCommand(new LikeComment());
		commandList.addCommand(new DislikeComment());
		commandList.addCommand(new RateRecipe());
		commandList.addCommand(new ShowTop5());

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
