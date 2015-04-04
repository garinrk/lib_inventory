package garinrphase2;

import com.sun.prism.impl.Disposer;

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
public class Database {

    /* Globals */
    static boolean verbose = false;

    /* Used for user input and input parsing */
    static Scanner in = new Scanner(System.in);
    static int choice = 0;
    static String userSelection = null;

    //Information related to the newly added user
    public static String newUsername;
    static String newID;
    static String newFullName;
    static String newAddress;
    static String newPhoneNumber;
    static String newEmail;

    //Used in conjunction with sql queries
//    static String invTable = "Inventory";
//    static String bookTable = "Book";
//    static String checkoutTable = "CheckoutRecord";
//    static String inventoryTable = "Inventory";
//    static String reviewTable = "Review";
//    static String userTable = "User";
//    static String waitlistTable = "Waitlist";
//    static String authorTable = "Authors";

    static String AuthorListTable = "_Authors";
    static String CheckoutRecordTable = "_CheckoutRecord";
    static String InventoryTable = "_Inventory";
    static String RecordsTable = "_Records";
    static String ReviewTable = "_Review";
    static String UserTable = "_UserTable";
    static String WaitListTable = "_WaitList";

    /* Represents the currently logged in user using the library */
    static String loggedInUser = null;

    /* Used for calculating dates */
    static Calendar c = Calendar.getInstance();

    /* Related to the SQL Connection */
    static Statement statem = null;
    static Connection con = null;

