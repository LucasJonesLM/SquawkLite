package squawk;

public class SquawkUser {
	String userName;
	String password;
	String email;
	int userID;
	public SquawkUser(String userName, String password, String email, int userID) {
		super();
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.userID = userID;
	}
	@Override
	public String toString() {
		return "SquawkUser [userName=" + userName + ", password=" + password + ", email=" + email + ", userID=" + userID
				+ "]";
	}
	
	

	
}
