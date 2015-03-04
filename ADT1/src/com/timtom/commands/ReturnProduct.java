package com.timtom.commands;

import java.util.Scanner;

import com.timtom.DatabaseHelper;

public class ReturnProduct extends Command
{

	public ReturnProduct()
	{
		super("Return Product");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("product id:");
		DatabaseHelper.getDatabaseHelper().returnProduct(scanner.nextInt());

		return -1;
	}
}
