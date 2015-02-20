package com.timtom.commands;

import java.util.Scanner;

public class FindArtist extends Command
{

	public FindArtist()
	{
		super("Find existing artist");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("artist id:");
		return scanner.nextInt();
	}

}
