package com.timtom.commands;

import java.util.ArrayList;
import java.util.Scanner;

import com.timtom.DatabaseHelper;

public class AddMovie extends Command
{

	CommandList subMenuArtist = new CommandList();

	public AddMovie()
	{
		super("Add movie");
		subMenuArtist.addCommand(new AddArtist());
		subMenuArtist.addCommand(new FindArtist());
		subMenuArtist.addCommand(new ExitCommand());
	}

	@Override
	public Object execute(Scanner scanner)
	{
		scanner.nextLine();
		System.out.println("Movie name:");
		String name = scanner.nextLine();
		System.out.println("Publisher:");
		String publisher = scanner.next();
		System.out.println("Duration:");
		int duration = scanner.nextInt();

		ArrayList<Integer> artists = new ArrayList<Integer>();
		while (true)
		{
			Object res = subMenuArtist.ExecuteCommand(scanner);

			if (res instanceof Integer)
			{
				artists.add((Integer) res);
			} else
			{
				break;
			}
		}

		int id = DatabaseHelper.getDatabaseHelper().insertMovie(publisher, name, duration, artists);

		System.out.println("Created a movie with id: " + id);
		return id;
	}
}
