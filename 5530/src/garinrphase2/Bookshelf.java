package garinrphase2;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
    //TODO: Make sure dates in tables make sense, e.g, they return before checkout
    //TODO: Constraints, rating between 0 - 10, ISBNs, phone numbers.
    //TODO: AUTHORS!
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

    static String invTable = "Inventory";
    static String bookTable = "Book";
    static String checkoutTable = "CheckoutRecord";
    static String inventoryTable = "Inventory";
    static String reviewTable = "Review";
    static String userTable = "User";
    static String waitlistTable = "Waitlist";
    static String authorTable = "Authors";

    /* Represents the currently logged in user using the library */
    static String loggedInUser = null;

    /* Used for calculating dates */
    static Calendar c = Calendar.getInstance();

    /* Related to the SQL Connection */
    static Statement statem = null;
    static Connection con = null;

    public static void SetConnection(Connection c) {

        Statement st = null;
        try {

            //set statement
            st = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //store connection
            con = c;
        } catch (Exception e) {

        }
    }

    /**
     * Adds a new user to the library database. Queries the user
     * for a unique username and id, full name, address, phone number, and email address
     */
    public static void AddUser() {

        String sql;
        ResultSet r = null;
        PreparedStatement st = null;

        System.out.println("Adding a new user...");
        System.out.println();
        System.out.print("New Unique Username: ");
        newUsername = in.nextLine();


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

        sql = "INSERT INTO " + userTable + " (username, cardid, fullname, email, address, phonenumber) VALUES (?, ?, ?, ?, ?, ?);";

        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, newUsername);
            st.setString(2, newID);
            st.setString(3, newFullName);
            st.setString(4, newEmail);
            st.setString(5, newAddress);
            st.setString(6, newPhoneNumber);
            st.executeUpdate();
            r = st.executeQuery();


        } catch (Exception e) {

        }
        if (verbose) {
            System.out.println("RAW SQL: " + sql);
            System.out.println("PREPARED STATEMENT: " + st);
            System.out.println();

        }

        setLoggedInUser(newUsername);
        Main.setLoggedInUser(newUsername);

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
     * Checkout a book from the library
     * NOTE: MONTH IS ZERO BASED
     * ISBNs are : 641787528-8
     * Compute the due date to be 30 days from today
     */
    public static void CheckoutBook(Date today) {
        String ISBN;
        String duedate;
        boolean check;
        boolean availableforcheckout = false;
        String sql;
        String oldestuser = null;
        int thisMonth = 0;
        int thisYear = 0;
        int thisDay = 0;
        String todayDate;


        ResultSet r = null;
        PreparedStatement st = null;

        System.out.println("Checking out a book...");
        System.out.println();
        System.out.print("Please enter the ISBN of the book you wish to check out: ");

        do {
            userSelection = in.nextLine();
            check = CheckForISBN(userSelection);


            if (!check) {
                System.out.print("ISBN doesn't exist. Please try again: ");
            } else {
                ISBN = userSelection;
                break;
            }

        } while (true);

        if (verbose) {
            System.out.println("The ISBN to be checked out is: " + ISBN);
        }

        //get today's date
        c.setTime(today);

        thisMonth = c.get(Calendar.MONTH) + 1;
        thisDay = c.get(Calendar.DAY_OF_MONTH);
        thisYear = c.get(Calendar.YEAR);


        todayDate = thisYear + "-" + thisMonth + "-" + thisDay;

        if (verbose) {
            System.out.println("It is currently " + (c.get(Calendar.MONTH) + 1) + " the " + c.get(Calendar.DAY_OF_MONTH));
        }

        //compute 30 days ahead
        c.add(Calendar.DATE, 30);

        //save duedate
        int futureMonth = c.get(Calendar.MONTH) + 1;
        int futureDay = c.get(Calendar.DAY_OF_MONTH);
        int futureYear = c.get(Calendar.YEAR);


        if (verbose) {
            System.out.println("30 days from now it will be " + (c.get(Calendar.MONTH) + 1) + " the " + c.get(Calendar.DAY_OF_MONTH));
        }


        //create date string that will be inserted
        duedate = Integer.toString(futureYear) + "-" + Integer.toString(futureMonth) + "-" + Integer.toString(futureDay);

        if (verbose) {
            System.out.println("RAW DATE STRING: " + duedate);
            System.out.println();
        }

        sql = "SELECT ISBN as r FROM " + waitlistTable + " WHERE " + waitlistTable + ".ISBN = ?";
        //        INSERT INTO table_name(today)
//        VALUES(STR_TO_DATE('07-25-2012','%m-%d-%y'));
//        String aString = Integer.toString(aInt);

        if (verbose) {
            System.out.println("User: " + loggedInUser + " is going to check out book ISBN: " + ISBN + " with a check in date of " + duedate);


        }

        sql = "SELECT username AS oldestuserforisbn FROM " + waitlistTable + " WHERE isbn = ? AND dateadded = (SELECT MIN(dateadded) from " + waitlistTable + " where isbn =  ? )";
        try {


            st = con.prepareStatement(sql);
            st.setString(1, ISBN);
            st.setString(2, ISBN);
//            st.executeUpdate();
            r = st.executeQuery();

            if (!r.wasNull()) {
                while (r.next()) {
                    oldestuser = r.getString("oldestuserforisbn");
                }


            }

        } catch (Exception e) {

        }


        if (verbose) {
            System.out.println("RAW SQL: " + sql);
            System.out.println("PREPARED STATEMENT: " + st);
            System.out.println();
            System.out.println("Oldest User: " + oldestuser);
        }

        if (oldestuser == null) {
            System.out.println("There is no wait list for the book!");
        } else {
            //determine if the oldest user waiting for book is the currently logged in user
            availableforcheckout = oldestuser.matches(loggedInUser);
        }
        //Check to see if current user is the user waiting the longest for the book
        if (!availableforcheckout && oldestuser != null) {


            System.out.print("There is a Wait List for this book, and you are not at top of the list, would you like to be added to the Wait List for this book? [0] No [1] Yes: ");

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

            if (choice == 1) {
                //add user to waitlist for book
                AddToWaitList(ISBN, today);

            } else {
                //exit and return to main menu
                System.out.println("You will not be added to the waitlist, returning to main menu...");
                Main.MainMenu();
            }
        }

        //TODO: Book is available to checkout, create checkout record
        else {
            //TODO: Check if user already has copy of book
            if (!CheckForCheckoutRecord(loggedInUser, ISBN)) {
                //if not, create checkout record
                CreateCheckoutRecord(loggedInUser, duedate, " ", ISBN);
            }

        }


        //TODO: The user can only check out the book when they have been on said walist the longest.

        //TODO: Increment number of times that book has been checkd out by one

        //return to main menu
        Main.MainMenu();

    }//end of CheckoutBook

    public static void CreateCheckoutRecord(String username, String duedate, String checkoutdate, String isbn) {
        //get bookid for book with given isbn
        ResultSet r = null;
        PreparedStatement st = null;
        String sql;
        boolean backtomenu = false;
        int copies = 0;
        int unavailablecopies = 0;

        //TODO: Check if there are copies available to checkout
//        # get count of times that isbn shows up in book table
//
//        # check checkout record for book isbn. If it is checked out, but not returned (date is null) decrease amount
//        #or the book was returned and marked as lost, decrease amount. If this amount hits 0. There is no book to checkout

        sql = "SELECT count(isbn) as copies from " + bookTable + " where isbn = ?;";

//        sql = "SELECT username FROM " + checkoutTable + " c, " + inventoryTable + " i, " + bookTable + " b WHERE c.bookid = i.bookid AND b.bookid = c.bookid AND b.isbn = ? AND  c.username  = ?;";

        try {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
//            st.setString(2, username);

//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                if (r.getString("copies").matches("0")) {
                    System.out.println("There are no copies of the book to checkout. Returning to main menu...");
                    backtomenu = true;
                } else {
                    copies = Integer.parseInt(r.getString("copies"));
                }
            }

        } catch (Exception e) {

        }

        if (backtomenu) {
            Main.MainMenu();
        }

        if (verbose) {
            System.out.println();
            System.out.println("SQL RAW STRING: " + sql);
            System.out.println();
            System.out.println("PREPARED STATEMENT: " + st);
            System.out.println();
        }


