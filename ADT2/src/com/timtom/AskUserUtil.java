package com.timtom;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.timtom.exeception.NoModelFoundException;

public class AskUserUtil
{

	public AskUserUtil()
	{
		// TODO Auto-generated constructor stub
	}

	public static Object AskUserFieldsInput(Scanner scan, String objectName) throws NoModelFoundException
	{
		if (objectName.equals("string"))
		{
			String value = scan.nextLine();
			return value;
		} else if (objectName.equals("int"))
		{
			Integer value = null;
			while (value == null)
			{
				try
				{
					value = scan.nextInt();
					scan.nextLine();
				} catch (InputMismatchException e)
				{
					scan.nextLine();
					System.err.println("Geef een integer!");
				}
			}
			return value;
		} else
		{
			BasicDBObject result = new BasicDBObject();
			BasicDBList fields = DatabaseHelper.getDatabaseHelper().getFields(objectName);

			if (fields == null)
			{
				throw new NoModelFoundException("Object \"" + objectName + "\" not found in collection \"models\"");
			}

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

	public static BasicDBList AskUserFieldsInputArray(Scanner scan, DBObject field) throws NoModelFoundException
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
