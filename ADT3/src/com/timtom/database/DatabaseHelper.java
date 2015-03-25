package com.timtom.database;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class DatabaseHelper {
	private static DatabaseHelper instance;
	
	private DatabaseHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public DatabaseHelper getDatabaseHelper()
	{
		if(instance == null)
		{
			instance = new DatabaseHelper();
		}
		return instance;
	}
	
	public boolean createTable()
	{
		Configuration conf = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(conf);
		HTableDescriptor tableDescriptor = new HTableDescriptor("Mail");
		tableDescriptor.addFamily(new HColumnDescriptor("personal"));
		tableDescriptor.addFamily(new HColumnDescriptor("contactinfo"));
		tableDescriptor.addFamily(new HColumnDescriptor("creditcard"));
		admin.createTable(tableDescriptor);
		return true;
	}
}