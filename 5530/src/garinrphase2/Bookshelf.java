package garinrphase2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

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
	
	private static String DBUSER = "cs5530u18";
	private static String DBPASS = "f96qb5pr";
	private static String DBURL = "Jdbc:mysql://georgia.eng.utah.edu/cs5530db18";
	
	static Scanner in = new Scanner(System.in);
	static int choice = 0;
	static String userSelection = null;
	

	/**
	 * Adds a new user to the library database. Queries the user
	 * for a unique username and id, full name, address, phone number, and email address
	 */
	public static void AddUser()
	{
		
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
			if(!Main.IsInteger(userSelection))
			{
				System.out.print(userSelection + " is not a number, ");
				System.out.print("Please enter [1] to return to main menu or [2] to exit: ");
			}
			if(Main.IsInteger(userSelection))
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
			Main.MainMenu();
		else
			Main.ExitProgram();
		
	}//end of AddUser

	/**
	 * Sends a sql statement to the specified database
	 * @param query
	 * @return
	 */
	public static ResultSet SendQuery(String query)
	{
		ResultSet rs = null;
		
		return rs;
	}

}
