package com.timtom;

import java.util.Scanner;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class AskUserUtil
{

	public AskUserUtil()
	{
		// TODO Auto-generated constructor stub
	}

	public static Object AskUserFieldsInput(Scanner scan, String objectName)
	{
		if (objectName.equals("string"))
		{
			String value = scan.nextLine();
			return value;
		} else if (objectName.equals("int"))
		{
			int value = scan.nextInt();
			scan.nextLine();
			return value;
		} else
		{
			BasicDBObject result = new BasicDBObject();
			BasicDBList fields = DatabaseHelper.getDatabaseHelper().getFields(objectName);

			for (Object object : fields)
			{
				BasicDBObject field = (BasicDBObject) object;

				String fieldType = field.get("type").toString();

				if (field.get("type").toString().equals("array"))
				{
					System.out.println("give a value for " + field.getString("name") + " (" + fieldType + ")");
					result.append(field.getString("name"), AskUserFieldsInputArray(scan, (DBObject) field.get("arrayType")));
				} else if (field.get("type").toString().equals("ObjectId"))
				{
					result.append(field.getString("name"), new ObjectId());
				} else
				{
					System.out.println("give a value for " + field.getString("name") + " (" + fieldType + ")");
					Object value = AskUserFieldsInput(scan, fieldType);
					result.append(field.getString("name"), value);
				}
			}
			return result;
		}
	}

	public static BasicDBList AskUserFieldsInputArray(Scanner scan, DBObject field)
	{
		BasicDBList result = new BasicDBList();

		if (field.get("type").toString().equals("array"))
		{
			while (true)
			{
				System.out.println("Do you want to at another value?(y/n):");
				if (scan.nextLine().toLowerCase().equals("y"))
				{
					result.add(AskUserFieldsInputArray(scan, (DBObject) field.get("arrayType")));
				} else
				{
					break;
				}
			}
		} else
		{
			while (true)
			{
				System.out.println("Do you want to at another value?(y/n):");
				if (scan.nextLine().toLowerCase().equals("y"))
				{
					result.add(AskUserFieldsInput(scan, field.get("type").toString()));
				} else
				{
					break;
				}
			}
		}

		return result;
	}
}
