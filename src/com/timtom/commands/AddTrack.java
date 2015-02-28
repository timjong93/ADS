package com.timtom.commands;

import java.util.Scanner;

import com.timtom.DatabaseHelper;

public class AddTrack extends Command
{

	public AddTrack()
	{
		super("Add a track");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		scanner.nextLine();
		System.out.println("Track name:");
		String name = scanner.nextLine();
		System.out.println("Duration (in seconds):");
		int duration = scanner.nextInt();
		System.out.println("Genre:");
		String genre = scanner.next();

		int id = DatabaseHelper.getDatabaseHelper().insertTrack(name, duration, genre);
        System.out.println("Created Track with id: " + id);
		return id;
	}
}