//        SELECT isbn, count(isbn) as unavailablecopies from CheckoutRecord JOIN Book ON CheckoutRecord.bookid = Book.bookid WHERE CheckoutRecord.lost = 1 OR CheckoutRecord.returndate = '0000-00-00' AND Book.isbn = ?

        sql = "SELECT isbn, count(isbn) as unavailablecopies from "
                + checkoutTable + " JOIN " + bookTable + " ON " + checkoutTable +
                ".bookid = " + bookTable + ".bookid WHERE " + checkoutTable + ".lost = 1 OR " +
                "" + checkoutTable + ".returndate  = '0000-00-00' AND " + bookTable + ".isbn = ?";

        try {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                //get the number of unavailable copies
                unavailablecopies = Integer.parseInt(r.getString("unavailablecopies"));
                if (copies - unavailablecopies <= 0) {
                    System.out.println("There are no copies of the book to checkout. Returning to main menu...");
                    backtomenu = true;
                }

            }

        } catch (Exception e) {

        }

        if (verbose) {
            System.out.println();
            System.out.println("SQL RAW STRING: " + sql);
            System.out.println();
            System.out.println("PREPARED STATEMENT: " + st);
            System.out.println();
        }
        if (backtomenu) {
            Main.MainMenu();
        }


    }//end of CreateCheckoutRecord

    public static boolean CheckForCheckoutRecord(String username, String isbn) {
        ResultSet r = null;
        PreparedStatement st = null;
        String sql;
        boolean found = false;

        sql = "SELECT username FROM " + checkoutTable + " c, " + inventoryTable + " i, " + bookTable + " b WHERE c.bookid = i.bookid AND b.bookid = c.bookid AND b.isbn = ? AND  c.username  = ?;";

        try {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            st.setString(2, username);

//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                if (r.getString("username").matches(loggedInUser) && found == false) {
                    found = true;
                    break;
                }
            }
        } catch (Exception e) {

        }

        return found;
    }

    /**
     * Adds user to the waitlist for a specified ISBN
     *
     * @param isbn
     */
    public static void AddToWaitList(String isbn, Date today) {
        String sql;
        ResultSet r = null;
        PreparedStatement st = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int thisMonth = 0;
        int thisDay = 0;
        int thisYear = 0;
        String todayDate;
        boolean found = false;

        sql = "SELECT username FROM " + waitlistTable + " where isbn = ?;";
        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                if (r.getString("username").matches(loggedInUser) && found == false) {
                    found = true;
                    break;
                }
            }

        } catch (Exception e) {

        }

        if (found) {
            System.out.println("You are already on a Wait List for this book, returning to Main Menu...");
            Main.MainMenu();
        }


        sql = "INSERT INTO " + waitlistTable + " (username, isbn, dateadded) VALUES (?, ?, ?);";

        thisMonth = cal.get(Calendar.MONTH) + 1;
        thisDay = cal.get(Calendar.DAY_OF_MONTH);
        thisYear = cal.get(Calendar.YEAR);


        todayDate = thisYear + "-" + thisMonth + "-" + thisDay;
        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, loggedInUser);
            st.setString(2, isbn);
            st.setString(3, todayDate);
            st.executeUpdate();
