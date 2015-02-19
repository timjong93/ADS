package com.timtom.commands;

import java.util.Scanner;

import com.timtom.DatabaseHelper;

public class AddCustomer extends Command
{

	public AddCustomer()
	{
		super("Add Customer");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("Please enter the following customer data:");
		System.out.println("First name:");
		String firstName = scanner.next();
		System.out.println("Last name:");
		String lastName = scanner.next();
		System.out.println("Email adress:");
		String mailAdress = scanner.next();

		int id = DatabaseHelper.getDatabaseHelper().insertCustomer(firstName, lastName, mailAdress);
		System.out.println("Created Customer with id: " + id);
		return id;
	}
}
