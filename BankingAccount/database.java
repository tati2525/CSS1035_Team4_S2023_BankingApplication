package BankingAccount;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *Class implementing the connection with the database and it's used functionalities
 *The Database contains a table named Userpass, in which different information are specified:
 *-UserID, unique for each user
 *-Balance
 *-Account type (e.g. checking or savings)
 *-Withdrawals available if savings type
 *-overdraft counter if checking type
 *-hash of the password inserted by the user, used into the login procedure   
 */
public class database {
	
	private static String connectionUrl =
            "jdbc:sqlserver://sjubank.database.windows.net:1433;"
                    + "database=SJUbank;"
                    + "user=sjubank@sjubank;"
                    + "password=vERY@iNSECURE@pASSWORD;"
                    + "encrypt=true;"
                    + "trustServerCertificate=false;"
                    + "loginTimeout=30;";
	
	/**
	 *  function used to compute the MD5 hash over the password. This is then used to check into login function
	 *  with the data contained into the database
	 */
	public static String MD5hash(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
			String x = password;
			byte[] bytesOfMessage = x.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(bytesOfMessage);
			BigInteger no = new BigInteger(1, messageDigest);
			 String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
	            hashtext = "0" + hashtext;
	        }
		return hashtext;     
		}
	
	public static String selectData(String Username) throws SQLException {
		String result = null;
	 	ResultSet resultSet = null;
	    
	        try (Connection connection = DriverManager.getConnection(connectionUrl);
	                Statement statement = connection.createStatement();) {
	
	            // Create and execute a SELECT SQL statement.
	            String selectSql = "SELECT Password FROM Userpass WHERE Username = ?";
	            PreparedStatement stmt = connection.prepareStatement(selectSql);
	            stmt.setString(1, Username);
	            resultSet = stmt.executeQuery();
	             resultSet.next();
	            result = resultSet.getString(1);
	            
	        }
	        catch (SQLException e) {
	            System.out.println("Cannot find a result");
	            return "";
	        }
			return result;
	}
	
	/**
	 * This function is used to perform the login, checking the data inserted by the user with 
	 * the one contained into the database. The MD5Hash is used to perform this computation. 
	 */
	public static int login(String Username, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, SQLException {
		String Uname = selectData(Username);
		String Pwd = MD5hash(password);
		if(!Uname.isEmpty() && Uname.contains(Pwd)) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	/**
	 * This method is used to retrieve the UserID of the user whose username is being specified;
	 */
	public static int checkUID(String Username) throws SQLException {
		int result = 0;
	 	ResultSet resultSet = null;
	    
	        try (Connection connection = DriverManager.getConnection(connectionUrl);
	                Statement statement = connection.createStatement();) {

	            // Create and execute a SELECT SQL statement.
	            String selectSql = "SELECT UserID FROM Userpass WHERE Username = ?";
	            PreparedStatement stmt = connection.prepareStatement(selectSql);
	            stmt.setString(1, Username);
	            resultSet = stmt.executeQuery();
	             resultSet.next();
	            result = resultSet.getInt(1);
	            
	        }
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
			return result;
	}
	
	/**
	 * This method is used to check which kind of account type is contained into the db
	 * associated with the user specified as parameter
	 */
	public static String checkAccountType(String Username) throws SQLException {
		String result = null;
	 	ResultSet resultSet = null;
	    
	        try (Connection connection = DriverManager.getConnection(connectionUrl);
	                Statement statement = connection.createStatement();) {

	            // Create and execute a SELECT SQL statement.
	            String selectSql = "SELECT Accounttype FROM Userpass WHERE Username = ?";
	            PreparedStatement stmt = connection.prepareStatement(selectSql);
	            stmt.setString(1, Username);
	            resultSet = stmt.executeQuery();
	             resultSet.next();
	            result = resultSet.getString(1);
	            
	        }
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
			return result;
	}
	
	/**
	 * Method used to retrieve the balance of the user whose username is specified as parameter
	 */
	public static int checkBalance(String Username) throws SQLException {
		int result = 0;
	 	ResultSet resultSet = null;
	    
	        try (Connection connection = DriverManager.getConnection(connectionUrl);
	                Statement statement = connection.createStatement();) {

	            // Create and execute a SELECT SQL statement.
	            String selectSql = "SELECT Balance FROM Userpass WHERE Username = ?";
	            PreparedStatement stmt = connection.prepareStatement(selectSql);
	            stmt.setString(1, Username);
	            resultSet = stmt.executeQuery();
	             resultSet.next();
	            result = resultSet.getInt(1);
	            // Print results from select statement
	            
	        }
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
			return result;
	}
	
	/**
	 * This method is used to update the balance column of the specified user into the database
	 */
	public static void updateSQLBalance(String Username,double balance) throws SQLException {
		int result = 0;
	 	ResultSet resultSet = null;
	    
	        try (Connection connection = DriverManager.getConnection(connectionUrl);
	                Statement statement = connection.createStatement();) {

	            // Create and execute a SELECT SQL statement.
	            String selectSql = "UPDATE Userpass SET Balance = "+ balance + " WHERE Username = ?";
	            PreparedStatement stmt = connection.prepareStatement(selectSql);
	            stmt.setString(1, Username);
	            stmt.execute();
	        }
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
}
