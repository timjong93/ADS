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
		commandList.addCommand(new DeleteMail());
		
		// Must be last
		commandList.addCommand(new ExitCommand());

	}

	public void printWelcomeText() {
		System.out.println("Welcome to the Recepies System.");
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
