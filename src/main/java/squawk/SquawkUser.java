package squawk;

public class SquawkUser {
	String userName;
	String password;
	String email;
	
	public SquawkUser(String userName, String password, String email) {
		super();
		this.userName = userName;
		this.password = password;
		this.email = email;
	}

	@Override
	public String toString() {
		return "SquawkUser [userName=" + userName + ", password=" + password + ", email=" + email + "]";
	}

	
}
