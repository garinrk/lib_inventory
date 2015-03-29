package garinrphase2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

/**
 * 
 * @author Garin Richards
 *
 */

//TODO: Better parse check for invalid input in user creation?

public class Main {

	static String user = "cs5530u18";
	static String userpass = "f96qb5pr";
	static String userurl = "Jdbc:mysql://georgia.eng.utah.edu/cs5530db18";
	static Scanner in = new Scanner(System.in);
	static String userSelection = null;
	static int choice = 0;
	
	public static void main(String[] args) {
		
		System.out.println("Welcome to the library!");
		Connection c = null;
		
		//connect to database
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			c = DriverManager.getConnection(userurl, user, userpass);
//			System.out.println("Connection established to database");
			Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			/* Construct query */
//			String sql = "select * from course";
//			ResultSet rs = stmt.executeQuery(sql);
//			while(rs.next())
//			{
//				System.out.print("number of students: ");
//				System.out.print(rs.getString("numberofstudents")+" || ");
//			}
//			System.out.println(" ");
//			rs.close();
			
//			String userQuery = "This is a query";
//			PreparedStatement query_statement = c.prepareStatement(userQuery);
//			query_statement.setString(1, "%" + );
			
			
			/* Final method of constructing query */
			
			//show initial main menu
			MainMenu();
			
			
			
			System.out.println("The user selected " + userSelection);
			System.out.print("Enter name of course: ");
			
			String name = in.nextLine();
//			System.out.println("You have selected course: " + name);
			
			String q = "Select * from course";
			PreparedStatement qs = c.prepareStatement(q);
//			qs.setString(0, "%" + name + "%"); //only good if there are ?s in the query 
			ResultSet rs2 = qs.executeQuery();
			System.out.println("Executed Query");
			while(rs2.next())
			{
				System.out.println("Name: " + rs2.getString("cname") + " Number of students: " + rs2.getString("numberofstudents"));
			}
			System.out.println("End of program");
			
			stmt.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Cannot connect to Database Server");
		}
		finally
		{
			if(c != null)
			{
				try
				{
					c.close();
					System.out.println("Database Connection Terminated");
				}
				catch (Exception e)
				{
					/* Do nothing, ignoring close errors */
				}
			}
		}
		
		System.out.println("End of Program");
		

	}//end of main

	/**
	 * Check if the user's input was an integer or not
	 * @param i
	 * @return
	 */
	public static boolean IsInteger(String i)
	{
		try
		{
			Integer.parseInt(i);
		} catch (NumberFormatException e )
		{
			//string is not an integer
			return false;
		}
		//string is integer
		return true;
	}
	
	/**
	 * Depending on the integer passed through to the console,
	 * the correct information will be prompted and stored to make 
	 * a query.
	 * @param selection, representative of user's choice
	 */
	public static void FunctionParse(int selection)
	{
		if(selection == 1)
		{
			AddUser();
		}
	}
	
	/**
	 * Adds a new user to the library database. Queries the user
	 * for a unique username and id, full name, address, phone number, and email address
	 */
	public static void AddUser()
	{
		//TODO: Check for whether number inputs are numbers
		//TODO: Check for if user already exists in table
		
		String newUsername;
		String newID;
		String newFullName;
		String newAddress;
		String newPhoneNumber;
		String newEmail;
		
		System.out.println("Adding a new user: ");
		System.out.print("New Unique Username: ");
		newUsername = in.nextLine();
		
		/* User must enter a number for an ID */
		System.out.print("New ID (Number): ");
		do
		{
			newID = in.nextLine();
			
			if(!IsInteger(newID))
			{
				System.out.print(newID + " is not a number, ");
				System.out.print("Please enter a number for this user's ID: ");
			}
			else
			{
				break;
			}
		} while (true);
		
		System.out.print("New User's Full Name: ");
		newFullName = in.nextLine();
		System.out.print("New User's Home Address: ");
		newAddress = in.nextLine();
		System.out.print("New User's Email Address: ");
		newEmail = in.nextLine();
		System.out.print("New User's Phone Number: ");
		
		/* User must enter a number for a phone number */
		do
		{
			newPhoneNumber = in.nextLine();
			
			if(!IsInteger(newID))
			{
				System.out.print(newPhoneNumber + " is not a number, ");
				System.out.print("Please enter a number for this user's Phone Number: ");
			}
			else
			{
				break;
			}
		} while (true);
		
		System.out.println("New User Info: ");
		System.out.println("Full Name: " + newFullName);
		System.out.println("Username: " + newUsername);
		System.out.println("ID: " + newID);
		System.out.println("Address: " + newAddress);
		System.out.println("Email: " + newEmail);
		System.out.println("Phone Number: " + newPhoneNumber);	
		
		//TODO: Construct query from provided information, send to server
		//TODO: If successful, return success and throw user back to main menu
		
		System.out.print("[1] to exit or [2] to return to main menu");
		
		do
		{
			userSelection = in.nextLine();
			if(!IsInteger(userSelection))
			{
				System.out.print(userSelection + " is not a number, ");
				System.out.print("Please enter [1] to return to main menu or [2] to exit: ");
			}
			if(IsInteger(userSelection))
			{
				choice = Integer.parseInt(userSelection);
				if(choice != 1 && choice != 2)
				{
					System.out.print(choice + " is not a valid option, ");
					System.out.print("Please enter [1] to return to main menu or [2] to exit: ");
				}
				
				else
				{
					break;
				}
			}
		}
		while (true);
		
		if(choice == 1)
			MainMenu();
		else
			ExitProgram();
		
	}//end of AddUser
	
	public static void ExitProgram()
	{
		System.out.println("Thanks for using the Library, Goodbye!");
		System.exit(0);
	}
	public static boolean CheckInput(String input)
	{
		return false;
	}
	
	/**
	 * Represents the main menu of the application
	 */
	public static void MainMenu()
	{

		System.out.println("====================================================");
		System.out.println("What would you like to do?");
		System.out.println();
		System.out.println("Add a new user [1]");
		System.out.println("Check out a book [2]");
		System.out.println("Print out the record of a specific user [3]");
		System.out.println("Add a new book record to the library database [4]");
		System.out.println("Add a new copy of a book to the library database [5]");
		System.out.println("Check the late book list at a certain date [6]");
		System.out.println("Leave a review for a book [7]");
		System.out.println("Browse the Library [8]");
		System.out.println("Return a book [9]");
		System.out.println("Print a book's record [10]");
		System.out.println("Print Library statistics[11]");
		System.out.println("Print User Statistics [12]");
		System.out.println("Exit Program: [13]");
		System.out.println();
		System.out.print("Please make a selection: ");

		
		try
		{
			do
			{
				userSelection = in.nextLine();
				
				if(!IsInteger(userSelection))
				{
					System.out.println(userSelection + " is an invalid option");
					System.out.print("Please make a selection: ");
				}
				else
				{
					break;
				}
			} while (true);
		}
		catch (Exception e)
		{
			
		}
		
		choice = Integer.parseInt(userSelection);
		FunctionParse(choice);
		
	}
}//end of class
