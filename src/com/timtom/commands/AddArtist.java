package com.timtom.commands;

import java.util.Scanner;

import com.timtom.DatabaseHelper;

/**
 * Created by Tim on 12-2-2015.
 */
public class AddArtist extends Command
{

	public AddArtist()
	{
		super("Add Artist");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("Please enter the following artist data:");
		System.out.println("First name:");
		String firstName = scanner.next();
		System.out.println("Last name:");
		String lastName = scanner.next();

		System.out.println("Created Artist with id: " + DatabaseHelper.getDatabaseHelper().insertArtist(firstName, lastName));

		return null;
	}
}
