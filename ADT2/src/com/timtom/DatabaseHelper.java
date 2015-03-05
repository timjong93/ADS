package com.timtom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.AggregationOutput;
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

	public List<DBObject> getAllRecipes()
	{
		BasicDBObject querie = new BasicDBObject();

		DBCursor cursor = collections.get("recipes").find(querie);

		return cursor.toArray();
	}

	public List<DBObject> FindRecipeById(String id)
	{
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));

		DBCursor cursor = collections.get("recipes").find(query);

		return cursor.toArray();
	}

	public List<DBObject> getRecipesByIngredientsOR(ArrayList<String> Ingredients)
	{
		BasicDBList ingredientDocs = new BasicDBList();
		ingredientDocs.addAll(Ingredients);
		DBObject inClause = new BasicDBObject("$in", ingredientDocs);
		DBObject query = new BasicDBObject("Ingredients.Ingredient", inClause);
		DBCursor cursor = collections.get("recipes").find(query);
		System.out.println(query);

		return cursor.toArray();
	}

	public List<DBObject> getRecipesByIngredientsAND(ArrayList<String> Ingredients)
	{
		BasicDBList ingredientDocs = new BasicDBList();
		ingredientDocs.addAll(Ingredients);
		DBObject inClause = new BasicDBObject("$all", ingredientDocs);
		DBObject query = new BasicDBObject("Ingredients.Ingredient", inClause);
		DBCursor cursor = collections.get("recipes").find(query);
		return cursor.toArray();
	}

	public List<DBObject> getTop5Recipes()
	{
		// db.recipes.aggregate([{$unwind:"$Ratings"},{$group:{_id:"$_id",
		// avg_ratings:{$avg:"$Ratings"}}},{$sort:{"avg_ratings":-1}},{$limit:5}])

		// create our pipeline operations, first with the $unwind
		DBObject unwind = new BasicDBObject("$unwind", "$Ratings");

		// build the $group operation
		DBObject groupFields = new BasicDBObject("_id", "$_id");
		DBObject average = new BasicDBObject("$avg", "$Ratings");
		groupFields.put("avg_ratings", average);
		DBObject group = new BasicDBObject("$group", groupFields);

		// build the $sort operation
		DBObject sortFields = new BasicDBObject("avg_ratings", -1);
		DBObject sort = new BasicDBObject("$sort", sortFields);

		// build the $limit operation
		DBObject limit = new BasicDBObject("$limit", 5);

		List<DBObject> pipeline = Arrays.asList(unwind, group, sort, limit);
		AggregationOutput output = collections.get("recipes").aggregate(pipeline);

		ArrayList<DBObject> result = new ArrayList<DBObject>();

		for (DBObject topCandidate : output.results())
		{

			result.addAll(FindRecipeById(topCandidate.get("_id").toString()));
		}

		return result;

	}

	public void insertInto(String collection, DBObject object)
	{
		collections.get(collection).insert(object);
	}
}

// db.recipes.aggregate([{$unwind:"$Ratings"},{$group:{_id:"$_id",
// avg_ratings:{$avg:"$Ratings"}}},{$sort:{"avg_ratings":-1}},{$limit:5}])
