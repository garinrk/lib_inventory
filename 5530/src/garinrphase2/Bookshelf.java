package garinrphase2;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;
import java.lang.*;
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
	
	static boolean verbose = false;
	
	//TODO: Check ISBN Legality, ISBNs are Strings. Too large for ints.
	
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
		System.out.println();
		System.out.print("New Unique Username: ");
		newUsername = in.nextLine();
		
		//TODO: This should return whether or not said username already exists in table
		while(CheckForUser(newUsername))
		{
			System.out.print(newUsername + " already exists, please choose a new username: ");
			newUsername = in.nextLine();
		}
		
		/* User must enter a number for an ID */
		System.out.print("New ID (Number): ");
		do
		{
			newID = in.nextLine();
			
			if(!Main.IsNumber(newID))
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
			
			if(!Main.IsNumber(newID))
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
		
		//if there wasn't a previously set user, as in this is a new user being created upon start
		if(loggedInUser == null)
			return;
		else
			Main.MainMenu();
		
//		System.out.print("[1] to exit or [2] to return to main menu");
//		
//		do
//		{
//			userSelection = in.nextLine();
//			if(!Main.IsNumber(userSelection))
//			{
//				System.out.print(userSelection + " is not a number, ");
//				System.out.print("Please enter [1] to return to main menu or [2] to exit: ");
//			}
//			if(Main.IsNumber(userSelection))
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

//		if(choice == 1)
//			Main.MainMenu();
//		else
//			Main.ExitProgram();
		
	}//end of AddUser
	
	public static void setLoggedInUser(String username)
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
		System.out.println("Checking out a book");
		System.out.println();

		String ISBN;
		System.out.print("Please enter the 13 digit ISBN of the book you wish to check out: ");
		do{
			userSelection = in.nextLine();
			
			if(verbose)
			{
				System.out.println("Length of isbn: " + userSelection.length());
			}
			
			if(/*!Main.IsNumber(userSelection) ||*/ userSelection.length() != 13)
			{
				System.out.print("Not a valid ISBN, please try again: ");
			}
			else
			{
				ISBN = userSelection;
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
		
		//TODO: Increment number of times that book has been checkd out by one
		
		//return to main menu
		Main.MainMenu();
		
	}//end of CheckoutBook

	/**
	 * The following should be printed:
	 * 
	 * Personal Data of User:
	 * 		name
	 * 		username
	 * 		id
	 * 		address
	 * 		phone number
	 * 		email address
	 * Full book history:
	 * 		All books checked out in the past
	 * 			ISBN
	 * 			Title
	 * 			Dates of checkout and return
	 *	Full list of books lost by user
	 *	Full list of books requested for future checkout (User is currently on waitlist for)
	 *	Full list of review they have written for books
	 *		Score
	 *		Text
	 * 		
	 */
	public static void PrintUserRecord()
	{
		String lookedUpUser;
		System.out.println();
		System.out.println("Username to view record of: ");
		lookedUpUser = in.nextLine();
		
		if(!CheckForUser(lookedUpUser))
		{
			System.out.println("User does not exist, returning to main menu");
			System.out.println();
			Main.MainMenu();
		}
		
		else
		{
			if(verbose)
			{
				System.out.println("Printing record for " + lookedUpUser);
			}
			
			//TODO: Holy SQL batman
		}
		
		//return to main menu
		Main.MainMenu();

		
	}
	
	/**
	 * Adds a new record of a book to the library, but not a copy
	 * Prompt user for:
	 * 		ISBN
	 * 		title
	 * 		author
	 * 		publisher
	 * 		year of publication
	 * 		format
	 * 		subject
	 * 		book summary
	 * 
	 * Set the following to null/empty
	 * 		individual book copy location
	 * 		availability
	 */
	public static void AddBookRecord()
	{
		BigInteger newISBN;
		String newTitle;
		String newPublisher;
		String newYearPub;
		String newFormat;
		String newSubject;
		String newSummary;
		
		int numberOfAuthors;
		
		boolean multipleAuthors;
		
		
		System.out.println("Entering new book data...");
		System.out.println();
		
		System.out.println("ISBN: ");
		newISBN = new BigInteger(in.nextLine());
		System.out.print("Title: ");
		newTitle = in.nextLine();
		
		
		System.out.print("How many authors are there for this title? :");
		
		do
		{
			userSelection = in.nextLine();
			
			if(!Main.IsNumber(userSelection))
			{
				System.out.print(userSelection + " is not a number, ");
				System.out.print("Please enter the number of authors: ");
			}
			else
			{
				numberOfAuthors = Integer.parseInt(userSelection);
				break;
			}
		} while (true);
		
		//create an array to hold the names of multiple authors for the title
		String[] authors = new String[numberOfAuthors];
		
		//only one author, grab name and carry on
		if(numberOfAuthors == 1)
		{
			System.out.print("Author: ");
			authors[0] = in.nextLine();
			multipleAuthors = false;
		}
		else
		{			
			//there is more than one author for this title
			multipleAuthors = true;
			
			//add authors to arrays 
			for(int i = 0; i < numberOfAuthors; i++)
			{
				System.out.print("Author #" + i + ": ");
				authors[i] = in.nextLine();
			}
			
			multipleAuthors = true;
		}
		
		
		System.out.print("Publisher: ");
		newPublisher = in.nextLine();
		System.out.print("Year of Publication: ");
		newYearPub = in.nextLine();
		System.out.println("Subject: ");
		newSubject = in.nextLine();
		System.out.println("Format: ");
		newFormat = in.nextLine();
		System.out.println("Book Summary: ");
		newSummary = in.nextLine();
		
		
		/*
		 * Show inputted information back to user to see if they have made a mistake
		 */
		
		System.out.println("Is the following data correct?");
		System.out.println("Title: " + newTitle);
		System.out.println("ISBN: " + newISBN);
		System.out.print("Author(s): ");
		
		for(int i = 0; i < numberOfAuthors; i++)
		{
			if(i == numberOfAuthors - 1)
			{
				System.out.println(authors[i]);
			}
			else
			{
				System.out.print(authors[i]);
			}
		}
		System.out.println("Publisher: " + newPublisher);
		System.out.println("Year of Publication: " + newYearPub);
		System.out.println("Format: " + newFormat);
		System.out.println("Subject: " + newSubject);
		System.out.println("Book Summary: " + newSummary);
		System.out.print("Please answer [1] for yes and [0] for no: ");
		
		try
		{
			do
			{
				userSelection = in.nextLine();
				
				if(!Main.IsNumber(userSelection))
				{
					System.out.print(userSelection + " is an invalid option, ");
					System.out.print("Please make a selection: ");
				}
				
				//if the user did enter a number
				if(Main.IsNumber(userSelection))
				{
					choice = Integer.parseInt((userSelection));
					
					//check to see if it's a valid option
					if(choice != 0 && choice != 1)
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
		
		if(choice == 0)
		{
			//the user has detected an error. restart process
			AddBookRecord();
		}
		
		else
		{
			//the user has entered the correct information
			
			//TODO: Construct SQL Query to add new book record to database.
		}
		
		
		//return to main menu
		Main.MainMenu();
	}
	
	public static void AddBookCopy()
	{
		String isbntoaddto;
		int newcopies;
		//Specify the isbn and number of copies to add
		System.out.print("ISBN of book to add copies to: ");
		isbntoaddto = in.nextLine();
		System.out.println("How many copies would you like to add? :");
		
		do
		{
			userSelection = in.nextLine();
			
			if(!Main.IsNumber(userSelection))
			{
				System.out.print(userSelection + " is not a number, ");
				System.out.print("Please enter a number for this user's ID: ");
			}
			else
			{
				newcopies = Integer.parseInt(userSelection);
				break;
			}
		} while (true);
		
		//TODO: Lookup book record by ISBN, add x number of copies and return back to user how many copies
		//there now exists in the database.
		
		//return to main menu
		Main.MainMenu();
		
		
	}
	
	public static void CheckLateList()
	{
		String month;
		String day;
		String year;
		//have prompt user for date they wish to look up
		System.out.println("Late Book List Date Lookup");
		System.out.println();
		System.out.print("Month: ");
		month = in.nextLine();
		System.out.print("Day: ");
		day = in.nextLine();
		System.out.println("Year: ");
		year = in.nextLine();
		
		//TODO: Construct sql date object based off of given data
		
		
		//construct date based on input
		
		//look up lists of late books (or books that have not been returned and where return date is less than specified date
		
		//list book, due date of said book, name of user who had the book at the time, 
		
		//list offending user's phone number and email
		
		//return to main menu
		Main.MainMenu();
	}
	
	public static void LeaveReview()
	{
		String isbnreview;
		int score;
		String userOpinion;
		
		System.out.println("Leaving a Review: ");
		System.out.println();
		System.out.print("What is the ISBN of the book you wish to leave a review for? :");
		isbnreview = in.nextLine();
		
		System.out.print("Please enter a score [1 - 10]: " );
		try{
			do
			{
				userSelection = in.nextLine();
				
				if(!Main.IsNumber(userSelection))
				{
					System.out.print(userSelection + " is not a number, ");
					System.out.print("Please enter a number for this user's ID: ");
				}
				else
				{
					choice = Integer.parseInt(userSelection);
					if(choice < 0 || choice > 10)
					{
						System.out.print("Please enter a number from 1 - 10");
					}
					else
					{
						break;
					}
				}
			} while (true);
		}
		catch(Exception e)
		{
			
		}
		
		//save score
		score = choice;
		
		System.out.print("Would you like to leave a short descriptive text? Please answer [1] for yes and [0] for no: ");
		
		try
		{
			do
			{
				userSelection = in.nextLine();
				
				if(!Main.IsNumber(userSelection))
				{
					System.out.print(userSelection + " is an invalid option, ");
					System.out.print("Please make a selection: ");
				}
				
				//if the user did enter a number
				if(Main.IsNumber(userSelection))
				{
					choice = Integer.parseInt((userSelection));
					
					//check to see if it's a valid option
					if(choice != 0 && choice != 1)
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
		
		if(choice == 1)
		{
			System.out.print("Your Text: ");
			userOpinion = in.nextLine();
			System.out.println();
			System.out.println("Thank you for the following review: ");
			System.out.println("Username: " + loggedInUser);
			System.out.println("Score: "  + score);
		}
		
		else{
			System.out.println("Thank you for the following review: ");
			System.out.println("Username: " + loggedInUser);
			System.out.println("Score: "  + score);
		}
		
		//TODO: Create sql statement that would add this to the reviews table
		
		//return to main menu
		Main.MainMenu();
		
	}
	
	/**
	 * User can search for books based on author(s), publisher, title-word, subject, 
	 * or a mixture of all the above.
	 * 
	 * The user should be able to specify to only display books that are
	 * 	a: Available in the library at all
	 * 	b: Only available for checkout from the library
	 *
	 * The user should be able to sort return results by
	 * 	c: year published
	 *	d: average number score of reviews
	 *	e: popularity (number of times book has been checked out)
	 */
	public static void BrowseLibrary()
	{
		boolean entireLibrary = false;
		boolean onlyAvailable = false;
		
		boolean sortByYear = false;
		boolean sortByScore = false;
		boolean sortByPopularity = false;
		
//		boolean searchAuthor = false;
//		boolean searchPublisher = false;
//		boolean searchTitle = false;
//		boolean searchSubject = false;
//		
		
		
//		boolean wrongInput = false;
		
//		String searchOptions = "";
//		String[] options = new String[3];
		
		System.out.println();
		System.out.println("Browsing the library: ");
		System.out.println();
		System.out.print("Would you like to display your results as all in the library [1] or only those of which are available to checkout [2]?: ");
		

		try
		{
			do
			{
				userSelection = in.nextLine();
				
				if(!Main.IsNumber(userSelection))
				{
					System.out.print(userSelection + " is an invalid option, ");
					System.out.print("Please make a selection: ");
				}
				
				//if the user did enter a number
				if(Main.IsNumber(userSelection))
				{
					choice = Integer.parseInt((userSelection));
					
					//check to see if it's a valid option
					if(choice != 2 && choice != 1)
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
		
		if(choice == 1)
		{
			entireLibrary = true;
		}
		
		else if(choice == 2)
		{
			onlyAvailable = true;
		}
		
		if(verbose)
		{
			System.out.println("User is searching entire library: " + entireLibrary);
			System.out.println("User is searching only what's available: " + onlyAvailable);
		}
		
		System.out.println("Please input information below, leave empty if you would not want to search via said information");
//		System.out.println();
		System.out.print("Please enter Author(s) separated by a comma: ");
		userSelection = in.nextLine();
		String[] authors = userSelection.split(",");
		
		System.out.print("Please enter publisher(s) separated by a comma: ");
		userSelection = in.nextLine();
		String[] publishers = userSelection.split(",");
		
		System.out.println("Please enter title words separated by a comma: ");
		userSelection = in.nextLine();
		String[] wordsInTitle = userSelection.split(",");

		System.out.println("Please enter subject(s) separated by a comma: ");
		userSelection = in.nextLine();
		String subjects[] = userSelection.split(",");
		
		if(verbose)
		{
			System.out.println("Authors:");
			
			for(int i = 0; i < authors.length; i++)
			{
				System.out.println(authors[i]);
			}
			
			System.out.println("Publishers:");
			
			for(int i = 0; i < publishers.length; i++)
			{
				System.out.println(publishers[i]);
			}
			
			System.out.println("Title Words:");
			
			for(int i = 0; i < wordsInTitle.length; i++)
			{
				System.out.println(wordsInTitle[i]);
			}
			
			System.out.println("Subjects:");
			
			for(int i = 0; i < subjects.length; i++)
			{
				System.out.println(subjects[i]);
			}
			
			System.out.println();
		}
		
//		 * 	c: year published
//		 *	d: average number score of reviews
//		 *	e: popularity (number of times book has been checked out)
		
		System.out.print("Do you want to sort your result by year published [1], average review score [2], or book popularity[3]?: ");
		
		try
		{
			do
			{
				userSelection = in.nextLine();
				
				if(!Main.IsNumber(userSelection))
				{
					System.out.print(userSelection + " is an invalid option, ");
					System.out.print("Please make a selection: ");
				}
				
				//if the user did enter a number
				if(Main.IsNumber(userSelection))
				{
					choice = Integer.parseInt((userSelection));
					
					//check to see if it's a valid option
					if(choice != 2 && choice != 1 && choice != 3)
					{
						System.out.print(choice + " is an invalid option ");
						System.out.print("please make a selection: ");
						
					}
					
					//make sure they only entered one digit
					if(userSelection.length() != 1)
					{
						System.out.print("Invalid number of options, please select only one: ");
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
		
//		boolean sortByYear = false;
//		boolean sortByScore = false;
//		boolean sortByPopularity = false;
		
		if(choice == 1)
		{
			sortByYear = true;
		}
		
		else if(choice == 2)
		{
			sortByScore = true;
		}
		
		else if(choice == 3)
		{
			sortByPopularity = true;
		}
		
		
		//TODO: Create sql command with appropriate fields to search for
		//TODO: Group results by specified method
	}

	/**
	 * Returning a book
	 * A book can be marked as returned or lost. Record the day it was returned or when it was marked as lost
	 *
	 *
	 * If the book was returned, show the list of people on the wait list for said book
	 */
	public static void ReturnBook()
	{

		String ISBN;
		String month;
		String day;
		String year;

		int monthNumber = 0;
		int dayNumber = 0;
		int yearNumber = 0;

		boolean validDate = false;
		boolean validMonth = false;
		boolean validDay = false;
		boolean validYear = false;

		boolean lost;

		System.out.println("Returning a book");
		System.out.println("");

		System.out.print("Please enter the 13 digit ISBN of the book you wish to return: ");
		do{
			userSelection = in.nextLine();

			if(verbose)
			{
				System.out.println("Length of isbn: " + userSelection.length());
			}

			if(/*!Main.IsNumber(userSelection) ||*/ userSelection.length() != 13)
			{
				System.out.print("Not a valid ISBN, please try again: ");
			}
			else
			{
				ISBN = userSelection;
				break;
			}

		}while(true);

		do {

			System.out.println("Please enter the date of which this book was returned: ");
			System.out.print("What month was this book returned? [1 - 12]: ");
			month = in.nextLine();
			System.out.print("What day was this book returned? [1 - 31]: ");
			day = in.nextLine();
			System.out.print("What year was this book returned? [YYYY]: ");
			year = in.nextLine();

			//check for valid input
			if(!Main.IsNumber(month))
			{
				System.out.println(month + " is not a valid month [1 - 12]");
				validMonth = false;

			}

			if(!Main.IsNumber(day))
			{
				System.out.println(day + " is not a valid day [1 - 31]");
				validDay = false;
			}

			if(!Main.IsNumber(year))
			{
				System.out.println(year + " is not a valid year [YYYY]");
				validYear = false;
			}

			if(Main.IsNumber(month))
			{
				monthNumber = Integer.parseInt(month);
				if(monthNumber > 12 || monthNumber < 1)
				{
					System.out.println(month + " is not a valid month [1 - 12]");
				}
				else
					validMonth = true;
			}

			if(Main.IsNumber(day))
			{
				dayNumber = Integer.parseInt(day);
				if(dayNumber > 31 || dayNumber < 1)
				{
					System.out.println(day + " is not a valid day [1 - 31]");
				}
				else
					validDay = true;
			}

			if(Main.IsNumber(year))
			{
				yearNumber = Integer.parseInt(year);
				validYear = true;
			}

			if(validMonth && validDay && validYear)
			{
				validDate = true;
			}

			if(verbose)
			{

				System.out.println("The user has presented a valid date: " + validDate);
			}

			if(validDate == false)
			{
				System.out.println("Retrying...");
				System.out.println();
			}


		}
		while (validDate == false);



		if(verbose)
		{
			System.out.println("User returning book on " +  monthNumber + "/" + dayNumber + "/" + yearNumber);
		}

		System.out.print("Was this book properly returned or lost? [1] yes [0] no: ");

		try
		{
			do
			{
				userSelection = in.nextLine();

				if(!Main.IsNumber(userSelection))
				{
					System.out.print(userSelection + " is an invalid option, ");
					System.out.print("Please make a selection: ");
				}

				//if the user did enter a number
				if(Main.IsNumber(userSelection))
				{
					choice = Integer.parseInt((userSelection));

					//check to see if it's a valid option
					if(choice != 0 && choice != 1)
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

		//determine whether book was lost
		if(choice == 1)
		{
			lost = true;
		}
		else
		{
			lost = false;
		}

		if(lost)
		{
			//specify checkout record as lost
		}

		else
		{
			//TODO: Return wait list for specified book via ISBN
		}


		//TODO: Check if a book with specified isbn has been checked out by currently logged in user
	}
	
	public static void PrintBookRecord()
	{
		
	}
	
	public static void PrintLibraryStatistics()
	{
		
	}
	
	public static void PrintUserStatistics()
	{
		
	}
	
	public static boolean CheckForUser(String username)
	{
		boolean found = false;
		
		//check to see if the user already exists in the database
	
		return found;
	}
	

}
