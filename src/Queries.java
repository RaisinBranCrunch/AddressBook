import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Queries extends GUI{
	
	private static String username; 
	
	public void insertQuery(String firstName, String lastName, String address_1, String address_2, String city, String state, String zipcode, String phoneNumber, String emailAddress)
	{
		try {

			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

			Connection connection = DriverManager.getConnection("jdbc:odbc:addressbook");

			username = String.valueOf(firstName.charAt(0) + String.valueOf(lastName.charAt(0) + "_" + String.valueOf(phoneNumber.charAt(6) + String.valueOf(phoneNumber.charAt(7) + String.valueOf(phoneNumber.charAt(8) + String.valueOf(phoneNumber.charAt(9)))))));

			PreparedStatement names = connection.prepareStatement("INSERT INTO names (personID, firstName, lastName) VALUES (?, ?, ?)");
			names.setString(1, username);
			names.setString(2, firstName);
			names.setString(3, lastName);
			names.executeUpdate();

			PreparedStatement addresses = connection.prepareStatement("INSERT INTO addresses (personID, [Address 1], [Address 2], City, State, Zipcode) VALUES (?, ?, ?, ?, ?, ?)");
			addresses.setString(1, username);
			addresses.setString(2, address_1);
			addresses.setString(3, address_2);
			addresses.setString(4, city);
			addresses.setString(5, state);
			addresses.setString(6, zipcode);
			addresses.executeUpdate();

			PreparedStatement phones = connection.prepareStatement("INSERT INTO phoneNumbers (personID, phoneNumber) VALUES (?, ?)");
			phones.setString(1, username);
			phones.setString(2, phoneNumber);
			phones.executeUpdate();

			PreparedStatement emails = connection.prepareStatement("INSERT INTO emailAddresses (personID, emailAddress) VALUES (?, ?)");
			emails.setString(1, username);
			emails.setString(2, emailAddress);
			emails.executeUpdate();

			JOptionPane.showMessageDialog(messagePane, "Insertion Completed!");

			connection.close();
		}
		
	      // detect problems interacting with the database
	    catch ( SQLException sqlException ) {
	    	JOptionPane.showMessageDialog( null, sqlException.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE );
	    }
	      // detect problems loading database driver
	    catch ( ClassNotFoundException classNotFound ) {
	    	JOptionPane.showMessageDialog( null, classNotFound.getMessage(), "Driver Not Found", JOptionPane.ERROR_MESSAGE );
	    }
	}
	
	public void updateQuery(String firstName, String lastName, String address_1, String address_2, String city, String state, String zipcode, String phoneNumber, String emailAddress)
	{
		try {

			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			Connection connection = DriverManager.getConnection("jdbc:odbc:addressbook");
			
			String newUsername = String.valueOf(firstName.charAt(0) + String.valueOf(lastName.charAt(0) + "_" + String.valueOf(phoneNumber.charAt(6) + String.valueOf(phoneNumber.charAt(7) + String.valueOf(phoneNumber.charAt(8) + String.valueOf(phoneNumber.charAt(9)))))));

            if (username == null)
                username = newUsername;

			PreparedStatement names = connection.prepareStatement("UPDATE names SET personID = ?, firstName = ?, lastName = ? WHERE personID = ?");
			names.setString(1, newUsername);
			names.setString(2, firstName);
			names.setString(3, lastName);
			names.setString(4, username);
			names.executeUpdate();
			
			PreparedStatement addresses = connection.prepareStatement("UPDATE addresses SET personID = ?, [Address 1] = ?, [Address 2] = ?, City = ?, State = ?, Zipcode = ? WHERE personID = ?");
			addresses.setString(1, newUsername);
			addresses.setString(2, address_1);
			addresses.setString(3, address_2);
			addresses.setString(4, city);
			addresses.setString(5, state);
			addresses.setString(6, zipcode);
			addresses.setString(7, username);
			addresses.executeUpdate();
			
			PreparedStatement phones = connection.prepareStatement("UPDATE phoneNumbers SET personID = ?, phoneNumber = ? WHERE personID = ?");
			phones.setString(1, newUsername);
			phones.setString(2, phoneNumber);
			phones.setString(3, username);
			phones.executeUpdate();
			
			PreparedStatement emails = connection.prepareStatement("UPDATE emailAddresses SET personID = ?, emailAddress = ? WHERE personID = ?");
			emails.setString(1, newUsername);
			emails.setString(2, emailAddress);
			emails.setString(3, username);
			emails.executeUpdate();
			
			username = newUsername;

			JOptionPane.showMessageDialog(messagePane, "Update Completed!");

			connection.close();
		}
		
	      // detect problems interacting with the database
	    catch ( SQLException sqlException ) {
	    	JOptionPane.showMessageDialog( null, sqlException.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE );
	    	System.exit( 1 );
	    }
	      // detect problems loading database driver
	    catch ( ClassNotFoundException classNotFound ) {
	    	JOptionPane.showMessageDialog( null, classNotFound.getMessage(), "Driver Not Found", JOptionPane.ERROR_MESSAGE );
	    	System.exit( 1 );
	    }
	}
	
	public void searchQuery(String firstName, String lastName, String phoneNumber)
	{
		try {
			
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			Connection connection = DriverManager.getConnection("jdbc:odbc:addressbook");
			
	        java.sql.Statement statement = connection.createStatement();
			
			username = String.valueOf(firstName.charAt(0) + String.valueOf(lastName.charAt(0) + "_" + String.valueOf(phoneNumber.charAt(6) + String.valueOf(phoneNumber.charAt(7) + String.valueOf(phoneNumber.charAt(8) + String.valueOf(phoneNumber.charAt(9)))))));

			String fName = null, lName = null, address1 = null, address2 = null, city = null, state = null, zipcode = null, phone = null, email = null;

	        ResultSet resultSet = statement.executeQuery("SELECT names.firstName, names.lastName, addresses.[Address 1], addresses.[Address 2], addresses.City, addresses.State, addresses.Zipcode, phoneNumbers.phoneNumber, emailAddresses.emailAddress FROM (([names] INNER JOIN addresses ON names.personID = addresses.personID) INNER JOIN phoneNumbers ON names.personID = phoneNumbers.personID) INNER JOIN emailAddresses ON names.personID = emailAddresses.personID WHERE (((names.personID)=" + "\'" + username + "\'" + "));");

	         //StringBuffer results = new StringBuffer();
	        ResultSetMetaData metaData = resultSet.getMetaData();
	        
	        int numberOfColumns = metaData.getColumnCount();

	        while ( resultSet.next() ) {

				for (int i = 1; i <= numberOfColumns; i++) {
					switch (i) {
						case 1:
							fName = resultSet.getObject(i).toString();
							break;
						case 2:
							lName = resultSet.getObject(i).toString();
							break;
						case 3:
							address1 = resultSet.getObject(i).toString();
							break;
						case 4:
							address2 = resultSet.getObject(i).toString();
							break;
						case 5:
							city = resultSet.getObject(i).toString();
							break;
						case 6:
							state = resultSet.getObject(i).toString();
							break;
						case 7:
							zipcode = resultSet.getObject(i).toString();
							break;
						case 8:
							phone = resultSet.getObject(i).toString();
							break;
						case 9:
							email = resultSet.getObject(i).toString();
							break;
					}
				}
			}

	         updateFields(fName, lName, address1, address2, city, state, zipcode, phone, email);
	         // close statement and connection
	         statement.close();
	         connection.close();
	      }  // end try

	      // detect problems interacting with the database
	      catch ( SQLException sqlException ) {
	         JOptionPane.showMessageDialog( null, sqlException.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE );
	         System.exit( 1 );
	      }
	      // detect problems loading database driver
	      catch ( ClassNotFoundException classNotFound ) {
	         JOptionPane.showMessageDialog( null, classNotFound.getMessage(), "Driver Not Found", JOptionPane.ERROR_MESSAGE );
	         System.exit( 1 );
	      }
	   }  // end DbConnection constructor definition
        
	public void deleteQuery(String firstName, String lastName, String phoneNumber)
	{
		try {
			
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			Connection connection = DriverManager.getConnection("jdbc:odbc:addressbook");
			
			String username = String.valueOf(firstName.charAt(0) + String.valueOf(lastName.charAt(0) + "_" + String.valueOf(phoneNumber.charAt(6) + String.valueOf(phoneNumber.charAt(7) + String.valueOf(phoneNumber.charAt(8) + String.valueOf(phoneNumber.charAt(9)))))));
			
			PreparedStatement names = connection.prepareStatement("DELETE FROM names WHERE personID = ?");
			names.setString(1, username);
			names.executeUpdate();
			
			PreparedStatement addresses = connection.prepareStatement("DELETE FROM addresses WHERE personID = ?");
			addresses.setString(1, username);
			addresses.executeUpdate();
			
			PreparedStatement phones = connection.prepareStatement("DELETE FROM phoneNumbers WHERE personID = ?");
			phones.setString(1, username);
			phones.executeUpdate();
			
			PreparedStatement emails = connection.prepareStatement("DELETE FROM emailAddresses WHERE personID = ?");
			emails.setString(1, username);
			emails.executeUpdate();

			JOptionPane.showMessageDialog(messagePane, "Successfully Deleted!");
			
			connection.close();
		}
		
	      // detect problems interacting with the database
	    catch ( SQLException sqlException ) {
	    	JOptionPane.showMessageDialog( null,
	    			sqlException.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE );
	    	System.exit( 1 );
	    }
	      // detect problems loading database driver
	    catch ( ClassNotFoundException classNotFound ) {
	    	JOptionPane.showMessageDialog( null,
	    			classNotFound.getMessage(), "Driver Not Found", JOptionPane.ERROR_MESSAGE );
	    	System.exit( 1 );
	    }
	}
}