    public static void AddBookRecord()
    {
        String newISBN;
        String newTitle;
        String newPublisher;
        String newYearPub;
        String newFormat;
        String newSubject;
        String newSummary;
        String sql;
        boolean multipleAuthors;
        int numberOfAuthors;
        boolean check;

        ResultSet r = null;
        PreparedStatement st = null;

        System.out.println("Entering new book record...");
        System.out.println();
        System.out.print("ISBN: ");

        do {
            newISBN = in.nextLine();

            check = CheckForISBN(newISBN);

            if(check)
            {
                System.out.print("That ISBN is already in the database, please enter a new UNIQUE isbn: ");
                newISBN = in.nextLine();
            }
            else
                break;

        } while(true);

        System.out.print("Title: " );
        newTitle = in.nextLine();

        System.out.println("How many authors are there for this new record?: ");

        do {
            userSelection = in.nextLine();

            if (!Console.IsNumber(userSelection)) {
                System.out.print(userSelection + " is not a number, ");
                System.out.print("Please enter the number of authors: ");
            } else if (userSelection.matches("0")) {
                System.out.print("There must be at least one author, please enter the positive number of authors: ");
            } else {
                numberOfAuthors = Integer.parseInt(userSelection);
                break;
            }
        } while (true);

        //create array to hold names of multiple authors for the title
        String[] authors = new String[numberOfAuthors];

        //add user to enter authors
        for(int i = 0; i < numberOfAuthors; i++)
        {
            System.out.print("Author #" + (i + 1) + ": ");
            authors[i] = in.nextLine();
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

        //have user verify information
        System.out.println();
        System.out.println("Is the following data correct?");
        System.out.println("Title: " + newTitle);
        System.out.println("ISBN: " + newISBN);
        System.out.print("Author(s): ");

        for(int i = 0; i < numberOfAuthors; i++)
        {
            System.out.println("\t" + authors[i]);
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

        if(choice == 0)
        {
            //the user detected an error, restart process
            AddBookRecord();
        }
        else
        {
            //the user entered the correct info! add information to records database
            sql = "INSERT INTO " + RecordsTable + " (isbn, title, publisher, pubyear, format, subject, booksummary) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try
            {
                st = con.prepareStatement(sql);
                st.setString(1, newISBN);
                st.setString(2, newTitle);
                st.setString(3, newSummary);
                st.setString(4, newSubject);
                st.setString(5, newFormat);
                st.setString(6, newYearPub);
                st.setString(7, newPublisher);
                st.executeUpdate();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            //add authors to authors table
            sql = "INSERT INTO " + AuthorListTable + " (isbn, authorname) VALUES (?, ?)";

            for(int i = 0; i < numberOfAuthors; i++) {
                try {
                    st = con.prepareStatement(sql);
                    st.setString(1, newISBN);
                    st.setString(2, authors[i]);
                    st.executeUpdate();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

        }

        Console.MainMenu();
    }//end of add book record

    public static void AddBookCopy()
    {
        String isbn;
        String id;
        String copiestoadd;
        int existingcopies = 0;
        boolean check;
        boolean noexistingrecord = false;

        ResultSet r = null;
        PreparedStatement st = null;
        String sql;

        System.out.println("Adding new copies of a book...");
        System.out.println();

        System.out.print("ISBN to add copies to: ");

        do {
            isbn = in.nextLine();

            check = CheckForISBN(isbn);

            if(!check)
            {
                System.out.print("That ISBN does not exist in the database, please enter an existing isbn: ");

            }
            else
                break;

        } while(true);

        System.out.print("How many copies would you like to add? :");

        do {
            userSelection = in.nextLine();

            if (!Main.IsNumber(userSelection)) {
                System.out.print(userSelection + " is not a number, ");
                System.out.print("Please enter a number for the number of copies to add: ");
            } else {
                copiestoadd = userSelection;
                break;
            }
        } while (true);

        //we must check if there already exists a record for this isbn in the inventory database

        sql = "SELECT copies FROM " + InventoryTable + " where isbn = ?";

        try
        {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            r = st.executeQuery();


            //if the query returned nothing, we need to add a new record
            if(!r.next())
            {
                 noexistingrecord = true;
            }

            //else, there already is a record.
            else
            {
                existingcopies = Integer.parseInt(r.getString("copies"));
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }


        if(noexistingrecord)
        {
            //we have to create a record in the inventory table
            //we need to get the book's r_id for the entry
            id = GetRecordID(isbn);
            sql = "INSERT INTO " + InventoryTable + " (r_id, isbn, copies) VALUES(?, ?, ?)";

            try
            {
                st = con.prepareStatement(sql);
                st.setString(1, id);
                st.setString(2, isbn);
                st.setString(3, copiestoadd);
                st.executeUpdate();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            System.out.println("You have added " + copiestoadd + " copies of book with ISBN: " + isbn + " to the database, thank you!");
        }

        else
        {
            //there already exists a record in the inventory table, update that existing record with the new number of copies

//            sql = "UPDATE " + RecordsTable + " SET checkoutcount = checkoutcount + 1 WHERE isbn = ?";
            sql = "UPDATE " + InventoryTable + " SET copies = copies + ? WHERE isbn = ?";
            try
            {
                st = con.prepareStatement(sql);
                st.setString(1, copiestoadd);
                st.setString(2,isbn);
                st.executeUpdate();

            } catch (Exception e)
            {
                e.printStackTrace();
            }

            System.out.println("You have added " + copiestoadd + " copies of book with ISBN: " + isbn + " to the database, thank you!");
        }

        //return to main menu
        Console.MainMenu();

    }

    public static String GetRecordID(String isbn)
    {
        ResultSet r = null;
        PreparedStatement st = null;
        String sql;
        String id = "";

        sql = "SELECT r_id FROM " + RecordsTable + " WHERE isbn = ?";

        try
        {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            r = st.executeQuery();

            while(r.next())
            {
                id = r.getString("r_id");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return id;

    }//end of GetRecordID

    /**
     * Adds user to the waitlist for a specified ISBN
     *
     * @param isbn
     */
    public static void AddToWaitList(String isbn, Date today) {

        //TODO: INCREMENT WAITCOUNT on isbn in records table
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

        sql = "SELECT username FROM " + WaitListTable + " where isbn = ?;";
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


        sql = "INSERT INTO " + WaitListTable + " (username, isbn, dateadded) VALUES (?, ?, ?);";

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

        if(verbose)
            PrintSQLStatement(st, sql);

        System.out.println("You have been added to the Wait List for book with ISBN: " + isbn + " with a Date Added of " + todayDate);

        //return to main menu
        Main.MainMenu();
    }

    public static void AddUser()
    {
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

        sql = "INSERT INTO " + UserTable + " (username, cardid, full_name, email, address, phonenumber) VALUES (?, ?, ?, ?, ?, ?)";

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
//            r = st.executeQuery();


        } catch (Exception e) {

        }
        if(verbose)
            PrintSQLStatement(st, sql);

        setLoggedInUser(newUsername);
        Main.setLoggedInUser(newUsername);

        Main.MainMenu();
    }

    public static void BrowseLibrary()
    {}

    public static boolean CheckForCheckoutRecord(String user, String isbn)
    {
        boolean found = false;

        String sql;
        PreparedStatement st;
        ResultSet r = null;


        sql = "SELECT username FROM " + CheckoutRecordTable + " c WHERE isbn = ? AND username = ?";

        try {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            st.setString(2, user);

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

    }//end of CheckForCheckoutRecord

    public static boolean CheckForISBN(String isbn) {
        boolean found = false;
        ResultSet r = null;
        PreparedStatement st = null;

        //construct sql query
        String sql = "SELECT isbn FROM " + RecordsTable + " WHERE isbn = ?";

        try {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            r = st.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();

        }

        if(verbose)
            PrintSQLStatement(st, sql);

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

    public static boolean CheckForReview(String username, String isbn)
    {
        boolean found = false;
        ResultSet r = null;
        PreparedStatement st = null;

        String sql = "SELECT username FROM " + ReviewTable + " WHERE username = ? AND isbn = ?";

        try{
            st = con.prepareStatement(sql);
            st.setString(1, username);
            st.setString(2, isbn);
            r = st.executeQuery();

            if(!r.next())
            {
                return found;
            }

            else
            {
                found = true;
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }


        return found;
    }

    public static boolean CheckForUser(String username)
    {
        boolean found = false;
        ResultSet r = null;
        PreparedStatement st = null;

        //construct sql query
        String sql = "SELECT username FROM " + UserTable + " WHERE username = ?";

        try {
            st = con.prepareStatement(sql);
            st.setString(1, username);
            r = st.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();

        }

        if(verbose)
            PrintSQLStatement(st, sql);

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

    public static void CheckoutBook(Date today)
    {
        String isbn, duedate, oldestuser = null, todayDate;
        boolean check = false, availableforcheckout = false;
        int thisMonth = 0, thisYear = 0, thisDay = 0;

        String sql;
        ResultSet r = null;
        PreparedStatement st = null;

        System.out.println("Checking out a book...");
        System.out.println();
        System.out.print("Please enter the ISBN of the book you wish to print out: ");

        //make sure that isbn exists in table
        do {
            userSelection = in.nextLine();
            check = CheckForISBN(userSelection);


            if (!check) {
                System.out.print("ISBN doesn't exist. Please try again: ");
            } else {
                isbn = userSelection;
                break;
            }

        } while (true);

        //get today's date
        c.setTime(today);

        thisMonth = c.get(Calendar.MONTH) + 1;
        thisDay = c.get(Calendar.DAY_OF_MONTH);
        thisYear = c.get(Calendar.YEAR);


        todayDate = thisYear + "-" + thisMonth + "-" + thisDay;

        //compute 30 days ahead
        c.add(Calendar.DATE, 30);

        //save duedate
        int futureMonth = c.get(Calendar.MONTH) + 1;
        int futureDay = c.get(Calendar.DAY_OF_MONTH);
        int futureYear = c.get(Calendar.YEAR);

        //create date string that will be inserted
        duedate = Integer.toString(futureYear) + "-" + Integer.toString(futureMonth) + "-" + Integer.toString(futureDay);

        sql = "SELECT username AS oldestuserforisbn FROM " + WaitListTable + " WHERE isbn = ? AND dateadded = (SELECT MIN(dateadded) from " + WaitListTable + " where isbn =  ? )";

        try {


            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            st.setString(2, isbn);
//            st.executeUpdate();
            r = st.executeQuery();

            if (!r.wasNull()) {
                while (r.next()) {
                    oldestuser = r.getString("oldestuserforisbn");
                }


            }

        } catch (Exception e) {

        }

        if(verbose)
            PrintSQLStatement(st, sql);

        //if there was no waitlist for the book
        if(oldestuser == null)
        {
            System.out.println("There was no waitlist for the book, you may check it out!");
            availableforcheckout = true;
        }
        //there is a waitlist for the book
        else {
            //the oldest user SHOULD match the current user.
            availableforcheckout = oldestuser.matches(loggedInUser);
        }

        //if the book isn't available to checkout, and they aren't at the top of the list to check out
        //prompt the user with the option to be added to the book's waitlist

        if(!availableforcheckout && oldestuser != null)
        {
            System.out.print("There is a waitlist for this book, and you are not at the top if the list" +
                    " would you like to be added to the waitlist? [1] Yes [2] No: ");

            //ask user for choice, check for correctness
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

            //user would like to be added to a waitlist
            if(choice == 1)
            {
                AddToWaitList(isbn, today);
            }
            else
            {
                //exit and return to main menu
                System.out.println("You will NOT be added to the waitlist, returning to main menu...\n");
                Console.MainMenu();
            }
        }

        //However, if the book IS available to checkout, create a checkout record for the user
        else
        {
            check = CheckForCheckoutRecord(loggedInUser, isbn);

            //if the user doesn't already have a checkout record for this book,
            if(!check)
            {
                //create one
                CreateCheckoutRecord(loggedInUser, duedate, todayDate, isbn);
            }
            else
            {
                System.out.println("You already have a checkout record for this book. Returning to main menu...\n");
            }
        }

        Main.MainMenu();


    }//end of checkoutbook

    public static void CreateCheckoutRecord(String user, String due, String today, String isbn) {

        ResultSet r = null;
        PreparedStatement st = null;
        String sql;

        boolean backtomenu = false;
        int copies = 0;
        String title = "DEFAULT";

        //We need to check first if there are copies available to checkout

        sql = "SELECT copies FROM " + InventoryTable + " WHERE isbn = ?";

        try
        {
            st = con.prepareStatement(sql);
            st.setString(1,isbn);

            r = st.executeQuery();

            while(r.next())
            {
                if(r.getString("copies").matches("0"))
                {
                    System.out.println("There are no copies of this book to check out!");
                    backtomenu = true;
                }
                else
                    copies = Integer.parseInt(r.getString("copies"));
            }
        } catch (Exception e)
        {}

        if(verbose)
            PrintSQLStatement(st, sql);

        if(backtomenu)
        {
            Console.MainMenu();
        }

        //There are copies to checkout! We need to insert a new record into the checkout table and also incremement
        //the number of times a book has been checked out in the records table, but first we need the book's title.


        sql = "SELECT title FROM " + RecordsTable + " WHERE isbn = ?";

        try
        {
            st = con.prepareStatement(sql);
            st.setString(1,isbn);

            r = st.executeQuery();

            while(r.next()) {
                title = r.getString("title");
            }

        } catch(Exception e)
        {
            e.printStackTrace();
        }

        //now that we have the username, isbn, title, and the pertinent dates, we can insert the book into the checkout record table
        sql = "INSERT INTO " + CheckoutRecordTable + " (username, isbn, title, checkoutdate, duedate) VALUES (?, ?, ?, ?, ?)";

        try
        {
            st = con.prepareStatement(sql);
            st.setString(1, loggedInUser);
            st.setString(2, isbn);
            st.setString(3, title);
            st.setString(4, today);
            st.setString(5, due);
            st.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        //Don't forget, we must incrememnt the number of times that this book has been checked in the records table
        sql = "UPDATE " + RecordsTable + " SET checkoutcount = checkoutcount + 1 WHERE isbn = ?";

        try
        {
            st = con.prepareStatement(sql);
            st.setString(1,isbn);
            st.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        //finally, we must also decrement the number of copies that we have of this book in the library
        sql = "UPDATE " + InventoryTable + " SET copies = copies - 1 WHERE isbn = ?";

        try{
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            st.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("Thank you " + loggedInUser + " you now have a checkout record for " + title);
        System.out.println();

        Console.MainMenu();

    }



    public static void LeaveReview() {
        String isbnreview;
        int score;
        String userOpinion;
        boolean check;


        String sql;
        ResultSet r = null;
        PreparedStatement st = null;

        System.out.println("Leaving a Review...");
        System.out.println();

        System.out.print("What is the ISBN of the book you wish to leave a review for? :");
        isbnreview = in.nextLine();

        check = Database.CheckForISBN(isbnreview);

        //if the username doesn't exist, prompt user again for existing username
        while (!check) {
            System.out.print(isbnreview + " does not exist, please enter a valid ISBN: ");
            isbnreview = in.nextLine();
            check = Database.CheckForISBN(isbnreview);
        }

        if(CheckForReview(loggedInUser, isbnreview))
        {
            System.out.println("You already have left a review for that book, you cannot enter duplicate reviews. Returning to main menu...");
            Main.MainMenu();
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
            System.out.println("Opinion: " + userOpinion);
            System.out.println("Username: " + loggedInUser);
            System.out.println("Score: " + score);

//            Insert into Review (username, ISBN, score, opinion) VALUES("garin", "376418621-6", 13, "duerp")


            sql = "INSERT INTO " + ReviewTable + " (username, ISBN, score, opinion) VALUES(?, ?, ?, ?)";

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


            if(verbose)
                PrintSQLStatement(st, sql);

        }
        else
            {
            System.out.println("Thank you for the review for " + isbnreview + ", " + loggedInUser + "!");
            System.out.println("Username: " + loggedInUser);
            System.out.println("Score: " + score);

            sql = "INSERT INTO " + ReviewTable + " (username, ISBN, score) VALUES(?, ?, ?)";

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
            if(verbose)
                PrintSQLStatement(st, sql);

        }

        //return to main menu
        Main.MainMenu();

    }//end of LeaveReview

    public static void PrintSQLStatement(PreparedStatement st, String sql)
    {
        System.out.println();
        System.out.println("SQL RAW STRING: " + sql);
        System.out.println();
        System.out.println("PREPARED STATEMENT: " + st);
        System.out.println();
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

        }

        System.out.println();
        //Printing personal data of user
        System.out.println("Personal data for " + lookedUpUser);

        sql = "SELECT * FROM " + UserTable + " where username = ?";

        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, lookedUpUser);
//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                System.out.println("Full Name: " + r.getString("full_name"));
                System.out.println("Username: " + r.getString("username"));
                System.out.println("CardID: " + r.getString("cardid"));
                System.out.println("Email: " + r.getString("email"));
                System.out.println("Address: " + r.getString("address"));
                System.out.println("Phone Number: " + r.getString("phonenumber"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (verbose)
            PrintSQLStatement(st, sql);


        System.out.println();
        System.out.println("Checkout History for " + lookedUpUser);
        System.out.println();

        //print book checkout history for user
//        Select username, isbn, title, checkoutdate, returndate from _CheckoutRecord where username ='garin'
        sql = "SELECT isbn, title, checkoutdate, returndate FROM " + CheckoutRecordTable + " WHERE username = ?";

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

        if (verbose)
            PrintSQLStatement(st, sql);

        System.out.println("Books lost by: " + lookedUpUser);

        //print books lost by user
        sql = "SELECT isbn, title, checkoutdate, returndate FROM " + CheckoutRecordTable + " WHERE username = ? AND lost = 1";
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

        if (verbose)
            PrintSQLStatement(st, sql);

        //print books books that user is on waitlist for
        System.out.println("Books requested for future checkout by " + lookedUpUser);
        System.out.println();

//       Select w.isbn, r.title, w.dateadded from _Records r, _WaitList w where w.isbn = r.isbn and w.username = 'garin'
        sql = "SELECT r.title, w.dateadded FROM " + RecordsTable + " r, " + WaitListTable + " w WHERE w.isbn = r.isbn AND w.username = ?";
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

        if (verbose)
            PrintSQLStatement(st, sql);

        //print reviews
//        Select r.title, a.score, a. opinion from _Review a, _Records r where r.isbn = a.isbn and a.username = 'garin'
        sql = "SELECT r.title, a.score, a.opinion FROM " + ReviewTable + " a, " + RecordsTable + " r WHERE r.isbn = a.isbn AND a.username = ?";
        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, lookedUpUser);
//            st.executeUpdate();
            r = st.executeQuery();

            while (r.next()) {
                System.out.println("Title: " + r.getString("title") + "\t ");
                System.out.println("Score: " + r.getString("score") + "\t ");
                System.out.println("Opinion: " + r.getString("opinion"));
                System.out.println();
            }

        } catch (Exception e) {

        }

        if (verbose)
            PrintSQLStatement(st, sql);


        //return to main menu
        Main.MainMenu();

    }//end of PrintUserRecord

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

            sql = "SELECT COUNT(username) AS maxcheckoutcount FROM " + CheckoutRecordTable + " GROUP BY username ORDER BY maxcheckoutcount DESC LIMIT 1";

            try {
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while (r.next()) {
                    topFrequency = r.getString("maxcheckoutcount");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


            if (verbose)
                PrintSQLStatement(st, sql);


            sql = "SELECT username, COUNT(username) AS count FROM " + CheckoutRecordTable + " GROUP BY username";

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


            if (verbose)
                PrintSQLStatement(st, sql);

        }

        //users who rated the most number of books
        if (choice == 2) {
            System.out.println();
            System.out.println("Printing users who have reviewed the most books");
            System.out.println();

            sql = "SELECT COUNT(username) AS reviewcount FROM " + ReviewTable + " GROUP BY username ORDER BY reviewcount DESC LIMIT 1";

            try {
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while (r.next()) {
                    topFrequency = r.getString("reviewcount");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


            if (verbose)
                PrintSQLStatement(st, sql);


            sql = "SELECT username, COUNT(username) AS count FROM " + ReviewTable + " GROUP BY username";

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


            if (verbose)
                PrintSQLStatement(st, sql);

        }

        //users who lost the most books
        if (choice == 3) {
            System.out.println();
            System.out.println("Printing users who have lost the most books");
            System.out.println();

            sql = "SELECT COUNT(username) AS lostcount FROM " + CheckoutRecordTable + " c WHERE c.lost = 1 GROUP BY username ORDER BY lostcount DESC LIMIT 1";

            try {
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while (r.next()) {
                    topFrequency = r.getString("lostcount");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


            if (verbose)
                PrintSQLStatement(st, sql);


            sql = "SELECT username, COUNT(username) AS count FROM " + CheckoutRecordTable + " c WHERE c.lost = 1 GROUP BY username";

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


            if (verbose)
                PrintSQLStatement(st, sql);
        }

        Main.MainMenu();

    }//end of PrintUserStatistics

    public static void PrintBookRecord()
    {
        //TODO: Assuming all copies of a book exist in one location
        String sql;
        PreparedStatement st = null;
        ResultSet r = null;
        boolean check;

        String isbn = "";
        String title = "";
        String summary = "";
        String subject = "";
        String format = "";
        String pubyear = "";
        String publisher = "";
        String authors = "";
        String loc = "";
        int copies;

        System.out.println("Printing Book Record...");
        System.out.println();
        System.out.print("Please enter the ISBN of the book you wish to look up: ");

        do {
            userSelection = in.nextLine();

            check = CheckForISBN(userSelection);

            if(!check)
            {
                System.out.print("ISBN does not exist in database, please enter an existing isbn: ");
            }
            else
            {
                isbn = userSelection;
                break;
            }
        } while (true);

        //get book information
        sql = "SELECT * FROM " + RecordsTable + " WHERE isbn = ? ";

        try
        {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            r = st.executeQuery();


            while(r.next())
            {
                isbn = r.getString("ISBN");
                title = r.getString("title");
                summary = r.getString("bookSummary");
                subject = r.getString("subject");
                format = r.getString("format");
                pubyear = r.getString("pubyear");
                publisher = r.getString("publisher");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println();
        //print book info
        System.out.println("Book Info:");
        System.out.println("Title: " + title);
        System.out.print("Author(s): \n");

        //get authors for book
        sql = "SELECT authorname FROM " + AuthorListTable + " WHERE isbn = ?";

        try{
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            r = st.executeQuery();

            while(r.next())
            {
                System.out.println("\t " + r.getString("authorname"));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("ISBN: " + isbn);
        System.out.println("Summary: " + summary);
        System.out.println("Subject: " + subject);
        System.out.println("Format: " + format);
        System.out.println("Publisher: " + publisher);
        System.out.println("Year Published: " + pubyear);
        System.out.println();

        System.out.println("Location information: ");
        System.out.println();

        //get location information for book from inventory table
        sql = "SELECT copies, location FROM " + InventoryTable + " WHERE isbn = ?";

        try
        {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            r = st.executeQuery();

            while(r.next())
            {
                System.out.println("There are " + r.getString("copies") + " located on shelf: " + r.getString("location"));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }


        //get checkout information
        System.out.println();
        System.out.println("Checkout Information: ");
        System.out.println();

        sql = "SELECT username, checkoutdate, returndate FROM " + CheckoutRecordTable + " WHERE isbn = ?";
        try
        {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            r = st.executeQuery();

            while(r.next())
            {
                System.out.println("User: " + r.getString("username"));
                System.out.println("\t\t\t" + "Checkout Date: " +
                        r.getString("checkoutdate") + "\t" + "Return Date: " +
                        r.getString("returndate"));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        //get review information
        System.out.println("Book Reviews (if applicable): ");

        sql = "SELECT username, score, opinion FROM " + ReviewTable + " WHERE isbn = ? ";

        try
        {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            r = st.executeQuery();

            while(r.next())
            {
                System.out.println("User: " + r.getString("username"));
                System.out.println("\t\t\t" + "Score: " + r.getString("score") + "\t" + "Optional Opinion: " + r.getString("opinion"));
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        sql = "SELECT AVG(score) AS avg FROM " + ReviewTable + " WHERE isbn = ? ";

        try{
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            r = st.executeQuery();

            while (r.next()) {
                System.out.println("Average Review Score for this book: " + r.getString("avg"));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }









    }

    public static void PrintLibraryStatistics()
    {}

    public static void ReturnBook()
    {

        //TODO: Assuming that the user does have a currently open checkout record for given book, maybe check for if they haven't returned it later
        //TODO: Assuming that the waitlist isn't displayed when a book is marked as lost
        String isbn, month, day, year, returndate;

        ResultSet r = null;
        PreparedStatement st = null;
        String sql;

        boolean lost = false;

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
                isbn = userSelection;
                break;
            }

        } while (true);


        System.out.println("Please enter the date of which this book was returned: ");
        System.out.print("What month was this book returned? [1 - 12]: ");
        month = in.nextLine();
        System.out.print("What day was this book returned? [1 - 31]: ");
        day = in.nextLine();
        System.out.print("What year was this book returned? [YYYY]: ");
        year = in.nextLine();

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

        if(lost)
        {
            System.out.println("Marking book as lost in database...");
            sql = "UPDATE " + CheckoutRecordTable + " SET returndate = ?, lost = 1 WHERE isbn = ? AND username = ?";

            try
            {
                st = con.prepareStatement(sql);
                st.setString(1, returndate);
                st.setString(2, isbn);
                st.setString(3, loggedInUser);
                st.executeUpdate();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            //increment lost count
            sql = "UPDATE " + RecordsTable + " SET lostcount = lostcount + 1 WHERE isbn = ?";

            try
            {
                st = con.prepareStatement(sql);
                st.setString(1, isbn);
                st.executeUpdate();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        else
        {
            //the book wasn't lost, update checkout record, incrememnt number of copies by one, return waitlist for book.

            sql = "UPDATE " + CheckoutRecordTable + " SET returndate = ? WHERE isbn = ? AND username = ?";

            try
            {
                st = con.prepareStatement(sql);
                st.setString(1, returndate);
                st.setString(2, isbn);
                st.setString(3, loggedInUser);
                st.executeUpdate();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            sql = "UPDATE " + InventoryTable + " SET copies = copies + 1 WHERE isbn = ?";

            try
            {
                st = con.prepareStatement(sql);
                st.setString(1, isbn);
                st.executeUpdate();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            System.out.println("Users currently on the waitlist for this book (If Any): ");
            sql = "SELECT username FROM "+ WaitListTable +" WHERE isbn = ?";
            try
            {
                st = con.prepareStatement(sql);
                st.setString(1, isbn);
                r = st.executeQuery();

                while(r.next())
                {
                    System.out.print("\t" + r.getString("username"));
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        //return to main menu
        Console.MainMenu();

    }

    public static void SetConnection(Connection c)
    {
        try {

            //set statement
            statem = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //store connection
            con = c;
        } catch (Exception e) {

        }
    }

    /**
     * Used to set user context string in menus and greetings
     *
     * @param username
     */
    public static void setLoggedInUser(String username) {
        loggedInUser = username;

    }//end of SetLoggedInUser
}
