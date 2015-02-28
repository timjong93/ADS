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
	private static String publisherTable = "publisher";

	// Artist COLUMNS
	private static String firstNameCol = "firstname";
	private static String lastNameCol = "lastname";
	private static String mailAdressCol = "mailAdress";

	private static String trackNameCol = "name";
	private static String trackDurationCol = "duration";
	private static String trackGenreCol = "genre";

	private static String publisherNameCol = "name";

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

	public ArrayList<String> findAlbumByTrack(String trackName)
	{
		PreparedStatement statement = null;

		try
		{
			String query = "SELECT album.name FROM track INNER JOIN album_tracks ON track.id=album_tracks.id_track INNER JOIN album on album_tracks.id_album = album.id WHERE track.name = ?";

			statement = conn.prepareStatement(query);

			statement.setString(1, trackName);

			ResultSet rs = statement.executeQuery();
			ArrayList<String> result = new ArrayList<String>();
			if (rs.next())
			{
				result.add(rs.getString(1));
			}

			rs.close();
			statement.close();
			return result;
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
		return null;
	}

	public ArrayList<String> findMovieByArtist(String firstName, String lastName)
	{
		PreparedStatement statement = null;

		try
		{
			String query = "SELECT movie.name FROM artist INNER JOIN movie_artists ON artist.id=movie_artists.id_artist INNER JOIN movie on movie_artists.id_movie = movie.id where artist.firstname = ? AND artist.lastname = ?";

			statement = conn.prepareStatement(query);

			statement.setString(1, firstName);
			statement.setString(2, lastName);

			ResultSet rs = statement.executeQuery();
			ArrayList<String> result = new ArrayList<String>();
			if (rs.next())
			{
				result.add(rs.getString(1));
			}

			rs.close();
			statement.close();
			return result;
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
		return null;
	}

	public int insertArtist(String firstname, String lastName)
	{
		PreparedStatement statement = null;

		try
		{
			String querie = "INSERT INTO " + artistTable + " (" + firstNameCol + ", " + lastNameCol + ")" + " VALUES (?,?) RETURNING id1";

			statement = conn.prepareStatement(querie);

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

	public void insertPublisher(String name)
	{
		PreparedStatement statement = null;

		try
		{
			String querie = "INSERT INTO " + publisherTable + " (" + publisherNameCol + ")" + " VALUES (?)";

			statement = conn.prepareStatement(querie);

			statement.setString(1, name);

			statement.executeUpdate();

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

	public int insertCustomer(String firstname, String lastName, String mailAdress)
	{
		PreparedStatement statement = null;

		try
		{
			String querie = "INSERT INTO " + customerTable + " (" + firstNameCol + ", " + lastNameCol + ", " + mailAdressCol + ")" + " VALUES (?,?,?) RETURNING id";

			statement = conn.prepareStatement(querie);

			statement.setString(1, firstname);
			statement.setString(2, lastName);
			statement.setString(3, mailAdress);

			ResultSet rs = statement.executeQuery();
			int id = -1;

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

	public int insertTrack(String name, int duration, String genre, ArrayList<Integer> artistids)
	{
		PreparedStatement statement = null;

		try
		{
			String procedure = "{call add_track(?,?,?,?)}";

			statement = conn.prepareCall(procedure);

			statement.setString(1, name);
			statement.setString(2, genre);
			statement.setInt(3, duration);
			Array array = conn.createArrayOf("integer", artistids.toArray());
			statement.setArray(4, array);

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

	public int rentMovie(int movieId, int customerId, Date returnDate)
	{
		CallableStatement statement = null;

		try
		{
			String procedure = "{call rent_movie(?,?,?)}";

			statement = conn.prepareCall(procedure);

			statement.setInt(1, customerId);
			statement.setInt(2, movieId);
			statement.setDate(3, returnDate);

			ResultSet rs = statement.executeQuery();

			int id = -1;

			while (rs.next())
			{
				id = rs.getInt(1);
			}

			rs.close();
			return id;
		} catch (SQLException e)
		{
			System.err.println("[ERROR] fout in de DB");
			System.err.println(e.getMessage());
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

	public int rentAlbum(int albumId, int customerId, Date returnDate)
	{
		CallableStatement statement = null;

		try
		{
			String procedure = "{call rent_album(?,?,?)}";

			statement = conn.prepareCall(procedure);

			statement.setInt(1, customerId);
			statement.setInt(2, albumId);
			statement.setDate(3, returnDate);

			ResultSet rs = statement.executeQuery();

			int id = -1;

			while (rs.next())
			{
				id = rs.getInt(1);
			}

			rs.close();
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

	public boolean returnProduct(int productId)
	{
		PreparedStatement statement = null;

		try
		{
			String updateProduct = "UPDATE " + productTable + " SET id_customer=?,expected_return=? WHERE id=?";

			statement = conn.prepareStatement(updateProduct);

			statement.setNull(1, Types.INTEGER);
			statement.setNull(2, Types.DATE);
			statement.setInt(3, productId);

			statement.executeUpdate();
			return true;
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
		return false;
	}

	public void productReservation(int movieId, int albumId, int customerId)
	{
		CallableStatement statement = null;

		try
		{
			String procedure = "INSERT INTO reservation(id_customer,id_movie, id_album) values (?,?,?)";

			statement = conn.prepareCall(procedure);

			statement.setInt(1, customerId);

			if (movieId != -1)
				statement.setInt(2, movieId);
			else
				statement.setNull(2, Types.INTEGER);

			if (albumId != -1)
				statement.setInt(3, albumId);
			else
				statement.setNull(3, Types.INTEGER);

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
