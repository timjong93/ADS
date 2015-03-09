package com.timtom.commands;

import java.util.Scanner;

import com.timtom.DatabaseHelper;

public class RateRecipe extends Command
{

	public RateRecipe()
	{
		super("Rate recipe");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("Recipe name:");
		String recipeName = scanner.nextLine();

		System.out.println("Rating");
		int rating = scanner.nextInt();
		scanner.nextLine();

		DatabaseHelper.getDatabaseHelper().insertIntoArray(recipeName, "Ratings", rating);
		return 0;
	}
}
