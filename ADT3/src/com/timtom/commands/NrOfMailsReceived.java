package com.timtom.commands;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.internal.matchers.Each;

import com.timtom.database.DatabaseHelper;
import com.timtom.model.Mail;

public class NrOfMailsReceived extends Command {

 public NrOfMailsReceived() {
  super("See nr of mails received for user");
  // TODO Auto-generated constructor stub
 }

 @Override
 public Object execute(Scanner scanner) {
  System.out.println("your email:");
  String owner = scanner.nextLine();
  
  try {
   int result = DatabaseHelper.getDatabaseHelper().nrOfReceivedMails(owner);
   System.out.println("Nr of mails received by "+owner+" is: "+result);
  } catch (Exception e) {
   System.out.println("Something went wrong!");
   e.printStackTrace();
  }
  return 0;
 }

}