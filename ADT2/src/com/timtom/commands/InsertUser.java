package com.timtom.commands;

import java.util.Scanner;

import com.mongodb.DBObject;
import com.timtom.AskUserUtil;
import com.timtom.DatabaseHelper;

public class InsertUser extends Command
{

	public InsertUser()
	{
		super("Insert a user");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		DatabaseHelper.getDatabaseHelper().insertInto("users", (DBObject) AskUserUtil.AskUserFieldsInput(scanner, "User"));
		return 0;
	}
}
