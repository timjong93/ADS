package com.timtom.commands;

import com.timtom.DatabaseHelper;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Tim on 28-2-2015.
 */
public class FindAlbumByTrack extends Command
{

    public FindAlbumByTrack()
    {
        super("Fin album by track name");
    }

    @Override
    public Object execute(Scanner scanner)
    {
        scanner.nextLine();
        System.out.println("Track name:");
        String trackName = scanner.nextLine();

        ArrayList<String> result = DatabaseHelper.getDatabaseHelper().findAlbumByTrack(trackName);
        System.out.println("Albums found: ");
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result);
        }
        return result;
    }
}

