package garinrphase2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

/**
 * CS 5530 Database Systems
 * Phase 2 Code [Main.java]
 * 
 * This project represents the Phase 2 of the Spring 2015 semester project, the implementation
 * of a database that would manage a small library. 
 * @author Garin Richards
 *
 */


public class Main {

	/* Sooper Sekret Database credentials */
	private static String DBUSER = "cs5530u18";
	private static String DBPASS = "f96qb5pr";
	private static String DBURL = "Jdbc:mysql://georgia.eng.utah.edu/cs5530db18";
	
	
	static Scanner in = new Scanner(System.in);
	static String userSelection = null;
	static int choice = 0;
	
	//represents the current user accessing the library database
	static String currentUser = null;
	
	public static void main(String[] args) {
		
		System.out.println("Welcome to the library!");
		Connection c = null;
		   
		//connect to database
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			c = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
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
		//adding a user to the library
		if(selection == 1)
		{
			Bookshelf.AddUser();
		}
		
		//checking out a book
		else if(selection == 2)
		{
			
		}
		
		//prints out record of a specific user
		else if(selection == 3)
		{
			
		}
		
		//add a new book record to the library database
		else if(selection == 4)
		{
			
		}
		
		//add a new copy of a book to the library database
		else if(selection == 5)
		{
			
		}
		
		//check the late book list at a certain date
		else if(selection == 6)
		{
			
		}
		
		//leave a review for a book
		else if(selection == 7)
		{
			
		}
		
		//browse the library
		else if(selection == 8)
		{
			
		}
		
		//return a book to the library
		else if(selection == 9)
		{
			
		}
		
		//print the book record
		else if(selection == 10)
		{
			
		}
		
		//print library statistics
		else if(selection == 11)
		{
			
		}
		
		//print user statistics
		else if(selection == 13)
		{
			
		}
		else {
			
		}
	}
		
		
	/**
	 * Exits the program
	 */
	public static void ExitProgram()
	{
		System.out.println("Thanks for using the Library, Goodbye!");
		System.exit(0);
	}//end of ExitProgram
	
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
					System.out.print(userSelection + " is an invalid option, ");
					System.out.print("Please make a selection: ");
				}
				
				//if the user did enter a number
				if(IsInteger(userSelection))
				{
					choice = Integer.parseInt((userSelection));
					
					//check to see if it's a valid option
					if(choice < 1 || choice > 13)
					{
						System.out.print(choice + " is an invalid option ");
						System.out.print("please make a selection: ");
						
					}
					
					//case where the user did enter a valid option number
					else
					{
						break;
					}
					
				}
			} while (true);
		}
		catch (Exception e)
		{
			
		}
		
		//send the user to the correct function
		FunctionParse(choice);
		
	}//end of MainMenu function
}//end of class
