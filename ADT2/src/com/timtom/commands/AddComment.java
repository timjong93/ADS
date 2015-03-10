package com.timtom.commands;

import java.util.Scanner;

import com.mongodb.DBObject;
import com.timtom.AskUserUtil;
import com.timtom.DatabaseHelper;
import com.timtom.exeception.NoModelFoundException;

public class AddComment extends Command
{

	public AddComment()
	{
		super("Add Comment");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("Please give name of Recipe:");
		String name = scanner.nextLine();

		try
		{
			DatabaseHelper.getDatabaseHelper().insertIntoArray(name, "Comments", (DBObject) AskUserUtil.AskUserFieldsInput(scanner, "Comment"));
		} catch (NoModelFoundException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
}
