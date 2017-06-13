package squawk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SquawkDB {
	static String url = "jdbc:sqlite:SquawkDB.db";
	static Connection conn = null;
	
	public static void connect() {
		try {
			// create a connection to the database
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to Squawk has been established.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void createNewTable(String createDDL) {

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
	   public void writeSQL(String SQLstmt){
	        String sql = SQLstmt;
	        
	        try (Statement stmt  = conn.createStatement();
	             ResultSet rs    = stmt.executeQuery(sql)){
	            
	            // loop through the result set
	            while (rs.next()) {
	                System.out.println(rs.getInt("id") +  "\t" + 
	                                   rs.getString("name") + "\t" +
	                                   rs.getDouble("capacity"));
	            }
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	    }
	   public void insert(String name, double capacity) {
	        String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";
	 
	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, name);
	            pstmt.setDouble(2, capacity);
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	    }

	
}
