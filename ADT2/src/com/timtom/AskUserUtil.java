package com.timtom;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class AskUserUtil
{

	public AskUserUtil()
	{
		// TODO Auto-generated constructor stub
	}

	public static DBObject AskUserFieldsInput(String objectName)
	{
		BasicDBObject result = new BasicDBObject();
		BasicDBList fields = DatabaseHelper.getDatabaseHelper().getFields(objectName);

		for (Object object : fields)
		{
			BasicDBObject field = (BasicDBObject) object;

			if (field.get("type").toString().equals("array"))
			{
				result.append(field.getString("name"), AskUserFieldsInputArray(field.get("arrayType").toString()));
			} else
			{
				// ask
				System.out.println("give a value for " + field.getString("name"));
			}
		}

		return result;
	}

	public static BasicDBList AskUserFieldsInputArray(String objectnaam)
	{
		BasicDBList result = new BasicDBList();

		while (true)
		{

			break;
		}

		return result;
	}

}
