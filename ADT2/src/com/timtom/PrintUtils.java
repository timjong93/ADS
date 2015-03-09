package com.timtom;

import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class PrintUtils
{

	public PrintUtils()
	{

	}

	public static void printDBObject(DBObject object, int depth)
	{
		for (String s : object.keySet())
		{

			if (object.get(s) instanceof BasicDBList)
			{
				for (int i = 0; i < depth; i++)
				{
					System.out.print("\t");
				}
				System.out.println(String.format("%-16s:", s));
				printDBObjects(((BasicDBList) object.get(s)), depth);
			} else if (object.get(s) instanceof DBObject)
			{
				printDBObject((DBObject) object.get(s), depth + 1);
			} else
			{
				for (int i = 0; i < depth; i++)
				{
					System.out.print("\t");
				}
				System.out.println(String.format("%-16s:%-32s", s, object.get(s)));
			}
		}
	}

	public static void printDBObjects(BasicDBList objects, int depth)
	{
		for (Object object : objects)
		{
			for (int i = 0; i < depth + 1; i++)
			{
				System.out.print("\t");
			}
			System.out.println("-----");

			if (object instanceof BasicDBList)
			{
				printDBObjects(((BasicDBList) object), depth);
			} else if (object instanceof DBObject)
			{
				printDBObject((DBObject) object, depth + 1);
			} else
			{
				for (int i = 0; i < depth + 1; i++)
				{
					System.out.print("\t");
				}
				System.out.println(String.format("%-32s", object));
			}
		}
	}

	public static void printDBObjects(List<DBObject> objects, int depth)
	{
		for (DBObject object : objects)
		{
			for (int i = 0; i < depth; i++)
			{
				System.out.print("\t");
			}
			System.out.println("-----");

			if (object instanceof BasicDBList)
			{
				printDBObjects(((BasicDBList) object), depth);
			} else if (object instanceof DBObject)
			{
				printDBObject((DBObject) object, depth + 1);
			} else
			{
				for (int i = 0; i < depth; i++)
				{
					System.out.print("\t");
				}
				System.out.println(String.format("%-32s", object));
			}
		}

	}
}
