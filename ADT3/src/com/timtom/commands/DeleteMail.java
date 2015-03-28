package com.timtom.commands;

import java.util.Date;
import java.util.Scanner;

import com.timtom.database.DatabaseHelper;
import com.timtom.model.Mail;


public class DeleteMail extends Command {

	public DeleteMail() {
		super("Delete Mail");
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
		
		DatabaseHelper.getDatabaseHelper().removeMail(owner, m);
		
		return 0;
	}
}
