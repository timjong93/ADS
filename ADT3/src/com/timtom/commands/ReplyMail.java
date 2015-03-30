package com.timtom.commands;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.timtom.database.DatabaseHelper;
import com.timtom.model.Mail;

public class ReplyMail extends Command {

	public ReplyMail() {
		super("Reply on mail");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object execute(Scanner scanner) {
		System.out.println("Jouw mail adres:");
		String owner = scanner.nextLine();
		System.out.println("Mail van:");
		String sender = scanner.nextLine();
		System.out.println("tijd ontvangen in miliseconden sinds epoch:");
		long tijd = scanner.nextLong();
		scanner.nextLine();
		
		Mail m = new Mail();
		
		m.sender = sender;
		m.sendTime = new Date(tijd);
		
		m = DatabaseHelper.getDatabaseHelper().getMail(owner, m);
		
		if(m.recievers == null)
		{
			System.out.println("Mail not found");
			return 1;
		}
		
		System.out.println(m);
		
		System.out.println("reply body:");
		String s = scanner.nextLine();
		
		ArrayList<String> recievers = new ArrayList<String>();
		recievers.add(m.sender);
		
		Mail reply = new Mail(owner,recievers , new ArrayList<String>(), new ArrayList<String>(), new File[0], "RE:" + m.subject, s + "\n++++++ Reply on:\n" + m.mailBody, "", false);
		
		try {
			DatabaseHelper.getDatabaseHelper().insertMail(reply);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}

}