//            r = st.executeQuery();


        } catch (Exception e) {

        }

        if (verbose) {
            System.out.println();
            System.out.println("RAW SQL: " + sql);
            System.out.println("PREPARED STATEMENT: " + st);
            System.out.println();
//            System.out.println("Today is " + thisYear + "-" + thisMonth + "-" + thisDay);
        }

        System.out.println("You have been added to the Wait List for book with ISBN: " + isbn + " with a Date Added of " + todayDate);

        //return to main menu
        Main.MainMenu();
    }

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
        String sql;
        ResultSet r = null;
        PreparedStatement st = null;

        System.out.println();
        System.out.println("Printing User Record...");
        System.out.println();
        System.out.print("Username to view record of: ");
        lookedUpUser = in.nextLine();

        if (!CheckForUser(lookedUpUser)) {
            System.out.println("User does not exist, returning to main menu");
            System.out.println();

        } else {
            if (verbose) {
                System.out.println("Printing record for " + lookedUpUser);
            }


        }

        System.out.println();
        //Printing personal data of user
        System.out.println("Personal data for " + lookedUpUser);

        sql = "SELECT * FROM " + userTable + " where username = ?";

        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, lookedUpUser);
//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                System.out.println("Full Name: " + r.getString("fullname"));
                System.out.println("Username: " + r.getString("username"));
                System.out.println("CardID: " + r.getString("cardid"));
                System.out.println("Email: " + r.getString("email"));
                System.out.println("Address: " + r.getString("address"));
                System.out.println("Phone Number: " + r.getString("phonenumber"));

            }

        } catch (Exception e) {

        }

        if (verbose) {
            System.out.println();
            System.out.println("RAW SQL: " + sql);
            System.out.println("PREPARED STATEMENT: " + st);
            System.out.println();

        }


        System.out.println();
        System.out.println("Checkout History for " + lookedUpUser);

        //print book checkout history for user

        sql = "SELECT isbn, title, checkoutdate, returndate FROM " + checkoutTable + " JOIN " + bookTable + " on " + checkoutTable + ".bookid = " + bookTable + ".bookid AND username = ?;";

        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, lookedUpUser);
//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                System.out.println("ISBN: " + r.getString("isbn") + "\t ");
                System.out.println("Title: " + r.getString("title") + "\t ");
                System.out.println("Checkout Date: " + r.getString("checkoutdate") + "\t ");
                System.out.println("Return Date: " + r.getString("returndate") + "\t ");
                System.out.println();

            }

        } catch (Exception e) {

        }

        if (verbose) {
            System.out.println();
            System.out.println("RAW SQL: " + sql);
            System.out.println("PREPARED STATEMENT: " + st);
            System.out.println();
//            System.out.println("Today is " + thisYear + "-" + thisMonth + "-" + thisDay);
        }

        System.out.println("Books lost by: " + lookedUpUser);

        //print books lost by user
        sql = "SELECT isbn, title, returndate FROM " + checkoutTable + " JOIN " + bookTable + " on " + checkoutTable + ".bookid = " + bookTable + ".bookid AND username = ? and lost = 1;";
        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, lookedUpUser);
//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                System.out.println("ISBN: " + r.getString("isbn") + "\t ");
                System.out.println("Title: " + r.getString("title") + "\t ");
                System.out.println("Date marked as Lost: " + r.getString("returndate") + "\t ");
                System.out.println();
            }

        } catch (Exception e) {

        }

        if (verbose) {
            System.out.println();
            System.out.println("RAW SQL: " + sql);
            System.out.println("PREPARED STATEMENT: " + st);
            System.out.println();
//            System.out.println("Today is " + thisYear + "-" + thisMonth + "-" + thisDay);
        }

        //print books books that user is on waitlist for
        System.out.println("Books requested for future checkout by " + lookedUpUser);
        System.out.println();

//        Select title from Waitlist JOIN Book on Waitlist.isbn = Book.isbn AND Waitlist.username = 'mrtestuser'
        sql = "SELECT title, dateadded from " + waitlistTable + " JOIN " + bookTable + " on " + waitlistTable + ".ISBN = " + bookTable + ".ISBN AND " + waitlistTable + ".username = ?";
        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, lookedUpUser);
