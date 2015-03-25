package com.timtom.database;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.sound.midi.SysexMessage;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.timtom.model.Mail;

public class DatabaseHelper {
	private static DatabaseHelper instance;
	private HBaseAdmin admin;
	private Configuration conf;
	private HTable mailTable;
	public DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private static final String recipients = "recipients";
	private static final String sender = "sender";
	private static final String content = "content";
	private static final String meta = "meta";
	private static final String attachements = "attachements";
	
	private DatabaseHelper() {
		conf = HBaseConfiguration.create();
		try {
			admin = new HBaseAdmin(conf);
			if(!admin.tableExists("Mail"))
			{
				if(createTable())
				{
					System.out.println("Created table \"Mail\"");
				}
				else
				{
					System.err.println("failed to create tables");
					System.exit(1);
				}
			}
			
			mailTable = new HTable(conf, "Mail");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static DatabaseHelper getDatabaseHelper()
	{
		if(instance == null)
		{
			instance = new DatabaseHelper();
		}
		return instance;
	}
	
	public boolean createTable()
	{
		try {
			HTableDescriptor tableDescriptor = new HTableDescriptor("Mail");
			tableDescriptor.addFamily(new HColumnDescriptor(recipients));
			tableDescriptor.addFamily(new HColumnDescriptor(sender));
			tableDescriptor.addFamily(new HColumnDescriptor(content));
			tableDescriptor.addFamily(new HColumnDescriptor(meta));
			tableDescriptor.addFamily(new HColumnDescriptor(attachements));
			admin.createTable(tableDescriptor);
			return true;			
		} catch (MasterNotRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void closeConnection()
	{
		try {
			mailTable.close();
			admin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertMail(Mail mail) throws NoSuchAlgorithmException, IOException
	{
		ArrayList<Put> puts = new ArrayList<Put>(); 
		for (int i = 0; i < mail.recievers.length + mail.ccRecievers.length + mail.bccRecievers.length; i++)
		{
			Put put = new Put();
			
			if(i < mail.recievers.length)
			{
				put = new Put(mail.getKey(mail.recievers[i]));
			}else if (i < mail.recievers.length + mail.ccRecievers.length)
			{
				put = new Put(mail.getKey(mail.ccRecievers[i - mail.recievers.length]));
			}
			else
			{
				put = new Put(mail.getKey(mail.bccRecievers[i - (mail.recievers.length + mail.ccRecievers.length)]));
				put.add(Bytes.toBytes(recipients), Bytes.toBytes(mail.bccRecievers[i - (mail.recievers.length + mail.ccRecievers.length)]), Bytes.toBytes("BCC"));
			}
			
			for(String s : mail.recievers)
			{
				put.add(Bytes.toBytes(recipients), Bytes.toBytes(s), Bytes.toBytes("REC"));
			}
			for(String s : mail.ccRecievers)
			{
				put.add(Bytes.toBytes(recipients), Bytes.toBytes(s), Bytes.toBytes("CC"));
			}
			
			put.add(Bytes.toBytes(sender), Bytes.toBytes("name"), Bytes.toBytes(mail.sender));
			
			put.add(Bytes.toBytes(content), Bytes.toBytes("subject"), Bytes.toBytes(mail.subject));
			put.add(Bytes.toBytes(content), Bytes.toBytes("body"), Bytes.toBytes(mail.mailBody));
			
			put.add(Bytes.toBytes(meta), Bytes.toBytes("send_time"), Bytes.toBytes(dateFormat.format(mail.sendTime)));
			put.add(Bytes.toBytes(meta), Bytes.toBytes("read"), Bytes.toBytes(false));
			
			for(File f : mail.attachements)
			{
				put.add(Bytes.toBytes(attachements), Bytes.toBytes(f.getName()), Bytes.toBytes(f.getAbsolutePath()));
			}
			
			puts.add(put);
		}
		
		mailTable.put(puts);
		mailTable.flushCommits();
	}
	
	
}