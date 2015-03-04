package com.timtom.commands;

import java.util.Scanner;

import com.timtom.DatabaseHelper;

public class AddDVD extends Command
{

	public AddDVD()
	{
		super("Add DVD");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("Insert movieId:");
		int movieId = scanner.nextInt();
		System.out.println("Insert amount of copies:");
		int amount = scanner.nextInt();

		DatabaseHelper.getDatabaseHelper().insertProduct(movieId, -1, amount);

		return 1;
	}
}