//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                System.out.println("Title: " + r.getString("title") + "\t ");
                System.out.println("Date added to waitlist: " + r.getString("dateadded") + "\t ");
                System.out.println();
            }

        } catch (Exception e) {

        }

        if (verbose) {
            System.out.println();
            System.out.println("RAW SQL: " + sql);
            System.out.println("PREPARED STATEMENT: " + st);
            System.out.println();
//            System.out.println("Today is " + thisYear + "-" + thisMonth + "-" + thisDay);
        }

        //print reviews
//        SELECT title, score, opinion FROM Review JOIN Book on Review.ISBN = Book.ISBN WHERE username = 'garin'
        sql = "SELECT title, score, opinion FROM " + reviewTable + " JOIN " + bookTable + " on " + reviewTable + ".ISBN = " + bookTable + ".isbn WHERE username = ?;";
        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, lookedUpUser);
//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                System.out.println("Title: " + r.getString("title") + "\t ");
                System.out.println("Score: " + r.getString("score") + "\t ");
                System.out.println("Optional Text: " + r.getString("opinion"));
                System.out.println();
            }

        } catch (Exception e) {

        }

        if (verbose) {
            System.out.println();
            System.out.println("RAW SQL: " + sql);
            System.out.println("PREPARED STATEMENT: " + st);
            System.out.println();
//            System.out.println("Today is " + thisYear + "-" + thisMonth + "-" + thisDay);
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
        String newISBN;
        String newTitle;
        String newPublisher;
        String newYearPub;
        String newFormat;
        String newSubject;
        String newSummary;
        String sql;

        int numberOfAuthors;
        ResultSet r = null;
        PreparedStatement st = null;
        boolean multipleAuthors;



        System.out.println("Entering new book data...");
        System.out.println();

        System.out.print("ISBN: ");
        newISBN = in.nextLine();
        System.out.print("Title: ");
        newTitle = in.nextLine();

        //TODO: ERROR CHECK for existing isbn
        System.out.print("How many authors are there for this title? :");

        do {
            userSelection = in.nextLine();

            if (!Main.IsNumber(userSelection)) {
                System.out.print(userSelection + " is not a number, ");
                System.out.print("Please enter the number of authors: ");
            } else if (userSelection.matches("0")) {
                System.out.print("There must be at least one author, please enter the positive number of authors: ");
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
                System.out.print("Author # " + (i + 1) + ": ");
                authors[i] = in.nextLine();
            }

            multipleAuthors = true;
        }


        System.out.print("Publisher: ");
        newPublisher = in.nextLine();
        System.out.print("Year of Publication: ");
        newYearPub = in.nextLine();
        System.out.print("Subject: ");
        newSubject = in.nextLine();
        System.out.print("Format: ");
        newFormat = in.nextLine();
        System.out.print("Book Summary: ");
        newSummary = in.nextLine();

		/*
         * Show inputted information back to user to see if they have made a mistake
		 */
        System.out.println();
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


        if (choice == 0) {
            //the user has detected an error. restart process
            AddBookRecord();
        } else {
            //the user has entered the correct information


//            INSERT into Book (ISBN, title, booksummary, subject, format, pubyear, publisher) VALUES(?, ?, ?, ?, ?, ?, ?);

            sql = "INSERT into Inventory (ISBN, title, booksummary, subject, format, pubyear, publisher) VALUES(?, ?, ?, ?, ?, ?, ?)";

            try {

                //set sql parameters
                st = con.prepareStatement(sql);
                st.setString(1, newISBN);
                st.setString(2, newTitle);
                st.setString(3, newSummary);
                st.setString(4, newSubject);
                st.setString(5, newFormat);
                st.setString(6, newYearPub);
                st.setString(7, newPublisher);
            st.executeUpdate();
//                r = st.executeQuery();


            } catch (Exception e) {

            }

        }


        //return to main menu
        Main.MainMenu();

    }//end of AddBookRecord

    public static void AddBookCopy() {
        String isbntoaddto;
        int newcopies;
        String sql;
        ResultSet r = null;
        PreparedStatement st = null;

        String isbn = null;
        String title = null;
        String summary = null;
        String subject = null;
        String format = null;
        String pubyear = null;
        String publisher = null;
        String loc = null;

        String maxid = null;

        System.out.println("Adding new copies of a book...");
        System.out.println();

        //TODO: Check if isbn exists
        //Specify the isbn and number of copies to add
        System.out.print("ISBN of book to add copies to: ");
        isbntoaddto = in.nextLine();
        System.out.print("How many copies would you like to add? :");

        do {
            userSelection = in.nextLine();

            if (!Main.IsNumber(userSelection)) {
                System.out.print(userSelection + " is not a number, ");
                System.out.print("Please enter a number for the ISBN: ");
            } else {
                newcopies = Integer.parseInt(userSelection);
                break;
            }
        } while (true);

        sql = "SELECT * FROM " + inventoryTable + " where isbn = ?";

        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, isbntoaddto);
//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                isbn = r.getString("ISBN");
                title = r.getString("title");
                summary = r.getString("bookSummary");
                subject = r.getString("subject");
                format = r.getString("format");
                pubyear = r.getString("pubyear");
                publisher = r.getString("publisher");
                maxid = r.getString("bookid");
