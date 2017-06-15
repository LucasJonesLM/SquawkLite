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

	public void createNewTable(String createDDL) {

		// SQL statement for creating a new table
		String sql = createDDL;

		try (Statement stmt = conn.createStatement()) {
			// create a new table
			stmt.execute(sql);
			System.out.println("Table created.");
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

	public boolean checkUserExists(String userName) throws SQLException {
		String sql = "SELECT UserName FROM users WHERE UserName = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setString(1, userName);
			try (ResultSet rs = stmt.executeQuery();) {
				// loop through the result set
				if (rs.next()) {
					//System.out.println(
//							rs.getInt("id") + "\t" + rs.getString("name") + "\t"
//									+ rs.getDouble("capacity"));
					return false;
				} else {
					return true;
				}
			}
		}
	}

}
