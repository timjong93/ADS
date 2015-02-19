package com.timtom;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelper
{
	private static DatabaseHelper dbHelper;
	private Connection conn;

	// TABLES
	private static String artistTable = "artist";
	private static String productTable = "product";
    private static String customerTable = "customer";

	// Artist COLUMNS
	private static String firstNameCol = "firstname";
	private static String lastNameCol = "lastname";
    private static String mailAdressCol = "mailAdress";

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

	public int insertArtist(String firstname, String lastName)
	{
		PreparedStatement statement = null;

		try
		{
			String insertArticle = "INSERT INTO " + artistTable + " (" + firstNameCol + ", " + lastNameCol + ")" + " VALUES (?,?)";

			statement = conn.prepareStatement(insertArticle);

			statement.setString(1, firstname);
			statement.setString(2, lastName);

			int id = statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();

			if (rs.next())
			{
				id = rs.getInt(1);
			}

			rs.close();
			statement.close();
			return id;
		} catch (SQLException e)
		{
			System.err.println("[ERROR] fout in de DB");
			e.printStackTrace();
		} finally
		{
			if (statement != null)
				try
				{
					statement.close();
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
		}
		return -1;
	}

    public int insertCustomer(String firstname, String lastName, String mailAdress)
    {
        PreparedStatement statement = null;

        try
        {
            String insertArticle = "INSERT INTO " + customerTable + " (" + firstNameCol + ", " + lastNameCol + ", " + mailAdressCol +")" + " VALUES (?,?,?)";

            statement = conn.prepareStatement(insertArticle);

            statement.setString(1, firstname);
            statement.setString(2, lastName);
            statement.setString(3, mailAdress);

            int id = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();

            if (rs.next())
            {
                id = rs.getInt(1);
            }

            rs.close();
            statement.close();
            return id;
        } catch (SQLException e)
        {
            System.err.println("[ERROR] fout in de DB");
            e.printStackTrace();
        } finally
        {
            if (statement != null)
                try
                {
                    statement.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
        }
        return -1;
    }

	public int insertAlbum(int artistId, String publisher, String name, Date releaseDate)
	{
		CallableStatement statement = null;

		try
		{
			String procedure = "{call add_album(?,?,?,?)}";

			statement = conn.prepareCall(procedure);

			statement.setInt(1, artistId);
			statement.setString(2, publisher);
			statement.setString(3, name);
			statement.setDate(4, releaseDate);

			statement.execute();

			ResultSet rs = statement.getResultSet();

			int id = -1;

			while (rs.next())
			{
				id = rs.getInt(1);
			}

			rs.close();
			statement.close();
			return id;
		} catch (SQLException e)
		{
			System.err.println("[ERROR] fout in de DB");
			e.printStackTrace();
		} finally
		{
			if (statement != null)
				try
				{
					statement.close();
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
		}
		return -1;
	}
}
