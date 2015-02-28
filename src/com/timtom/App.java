package com.timtom;

import java.util.Scanner;

import com.timtom.commands.AddAlbum;
import com.timtom.commands.AddArtist;
import com.timtom.commands.AddCD;
import com.timtom.commands.AddCustomer;
import com.timtom.commands.AddDVD;
import com.timtom.commands.AddMovie;
import com.timtom.commands.AddPublisher;
import com.timtom.commands.AlbumReservation;
import com.timtom.commands.CommandList;
import com.timtom.commands.ExitCommand;
import com.timtom.commands.FindAlbumByTrack;
import com.timtom.commands.FindMovieByArtist;
import com.timtom.commands.MovieReservation;
import com.timtom.commands.RentAlbum;
import com.timtom.commands.RentMovie;
import com.timtom.commands.ReturnProduct;

/**
 * Created by Tim on 12-2-2015.
 */
public class App
{

	private Scanner scanner;
	private CommandList commandList;

	public App()
	{
		scanner = new Scanner(System.in);
		commandList = new CommandList();
		commandList.addCommand(new AddArtist());
		commandList.addCommand(new AddPublisher());
		commandList.addCommand(new AddCustomer());
		commandList.addCommand(new AddAlbum());
		commandList.addCommand(new AddMovie());
		commandList.addCommand(new AddDVD());
		commandList.addCommand(new AddCD());
		commandList.addCommand(new RentMovie());
		commandList.addCommand(new RentAlbum());
		commandList.addCommand(new ReturnProduct());
		commandList.addCommand(new MovieReservation());
		commandList.addCommand(new AlbumReservation());
		commandList.addCommand(new FindAlbumByTrack());
		commandList.addCommand(new FindMovieByArtist());

		// Must be last
		commandList.addCommand(new ExitCommand());
		execute();

		DatabaseHelper.getDatabaseHelper();

	}

	public void printWelcomeText()
	{
		System.out.println("Welcome to the Audio/Video Rental System.");
	}

	public void execute()
	{
		printWelcomeText();
		while (true)
		{
			Object result = commandList.ExecuteCommand(scanner);

			if (result instanceof String && ((String) result).equals("EXITLOOP"))
			{
				break;
			}
		}
	}
}
