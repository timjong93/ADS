package com.timtom;

import com.timtom.commands.AddArtist;
import com.timtom.commands.Command;

import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

/**
 * Created by Tim on 12-2-2015.
 */
public class App {

    private Scanner scanner;
    private ArrayList<Command> commandList;

    public App(){
        scanner = new Scanner(System.in);
        commandList = new ArrayList<Command>();
        commandList.add(new AddArtist("Add an artist."));
        execute();
        try {
            new DatabaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printWelcomeText(){
        System.out.println("Welcome to the Audio/Video Rental System.");
    }

    public void execute(){
        printWelcomeText();
        while(true){
            printMenu();
            int i = scanner.nextInt();
            if(i-1<commandList.size()) {
                commandList.get(i - 1).execute(scanner);
            }else{
                System.err.println("ïnvalid choice");
            }
        }
    }

    public void printMenu(){
        System.out.println("Please make a choice from the menu");
        for (int i = 0; i < commandList.size(); i++) {
            System.out.println(String.format("%d).\t%s",i+1,commandList.get(i).getDescription()));
        }
    }

}
