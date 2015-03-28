package com.timtom.database;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.sound.midi.SysexMessage;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.google.common.primitives.Longs;
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
			
			put.add(Bytes.toBytes(meta), Bytes.toBytes("send_time"), Longs.toByteArray(mail.sendTime.getTime()));
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
	
	public void getMail(String owner, Mail m)
	{
		try {
			Get get = new Get(m.getKey(owner));
			get.addFamily(content.getBytes());
			get.addFamily(sender.getBytes());
			Result r = mailTable.get(get);
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Mail> getInbox(String owner) throws IOException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(owner.getBytes());
		byte[] ownerHashed = md.digest();
		md.reset();
		byte[] prefix = Arrays.copyOfRange(ownerHashed, 0, ownerHashed.length < 20 ? ownerHashed.length : 20);
		System.out.println(Arrays.toString(prefix));
		Scan scan = new Scan();
		Filter prefixFilter = new PrefixFilter(prefix);
	    scan.setFilter(prefixFilter);
	    scan.addColumn(Bytes.toBytes("sender"), Bytes.toBytes("name"));
	    scan.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("send_time"));
		scan.addColumn(Bytes.toBytes("content"), Bytes.toBytes("subject"));
		scan.addColumn(Bytes.toBytes("content"), Bytes.toBytes("body"));
		ResultScanner scanner = mailTable.getScanner(scan);
		ArrayList<Mail> inbox = new ArrayList<Mail>();
		for (Result result : scanner) {
		    inbox.add(new Mail(new String(result.getValue(Bytes.toBytes("sender"),Bytes.toBytes("name"))), null, null, null, new Date(Longs.fromByteArray((result.getValue(Bytes.toBytes("meta"),Bytes.toBytes("send_time"))))), null, new String(result.getValue(Bytes.toBytes("content"),Bytes.toBytes("subject"))), new String(result.getValue(Bytes.toBytes("content"),Bytes.toBytes("body"))), null, false));
		}
		return inbox;
	}
	
	public void removeMail(String owner, Mail mail)
	{
		try {
			Delete delete = new Delete(mail.getKey(owner));
			
			mailTable.delete(delete);
			
			mailTable.flushCommits();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
