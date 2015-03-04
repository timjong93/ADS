package com.timtom.commands;

import java.util.Scanner;

import com.timtom.AskUserUtil;

public class InsertRecipe extends Command
{

	public InsertRecipe()
	{
		super("Ïnsert a recipe");
	}

	@Override
	public Object execute(Scanner scanner)
	{
		AskUserUtil.AskUserFieldsInput("Ingredient");
		return 0;
	}
}
