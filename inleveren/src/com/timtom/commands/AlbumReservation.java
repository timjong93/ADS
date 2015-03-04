package com.timtom.commands;

import java.util.Scanner;

import com.timtom.DatabaseHelper;

public class AlbumReservation extends Command
{

	public AlbumReservation()
	{
		super("place reservation for a Album");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("album id:");
		int albumId = scanner.nextInt();
		System.out.println("customer id: ");
		int customerId = scanner.nextInt();

		DatabaseHelper.getDatabaseHelper().productReservation(-1, albumId, customerId);

		return -1;
	}
}
