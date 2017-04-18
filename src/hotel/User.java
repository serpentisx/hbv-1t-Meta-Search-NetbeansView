package hotel;

public class User {
	private int userID;
	private int authorizationLevel;
	private String username;
	private String password;
	private String fullName;
	private String address;
	private int phoneNumber;
	private String email;
	private boolean hasSavedCard;
	private int cardNumber;
	private String cardHolderName;
	private int cardCVC;
	private int[] cardExpiryDate;
	
	public User() {
		setAuthorizationLevel(0);
		setUsername("");
		setPassword("");
	}
	
	public User(int authorizationLevel, String username, String password) {
		this.setAuthorizationLevel(authorizationLevel);
		this.setUsername(username);
		this.setPassword(password);
	}

	public User(User user){
		this.authorizationLevel = user.getAuthorizationLevel();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.fullName = user.getFullName();
		this.address = user.getAddress();
		this.phoneNumber = user.getPhoneNumber();
		this.email = user.getEmail();
		this.hasSavedCard = user.getHasSavedCard();
		this.cardNumber = user.getCardNumber();
		this.cardHolderName = user.getCardHolderName();
		this.cardCVC = user.getCardCVC();
		this.cardExpiryDate = user.getCardExpiryDate();
	}
	
	public int getAuthorizationLevel() {
		return authorizationLevel;
	}

	public void setAuthorizationLevel(int authorizationLevel) {
		this.authorizationLevel = authorizationLevel;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getHasSavedCard() {
		return hasSavedCard;
	}

	public void setHasSavedCard(boolean hasSavedCard) {
		this.hasSavedCard = hasSavedCard;
	}

	public int getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public int getCardCVC() {
		return cardCVC;
	}

	public void setCardCVC(int cardCVC) {
		this.cardCVC = cardCVC;
	}

	public int[] getCardExpiryDate() {
		return cardExpiryDate;
	}

	public void setCardExpiryDate(int[] cardExpiryDate)
	{
		//int[mm,yy]
		this.cardExpiryDate = cardExpiryDate;
	}
	

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	
}
