package com.timtom.database;

import java.io.IOException;

import javax.sound.midi.SysexMessage;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;

public class DatabaseHelper {
	private static DatabaseHelper instance;
	private HBaseAdmin admin;
	private Configuration conf;
	private HTable mailTable;
	
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
			
			mailTable = new HTable(conf,"Mail" );
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
			tableDescriptor.addFamily(new HColumnDescriptor("recipients"));
			tableDescriptor.addFamily(new HColumnDescriptor("sender"));
			tableDescriptor.addFamily(new HColumnDescriptor("content"));
			tableDescriptor.addFamily(new HColumnDescriptor("meta"));
			tableDescriptor.addFamily(new HColumnDescriptor("attachements"));
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
}