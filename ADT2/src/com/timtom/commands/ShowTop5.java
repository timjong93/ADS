package com.timtom.commands;

import com.mongodb.DBObject;
import com.timtom.DatabaseHelper;
import com.timtom.PrintUtils;

import java.util.List;
import java.util.Scanner;

public class ShowTop5 extends Command
{

	public ShowTop5()
	{
		super("Show Top 5");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(Scanner scanner)
	{
		List<DBObject> items = DatabaseHelper.getDatabaseHelper().getTop5Recipes();

		PrintUtils.printDBObjects(items, 0);

		return 0;
	}
}
