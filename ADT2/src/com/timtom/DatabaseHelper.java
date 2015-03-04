package com.timtom;

import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class DatabaseHelper
{
	private static DatabaseHelper dbHelper;
	private MongoClient client;
	private DB database;
	private HashMap<String, DBCollection> collections = new HashMap<String, DBCollection>();

	private DatabaseHelper() throws Exception
	{
		client = new MongoClient("localhost", 27017);
		database = client.getDB("recipes");
		if (database == null)
		{
			throw new Exception("Db does not exist");
		}
		collections.put("recipes", database.getCollection("recipes"));
		collections.put("users", database.getCollection("users"));
	}

	public static DatabaseHelper getDatabaseHelper()
	{
		if (dbHelper == null)
		{
			try
			{
				dbHelper = new DatabaseHelper();
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return dbHelper;
	}

	private void createDb()
	{

	}

	public List<DBObject> getRecipes()
	{
		BasicDBObject querie = new BasicDBObject();

		DBCursor cursor = collections.get("recipes").find(querie);

		return cursor.toArray();
	}

	private HashMap<String, Object> cursorToHashMap(BasicDBObject o)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		for (String s : o.keySet())
		{

		}

		return o;
	}
}