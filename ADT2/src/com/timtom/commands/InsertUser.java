package com.timtom.commands;

import java.util.Scanner;

import com.mongodb.DBObject;
import com.timtom.AskUserUtil;
import com.timtom.DatabaseHelper;
import com.timtom.exeception.NoModelFoundException;

public class InsertUser extends Command
{

	public InsertUser()
	{
		super("Insert a user");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		try
		{
			DatabaseHelper.getDatabaseHelper().insertInto("users", (DBObject) AskUserUtil.AskUserFieldsInput(scanner, "User"));
		} catch (NoModelFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
