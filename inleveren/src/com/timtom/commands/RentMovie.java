package com.timtom.commands;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import com.timtom.DatabaseHelper;

public class RentMovie extends Command
{

	public RentMovie()
	{
		super("Rent Movie");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("movie id:");
		int movieId = scanner.nextInt();
		System.out.println("customer id:");
		int customerId = scanner.nextInt();
		System.out.println("Return date (dd-MM-yyyy):");
		Date date = null;

		while (date == null)
		{
			try
			{
				String releaseDate = scanner.next();
				DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				date = new Date(format.parse(releaseDate).getTime());
			} catch (ParseException e)
			{
				System.out.println("Not a valid date please try again (dd-MM-yyyy)");
			}

		}

		int id = DatabaseHelper.getDatabaseHelper().rentMovie(movieId, customerId, date);

		if (id > 0)
		{
			System.out.println("rented product with id:" + id);
		} else
		{
			System.out.println("not available");
		}

		return scanner;
	}

}
