package com.timtom.commands;

import java.util.Scanner;

import org.bson.types.ObjectId;

import com.timtom.DatabaseHelper;

public class DislikeComment extends Command
{

	public DislikeComment()
	{
		super("Dislike comment");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("Recipe name:");
		String recipeName = scanner.nextLine();
		System.out.println("Comment id:");
		DatabaseHelper.getDatabaseHelper().dislikeComment(recipeName, new ObjectId(scanner.next()));
		scanner.nextLine();
		return 0;
	}

}
