package com.timtom.commands;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.internal.matchers.Each;

import com.timtom.database.DatabaseHelper;
import com.timtom.model.Mail;

public class Inbox extends Command {

	public Inbox() {
		super("check Inbox");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(Scanner scanner) {
		System.out.println("your email:");
		String owner = scanner.nextLine();
		
		try {
			ArrayList<Mail> inbox = DatabaseHelper.getDatabaseHelper().getInbox(owner);
			for(Mail m:inbox){
				System.out.println(m);
			}
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			e.printStackTrace();
		}
		return 0;
	}

}

