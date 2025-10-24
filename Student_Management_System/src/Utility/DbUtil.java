package Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtil {
	public static Connection getConnection() throws SQLException
	{
		try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/","root","manager"))
		{
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS student_db;");
		}catch(Exception e) {}
		
		Connection dbConn = DriverManager.getConnection("jdbc:mysql://localhost/student_db","root","root");
		
		try(Statement stmt = dbConn.createStatement())
		{
			String createTable="CREATE TABLE IF NOT EXISTS Student("
					+ "name VARCHAR(50),"
					+ "roll INT PRIMARY KEY,"
					+ "grade CHAR(3),"
					+ "email VARCHAR(50) UNIQUE);";
			stmt.executeUpdate(createTable);
			
			return dbConn;
		}
	}
}
