package com.timtom.commands;

import java.util.Scanner;

import com.timtom.DatabaseHelper;

public class AddCD extends Command
{

	public AddCD()
	{
		super("Add CD");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("Insert albumId:");
		int albumId = scanner.nextInt();
		System.out.println("Insert amount of copies:");
		int amount = scanner.nextInt();

		DatabaseHelper.getDatabaseHelper().insertProduct(-1, albumId, amount);

		return 1;
	}
}
