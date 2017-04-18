package hotel;
import java.util.*;

public class UserManager {
	private ArrayList<User> users = new ArrayList<User>();
	private User currentUser;
	private UserStorage allUsers = new UserStorage();
	
	public User getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(User currentUser) {
		this.currentUser = new User(currentUser);
	}
	
	private void loadAllUsers()
	{
		this.users = (ArrayList<User>)(allUsers.selectAll());
	}
	
	
	
}
