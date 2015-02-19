package com.timtom.commands;

import java.util.ArrayList;
import java.util.Scanner;

public class AddMovie extends Command
{

	CommandList subMenuArtist;

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
		System.out.println("duration");
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

		return null;
	}
}
