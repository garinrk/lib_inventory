// package cs5530;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.text.*;

/**
 * CS 5530 Database Systems
 * Phase 2 Code [Console.java]
 * <p/>
 * This project represents the Phase 2 of the Spring 2015 semester project, the implementation
 * of a database that would manage a small library.
 *
 * @author Garin Richards
 */

public class Console {

    /* Sooper Sekret Database credentials */
    private static String DBUSER = "cs5530u18";
    private static String DBPASS = "f96qb5pr";
    private static String DBURL = "Jdbc:mysql://georgia.eng.utah.edu/cs5530db18";
    static Statement stmt;

    private static Date today = new Date();
    private static SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");

    static Scanner in = new Scanner(System.in);
    static String userSelection = null;
    static int choice = 0;
    static Calendar cal;

    static boolean verbose = true;
    static Connection c = null;

    static String currentUser = "DEFAULT";

    private static boolean firstTimeAddUser = true;





    public static void main(String[] args) {
        //connect to database
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            c = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
//			System.out.println("Connection established to database");
            stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            Database.SetConnection(c);

            //show initial main menu
            Welcome();


//            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Cannot connect to Database Server");
        } finally {
            if (c != null) {
                try {
                    c.close();
                    System.out.println("Database Connection Terminated");
                } catch (Exception e) {
                    /* Do nothing, ignoring close errors */
                }
            }
        }
    }//end of main

    public static void Welcome()
    {
        System.out.println("Welcome to the library!");
        System.out.println("Today's date is: " + ft.format(today));
        System.out.println();
        System.out.print("Are you a new [1] or an existing [2] user? Exit with [3]: ");

        do {
            userSelection = in.nextLine();

            if (!IsNumber(userSelection)) {
                System.out.print(userSelection + " is an invalid option, ");
                System.out.print("Please make a selection: ");
            }

            //if the user did enter a number
            if (IsNumber(userSelection)) {
                choice = Integer.parseInt((userSelection));

                //check to see if it's a valid option
                if (choice != 1 && choice != 2 && choice != 3) {
                    System.out.print(choice + " is an invalid option ");
                    System.out.print("please make a selection: ");

                }

                //case where the user did enter a valid option number
                else {
                    break;
                }

            }
        } while (true);

        if(choice == 1)
        {
            /* Add new user to the database and show menu */
            Database.AddUser(true);
            currentUser = Database.newUsername;

            Database.setLoggedInUser(currentUser);
            MainMenu();
        }

        //prompt user for existing username and check if exists
        if (choice == 2) {
            boolean check;

            System.out.print("What is your username? : ");
            currentUser = in.nextLine();

            check = Database.CheckForUser(currentUser);

            //if the username doesn't exist, prompt user again for existing username
            while (!check) {
                System.out.print(currentUser + " does not exist, please enter a valid username: ");
                currentUser = in.nextLine();
                check = Database.CheckForUser(currentUser);
            }

            Database.setLoggedInUser(currentUser);

            MainMenu();
        }
        if (choice == 3) {
            ExitProgram();
        }

    }//end of Welcome

    /**
     * Console menu that prompts the user with choices
     * <p/>
     * These options specify the 13 specified functionalities of the database
     * You can find their pertinent information in their subsequent
     * functions in Database
     */
    public static void MainMenu() {

        System.out.println("====================================================");
        System.out.println("Hello " + currentUser + " What would you like to do?");
        System.out.println();
        System.out.println("Add a new user: [1]");
        System.out.println("Check out a book: [2]");
        System.out.println("Print out the record of a specific user: [3]");
        System.out.println("Add a new book record to the library database: [4]");
        System.out.println("Add a new copy of a book to the library database: [5]");
        System.out.println("Check the late book list at a certain date: [6]");
        System.out.println("Leave a review for a book: [7]");
        System.out.println("Browse the Library: [8]");
        System.out.println("Return a book: [9]");
        System.out.println("Print a book's record: [10]");
        System.out.println("Print Library statistics: [11]");
        System.out.println("Print User Statistics: [12]");
        System.out.println("Print out a user's Record: [13]");
        System.out.println("Put yourself on a Wait List for a Book: [14]");
        System.out.println("Log in as a different user: [15]");
        System.out.println("Exit: [16]");
        System.out.println();
        System.out.print("Please make a selection: ");

        do {
            userSelection = in.nextLine();

            if (!IsNumber(userSelection)) {
                System.out.print(userSelection + " is an invalid option, ");
                System.out.print("Please make a selection: ");
            }

            //if the user did enter a number
            if (IsNumber(userSelection)) {
                choice = Integer.parseInt((userSelection));

                //check to see if it's a valid option
                if (choice < 1 || choice > 16) {
                    System.out.print(choice + " is an invalid option ");
                    System.out.print("please make a selection: ");

                }

                //case where the user did enter a valid option number
                else {
                    break;
                }

            }
        } while (true);


        //send the user to the correct function
        FunctionParse(choice);

    }//end of MainMenu function


    /**
     * Depending on the integer passed through to the console,
     * the correct information will be prompted and stored to make
     * a query.
     *
     * @param selection, representative of user's choice
     */
    public static void FunctionParse(int selection) {
        //adding a user to the library
        if (selection == 1) {
           Database.AddUser(false);
        }

        //checking out a book
        else if (selection == 2) {
            Database.CheckoutBook(today);
        }

        //prints out record of a specific user
        else if (selection == 3) {
            Database.PrintUserRecord();
        }

        //add a new book record to the library database
        else if (selection == 4) {
            Database.AddBookRecord();
        }

        //add a new copy of a book to the library database
        else if (selection == 5) {
            Database.AddBookCopy();
        }

        //check the late book list at a certain date
        else if (selection == 6) {
            Database.CheckLateList();
        }

        //leave a review for a book
        else if (selection == 7) {
            Database.LeaveReview();
        }

        //browse the library
        else if (selection == 8) {
            Database.BrowseLibrary();
        }

        //return a book to the library
        else if (selection == 9) {
            Database.ReturnBook();
        }

        //print the book record
        else if (selection == 10) {
            Database.PrintBookRecord();
        }

        //print library statistics
        else if (selection == 11) {
            Database.PrintLibraryStatistics();
        }

        //print user statistics
        else if (selection == 12) {
            Database.PrintUserStatistics();
        }

        //print user record
        else if (selection == 13) {
            Database.PrintUserRecord();
        }

        //put user on a waitlist
        else if(selection == 14) {
            Database.AddToWaitList(today);
        }

        else if (selection == 15)
        {
            //log out by clearing values
            System.out.println("Logging out of " + currentUser);
            System.out.println("Good bye");
            currentUser = null;
            Database.loggedInUser = null;

            //prompt for new username
            System.out.print("What is your username? : ");


            boolean check;

            currentUser = in.nextLine();

            check = Database.CheckForUser(currentUser);

            //if the username doesn't exist, prompt user again for existing username
            while (!check) {
                System.out.print(currentUser + " does not exist, please enter a valid username: ");
                currentUser = in.nextLine();
                check = Database.CheckForUser(currentUser);
            }

            //login as said user name
            Database.setLoggedInUser(currentUser);

            //display main menu
            MainMenu();
        }

        //log out and log in as new user
        else if (selection == 16) {
            ExitProgram();

        }
    }//end of FunctionParse






    /**
     * Check if the user's input was an integer or not
     *
     * @param i, a string to test if it is an integer
     * @return
     */
    public static boolean IsNumber(String i) {
        try {
            Integer.parseInt(i);
        } catch (NumberFormatException e) {
            //string is not an integer
            return false;
        }
        //string is integer
        return true;
    }

    /**
     * Exits the program
     */
    public static void ExitProgram() {
        System.out.println();
        System.out.println("Thanks for using the Library, Goodbye!");
        try {
            stmt.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        System.exit(0);
    }//end of ExitProgram
}
