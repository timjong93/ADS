package com.timtom.commands;

import java.util.Scanner;

import org.bson.types.ObjectId;

import com.timtom.DatabaseHelper;

public class LikeComment extends Command
{

	public LikeComment()
	{
		super("Like comment");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		System.out.println("Recipe name:");
		String recipeName = scanner.nextLine();
		System.out.println("Comment id:");
		DatabaseHelper.getDatabaseHelper().likeComment(recipeName, new ObjectId(scanner.next()));
		scanner.nextLine();
		return 0;
	}
}