//                loc = r.getString("loc");

            }

        } catch (Exception e) {

        }

        if (verbose) {
            System.out.println();
            System.out.println("SQL RAW STRING: " + sql);
            System.out.println();
            System.out.println("PREPARED STATEMENT: " + st);
            System.out.println();
        }

        for (int i = 0; i < newcopies; i++) {

            //create new bookid entry
            sql = "INSERT INTO " + inventoryTable + " (bookid) VALUES(0)";

            try {

                //set sql parameters
                st = con.prepareStatement(sql);
//                st.setString(1, isbntoaddto);
                st.executeUpdate();
//                r = st.executeQuery();

                while (r.next()) {
                    System.out.println("Max book id: " + r.getString("max"));
                    maxid = r.getString("max");
                }

            } catch (Exception e) {

            }

            if (verbose) {
                System.out.println();
                System.out.println("SQL RAW STRING: " + sql);
                System.out.println();
                System.out.println("PREPARED STATEMENT: " + st);
                System.out.println();
            }

            //grab previously added book entry
            sql = "SELECT MAX(bookid) as max FROM " + inventoryTable;

            try {

                //set sql parameters
                st = con.prepareStatement(sql);
//                st.setString(1, isbntoaddto);
//                st.executeUpdate();
                r = st.executeQuery();
//
                while (r.next()) {
                    System.out.println("Max book id: " + r.getString("max"));
                    maxid = r.getString("max");
                }

            } catch (Exception e) {

            }

            if (verbose) {
                System.out.println();
                System.out.println("SQL RAW STRING: " + sql);
                System.out.println();
                System.out.println("PREPARED STATEMENT: " + st);
                System.out.println();
            }

            sql = "SELECT bookid FROM " + bookTable + " WHERE ISBN = ? ;";

            try {

                //set sql parameters
                st = con.prepareStatement(sql);
                st.setString(1, isbntoaddto);
//            st.executeUpdate();
                r = st.executeQuery();

                while (r.next()) {
                    System.out.println("Max book id: " + r.getString("max"));
                    maxid = r.getString("max");
                }

            } catch (Exception e) {

            }

            if (verbose) {
                System.out.println();
                System.out.println("SQL RAW STRING: " + sql);
                System.out.println();
                System.out.println("PREPARED STATEMENT: " + st);
                System.out.println();
            }

//            INSERT INTO Book (bookid, ISBN, title, bookSummary, subject, format, pubyear, publisher, loc) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            sql = "INSERT INTO " + bookTable + " (bookid, ISBN, title, bookSummary, subject, format, pubyear, publisher, loc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

            try {

                //set sql parameters
                st = con.prepareStatement(sql);
                st.setString(1, maxid);
                st.setString(2, isbn);
                st.setString(3, title);
                st.setString(4, summary);
                st.setString(5, subject);
                st.setString(6, format);
                st.setString(7, pubyear);
                st.setString(8, publisher);
                st.setString(9, "83");
                st.executeUpdate();
//                r = st.executeQuery();

//                while(r.next())
//                {
//                    maxid = r.getString("bookid");
//                }

            } catch (Exception e) {

            }

            if (verbose) {
                System.out.println();
                System.out.println("SQL RAW STRING: " + sql);
                System.out.println();
                System.out.println("PREPARED STATEMENT: " + st);
                System.out.println();
            }


        }
        //TODO: books having multiple copies may be kind of broken.

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
        System.out.print("Year: ");
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
        boolean check;

        //TODO: ERROR CHECK has user already left a review for this book? That isn't allowed...

        String sql;
        ResultSet r = null;
        PreparedStatement st = null;

        System.out.println("Leaving a Review...");
        System.out.println();

        if (verbose) {
            System.out.println("Currently logged in user: " + loggedInUser);
        }

        System.out.print("What is the ISBN of the book you wish to leave a review for? :");
        isbnreview = in.nextLine();

        check = Bookshelf.CheckForISBN(isbnreview);

        //if the username doesn't exist, prompt user again for existing username
        while (!check) {
            System.out.print(isbnreview + " does not exist, please enter a valid ISBN: ");
            isbnreview = in.nextLine();
            check = Bookshelf.CheckForISBN(isbnreview);
        }

        System.out.print("Please enter a score [1 - 10]: ");
        try {
            do {
                userSelection = in.nextLine();

                if (!Main.IsNumber(userSelection)) {
                    System.out.print(userSelection + " is not a number, ");
                    System.out.print("Please enter a number for this user's ID: ");
                } else {
                    choice = Integer.parseInt(userSelection);
                    if (choice < 1 || choice > 10) {
                        System.out.print("Please enter a number from 1 - 10: ");
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


        if (choice == 1) {
            System.out.print("Your Text: ");
            userOpinion = in.nextLine();
            System.out.println();
            System.out.println("Thank you for the review for " + isbnreview + ", " + loggedInUser + "!");
//                System.out.println("Thank you for the review for " + isbnreview + ": " + userOpinion);
            System.out.println("Opinion: " + userOpinion);
            System.out.println("Username: " + loggedInUser);
            System.out.println("Score: " + score);

//            Insert into Review (username, ISBN, score, opinion) VALUES("garin", "376418621-6", 13, "duerp")

            //TODO: SG Not using proper naming vars
            sql = "Insert into Review (username, ISBN, score, opinion) VALUES(?, ?, ?, ?)";

            try {

                //set sql parameters
                st = con.prepareStatement(sql);
                st.setString(1, loggedInUser);
                st.setString(2, isbnreview);
                st.setString(3, Integer.toString(score));
                st.setString(4, userOpinion);
                st.executeUpdate();
//                r = st.executeQuery();

            } catch (Exception e) {

            }


            if (verbose) {
                System.out.println();
                System.out.println("SQL RAW STRING: " + sql);
                System.out.println();
                System.out.println("PREPARED STATEMENT: " + st);
                System.out.println();
            }


        } else {
            System.out.println("Thank you for the review for " + isbnreview + ", " + loggedInUser + "!");
            System.out.println("Username: " + loggedInUser);
            System.out.println("Score: " + score);

            //TODO: SG Not using proper naming vars
            sql = "Insert into Review (username, ISBN, score) VALUES(?, ?, ?)";

            try {

                //set sql parameters
                st = con.prepareStatement(sql);
                st.setString(1, loggedInUser);
                st.setString(2, isbnreview);
                st.setString(3, Integer.toString(score));

                st.executeUpdate();
//                r = st.executeQuery();

            } catch (Exception e) {

            }


            if (verbose) {
                System.out.println();
                System.out.println("SQL RAW STRING: " + sql);
                System.out.println();
                System.out.println("PREPARED STATEMENT: " + st);
                System.out.println();
            }

        }

        //TODO: Create sql statement that would add this to the reviews table

        //return to main menu
        Main.MainMenu();

    }//end of LeaveReview


    public static boolean CheckForISBN(String isbn) {
        boolean found = false;
        ResultSet r = null;
        PreparedStatement st = null;

        //construct sql query
        String sql = "SELECT ISBN FROM " + bookTable + " WHERE ISBN = ?";

        try {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            r = st.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();

        }

        if (verbose) {
            System.out.println();
            System.out.println("SQL RAW STRING: " + sql);
            System.out.println();
            System.out.println("PREPARED STATEMENT: " + st);
            System.out.println();
        }

        //check results for results if database has username
        try {

            while (r.next()) {
                if (r.getString("ISBN").matches(isbn) && found == false) {
                    found = true;
                    break;
                }

            }
        } catch (Exception e) {

        }

        return found;

    }

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

        String publisher;

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

        System.out.print("Please enter the publisher: ");
        publisher = in.nextLine();

        System.out.print("Please enter title words separated by a comma: ");
        userSelection = in.nextLine();
        String[] wordsInTitle = userSelection.split(",");

        System.out.print("Please enter subject(s) separated by a comma: ");
        userSelection = in.nextLine();
        String subjects[] = userSelection.split(",");


        if (verbose) {
            System.out.println("Authors:");

            for (int i = 0; i < authors.length; i++) {
                System.out.println(authors[i]);
            }

            System.out.println("Publisher: " + publisher);

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

        //TODO: Not printing above inputted information

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

        ResultSet r = null;
        PreparedStatement st = null;
        ResultSet r2 = null;
        PreparedStatement st2 = null;

        String returndate;

        String sql, sql2;


        boolean lost;

        System.out.println("Returning a book...");
        System.out.println();

        System.out.print("Please enter the ISBN of the book you wish to return: ");
        do {
            userSelection = in.nextLine();



            if(!CheckForISBN(userSelection))
            {
                System.out.print("ISBN does not exist, please try again: " );
            }
            else
            {
                ISBN = userSelection;
                break;
            }
            //TODO: ERROR Check if ISBN Exists
//            if (/*!Main.IsNumber(userSelection) ||*/ userSelection.length() != 13) {
//                System.out.print("Not a valid ISBN, please try again: ");
//            } else {
////                ISBN = userSelection;
//                break;
//            }

        } while (true);

        System.out.println("Please enter the date of which this book was returned: ");
        System.out.print("What month was this book returned? [1 - 12]: ");
        month = in.nextLine();
        System.out.print("What day was this book returned? [1 - 31]: ");
        day = in.nextLine();
        System.out.print("What year was this book returned? [YYYY]: ");
        year = in.nextLine();

        if (verbose) {
            System.out.println("User returning book on " + month + "/" + day + "/" + year);
        }

        //create return date
        returndate = year + "-" + month + "-" + day;

        System.out.print("Was this book returned or lost? [1] yes [0] no: ");


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


        //determine whether book was lost
        if (choice == 1) {
            lost = true;
        } else {
            lost = false;
        }



        if (lost) {
            //specify checkout record as lost and set return date


        } else {    //book is not lost


            sql = "UPDATE CheckoutRecord SET returndate=? where isbn=? and username = ?";

            sql2 = "SELECT username FROM Waitlist WHERE isbn = ?";

            try{
                st = con.prepareStatement(sql);
                st.setString(1,returndate);
                st.setString(2, ISBN);
                st.setString(3,loggedInUser);
                st.executeUpdate();



            } catch (Exception e)
            {

            }

            try{
                st2 = con.prepareStatement(sql2);
                st2.setString(1, ISBN);
                r2 = st2.executeQuery();

                System.out.println("Current Waitlist: " );
                while(r2.next())
                {
                    System.out.println("\t" + r2.getString("username"));
                }

            } catch (Exception e)
            {

            }

            Main.MainMenu();

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
        String sql;
        String sqltwo;
        ResultSet r = null;
        ResultSet r2 = null;
        PreparedStatement st2 = null;
        PreparedStatement st = null;
        int copies;

        String isbn = null;
        String title = null;
        String summary = null;
        String subject = null;
        String format = null;
        String pubyear = null;
        String publisher = null;
        String authors = null;
        String loc = null;
        String bid = null;
        String[] Authors = null;

        int c = 0;
        int numberOfAuthors = 0;

        System.out.println("Printing Book Record...");
        System.out.println();

        System.out.print("Please enter the ISBN of the book you wish to look up: ");

        //TODO: ERROR CHECK See if book even exists via isbn

        do {
            userSelection = in.nextLine();


            if (Main.IsNumber(userSelection)) {
                System.out.print("Not a valid ISBN, please try again: ");
            } else {
                isbn = userSelection;
                break;
            }

        } while (true);

        //Grab book information

        sql = "SELECT * FROM " + bookTable + " where isbn = ? ";
        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
//            st.executeUpdate();
            r = st.executeQuery();




            while (r.next()) {
                isbn = r.getString("ISBN");
                title = r.getString("title");
                summary = r.getString("bookSummary");
                subject = r.getString("subject");
                format = r.getString("format");
                pubyear = r.getString("pubyear");
                publisher = r.getString("publisher");
                loc = r.getString("loc");
            }


        } catch (Exception e) {

        }

//        Select authorname from Authors where isbn = '871027694-7'

        sql = "SELECT authorname FROM " + authorTable + " where isbn = ? ";
        sqltwo = "SELECT count(authorname) as numberofauthors FROM " + authorTable + " WHERE isbn = ? ";

        try {

            //set sql parameters
            st = con.prepareStatement(sql);

            st.setString(1, isbn);
//            st.executeUpdate();
            r = st.executeQuery();

            st2 = con.prepareStatement(sqltwo);
            st2.setString(1, isbn);
            r2 = st2.executeQuery();




            while(r2.next())
            {
                if(r2.getString("numberofauthors").matches("0"))
                {
                    numberOfAuthors = 0;
                }

                else {
                    numberOfAuthors = Integer.parseInt(r2.getString("numberofauthors"));
                }
            }

            Authors = new String[numberOfAuthors];

            while (r.next()) {

                Authors[c] = r.getString("authorname");
                c++;
            }


        } catch (Exception e) {

        }

        if (verbose) {
            System.out.println();
            System.out.println("SQL RAW STRING: " + sql);
            System.out.println("PREPARED STATEMENT: " + st);

            System.out.println("SQL RAW STRING TWO: " + sqltwo);
            System.out.println("PREPARED STATEMENT TWO: " + st2);

            System.out.println();
            for (int i = 0; i < numberOfAuthors; i++)
            {
                System.out.println("Author " + i + " " + Authors[i]);
            }
        }


        System.out.println();
        //print book info
        System.out.println("Book Info:");
        System.out.println("Title: " + title);
        System.out.print("Author(s): \n");



        for(int i = 0; i < numberOfAuthors; i++)
        {
            System.out.println("\t\t" + Authors[i] + " ");
        }

        System.out.println("ISBN: " + isbn);
        System.out.println("Summary: " + summary);
        System.out.println("Subject: " + subject);
        System.out.println("Format: " + format);
        System.out.println("Publisher: " + publisher);
        System.out.println("Year Published: " + pubyear);
        System.out.println();


        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                isbn = r.getString("ISBN");
                title = r.getString("title");
                loc = r.getString("loc");
                bid = r.getString("bookid");


                System.out.println("Title: " + title);
                System.out.println("ISBN: " + isbn);
                System.out.println("Unique Book ID: " + bid);
                System.out.println("Location " + loc);
                System.out.println();

            }

        } catch (Exception e) {

        }

        //TODO: SG: query not using proper table vars
        //print users who had the book and the dates they had the book
        System.out.println("Usernames of previous borrowers and dates (if applicable): ");

        sql = "SELECT username, checkoutdate, returndate FROM CheckoutRecord c, Book b WHERE c.bookid = b.bookid AND b.ISBN = ?;";

        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                System.out.println("User: " + r.getString("username"));
                System.out.println("\t\t\t" + "Checkout Date: " + r.getString("checkoutdate") + "\t" + "Return Date: " + r.getString("returndate"));

            }

        } catch (Exception e) {

        }

