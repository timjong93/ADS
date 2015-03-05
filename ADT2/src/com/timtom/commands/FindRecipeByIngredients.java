package com.timtom.commands;

import com.mongodb.DBObject;
import com.timtom.DatabaseHelper;
import com.timtom.PrintUtils;

import java.util.List;
import java.util.Scanner;

/**
 * Created by Tim on 5-3-2015.
 */
public class FindRecipeByIngredients extends Command {

    public FindRecipeByIngredients()
    {
        super("Find Recipe by Ingredient");
        // TODO Auto-generated constructor stub
    }

    @Override
    public Object execute(Scanner scanner)
    {
        String[] ingredients = {"UI"};

        
        List<DBObject> items = DatabaseHelper.getDatabaseHelper().getRecipesByIngredients(ingredients);

        PrintUtils.printDBObjects(items, 0);

        return 0;
    }
}
