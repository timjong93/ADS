package com.timtom.commands;

import java.util.ArrayList;
import java.util.Scanner;

import com.timtom.DatabaseHelper;

public class AddTrack extends Command
{
    CommandList subMenuArtist = new CommandList();

	public AddTrack()
	{
        super("Add a track");
        subMenuArtist.addCommand(new AddArtist());
        subMenuArtist.addCommand(new FindArtist());
        subMenuArtist.addCommand(new ExitCommand());
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

		int id = DatabaseHelper.getDatabaseHelper().insertTrack(name, duration, genre, artists);
        System.out.println("Created Track with id: " + id);
		return id;
	}
}