//        Select * from Review where isbn = '201410348-7';
        System.out.println();
        System.out.println("Book Reviews (if applicable): ");

        //TODO: Not using proper table vars
        sql = "SELECT username, score, opinion FROM Review WHERE isbn = ? ;";

        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                System.out.println("User: " + r.getString("username"));

                System.out.println("\t\t\t" + "Score: " + r.getString("score") + "\t" + "Optional Opinion: " + r.getString("opinion"));

            }

        } catch (Exception e) {

        }

        System.out.println();

//        Select avg(score) from Review where isbn = '201410348-7';

        //TODO: Not using proper table vars
        sql = "SELECT AVG(score) as avg FROM Review WHERE isbn = (?) ";

        try {


            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, isbn);

            r = st.executeQuery();

            while (r.next()) {
                System.out.println("Average Review Score: " + r.getString("avg"));

            }

        } catch (Exception e) {

        }

        System.out.println();

        Main.MainMenu();


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
        String sql;
        ResultSet r = null;
        PreparedStatement st = null;
        String topFrequency = null;
        String count = null;

        System.out.println("Printing User Statistics...");
        System.out.println();

        System.out.println("Do you want to..");
        System.out.println("List top users who have checked out the most books [1]");
        System.out.println("List top users who have rated the most number of books [2]");
        System.out.println("List top users who have lost the most books [3]");
        System.out.print("Please make a selection: ");

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

            System.out.println();
            System.out.println("Printing users who have checked out the most books");
            System.out.println();
            //construct sql query
            //TODO: Not using proper table vars
            sql = "SELECT COUNT(username) AS maxcheckoutcount FROM CheckoutRecord GROUP BY username ORDER BY maxcheckoutcount DESC LIMIT 1";

            try {
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while (r.next()) {
                    topFrequency = r.getString("maxcheckoutcount");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


            if (verbose) {
                System.out.println();
                System.out.println("SQL RAW STRING: " + sql);
                System.out.println();
                System.out.println("PREPARED STATEMENT: " + st);
                System.out.println();
            }

            //TODO: Not using proper table vars
            sql = "Select username, count(username) as count from CheckoutRecord group by username";

            try {
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while (r.next()) {
                    count = r.getString("count");
                    if (count.matches(topFrequency)) {
                        System.out.println("User: " + r.getString("username") + " Books checked out: " + count);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            System.out.println("The top user checked out " + topFrequency + " books");


            if (verbose) {
                System.out.println();
                System.out.println("SQL RAW STRING: " + sql);
                System.out.println();
                System.out.println("PREPARED STATEMENT: " + st);
                System.out.println();
            }

        }

        //users who rated the most number of books
        if (choice == 2) {
            System.out.println();
            System.out.println("Printing users who have reviewed the most books");
            System.out.println();
            //construct sql query
            //TODO: Not using proper table vars
            sql = "SELECT COUNT(username) AS reviewcount FROM Review GROUP BY username ORDER BY reviewcount DESC LIMIT 1";

            try {
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while (r.next()) {
                    topFrequency = r.getString("reviewcount");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


            if (verbose) {
                System.out.println();
                System.out.println("SQL RAW STRING: " + sql);
                System.out.println();
                System.out.println("PREPARED STATEMENT: " + st);
                System.out.println();
            }

            //TODO: Not using proper table vars
            sql = "Select username, count(username) as count from Review group by username";

            try {
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while (r.next()) {
                    count = r.getString("count");
                    if (count.matches(topFrequency)) {
                        System.out.println("User: " + r.getString("username") + " Books Reviewed: " + count);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            System.out.println("The top user reviewed " + topFrequency + " books");


            if (verbose) {
                System.out.println();
                System.out.println("SQL RAW STRING: " + sql);
                System.out.println();
                System.out.println("PREPARED STATEMENT: " + st);
                System.out.println();
            }

        }

        //users who lost the most books
        if (choice == 3) {
            System.out.println();
            System.out.println("Printing users who have lost the most books");
            System.out.println();
            //construct sql query
            //TODO: Not using proper table vars
            sql = "SELECT COUNT(username) AS lostcount FROM CheckoutRecord where CheckoutRecord.lost = 1 GROUP BY username ORDER BY lostcount DESC LIMIT 1";

            try {
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while (r.next()) {
                    topFrequency = r.getString("lostcount");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


            if (verbose) {
                System.out.println();
                System.out.println("SQL RAW STRING: " + sql);
                System.out.println();
                System.out.println("PREPARED STATEMENT: " + st);
                System.out.println();
            }

            //TODO: Not using proper table vars
            sql = "Select username, count(username) as count from CheckoutRecord where CheckoutRecord.lost = 1 group by username";

            try {
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while (r.next()) {
                    count = r.getString("count");
                    if (count.matches(topFrequency)) {
                        System.out.println("User: " + r.getString("username") + " Books lost: " + count);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            System.out.println("The top user lost " + topFrequency + " books");


            if (verbose) {
                System.out.println();
                System.out.println("SQL RAW STRING: " + sql);
                System.out.println();
                System.out.println("PREPARED STATEMENT: " + st);
                System.out.println();
            }
        }

        Main.MainMenu();

    }//end of PrintUserStatistics

    /**
     * Checks if a specific user already exists in the database.
     *
     * @param username, name to check for
     * @return, boolean indicating whether the user was already found in the database
     */
    public static boolean CheckForUser(String username) {
        boolean found = false;
        ResultSet r = null;
        PreparedStatement st = null;

        //construct sql query
        String sql = "SELECT username FROM " + userTable + " WHERE username = ?";
    //TODO: ERROR showing error when ? was used, just built string
        try {
            st = con.prepareStatement(sql);
            st.setString(1, username);
            r = st.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();

        }

        if (verbose) {
            System.out.println();
            System.out.println("SQL RAW STRING: " + sql);
            System.out.println();
            System.out.println("PREPARED STATEMENT: " + st);
            System.out.println();
        }

        //check results for results if database has username
        try {

            while (r.next()) {
                if (r.getString("username").matches(username) && found == false) {
                    found = true;
                    break;
                }

            }
        } catch (Exception e) {

        }

        return found;
    }


}
