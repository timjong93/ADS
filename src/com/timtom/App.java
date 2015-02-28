package com.timtom;

import java.util.Scanner;

import com.timtom.commands.*;


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
