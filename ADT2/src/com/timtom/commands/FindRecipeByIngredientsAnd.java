package com.timtom.commands;

import com.mongodb.DBObject;
import com.timtom.DatabaseHelper;
import com.timtom.PrintUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Tim on 5-3-2015.
 */
public class FindRecipeByIngredientsAnd extends Command {

    public FindRecipeByIngredientsAnd()
    {
        super("Find Recipe by Ingredient (and)");
        // TODO Auto-generated constructor stub
    }

    @Override
    public Object execute(Scanner scanner)
    {
        ArrayList<String> ingredients = new ArrayList<String>();
        System.out.println("Give ingredient: ");
        ingredients.add(scanner.nextLine());
        while (true)
        {
            System.out.println("Do you want to at another value?(y/n):");
            if (scanner.nextLine().toLowerCase().equals("y"))
            {
                System.out.println("Give ingredient: ");
                ingredients.add(scanner.nextLine());
            } else
            {
                break;
            }
        }

        List<DBObject> items = DatabaseHelper.getDatabaseHelper().getRecipesByIngredientsAND(ingredients);

        PrintUtils.printDBObjects(items, 0);

        return 0;
    }
}
