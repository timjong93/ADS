package com.timtom.commands;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.internal.matchers.Each;

import com.timtom.database.DatabaseHelper;
import com.timtom.model.Mail;

public class NrOfMailsSend extends Command {

 public NrOfMailsSend() {
  super("See nr of mails send by a user");
 }

 @Override
 public Object execute(Scanner scanner) {
  System.out.println("email:");
  String owner = scanner.nextLine();
  
  try {
   int result = DatabaseHelper.getDatabaseHelper().nrOfSendMails(owner);
   System.out.println("Nr of mails send by "+owner+" is: "+result);
  } catch (Exception e) {
   System.out.println("Something went wrong!");
   e.printStackTrace();
  }
  return 0;
 }

}