package com.timtom.commands;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.internal.matchers.Each;

import com.timtom.database.DatabaseHelper;
import com.timtom.model.Mail;

public class SearchContacts extends Command {

	public SearchContacts() {
		super("My Conctacts");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(Scanner scanner) {
		System.out.println("your email:");
		String owner = scanner.nextLine();
		
		try {
			List<String> contacts = DatabaseHelper.getDatabaseHelper().searchContacts(owner);
			for(String s: contacts){
				System.out.println(s);
			}
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			e.printStackTrace();
		}
		return 0;
	}

}

