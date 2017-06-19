package squawk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SquawkDB {
	private final String url = "jdbc:sqlite:SquawkDB.db";
	private Connection conn = null;
	//constructor instance of SquawkDB
	public SquawkDB() throws SQLException {
		this.connect();
	}

	public void connect() {
		try {
			// create a connection to the database
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to Squawk has been established.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void close() throws SQLException {
		this.conn.close();
	}

	public void createNewTable() {
        // SQLite connection string passed in using String URL
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	UserName TEXT NOT NULL UNIQUE,\n"
                + " UserID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
                + " password TEXT NOT NULL,\n"
                + " email TEXT NOT NULL,\n"
                + " MemberSince TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                + ");";
        
        String sql1 = "CREATE TABLE IF NOT EXISTS SquawkMsg(\n"
        		+ " msgID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
        		+ " userID INTEGER NOT NULL,\n"
        		+ " Msg	TEXT NOT NULL,\n"
        		+ " FOREIGN KEY(userID) REFERENCES users (userID),\n"
        		+ " MsgDT INTEGER NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
        		+ ");";
        
        String sql2 = "CREATE TABLE IF NOT EXISTS SquawkFollow(\n"
        		+ " FollowingMeID INTEGER NOT NULL,\n"
        		+ " MeFollowingYouID INTEGER NOT NULL,\n"
        		+ " rowID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
        		+ " FollowDT TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
        		+ ");";
        		
        try (Statement stmt = conn.createStatement()) {
            // create new tables
            stmt.execute(sql);
            stmt.execute(sql1);
            stmt.execute(sql2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

	public void writeSQL(String SQLstmt) {
		String sql = SQLstmt;

		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getInt("id") + "\t" + rs.getString("name")
						+ "\t" + rs.getDouble("capacity"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void insertUser(String UserName, String Password, String email) {
		String sql = "INSERT INTO users(UserName, password, email) VALUES(?,?,?)";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, UserName);
			pstmt.setString(2, Password);
			pstmt.setString(3, email);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void insertSquawk(int userID, String Msg) {
		String sql = "INSERT INTO SquawkMsg (userID, Msg) VALUES(?,?)";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userID);
			pstmt.setString(2, Msg);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	

	public boolean checkUserExists(String userName) throws SQLException {
		String sql = "SELECT UserName FROM users WHERE UserName = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setString(1, userName);
			try (ResultSet rs = stmt.executeQuery();) {
				if (rs.next()) {
					return false;
				} else {
					return true;
				}
			}
		}
	}
	public SquawkUser authenticateUsers(String userName, String password) throws SQLException {
		String sql = "SELECT UserName FROM users WHERE UserName = ? AND password = ?";
		String sql2 ="SELECT * FROM users WHERE UserName = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql); 
				PreparedStatement stmt2 = conn.prepareStatement(sql2);) {
			stmt.setString(1, userName);
			stmt.setString(2, password);
			stmt2.setString(1, userName);
			try (ResultSet rs = stmt.executeQuery();) {
				if (rs.next()) { 
					ResultSet rsUser = stmt2.executeQuery();
					SquawkUser nSk = new SquawkUser(rsUser.getString("UserName"), rsUser.getString("password"), rsUser.getString("email"), rsUser.getInt("UserID"));
					return nSk;
				} else {
					return null;
				}
			}
		}
	}
	
	

	public void timeLineSquawks(String userID) {
		String sql = "SELECT Msg, MsgDT FROM SquawkMsg WHERE UserID in (SELECT TargetID FROM SquawkFollow WHERE UserID="  + userID +");"; 

		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getString("Msg") + "\t" + rs.getString("MsgDT"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
//	 SELECT EMP_ID, NAME, DEPT FROM COMPANY INNER JOIN DEPARTMENT
//	   ON COMPANY.ID = DEPARTMENT.EMP_ID;
	
//	CREATE TABLE `SquawkMsg` (
//			`msgID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
//			`userID`	INTEGER NOT NULL,
//			`Msg`	TEXT NOT NULL,
//			`MsgDT`	TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
//			FOREIGN KEY(`userID`) REFERENCES `users`(`userID`)
//		);
	
	public void renderMySquawks(String userID) {
		String sql = "SELECT Msg, MsgDT FROM SquawkMsg INNER JOIN SquawkFollow ON users.userID = SquawkFollow.userID;";

		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getInt("Msg") + "\t" + rs.getString("MsgDT"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	// Takes parameter when click on page object or user
	public void otherSquawks(String SQLstmt) {
		String sql = SQLstmt;

		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getInt("id") + "\t" + rs.getString("name")
						+ "\t" + rs.getDouble("capacity"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	

}
