package com.timtom;

import java.util.Scanner;

import com.timtom.commands.*;
import com.timtom.database.DatabaseHelper;

/**
 * Created by Tim on 12-2-2015.
 */
public class App {

	private Scanner scanner;
	private CommandList commandList;

	public App() {
		scanner = new Scanner(System.in);
		commandList = new CommandList();

		// commands
		commandList.addCommand(new InsertMail());
		commandList.addCommand(new Inbox());
		commandList.addCommand(new SearchMail());
		commandList.addCommand(new DeleteMail());
		commandList.addCommand(new ReplyMail());
		commandList.addCommand(new SearchContacts());
		commandList.addCommand(new ChangeLabel());
		
		// Must be last
		commandList.addCommand(new ExitCommand());

	}

	public void printWelcomeText() {
		System.out.println(" ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄       ▄▄  ▄▄▄▄▄▄▄▄▄▄▄ \n▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░▌     ▐░░▌▐░░░░░░░░░░░▌\n▐░█▀▀▀▀▀▀▀▀▀  ▀▀▀▀█░█▀▀▀▀ ▐░▌░▌   ▐░▐░▌▐░█▀▀▀▀▀▀▀▀▀ \n▐░▌               ▐░▌     ▐░▌▐░▌ ▐░▌▐░▌▐░▌          \n▐░█▄▄▄▄▄▄▄▄▄      ▐░▌     ▐░▌ ▐░▐░▌ ▐░▌▐░█▄▄▄▄▄▄▄▄▄ \n▐░░░░░░░░░░░▌     ▐░▌     ▐░▌  ▐░▌  ▐░▌▐░░░░░░░░░░░▌\n ▀▀▀▀▀▀▀▀▀█░▌     ▐░▌     ▐░▌   ▀   ▐░▌ ▀▀▀▀▀▀▀▀▀█░▌\n          ▐░▌     ▐░▌     ▐░▌       ▐░▌          ▐░▌\n ▄▄▄▄▄▄▄▄▄█░▌ ▄▄▄▄█░█▄▄▄▄ ▐░▌       ▐░▌ ▄▄▄▄▄▄▄▄▄█░▌\n▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░▌       ▐░▌▐░░░░░░░░░░░▌\n ▀▀▀▀▀▀▀▀▀▀▀  ▀▀▀▀▀▀▀▀▀▀▀  ▀         ▀  ▀▀▀▀▀▀▀▀▀▀▀ \n                                                    ");
		System.out.println("Welcome to the SIMS.");
		System.out.println("(Saxion Internal Mailing System)");
	}

	public void execute() {
		try {
			printWelcomeText();
			while (true) {
				Object result = commandList.ExecuteCommand(scanner);

				if (result instanceof String
						&& ((String) result).equals("EXITLOOP")) {
					break;
				}
			}
		} finally {
			DatabaseHelper.getDatabaseHelper().closeConnection();
		}
	}
}
