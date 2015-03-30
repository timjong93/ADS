package com.timtom.model;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hdfs.util.ByteArray;
import org.hsqldb.Database;

import com.google.common.primitives.Longs;
import com.timtom.database.DatabaseHelper;

public class Mail {

	public String sender;
	public String[] recievers;
	public String[] ccRecievers;
	public String[] bccRecievers;
	public Date sendTime;
	public File[] attachements;
	public String subject;
	public String mailBody;
	public String label;
	public boolean read;
	
	public Mail(String sender, String[] recievers, String[] ccRecievers,String[] bccRecievers,
			File[] attachements, String subject,
			String mailBody, String label, boolean read) {
		this(sender,recievers,ccRecievers,bccRecievers, new Date(), attachements, subject, mailBody, label, read);
	}
	
	public Mail(String sender, String[] recievers, String[] ccRecievers, String[] bccRecievers,
			Date sendTime, File[] attachements, String subject,
			String mailBody, String label, boolean read) {
		super();
		this.sender = sender;
		this.recievers = recievers;
		this.ccRecievers = ccRecievers;
		this.bccRecievers = bccRecievers;
		this.sendTime = sendTime;
		this.attachements = attachements;
		this.subject = subject;
		this.mailBody = mailBody;
		this.label = label;
		this.read = read;
	}

	public Mail() {
		this.sendTime = new Date();
	}

	public byte[] getKey(String reciever) throws NoSuchAlgorithmException
	{
		byte[] senderHashed;
		byte[] recieverHashed;
		byte[] total;
		byte[] timebytes;
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		md.update(sender.getBytes());
		senderHashed = md.digest();
		md.reset();
		
		md.update(reciever.getBytes());
		recieverHashed = md.digest();
		
		timebytes = Longs.toByteArray(sendTime.getTime());
		
		total = new byte[40 + timebytes.length];
		System.arraycopy(recieverHashed, 0, total, 0, recieverHashed.length < 20 ? recieverHashed.length : 20);
		System.arraycopy(recieverHashed, 0, total, 20, timebytes.length);
		System.arraycopy(senderHashed, 0, total, 20 + timebytes.length, senderHashed.length < 20 ? senderHashed.length : 20);
		
		return total;
	}
	
	public static Mail parseResult(Result result)
	{
		return new Mail(new String(result.getValue(Bytes.toBytes("sender"),Bytes.toBytes("name"))), null, null, null, new Date(Longs.fromByteArray((result.getValue(Bytes.toBytes("meta"),Bytes.toBytes("send_time"))))), null, new String(result.getValue(Bytes.toBytes("content"),Bytes.toBytes("subject"))), new String(result.getValue(Bytes.toBytes("content"),Bytes.toBytes("body"))), null, false);
	}
	
	public String toString(){
		return "Sender: \t"+this.sender+"\nSend: \t"+this.sendTime+" (" + this.sendTime.getTime() + ")"+ "\nSubject: \t"+this.subject+"\nmail: \t"+this.mailBody+ "\n--------------------------------------\n";
		
	}
}
