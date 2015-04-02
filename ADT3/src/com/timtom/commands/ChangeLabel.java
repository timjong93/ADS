package com.timtom.commands;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Scanner;

import com.timtom.database.DatabaseHelper;
import com.timtom.model.Mail;

public class ChangeLabel extends Command {

	public ChangeLabel() {
		super("Change label of mail");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(Scanner scanner) {
		System.out.println("Jouw mail adres:");
		String owner = scanner.nextLine();
		System.out.println("Mail van:");
		String sender = scanner.nextLine();
		System.out.println("Epoch tijd verzonden:");
		long tijd = scanner.nextLong();
		scanner.nextLine();
		System.out.println("Nieuw Label:");
		String label = scanner.nextLine();
		

		Mail m = new Mail();

		m.sender = sender;
		m.sendTime = new Date(tijd);

		try {
			DatabaseHelper.getDatabaseHelper().setLabel(owner, m, label);
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