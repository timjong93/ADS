package com.timtom.commands;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

import com.timtom.DatabaseHelper;

public class AddAlbum extends Command
{

	private CommandList subMenuTrack = new CommandList();

	public AddAlbum()
	{
		super("Add Album");
		subMenuTrack.addCommand(new AddTrack());
		subMenuTrack.addCommand(new FindTrack());
		subMenuTrack.addCommand(new ExitCommand());
	}

	public Object execute(Scanner scanner)
	{
		System.out.println("Please enter the following artist data:");
		System.out.println("Artist ID:");
		int artistId = scanner.nextInt();
		System.out.println("Publisher:");
		String publisher = scanner.next();
		System.out.println("Album name:");
		String name = scanner.next();
		System.out.println("Release date (dd-MM-yyyy):");
		Date date = null;

		while (date == null)
		{
			try
			{
				String releaseDate = scanner.next();
				DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				date = new Date(format.parse(releaseDate).getTime());
			} catch (ParseException e)
			{
				System.out.println("Not a valid date please try again (dd-MM-yyyy)");
			}

		}

		ArrayList<Integer> tracks = new ArrayList<Integer>();

		while (true)
		{
			Object res = subMenuTrack.ExecuteCommand(scanner);

			if (res instanceof Integer)
			{
				tracks.add((Integer) res);
			} else
			{
				break;
			}
		}

		int id = DatabaseHelper.getDatabaseHelper().insertAlbum(artistId, publisher, name, date, tracks);

		System.out.println("Album generated with id: " + id);

		return id;
	}
}
