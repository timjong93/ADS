package com.timtom.commands;

import java.util.Scanner;

import com.mongodb.DBObject;
import com.timtom.AskUserUtil;
import com.timtom.DatabaseHelper;
import com.timtom.exeception.NoModelFoundException;

public class InsertRecipe extends Command
{

	public InsertRecipe()
	{
		super("Insert a recipe");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		try
		{
			DatabaseHelper.getDatabaseHelper().insertInto("recipes", (DBObject) AskUserUtil.AskUserFieldsInput(scanner, "Recipe"));
		} catch (NoModelFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
