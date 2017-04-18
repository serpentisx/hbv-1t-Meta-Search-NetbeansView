package hotel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ReservationStorage {

	private Reservation res;
	private Hotel hotel;
	
	private Connection connect() 
	{
		String url = "jdbc:sqlite:lib/1H.db";
		Connection conn = null;
		try
		{
			conn = DriverManager.getConnection(url);
	   }
		catch (SQLException e) 
		{
	        System.out.println(e.getMessage());
	   }
		return conn;
	}
	
	
	public int insert(Reservation r)
	{
		long returnedID = 0;
		this.res = r;
		this.hotel = r.getHotel();
		int hotelID = hotel.getHotelID();
		int userID = 1; //Change this when we start saving users
		String start = (res.getStartDate()).toString();
		String end = (res.getEndDate()).toString();
		int guests = r.getNumberOfPeople();
		boolean isPaid = true; //Change this if we start checking whether reservations are prepaid
		String insertString = ("INSERT into Reservations(hotelID, userID, startDate, endDate, numberOfPeople, hasBeenPaid) "
				+"VALUES ("+hotelID+", "+userID+", '"
				+start+"', '"+end+"', "+guests+", "+1+")");
		//System.out.println(insertString);
		
		try 
		{Connection conn = this.connect();
		Statement insertStatement = conn.createStatement(); 
		insertStatement.executeUpdate(insertString);
		/* The following search for our key would be really shaky if we had multiple users
		 * connected simultaneously to our system, but it's necessary because the SQLITE JDBC 
		 * driver explicitly doesn't support "insertStatement.executeUpdate(insertString, Statement.RETURN_GENERATED_KEYS);"
		 * If we were doing this for a system that needed to support multiple simultaneous users 
		 * or concurrent threads, we would simply use another database driver. But we're not, so we won't. */
		//
		ResultSet rs = insertStatement.getGeneratedKeys();
		if (rs != null && rs.next()) {
		    returnedID = rs.getInt("last_insert_rowid()");
		}
		conn.close();}

		catch (SQLException e)
		{
			System.out.println("Error in SQL Selectall()");
			System.out.println(e.getMessage());
      }
		
		return (int)returnedID;
	}
	
	
}
