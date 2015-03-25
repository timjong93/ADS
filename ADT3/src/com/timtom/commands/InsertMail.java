package com.timtom.commands;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import com.timtom.database.DatabaseHelper;
import com.timtom.model.Mail;

public class InsertMail extends Command {

	public InsertMail() {
		super("Send Mail");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(Scanner scanner) {
		Mail mail = new Mail();
		String line;
		System.out.println("your email:");
		mail.sender = scanner.nextLine();
		
		System.out.println("enter recipients (empty line breaks the loop)");
		ArrayList<String> list = new ArrayList<String>();
		while (true) {
			line = scanner.nextLine();
			if (line.trim().length() == 0) {
				break;
			} else {
				list.add(line);
			}
		}
		mail.recievers = new String[list.size()];
		list.toArray(mail.recievers);

		System.out.println("enter CC recipients (empty line breaks the loop)");
		list = new ArrayList<String>();
		while (true) {
			line = scanner.nextLine();
			if (line.trim().length() == 0) {
				break;
			} else {
				list.add(line);
			}
		}
		mail.ccRecievers = new String[list.size()];
		list.toArray(mail.ccRecievers);

		System.out.println("enter BCC recipients (empty line breaks the loop)");
		list = new ArrayList<String>();
		while (true) {
			line = scanner.nextLine();
			if (line.trim().length() == 0) {
				break;
			} else {
				list.add(line);
			}
		}
		mail.bccRecievers = new String[list.size()];
		list.toArray(mail.bccRecievers);
		
		System.out.println("Subject:");
		mail.subject = scanner.nextLine();
		
		System.out.println("mailBody");
		mail.mailBody = scanner.nextLine();
		
		System.out.println("enter attachements (empty line breaks the loop)");
		ArrayList<File> attachements = new ArrayList<File>();
		while (true) {
			line = scanner.nextLine();
			if (line.trim().length() == 0) {
				break;
			} else {
				File f = new File(line);
				if(f.exists())
					attachements.add(f);
				else
					System.out.println("Thats not a file!");
			}
		}
		
		mail.attachements = new File[attachements.size()];
		attachements.toArray(mail.attachements);

		try {
			DatabaseHelper.getDatabaseHelper().insertMail(mail);
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			e.printStackTrace();
		}
		return 0;
	}

}
