package com.timtom.commands;

import java.util.Scanner;

import com.timtom.DatabaseHelper;

public class AddPublisher extends Command
{

	public AddPublisher()
	{
		super("Add Publisher");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("Publisher name:");
		DatabaseHelper.getDatabaseHelper().insertPublisher(scanner.next());
		return 0;
	}

}
