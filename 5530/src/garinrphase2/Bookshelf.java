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
 * @author Garin Richards, u0738045
 */
public class Bookshelf {

    /* Display extra information to console? Used for debugging purposes mostly */
    static boolean verbose = false;

    //TODO: Check ISBN Legality, ISBNs are Strings. Too large for ints.
    //TODO: Remove or standardize try catch blocks around user input prompting

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
    public static void AddUser() {
        System.out.println("Adding a new user...");
        System.out.println();
        System.out.print("New Unique Username: ");
        newUsername = in.nextLine();

        //TODO: Check for if user already exists in table
        while (CheckForUser(newUsername)) {
            System.out.print(newUsername + " already exists, please choose a new username: ");
            newUsername = in.nextLine();
        }

		/* User must enter a number for an ID */
        System.out.print("New ID (Number): ");
        do {
            newID = in.nextLine();

            if (!Main.IsNumber(newID)) {
                System.out.print(newID + " is not a number, ");
                System.out.print("Please enter a number for this user's ID: ");
            } else {
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
        do {
            newPhoneNumber = in.nextLine();

            if (!Main.IsNumber(newID)) {
                System.out.print(newPhoneNumber + " is not a number, ");
                System.out.print("Please enter a number for this user's Phone Number: ");
            } else {
                break;
            }
        } while (true);
        System.out.println();

        if (verbose) {
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
        if (loggedInUser == null)
            return;
        else
            Main.MainMenu();

    }//end of AddUser

    /**
     * Used to set user context string in menus and greetings
     *
     * @param username
     */
    public static void setLoggedInUser(String username) {
        loggedInUser = username;

    }//end of SetLoggedInUser

    /**
     * Sends a sql statement to the database on the given connection
     *
     * @param query,         string composed of constructed sql query
     * @param sqlConnection, connection object currently connected to the database defined in main
     * @return a result set containing results from said query
     */
    public static ResultSet SendQuery(String query, Connection sqlConnection) {
        ResultSet rs = null;

        return rs;
    }//end of SendQuery

    /**
     * Checkout a book from the library
     * NOTE: MONTH IS ZERO BASED
     * ISBNs are 13 digits long
     * Compute the due date to be 30 days from today
     */
    public static void CheckoutBook(Date today) {
        System.out.println("Checking out a book...");
        System.out.println();

        String ISBN;
        System.out.print("Please enter the 13 digit ISBN of the book you wish to check out: ");
        do {
            userSelection = in.nextLine();

            if (verbose) {
                System.out.println("Length of isbn: " + userSelection.length());
            }

            if (/*!Main.IsNumber(userSelection) ||*/ userSelection.length() != 13) {
                System.out.print("Not a valid ISBN, please try again: ");
            } else {
                ISBN = userSelection;
                break;
            }

        } while (true);

        if (verbose) {
            System.out.println("The ISBN to be checked out is: " + ISBN);
        }

        //compute due date to be 30 days from today
        c.setTime(today);

        if (verbose) {
            System.out.println("It is currently " + c.get(Calendar.MONTH) + " the " + c.get(Calendar.DAY_OF_MONTH));
        }

        c.add(Calendar.DATE, 30);

        //save duedate
        int futureMonth = c.get(Calendar.MONTH) + 1;
        int futureDay = c.get(Calendar.DAY_OF_MONTH);
        int futureYear = c.get(Calendar.YEAR);


        if (verbose) {
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
     * <p>
     * Personal Data of User:
     * name
     * username
     * id
     * address
     * phone number
     * email address
     * Full book history:
     * All books checked out in the past
     * ISBN
     * Title
     * Dates of checkout and return
     * Full list of books lost by user
     * Full list of books requested for future checkout (User is currently on waitlist for)
     * Full list of review they have written for books
     * Score
     * Text
     */
    public static void PrintUserRecord() {
        String lookedUpUser;

        System.out.println("Printing User Record...");
        System.out.println();
        System.out.println("Username to view record of: ");
        lookedUpUser = in.nextLine();

        if (!CheckForUser(lookedUpUser)) {
            System.out.println("User does not exist, returning to main menu");
            System.out.println();
            Main.MainMenu();
        } else {
            if (verbose) {
                System.out.println("Printing record for " + lookedUpUser);
            }

            //TODO: Holy SQL batman
        }

        //return to main menu
        Main.MainMenu();

    }//end of PrintUserRecord

    /**
     * Adds a new record of a book to the library, but not a copy
     * Prompt user for:
     * ISBN
     * title
     * author
     * publisher
     * year of publication
     * format
     * subject
     * book summary
     * <p>
     * Set the following to null/empty
     * individual book copy location
     * availability
     */
    public static void AddBookRecord() {
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

        do {
            userSelection = in.nextLine();

            if (!Main.IsNumber(userSelection)) {
                System.out.print(userSelection + " is not a number, ");
                System.out.print("Please enter the number of authors: ");
            } else {
                numberOfAuthors = Integer.parseInt(userSelection);
                break;
            }
        } while (true);

        //create an array to hold the names of multiple authors for the title
        String[] authors = new String[numberOfAuthors];

        //only one author, grab name and carry on
        if (numberOfAuthors == 1) {
            System.out.print("Author: ");
            authors[0] = in.nextLine();
            multipleAuthors = false;
        } else {
            //there is more than one author for this title
            multipleAuthors = true;

            //add authors to arrays
            for (int i = 0; i < numberOfAuthors; i++) {
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

        for (int i = 0; i < numberOfAuthors; i++) {
            if (i == numberOfAuthors - 1) {
                System.out.println(authors[i]);
            } else {
                System.out.print(authors[i]);
            }
        }
        System.out.println("Publisher: " + newPublisher);
        System.out.println("Year of Publication: " + newYearPub);
        System.out.println("Format: " + newFormat);
        System.out.println("Subject: " + newSubject);
        System.out.println("Book Summary: " + newSummary);
        System.out.print("Please answer [1] for yes and [0] for no: ");

        try {
            do {
                userSelection = in.nextLine();

                if (!Main.IsNumber(userSelection)) {
                    System.out.print(userSelection + " is an invalid option, ");
                    System.out.print("Please make a selection: ");
                }

                //if the user did enter a number
                if (Main.IsNumber(userSelection)) {
                    choice = Integer.parseInt((userSelection));

                    //check to see if it's a valid option
                    if (choice != 0 && choice != 1) {
                        System.out.print(choice + " is an invalid option ");
                        System.out.print("please make a selection: ");

                    }

                    //case where the user did enter a valid option number
                    else {
                        break;
                    }

                }
            } while (true);
        } catch (Exception e) {

        }

        if (choice == 0) {
            //the user has detected an error. restart process
            AddBookRecord();
        } else {
            //the user has entered the correct information

            //TODO: Construct SQL Query to add new book record to database.
        }


        //return to main menu
        Main.MainMenu();

    }//end of AddBookRecord

    public static void AddBookCopy() {
        String isbntoaddto;
        int newcopies;

        System.out.println("Adding new copies of a book...");
        System.out.println();

        //Specify the isbn and number of copies to add
        System.out.print("ISBN of book to add copies to: ");
        isbntoaddto = in.nextLine();
        System.out.println("How many copies would you like to add? :");

        do {
            userSelection = in.nextLine();

            if (!Main.IsNumber(userSelection)) {
                System.out.print(userSelection + " is not a number, ");
                System.out.print("Please enter a number for this user's ID: ");
            } else {
                newcopies = Integer.parseInt(userSelection);
                break;
            }
        } while (true);

        //TODO: Lookup book record by ISBN, add x number of copies and return back to user how many copies
        //there now exists in the database.

        //return to main menu
        Main.MainMenu();

    }//end of AddBookCopy

    public static void CheckLateList() {
        String month;
        String day;
        String year;
        //have prompt user for date they wish to look up
        System.out.println("Late book list lookup...");
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
    }//end of CheckLateList

    public static void LeaveReview() {
        String isbnreview;
        int score;
        String userOpinion;

        System.out.println("Leaving a Review...");
        System.out.println();
        System.out.print("What is the ISBN of the book you wish to leave a review for? :");
        isbnreview = in.nextLine();

        System.out.print("Please enter a score [1 - 10]: ");
        try {
            do {
                userSelection = in.nextLine();

                if (!Main.IsNumber(userSelection)) {
                    System.out.print(userSelection + " is not a number, ");
                    System.out.print("Please enter a number for this user's ID: ");
                } else {
                    choice = Integer.parseInt(userSelection);
                    if (choice < 0 || choice > 10) {
                        System.out.print("Please enter a number from 1 - 10");
                    } else {
                        break;
                    }
                }
            } while (true);
        } catch (Exception e) {

        }

        //save score
        score = choice;

        System.out.print("Would you like to leave a short descriptive text? Please answer [1] for yes and [0] for no: ");

        try {
            do {
                userSelection = in.nextLine();

                if (!Main.IsNumber(userSelection)) {
                    System.out.print(userSelection + " is an invalid option, ");
                    System.out.print("Please make a selection: ");
                }

                //if the user did enter a number
                if (Main.IsNumber(userSelection)) {
                    choice = Integer.parseInt((userSelection));

                    //check to see if it's a valid option
                    if (choice != 0 && choice != 1) {
                        System.out.print(choice + " is an invalid option ");
                        System.out.print("please make a selection: ");

                    }

                    //case where the user did enter a valid option number
                    else {
                        break;
                    }

                }
            } while (true);
        } catch (Exception e) {

        }

        if (choice == 1) {
            System.out.print("Your Text: ");
            userOpinion = in.nextLine();
            System.out.println();
            System.out.println("Thank you for the following review: ");
            System.out.println("Username: " + loggedInUser);
            System.out.println("Score: " + score);
        } else {
            System.out.println("Thank you for the following review: ");
            System.out.println("Username: " + loggedInUser);
            System.out.println("Score: " + score);
        }

        //TODO: Create sql statement that would add this to the reviews table

        //return to main menu
        Main.MainMenu();

    }//end of LeaveReview

    /**
     * User can search for books based on author(s), publisher, title-word, subject,
     * or a mixture of all the above.
     * <p>
     * The user should be able to specify to only display books that are
     * a: Available in the library at all
     * b: Only available for checkout from the library
     * <p>
     * The user should be able to sort return results by
     * c: year published
     * d: average number score of reviews
     * e: popularity (number of times book has been checked out)
     */
    public static void BrowseLibrary() {
        boolean entireLibrary = false;
        boolean onlyAvailable = false;

        boolean sortByYear = false;
        boolean sortByScore = false;
        boolean sortByPopularity = false;

        System.out.println("Browsing the library...");
        System.out.println();
        System.out.print("Would you like to display your results as all in the library [1] or only those of which are available to checkout [2]?: ");


        try {
            do {
                userSelection = in.nextLine();

                if (!Main.IsNumber(userSelection)) {
                    System.out.print(userSelection + " is an invalid option, ");
                    System.out.print("Please make a selection: ");
                }

                //if the user did enter a number
                if (Main.IsNumber(userSelection)) {
                    choice = Integer.parseInt((userSelection));

                    //check to see if it's a valid option
                    if (choice != 2 && choice != 1) {
                        System.out.print(choice + " is an invalid option ");
                        System.out.print("please make a selection: ");

                    }

                    //case where the user did enter a valid option number
                    else {
                        break;
                    }

                }
            } while (true);
        } catch (Exception e) {

        }

        if (choice == 1) {
            entireLibrary = true;
        } else if (choice == 2) {
            onlyAvailable = true;
        }

        if (verbose) {
            System.out.println("User is searching entire library: " + entireLibrary);
            System.out.println("User is searching only what's available: " + onlyAvailable);
        }

        System.out.println("Please input information below, leave empty if you would not want to search via said information");

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

        if (verbose) {
            System.out.println("Authors:");

            for (int i = 0; i < authors.length; i++) {
                System.out.println(authors[i]);
            }

            System.out.println("Publishers:");

            for (int i = 0; i < publishers.length; i++) {
                System.out.println(publishers[i]);
            }

            System.out.println("Title Words:");

            for (int i = 0; i < wordsInTitle.length; i++) {
                System.out.println(wordsInTitle[i]);
            }

            System.out.println("Subjects:");

            for (int i = 0; i < subjects.length; i++) {
                System.out.println(subjects[i]);
            }

            System.out.println();
        }

        System.out.print("Do you want to sort your result by year published [1], average review score [2], or book popularity[3]?: ");

        try {
            do {
                userSelection = in.nextLine();

                if (!Main.IsNumber(userSelection)) {
                    System.out.print(userSelection + " is an invalid option, ");
                    System.out.print("Please make a selection: ");
                }

                //if the user did enter a number
                if (Main.IsNumber(userSelection)) {
                    choice = Integer.parseInt((userSelection));

                    //check to see if it's a valid option
                    if (choice != 2 && choice != 1 && choice != 3) {
                        System.out.print(choice + " is an invalid option ");
                        System.out.print("please make a selection: ");

                    }

                    //make sure they only entered one digit
                    if (userSelection.length() != 1) {
                        System.out.print("Invalid number of options, please select only one: ");
                    }


                    //case where the user did enter a valid option number
                    else {
                        break;
                    }


                }
            } while (true);
        } catch (Exception e) {

        }


        if (choice == 1) {
            sortByYear = true;
        } else if (choice == 2) {
            sortByScore = true;
        } else if (choice == 3) {
            sortByPopularity = true;
        }

        Main.MainMenu();

        //TODO: Create sql command with appropriate fields to search for
        //TODO: Group results by specified method
    }//end of BrowseLibrary

    /**
     * Returning a book
     * A book can be marked as returned or lost. Record the day it was returned or when it was marked as lost
     * <p>
     * <p>
     * If the book was returned, show the list of people on the wait list for said book
     */
    public static void ReturnBook() {

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

        System.out.println("Returning a book...");
        System.out.println();

        System.out.print("Please enter the 13 digit ISBN of the book you wish to return: ");
        do {
            userSelection = in.nextLine();

            if (verbose) {
                System.out.println("Length of isbn: " + userSelection.length());
            }

            if (/*!Main.IsNumber(userSelection) ||*/ userSelection.length() != 13) {
                System.out.print("Not a valid ISBN, please try again: ");
            } else {
                ISBN = userSelection;
                break;
            }

        } while (true);

        do {

            System.out.println("Please enter the date of which this book was returned: ");
            System.out.print("What month was this book returned? [1 - 12]: ");
            month = in.nextLine();
            System.out.print("What day was this book returned? [1 - 31]: ");
            day = in.nextLine();
            System.out.print("What year was this book returned? [YYYY]: ");
            year = in.nextLine();

            //check for valid input
            if (!Main.IsNumber(month)) {
                System.out.println(month + " is not a valid month [1 - 12]");
                validMonth = false;

            }

            if (!Main.IsNumber(day)) {
                System.out.println(day + " is not a valid day [1 - 31]");
                validDay = false;
            }

            if (!Main.IsNumber(year)) {
                System.out.println(year + " is not a valid year [YYYY]");
                validYear = false;
            }

            if (Main.IsNumber(month)) {
                monthNumber = Integer.parseInt(month);
                if (monthNumber > 12 || monthNumber < 1) {
                    System.out.println(month + " is not a valid month [1 - 12]");
                } else
                    validMonth = true;
            }

            if (Main.IsNumber(day)) {
                dayNumber = Integer.parseInt(day);
                if (dayNumber > 31 || dayNumber < 1) {
                    System.out.println(day + " is not a valid day [1 - 31]");
                } else
                    validDay = true;
            }

            if (Main.IsNumber(year)) {
                yearNumber = Integer.parseInt(year);
                validYear = true;
            }

            if (validMonth && validDay && validYear) {
                validDate = true;
            }

            if (verbose) {

                System.out.println("The user has presented a valid date: " + validDate);
            }

            if (!validDate) {
                System.out.println("Retrying...");
                System.out.println();
            }


        }
        while (!validDate);


        if (verbose) {
            System.out.println("User returning book on " + monthNumber + "/" + dayNumber + "/" + yearNumber);
        }

        System.out.print("Was this book properly returned or lost? [1] yes [0] no: ");

        try {
            do {
                userSelection = in.nextLine();

                if (!Main.IsNumber(userSelection)) {
                    System.out.print(userSelection + " is an invalid option, ");
                    System.out.print("Please make a selection: ");
                }

                //if the user did enter a number
                if (Main.IsNumber(userSelection)) {
                    choice = Integer.parseInt((userSelection));

                    //check to see if it's a valid option
                    if (choice != 0 && choice != 1) {
                        System.out.print(choice + " is an invalid option ");
                        System.out.print("please make a selection: ");

                    }

                    //case where the user did enter a valid option number
                    else {
                        break;
                    }

                }
            } while (true);
        } catch (Exception e) {

        }

        //determine whether book was lost
        if (choice == 1) {
            lost = true;
        } else {
            lost = false;
        }

        if (lost) {
            //specify checkout record as lost
        } else {    //book is not lost
            //TODO: Return wait list for specified book via ISBN
        }


        //TODO: Check if a book with specified isbn has been checked out by currently logged in user, if not. They can't return it
        //TODO: Else, set checkout record's specified checking date to given date, lost = false;

        Main.MainMenu();

    }//end of ReturnBook

    /**
     * Print a book's full info:
     * ISBN, title, authors, subject, Publisher, Publishing year, format, summary,
     * <p>
     * Listing of all copies tracked and their locations
     * <p>
     * Listing of users who have checked out the book and the dates of which they had said book
     * <p>
     * Average review score + individual review for the book
     */
    public static void PrintBookRecord() {
        String title;

        System.out.println("Printing Book Record...");
        System.out.println();

        System.out.print("Please enter the title of the book you wish to look up: ");

        do {
            userSelection = in.nextLine();


            if (Main.IsNumber(userSelection)) //TODO: Check if even said string is a number, integer is too small for 13 digits.
            {
                System.out.print("Not a valid title, please try again: ");
            } else {
                title = userSelection;
                break;
            }

        } while (true);

        //TODO: Given title of book, construct sql query to get the above information


    }//end of PrintBookRecord

    public static void PrintLibraryStatistics() {
        int nbooks;
        System.out.println("Printing Library Statistics...");
        System.out.println();
        System.out.print("How many books would you like to request stats about?: ");

        do {
            userSelection = in.nextLine();

            if (!Main.IsNumber(userSelection)) {
                System.out.print(userSelection + " is not a number, ");
                System.out.print("Please enter the amount of books you wish to requests stats about: ");
            } else {
                choice = Integer.parseInt(userSelection);
                break;

            }
        } while (true);

        nbooks = choice;

        System.out.println("Do you want to..");
        System.out.println("List " + choice + " most requested books [1]");
        System.out.println("List " + choice + " most checked out books [2]");
        System.out.println("List " + choice + " most lost books [3]");
        System.out.println("List " + choice + " most popular authors [4]");

        do {
            userSelection = in.nextLine();

            if (!Main.IsNumber(userSelection)) {
                System.out.println(userSelection + " is not a number, ");
                System.out.print("Please enter a number [1 - 4] for your selection: ");
            } else {
                choice = Integer.parseInt(userSelection);
                if (choice < 1 || choice > 4) {
                    System.out.println(userSelection + " is not a valid selection");
                    System.out.print("Please enter a number [1 - 4] for your selection: ");
                } else {
                    break;
                }

            }
        } while (true);


        //Most requested books
        if (choice == 1) {
            //TODO: Construct sql query for most requested books (size of waitlist)
        }

        //Most checked out books
        else if (choice == 2) {
            //TODO: Construct sql query for most checked out books (instances of checked out records)
        }

        //Most lost books
        else if (choice == 3) {
            //TODO: Construct sql query for most lost books (instances of checked out records where lost = true)
        }

        //most popular authors
        else if (choice == 4) {
            //TODO: Construct sql query for most popular authors (instances of checkout records with the most common authors)
        }

        //display main menu again
        Main.MainMenu();
    }//end of PrintLibraryStatistics

    /**
     * Print out specified number of users whom:
     * have checked out the most books
     * have rated the most number of books
     * have lost the most books
     */
    public static void PrintUserStatistics() {
        int numberOfUsers;
        System.out.println("Printing User Statistics...");
        System.out.println();
        System.out.print("How many users would you like to inquire about? :");

        do {
            userSelection = in.nextLine();

            if (!Main.IsNumber(userSelection)) {
                System.out.println(userSelection + " is not a number, ");
                System.out.print("Please enter the number of users you wish to inquire about: ");
            } else {
                choice = Integer.parseInt(userSelection);
                break;
            }
        } while (true);

        numberOfUsers = choice;

        System.out.println("Do you want to..");
        System.out.println("List top " + numberOfUsers + " users who have checked out the most books [1]");
        System.out.println("List top " + numberOfUsers + " users who have rated the most number of books [2]");
        System.out.println("List top " + numberOfUsers + " users who have lost the most books [3]");

        do {
            userSelection = in.nextLine();

            if (!Main.IsNumber(userSelection)) {
                System.out.println(userSelection + " is not a number, ");
                System.out.print("Please enter a number [1 - 3] for your selection: ");
            } else {
                choice = Integer.parseInt(userSelection);
                if (choice < 1 || choice > 3) {
                    System.out.println(userSelection + " is not a valid selection");
                    System.out.print("Please enter a number [1 - 3] for your selection: ");
                } else {
                    break;
                }

            }
        } while (true);

        //users who checked out the most books
        if (choice == 1) {
            //TODO: Compute highest number of duplicate checkout records. Grab users whom also exhibit that frequency
        }

        //users who rated the most number of books
        if (choice == 2) {
            //TODO: Compute highest number of duplicate reviews, grab users whom also have that same number
        }

        //users who lost the most books
        if (choice == 3) {
            //TODO: Compute highest number of duplicate checkout records where lost == true, grab users whom also display that same frequency
        }

        Main.MainMenu();

    }//end of PrintUserStatistics

    public static boolean CheckForUser(String username) {
        boolean found = false;

        //TODO: check to see if the user already exists in the database

        return found;
    }


}
