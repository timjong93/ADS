package com.timtom.commands;

import com.timtom.DatabaseHelper;

import java.util.ArrayList;
import java.util.Scanner;

public class FindRentalHistoryByAlbum extends Command
{

    public FindRentalHistoryByAlbum()
    {
        super("Find rental History by album name");
    }

    @Override
    public Object execute(Scanner scanner)
    {
        scanner.nextLine();
        System.out.println("Abum Name:");
        String albumName = scanner.nextLine();

        ArrayList<String> result = DatabaseHelper.getDatabaseHelper().findRentalHistoryByAlbum(albumName);
        System.out.println("History found: ");
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result);
        }
        return result;
    }

}
