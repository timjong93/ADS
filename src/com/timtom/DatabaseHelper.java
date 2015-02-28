package com.timtom;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class DatabaseHelper
{
	private static DatabaseHelper dbHelper;
	private Connection conn;

	// TABLES
	private static String artistTable = "artist";
	private static String productTable = "product";
	private static String customerTable = "customer";
	private static String trackTable = "track";

	// Artist COLUMNS
	private static String firstNameCol = "firstname";
	private static String lastNameCol = "lastname";
	private static String mailAdressCol = "mailAdress";

	private static String trackNameCol = "name";
	private static String trackDurationCol = "duration";
	private static String trackGenreCol = "genre";

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
			String insertArticle = "INSERT INTO " + artistTable + " (" + firstNameCol + ", " + lastNameCol + ")" + " VALUES (?,?) RETURNING id1";

			statement = conn.prepareStatement(insertArticle);

			statement.setString(1, firstname);
			statement.setString(2, lastName);

            ResultSet rs = statement.executeQuery();
            int id = 0;
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
			String insertArticle = "INSERT INTO " + customerTable + " (" + firstNameCol + ", " + lastNameCol + ", " + mailAdressCol + ")" + " VALUES (?,?,?)";

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

	public int insertTrack(String name, int duration, String genre)
	{
		PreparedStatement statement = null;

		try
		{
			String insertArticle = "INSERT INTO " + trackTable + " (" + trackNameCol + ", " + trackDurationCol + ", " + trackGenreCol + ")" + " VALUES (?,?,?) RETURNING id";

			statement = conn.prepareStatement(insertArticle);

			statement.setString(1, name);
			statement.setInt(2, duration);
			statement.setString(3, genre);

			ResultSet rs = statement.executeQuery();
			int id = 0;
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

	public int insertAlbum(int artistId, String publisher, String name, Date releaseDate, ArrayList<Integer> tracks)
	{
		CallableStatement statement = null;

		try
		{
			String procedure = "{call add_album(?,?,?,?,?)}";

			statement = conn.prepareCall(procedure);

			statement.setInt(1, artistId);
			statement.setString(2, publisher);
			statement.setString(3, name);
			statement.setDate(4, releaseDate);
			Array array = conn.createArrayOf("integer", tracks.toArray());
			statement.setArray(5, array);

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

	public int insertMovie(String publisher, String name, int duration, ArrayList<Integer> artistids)
	{
		CallableStatement statement = null;

		try
		{
			String procedure = "{call add_movie(?,?,?,?)}";

			statement = conn.prepareCall(procedure);

			statement.setString(1, publisher);
			statement.setString(2, name);
			statement.setInt(3, duration);
			Array array = conn.createArrayOf("integer", artistids.toArray());
			statement.setArray(4, array);

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

	public void insertProduct(int movieId, int albumId, int amount)
	{
		CallableStatement statement = null;

		try
		{
			String procedure = "{call add_product(?,?,?)}";

			statement = conn.prepareCall(procedure);

			if (movieId != -1)
				statement.setInt(1, movieId);
			else
				statement.setNull(1, Types.INTEGER);

			if (albumId != -1)
				statement.setInt(2, albumId);
			else
				statement.setNull(2, Types.INTEGER);

			statement.setInt(3, amount);

			statement.execute();

			statement.close();
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
	}
}
