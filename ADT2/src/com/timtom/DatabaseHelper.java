package com.timtom;

import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBList;
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
		collections.put("models", database.getCollection("models"));
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

	public BasicDBList getFields(String objectName)
	{
		BasicDBObject querie = new BasicDBObject("name", objectName);

		DBCursor cursor = collections.get("models").find(querie);

		return (BasicDBList) cursor.next().get("fields");
	}

	public List<DBObject> getRecipes()
	{
		BasicDBObject querie = new BasicDBObject();

		DBCursor cursor = collections.get("recipes").find(querie);

		return cursor.toArray();
	}

	public void insertInto(String collection, DBObject object)
	{
		collections.get(collection).insert(object);
	}
}