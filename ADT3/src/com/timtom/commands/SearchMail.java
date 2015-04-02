package com.timtom.commands;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.timtom.database.DatabaseHelper;
import com.timtom.model.Mail;

public class SearchMail extends Command {

	public SearchMail() {
		super("Search mail");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object execute(Scanner scanner) {
		
		System.out.println("Your email:");
		String owner = scanner.nextLine();
		
		System.out.println("word to search for:");
		String key = scanner.nextLine();
		
		List<Mail> mails = null;
		
		try {
			mails = DatabaseHelper.getDatabaseHelper().searchMail(owner, key);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(mails != null)
		{
			for(Mail m : mails)
			{
				System.out.println(m);
			}
		}
		
		return 0;
	}

}
