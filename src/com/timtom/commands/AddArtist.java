package com.timtom.commands;

import java.util.Scanner;

/**
 * Created by Tim on 12-2-2015.
 */
public class AddArtist extends Command {

    public AddArtist(String description) {
        super(description);
    }

    @Override
    public void execute(Scanner scanner){
        System.out.println("Please enter the following artist data:");
        System.out.println("First name:");
        String firstName = scanner.next();
        System.out.println("Last name:");
        String lastNames = scanner.next();

    }
}
