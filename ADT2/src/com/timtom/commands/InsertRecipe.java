package com.timtom.commands;

import java.util.Scanner;

import com.mongodb.DBObject;
import com.timtom.AskUserUtil;
import com.timtom.DatabaseHelper;

public class InsertRecipe extends Command
{

	public InsertRecipe()
	{
		super("Insert a recipe");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		DatabaseHelper.getDatabaseHelper().insertInto("recipes", (DBObject) AskUserUtil.AskUserFieldsInput(scanner, "Recipe"));
		return 0;
	}
}
