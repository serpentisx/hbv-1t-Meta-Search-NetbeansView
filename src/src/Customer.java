package src;

public class Customer {
	
	private String name;
	private String phone;
	private String email;
	private String address;
	
	/* Constructor */
	public Customer(String name, String phone, String email, String address) {
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.address = address;
	}
	
	/* Instance methods */
	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}
}
