package com.timtom.commands;

import java.util.Scanner;

import com.timtom.DatabaseHelper;

public class MovieReservation extends Command
{

	public MovieReservation()
	{
		super("place reservation for a movie");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("movie id:");
		int movieId = scanner.nextInt();
		System.out.println("customer id: ");
		int customerId = scanner.nextInt();

		DatabaseHelper.getDatabaseHelper().productReservation(movieId, -1, customerId);

		return -1;
	}
}
