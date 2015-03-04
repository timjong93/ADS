package com.timtom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper
{
	private static DatabaseHelper dbHelper;
	private Connection conn;

	private DatabaseHelper() throws SQLException
	{
		try
		{
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ADT", "ADT", "ADT");
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static DatabaseHelper getDatabaseHelper()
	{
		if (dbHelper == null)
		{
			try
			{
				dbHelper = new DatabaseHelper();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		return dbHelper;
	}
}