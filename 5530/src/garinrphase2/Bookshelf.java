package garinrphase2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.text.*;
/**
 * CS 5530 Database Systems
 * Phase 2 Code [Bookshelf.java]
 * 
 * Part of the Spring 2015 semester long project. Bookshelf contains
 * much of the functionality needed for the inventory to correctly operate.
 * 
 * @author Garin Richards, u0738045
 *
 */
public class Bookshelf {
	
	static boolean verbose = true;
	
//	private static String DBUSER = "cs5530u18";
//	private static String DBPASS = "f96qb5pr";
//	private static String DBURL = "Jdbc:mysql://georgia.eng.utah.edu/cs5530db18";
//	
	/* Used for user input and input parsing */
	static Scanner in = new Scanner(System.in);
	static int choice = 0;
	static String userSelection = null;
	
	/* Credentials belonging to newly created username */
	public static String newUsername;
	static String newID;
	static String newFullName;
	static String newAddress;
	static String newPhoneNumber;
	static String newEmail;
	
	/* Represents the currently logged in user using the library */
	static String loggedInUser = null;
	
	/* Used for calculating dates */
	static Calendar c = Calendar.getInstance();
	

	/**
	 * Adds a new user to the library database. Queries the user
	 * for a unique username and id, full name, address, phone number, and email address
	 */
	public static void AddUser()
	{
		
		//TODO: Check for if user already exists in table
		

		
		System.out.println("Adding a new user: ");
		System.out.print("New Unique Username: ");
		newUsername = in.nextLine();
		
		/* User must enter a number for an ID */
		System.out.print("New ID (Number): ");
		do
		{
			newID = in.nextLine();
			
			if(!Main.IsInteger(newID))
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
			
			if(!Main.IsInteger(newID))
			{
				System.out.print(newPhoneNumber + " is not a number, ");
				System.out.print("Please enter a number for this user's Phone Number: ");
			}
			else
			{
				break;
			}
		} while (true);
		System.out.println();
		
		if(verbose)
		{
			System.out.println("New User Info: ");
			System.out.println("Full Name: " + newFullName);
			System.out.println("Username: " + newUsername);
			System.out.println("ID: " + newID);
			System.out.println("Address: " + newAddress);
			System.out.println("Email: " + newEmail);
			System.out.println("Phone Number: " + newPhoneNumber);	
		}
		
		//TODO: Construct query from provided information, send to server
		//TODO: If successful, return success and throw user back to main menu
		
//		System.out.print("[1] to exit or [2] to return to main menu");
//		
//		do
//		{
//			userSelection = in.nextLine();
//			if(!Main.IsInteger(userSelection))
//			{
//				System.out.print(userSelection + " is not a number, ");
//				System.out.print("Please enter [1] to return to main menu or [2] to exit: ");
//			}
//			if(Main.IsInteger(userSelection))
//			{
//				choice = Integer.parseInt(userSelection);
//				if(choice != 1 && choice != 2)
//				{
//					System.out.print(choice + " is not a valid option, ");
//					System.out.print("Please enter [1] to return to main menu or [2] to exit: ");
//				}
//				
//				else
//				{
//					break;
//				}
//			}
//		}
//		while (true);
		if(loggedInUser == null)
			return;
		else
			Main.MainMenu();
//		if(choice == 1)
//			Main.MainMenu();
//		else
//			Main.ExitProgram();
		
	}//end of AddUser
	
	public static void Login(String username)
	{
		loggedInUser = username;
	}

	/**
	 * Sends a sql statement to the database on the given connection
	 * @param query, string composed of constructed sql query
	 * @param sqlConnection, connection object currently connected to the database defined in main
	 * @return a result set containing results from said query
	 */
	public static ResultSet SendQuery(String query, Connection sqlConnection)
	{
		ResultSet rs = null;
		
		return rs;
	}
	
	/**
	 * Checkout a book from the library
	 * NOTE: MONTH IS ZERO BASED
	 * ISBNs are 13 digits long
	 * Compute the due date to be 30 days from today
	 */
	public static void CheckoutBook(Date today)
	{
		int ISBN;
		System.out.print("Please enter the 13 digit ISBN of the book you wish to check out: ");
		do{
			userSelection = in.nextLine();
			
			if(!Main.IsInteger(userSelection) || userSelection.length() != 13)
			{
				System.out.print("Not a valid ISBN, please try again: ");
			}
			else
			{
				ISBN = Integer.parseInt(userSelection);
				break;
			}
			
		}while(true);
		
		if(verbose)
		{
			System.out.println("The ISBN to be checked out is: " + ISBN);
		}
		
		//compute due date to be 30 days from today
		c.setTime(today);
		if(verbose)
		{
			System.out.println("It is currently " + c.get(Calendar.MONTH) + " the " + c.get(Calendar.DAY_OF_MONTH));
		}

		c.add(Calendar.DATE, 30);
		
		//save duedate
		int futureMonth = c.get(Calendar.MONTH) + 1;
		int futureDay = c.get(Calendar.DAY_OF_MONTH);
		int futureYear = c.get(Calendar.YEAR);
		
//		System.out.println(c.getTime());
		if(verbose)
		{
			System.out.println("30 days from now it will be " + c.get(Calendar.MONTH) + " the " + c.get(Calendar.DAY_OF_MONTH));
		}
		
		//TODO: Construct proper date object for sql query
		
		//TODO: Check if there is a waitlist for the book
		
		//TODO: The user can only check out the book when they have been on said walist the longest.
		
		
		
		
	}

}
