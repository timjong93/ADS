package com.timtom.commands;

import com.timtom.DatabaseHelper;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Tim on 28-2-2015.
 */
public class FindMovieByArtist extends Command
{

    public FindMovieByArtist()
    {
        super("Find movie by artist name");
    }

    @Override
    public Object execute(Scanner scanner)
    {
        scanner.nextLine();
        System.out.println("Artist firstname:");
        String firstName = scanner.nextLine();
        System.out.println("Artist lastname:");
        String lastNAme = scanner.nextLine();

        ArrayList<String> result = DatabaseHelper.getDatabaseHelper().findMovieByArtist(firstName,lastNAme);
        System.out.println("Movies found: ");
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result);
        }
        return result;
    }
}
