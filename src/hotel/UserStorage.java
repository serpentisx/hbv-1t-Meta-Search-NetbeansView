package hotel;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UserStorage
{
	private ArrayList<User> users;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
	private LocalDate creditCardExpiry;
	private int[] mmyy = new int[2];
	
	
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
	
	public ArrayList<User> selectAll()
	{
		users.clear();
		
		String sqlAllUsers = "SELECT * FROM Users";
		try 
		(Connection conn = this.connect();
		Statement stmtUsers = conn.createStatement(); 
		ResultSet rsUsers = stmtUsers.executeQuery(sqlAllUsers);)
		{
			while(rsUsers.next())
			{
				User u = new User();
				u.setUserID(rsUsers.getInt("userID"));
				u.setAuthorizationLevel(rsUsers.getInt("authorizationLevel"));
				u.setUsername(rsUsers.getString("username"));
				u.setPassword(rsUsers.getString("password"));
				u.setFullName(rsUsers.getString("fullName"));
				u.setAddress(rsUsers.getString("address"));
				u.setPhoneNumber(rsUsers.getInt("phoneNumber"));
				u.setEmail(rsUsers.getString("email"));
				u.setHasSavedCard(rsUsers.getBoolean("hasSavedCard"));
				if(u.getHasSavedCard())
				{
					u.setCardNumber(rsUsers.getInt("cardNumber"));
					u.setCardHolderName(rsUsers.getString("cardHolderName"));
					u.setCardCVC(rsUsers.getInt("cardCVC"));
					creditCardExpiry = LocalDate.parse((rsUsers.getString("cardExpiryDate")), formatter);
					mmyy[0] = creditCardExpiry.getMonthValue();
					mmyy[1] = creditCardExpiry.getYear();
					u.setCardExpiryDate(mmyy);
				}
				else
				{
					u.setCardNumber(0);
					u.setCardHolderName("");
					u.setCardCVC(0);
					int[] noDate = {00,0000};
					u.setCardExpiryDate(noDate);
				}
			users.add(u);	
			} //while rsUsers.next()
			
		} //try
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
      }
		return users;
	} //selectAll()

	
	
}
