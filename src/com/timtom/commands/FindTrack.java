package com.timtom.commands;

import java.util.Scanner;

public class FindTrack extends Command
{

	public FindTrack()
	{
		super("Find Track");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("Track id:");
		return scanner.nextInt();
	}

}
