package com.timtom.commands;

import java.util.List;
import java.util.Scanner;

import com.mongodb.DBObject;
import com.timtom.DatabaseHelper;
import com.timtom.PrintUtils;

public class ShowAllRecipes extends Command
{

	public ShowAllRecipes()
	{
		super("Show All Recipes");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(Scanner scanner)
	{
		List<DBObject> items = DatabaseHelper.getDatabaseHelper().getAllRecipes();

		PrintUtils.printDBObjects(items, 0);

		return 0;
	}
}
