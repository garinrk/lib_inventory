package cs5530;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.lang.*;
import java.text.SimpleDateFormat;

/**
 * CS 5530 Database Systems
 * Phase 2 Code [Database.java]
 *
 * @author Garin Richards, u0738045
 */
public class Database {

    //TODO: Must assume that knows that they have already checked out a book

    /* Globals */
    static boolean verbose = true;

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

    static String AuthorListTable = "_AuthorList";
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
//    static Statement statem = null;
    static Connection con = null;

    /*
    *
    *   Constructor for access to methods
    */
    public Database()
    {}

    public static String AddBookRecordWeb(String isbn, String title, String publisher, String pubYear, String format, String subject, String summary, String[] authors, Connection con){
        
        String insertRecordSQL =  "INSERT INTO " + RecordsTable + " (isbn, title, publisher, pubyear, format, subject, booksummary) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String addAuthorSQL = "INSERT INTO " + AuthorListTable + " (isbn, authorname) VALUES (?, ?)";
        String resultStr = "";

        ResultSet r = null;
        PreparedStatement st = null;

        //check if isbn exists
        if(CheckForISBNWeb(isbn, con))
        {
            resultStr = "That isbn already exists in the database<BR>";
            return resultStr;
        }

        //add book record
        try{
            st = con.prepareStatement(insertRecordSQL);
            st.setString(1, isbn);
            st.setString(2, title);
            st.setString(3, publisher);
            st.setString(4, pubYear);
            st.setString(5, format);
            st.setString(6, subject);
            st.setString(7, summary);
            st.executeUpdate();

        } catch (Exception e) {

        }

        //add authors to author table
        for(int i = 0; i < authors.length; i++) {
                try {
                    st = con.prepareStatement(addAuthorSQL);
                    st.setString(1, isbn);
                    st.setString(2, authors[i]);
                    st.executeUpdate();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }


        resultStr += "<h2>Success!</h2><BR>A record with the following info has been added:<BR><BR>";
        resultStr += "Title: " + title + "<BR>";

        for(int i = 0; i < authors.length; i++)
        {
            resultStr += "Author " + (i+1) + ": " + authors[i] + "<BR>";
        }
        resultStr += "ISBN: " + isbn + "<BR>";
        resultStr += "Book Summary: " + summary + "<BR>";
        resultStr += "Publisher: " + publisher + "<BR>";
        resultStr += "Subject: " + subject + "<BR>";
        resultStr += "Format: " + format + "<BR>";
        resultStr += "Year of Publication: " + pubYear + "<BR>";

        return resultStr;
    }

    /**
     * Adds a book record to the database
     *
     * Prompts user for new information and adds record to _Records table,
     * and also inserts authors into authors table, using isbn as a key
     */
    public static void AddBookRecord()
    {
        //new information to add to the database
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

        System.out.println("====================================================");
        System.out.println("           ENTERING NEW BOOK RECORD");
        System.out.println("====================================================");
        System.out.println();
        System.out.print("ISBN: ");

        do {
            newISBN = in.nextLine();

            check = CheckForISBN(newISBN);

            //if isbn already exists in database
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

        System.out.print("How many authors are there for this new record?: ");

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
        System.out.println("Author(s): ");

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

            if (!Console.IsNumber(userSelection)) {
                System.out.print(userSelection + " is an invalid option, ");
                System.out.print("Please make a selection: ");
            }

            //if the user did enter a number
            if (Console.IsNumber(userSelection)) {
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
                st.setString(3, newPublisher);
                st.setString(4, newYearPub);
                st.setString(5, newFormat);
                st.setString(6, newSubject);
                st.setString(7, newSummary);
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

    public static String GetRecordIDWeb(String isbn, Connection con)
    {
        String id = "";
        String selectIDSQL = "SELECT r_id FROM " + RecordsTable + " WHERE isbn = ?";
        PreparedStatement st = null;
        ResultSet r = null;

        try{
            st = con.prepareStatement(selectIDSQL);
            st.setString(1,isbn);
            r = st.executeQuery();

            while(r.next())
            {
                id = r.getString("r_id");
            }

        } catch (Exception e) {

        }

        return id;
    }

    public static String AddBookCopyWeb(String isbn, String amount, Connection con)
    {
        boolean check;
        boolean existingrecord = false;

        ResultSet r = null;
        PreparedStatement st = null;

        String resultStr = "";
        String getCopiesSQL = "SELECT copies FROM " + InventoryTable + " where isbn = ?";
        String updateCopiesSQL = "UPDATE " + InventoryTable + " SET copies = copies + ? WHERE isbn = ?";;
        String insertCopiesSQL = "INSERT INTO " + InventoryTable + " (r_id, isbn, copies) VALUES(?, ?, ?)";
        String id = "";

        //check if isbn exists in database
        if(!CheckForISBNWeb(isbn, con))
        {
            resultStr = "That isbn does not exist<BR>";
            return resultStr;
        }

        //check if there already is an entry for this in the inventory
        try{
            st = con.prepareStatement(getCopiesSQL);
            st.setString(1, isbn);
            r = st.executeQuery();

            if(r.next())
            {
                existingrecord = true;
            }

        } catch (Exception e) {

        }

        //there already exists a record, we have to update the amount
        if(existingrecord)
        {
            

            try{
                st = con.prepareStatement(updateCopiesSQL);
                st.setString(1, amount);
                st.setString(2, isbn);
                st.executeUpdate();

            } catch (Exception e) {

            }

        }

        //there is no existing record, we have to make one
        else
        {
            id = GetRecordIDWeb(isbn, con);
            try{
                st = con.prepareStatement(insertCopiesSQL);
                st.setString(1, id);
                st.setString(2, isbn);
                st.setString(3, amount);
                st.executeUpdate();

            } catch (Exception e) {

            }     

        }

        resultStr += "Success!<BR>" + amount + " copies for isbn: " + isbn + " have been added<BR>";
        return resultStr; 

    }


    /**
     * Adds new copies of a book. If a record of a book isn't in the inventory
     * table, a new one is created. T
     */
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

        System.out.println("====================================================");
        System.out.println("            ADDING NEW BOOK COPIES");
        System.out.println("====================================================");
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

            if (!Console.IsNumber(userSelection)) {
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

    }//end of add book copy

    public static boolean CheckWaitListWeb(String isbn, String username, Connection con)
    {
        boolean found = false;
        PreparedStatement st = null;
        ResultSet r = null;

        String checkListSQL = "SELECT username FROM " + WaitListTable + " where isbn = ?;";

        try{
            st = con.prepareStatement(checkListSQL);
            st.setString(1, isbn);
            r = st.executeQuery();

            while(r.next()){
                if (r.getString("username").matches(username) && found == false) {
                    found = true;
                    break;
                }

            }
        } catch (Exception e) {

        }

        return found;
    }

    public static String AddToWaitListWeb(String username, String isbn, Date today, Connection con)
    {
        ResultSet r = null;
        PreparedStatement st = null;
        

        String addToListSQL = "INSERT INTO " + WaitListTable + " (username, isbn, dateadded) VALUES (?, ?, ?);";
        String updateWaitCountSQL = "UPDATE " + RecordsTable + " SET waitcount = waitcount + 1 WHERE isbn = ?";

        int month = 0, day = 0, year = 0;
        String todayDate, time;
        boolean found = false;
        String resultStr = "";

      

        //check if isbn exists
        if(!CheckForISBNWeb(isbn, con))
        {
            resultStr += "That ISBN does not exist.";
            return resultStr;
        }
  
        //construct date
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);


        //get the current time
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        time = sdf.format(date).toString();

        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        year = cal.get(Calendar.YEAR);

        //create today's date
        todayDate = year + "-" + month + "-" + day + " " + time;

        //Check if user is already on waitlist
        if(CheckWaitListWeb(isbn, username, con))
        {
            resultStr += "You are already on a waitlist for this book.";
            return resultStr;
        }

        //insert waitlist record
        try{
            st = con.prepareStatement(addToListSQL);
            st.setString(1, username);
            st.setString(2, isbn);
            st.setString(3, todayDate);
            st.executeUpdate();
        } catch (Exception e) {

        }

        //increment waitcount in records table
        try{
            st = con.prepareStatement(updateWaitCountSQL);
            st.setString(1,isbn);
            st.executeUpdate();

        } catch (Exception e) {

        }

        //report success
        resultStr += username + " has been added to the waitlist for ISBN " + isbn;

        return resultStr;

    }

    public static boolean CheckForISBNWeb(String isbn, Connection con)
    {
        boolean found = false;
        ResultSet r = null;
        PreparedStatement st = null;

        String isbnquery = "SELECT isbn FROM " + RecordsTable + " WHERE isbn = ?";

        try{
            st = con.prepareStatement(isbnquery);
            st.setString(1,isbn);
            r = st.executeQuery();

            while(r.next())
            {
                if(r.getString("ISBN").matches(isbn))
                {
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
     * @param
     */
    public static void AddToWaitList(Date today) {


        String sql;
        ResultSet r = null;
        PreparedStatement st = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int thisMonth = 0;
        int thisDay = 0;
        int thisYear = 0;
        String todayDate, time;
        boolean found = false;

        boolean check = false;
        String ISBN;

        System.out.println("====================================================");
        System.out.println("           ADD USER TO WAIT LIST");
        System.out.println("====================================================");
        System.out.println();
        System.out.print("Please enter the ISBN of the book you wish to be put on a Wait List for: ");

        do {
            userSelection = in.nextLine();
            check = Database.CheckForISBN(userSelection);


            if (!check) {
                System.out.print("ISBN doesn't exist. Please try again: ");
            } else {
                ISBN = userSelection;
                break;
            }

        } while (true);

        //We're going to use the current date for the day of which they are added to the waitlist
        cal = Calendar.getInstance();
        cal.setTime(today);


        //see if user is already on waitlist for book
        found = CheckWaitList(ISBN, loggedInUser);

        if (found) {
            System.out.println("You are already on a Wait List for this book, returning to Main Menu...");
            Console.MainMenu();
        }


        sql = "INSERT INTO " + WaitListTable + " (username, isbn, dateadded) VALUES (?, ?, ?);";

        //get the current time
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        time = sdf.format(date).toString();

        thisMonth = cal.get(Calendar.MONTH) + 1;
        thisDay = cal.get(Calendar.DAY_OF_MONTH);
        thisYear = cal.get(Calendar.YEAR);

        //create today's date
        todayDate = thisYear + "-" + thisMonth + "-" + thisDay + " " + time;

        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, loggedInUser);
            st.setString(2, ISBN);
            st.setString(3, todayDate);
            st.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }

        //increment waitcount in recordstable
        sql = "UPDATE " + RecordsTable + " SET waitcount = waitcount + 1 WHERE isbn = ?";

        try{
            st = con.prepareStatement(sql);
            st.setString(1, ISBN);
            st.executeUpdate();
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        if(verbose)
            PrintSQLStatement(st, sql);

        System.out.println("You have been added to the Wait List for book with ISBN: " + ISBN + " with a Date Added of " + todayDate);

        //return to main menu
        Console.MainMenu();
    }

    public static void AddUserWeb(String username, String idnumber, String fullname, String address, String phonenumber, String email, Connection con) throws Exception
    {
        String resultStr = "Success";
        String sql;
        ResultSet results;
        PreparedStatement st;
        //query to add user to database
        sql = "INSERT INTO " + UserTable + " (username, cardid, full_name, email, address, phonenumber) VALUES (?, ?, ?, ?, ?, ?)";


        try {
        //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, username);
            st.setString(2, idnumber);
            st.setString(3, fullname);
            st.setString(4, email);
            st.setString(5, address);
            st.setString(6, phonenumber);
            st.executeUpdate();
        } catch (Exception e) {

        }


    }

    /**
     * Adds a new user to the database
     */
    public static void AddUser(boolean firsttime)
    {
        String sql;
        ResultSet r = null;
        PreparedStatement st = null;

        System.out.println("====================================================");
        System.out.println("                 ADD NEW USER");
        System.out.println("====================================================");
        System.out.println();
        System.out.print("New Unique Username: ");
        newUsername = in.nextLine();

        //check to see if user already exists in database
        while (CheckForUser(newUsername)) {
            System.out.print(newUsername + " already exists, please choose a new username: ");
            newUsername = in.nextLine();
        }

        /* User must enter a number for an ID */
        System.out.print("New ID (Number): ");
        do {
            newID = in.nextLine();

            if (!Console.IsNumber(newID)) {
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

            if (!Console.IsNumber(newID)) {
                System.out.print(newPhoneNumber + " is not a number, ");
                System.out.print("Please enter a number for this user's Phone Number: ");
            } else {
                break;
            }
        } while (true);
        System.out.println();

        System.out.println("New User Info: ");
        System.out.println("Full Name: " + newFullName);
        System.out.println("Username: " + newUsername);
        System.out.println("ID: " + newID);
        System.out.println("Address: " + newAddress);
        System.out.println("Email: " + newEmail);
        System.out.println("Phone Number: " + newPhoneNumber);


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


        } catch (Exception e) {

        }
        if(verbose)
            PrintSQLStatement(st, sql);
        if(firsttime) {
            setLoggedInUser(newUsername);
            Console.currentUser = newUsername;
        }
        Console.MainMenu();

    }

    public static String BrowseLibraryWeb(String[] titleWords, String[] authors, String publisher, String subject, boolean allBooks, String sortBy, Connection con)
    {
        String resultStr = "This is a result";

        return resultStr;
    }

    /**
     * Returns a number of results from the library based on a number of criteria:
     *
     * Title Word(s)
     * Author(s)
     * Subject
     * Publisher
     *
     * User can sort results by avg review score, popularity, or publication year
     */
    public static void BrowseLibrary()
    {
        //TODO: Assuming that we are only searching books with authors and reviews
        //TODO: MISSING, search with multiple authors
        boolean entireLibrary = false;
        boolean onlyAvailable = false;


        boolean sortByYear = false;
        boolean sortByScore = false;
        boolean sortByPopularity = false;

        String publisher;
        String sql;

        PreparedStatement st = null;
        ResultSet r = null;
        String author;

        System.out.println("====================================================");
        System.out.println("               BROWSING LIBRARY");
        System.out.println("====================================================");
        System.out.println();
        System.out.print("Would you like to display your results as all in the library [1] or only those of which are available to checkout [2]?: ");

        do {
            userSelection = in.nextLine();

            if (!Console.IsNumber(userSelection)) {
                System.out.print(userSelection + " is an invalid option, ");
                System.out.print("Please make a selection: ");
            }

            //if the user did enter a number
            if (Console.IsNumber(userSelection)) {
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

        //does user want entire library or only those available?
        if (choice == 1) {
            onlyAvailable = false;
        } else if (choice == 2) {
            onlyAvailable = true;
        }

        System.out.print("Please enter Author(s) separated by a comma: ");
        userSelection = in.nextLine();
        String[] authors = userSelection.split(",");
//        System.out.print("Please enter an Author: ");
//        author = in.nextLine();

        System.out.print("Please enter the publisher: ");
        publisher = in.nextLine();

        System.out.print("Please enter title words separated by a comma: ");
        userSelection = in.nextLine();
        String[] wordsInTitle = userSelection.split(",");

        System.out.print("Please enter subject: ");
        userSelection = in.nextLine();
        String subject = userSelection;

        System.out.print("Do you want to sort your result by year published [1], average review score [2], or book popularity[3]?: ");

        do {
            userSelection = in.nextLine();

            if (!Console.IsNumber(userSelection)) {
                System.out.print(userSelection + " is an invalid option, ");
                System.out.print("Please make a selection: ");
            }

            //if the user did enter a number
            if (Console.IsNumber(userSelection)) {
                choice = Integer.parseInt((userSelection));

                //check to see if it's a valid option
                if (choice != 2 && choice != 1 && choice != 3) {
                    System.out.print(choice + " is an invalid option ");
                    System.out.print("please make a selection: ");

                }

                //TODO: TEST THIS

//                //make sure they only entered one digit
//                if (userSelection.length() != 1) {
//                    System.out.print("Invalid number of options, please select only one: ");
//                }


                //case where the user did enter a valid option number
                else {
                    break;
                }


            }
        } while (true);

        if (choice == 1) {
            sortByYear = true;


        } else if (choice == 2) {
            sortByScore = true;


        } else if (choice == 3) {
            sortByPopularity = true;
        }

        //construct awesome sql query

        sql = "SELECT * FROM " + RecordsTable +" r, " + AuthorListTable + " a, " + InventoryTable + " i WHERE r.isbn = a.isbn";

        //add title words
        for(int i = 0; i < wordsInTitle.length; i++)
        {
            if(wordsInTitle.length != 0)
                sql = sql.concat("\n AND title LIKE '%" + wordsInTitle[i] + "%'");
        }

        //add publisher
        sql = sql.concat("\n AND publisher LIKE '%" + publisher + "%'");

        //add subject
        sql = sql.concat("\n AND subject LIKE '%" + subject + "%'");

        //begin to add multiple authors
        sql = sql.concat("\n AND \n\t(");

        //add authors
            for(int i = 0; i < authors.length; i++)
            {
                if(authors.length == 0)
                    sql = sql.concat("\n authorname LIKE '%" + authors[i] + "%'");
                else if(i == authors.length - 1)
                    sql = sql.concat("\n authorname LIKE '%" + authors[i] + "%')");
                else
                    sql = sql.concat("\n authorname LIKE '%" + authors[i] + "%' \n\t OR");
            }

        //add author
//        sql = sql.concat("\n AND authorname LIKE '%" + author + "%'");

        //no duplicates
            sql = sql.concat("\n GROUP BY a.authorname");

        //only available copies?
            if(onlyAvailable)
                sql = sql.concat("\n AND copies > 0");

            if(sortByYear)
                sql = sql.concat("\n ORDER BY pubyear DESC");

            if(sortByScore)
                sql = sql.concat("\n ORDER BY avgreviewscore DESC");

            if(sortByPopularity)
                sql = sql.concat("\n ORDER BY checkoutcount DESC");



            System.out.println("HERES YOUR JAVA QUERY!: " + sql);

            System.out.println();
            System.out.println("====================================================");
            System.out.println("                    RESULTS");
            System.out.println("====================================================");
            System.out.println();
            try
            {
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while(r.next())
                {
                    System.out.println("ISBN: " + r.getString("isbn"));
                    System.out.println("Title: " + r.getString("title"));
                    System.out.println("Author: " + r.getString("authorname"));
                    System.out.println("Publisher: " + r.getString("publisher"));
                    System.out.println("Publication Year: " + r.getString("pubyear"));
                    System.out.println("Format: " + r.getString("format"));
                    System.out.println("Subject: " + r.getString("subject"));
                    System.out.println("booksummary: " + r.getString("booksummary"));
                    System.out.println("Average Review Score: " + r.getString("avgreviewscore"));
                    System.out.println("Amount of times checked out: " + r.getString("checkoutcount"));
                    System.out.println();
                    System.out.println();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }


        //return to main menu
            Console.MainMenu();

    }//end of browselibrary


    /**
     * Checks to see if there already is a checkout record
     * for book with isbn for a specific user
     * @param user, user to check for
     * @param isbn, isbn to look up
     * @return
     */
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

            r = st.executeQuery();

            while (r.next()) {
                if (r.getString("username").matches(loggedInUser) && found == false && !r.getString("lost").matches("9999-12-31")) {
                    found = true;
                    break;
                }
            }
        } catch (Exception e) {

        }

        return found;

    }//end of CheckForCheckoutRecord

    /**
     * Checks to see if a specific isbn already exists in the records table, used to test for
     * uniqueness
     * @param isbn, to look up
     * @return found
     */
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

        //check results for if database has username
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

    }//end of CheckForISBN

    /**
     * Check if user already has a review for a specific isbn, only one review per book per use
     * @param username, to look up
     * @param isbn, to look up
     * @return whether an existing review exists or not
     */
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
    }//end of CheckForReview


    /**
     * Checks to see if a user already exists in the database, used to test uniqueness
     * @param username, to look up
     * @return whether or not username exists
     */
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

        //check results for if database has username
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
    }//end of CheckForUser

    public static boolean CheckForUserWeb(String username, Connection conn)
    {
        boolean found = false;
        ResultSet r = null;
        PreparedStatement st = null;

        //construct sql query
        String sql = "SELECT username FROM " + UserTable + " WHERE username = ?";

        try {
            st = conn.prepareStatement(sql);
            st.setString(1, username);
            r = st.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();

        }

        if(verbose)
            PrintSQLStatement(st, sql);

        //check results for if database has username
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

    public static String CheckLateListWeb(String date, Connection con)
    {
        String checkDate;
        String resultStr = "";

        PreparedStatement st = null;
        ResultSet r = null;

        String lateBookSQL = "SELECT c.title, c.duedate, c.username, u.full_name, u.phonenumber, u.email FROM " + CheckoutRecordTable + " c, " + UserTable + " u WHERE (duedate < ? AND returndate > ?) and c.username = u.username "; 

        resultStr += "<h2>Late books for " + date + "</h2><br>";
        try{
            st = con.prepareStatement(lateBookSQL);
            st.setString(1, date);
            st.setString(2, date);
            r = st.executeQuery();

            while(r.next())
            {
                resultStr += "Book Title: "
                    + r.getString("title") + "<BR>Date Due: " + r.getString("duedate") +
                    "<BR>Username: " + r.getString("username") +
                    "<BR>Full Name: " + r.getString("full_name") +
                    "<BR>Phone Number: " + r.getString("phonenumber") +
                    "<BR>Email: " + r.getString("email") + "<BR><BR>";
            }

        } catch (Exception e) {
            
        }

        return resultStr;
    }

    /**
     * Checks for books that are checked out and are late for a specific date
     */
    public static void CheckLateList() {
        String month;
        String day;
        String year;
        String sql;
        String checkdate;

        PreparedStatement st = null;
        ResultSet r = null;


        //have prompt user for date they wish to look up
        System.out.println("====================================================");
        System.out.println("              LATE BOOK LIST LOOKUP");
        System.out.println("====================================================");
        System.out.println();
        System.out.print("Month: ");
        month = in.nextLine();
        System.out.print("Day: ");
        day = in.nextLine();
        System.out.print("Year: ");
        year = in.nextLine();
        System.out.println();

        //create date to check for
        checkdate = year + "-" + month + "-" + day;

        System.out.println("Books that were still checked out after their due date on " + checkdate);
        sql = "SELECT c.title, c.duedate, c.username, u.full_name, u.phonenumber, u.email FROM " + CheckoutRecordTable + " c, " + UserTable + " u WHERE (duedate < ? AND returndate > ?) and c.username = u.username ";
        try
        {
            st = con.prepareStatement(sql);
            st.setString(1, checkdate);
            st.setString(2, checkdate);
            r = st.executeQuery();

            while(r.next())
            {
                System.out.println("Book Title: "
                    + r.getString("title") + "\tDate Due: " + r.getString("duedate") +
                    "\tUsername: " + r.getString("username") +
                    "\tFull Name: " + r.getString("full_name") +
                    "\tPhone Number: " + r.getString("phonenumber") +
                    "\tEmail: " + r.getString("email"));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println();
        Console.MainMenu();
    }//end of CheckLateList

    /**
     * Checks to see if a user already exists on the waitilist for a given book
     * @param isbn, to look up
     * @param username, to look up
     * @return whether user already is on waitlist or not
     */
    public static boolean CheckWaitList(String isbn, String username)
    {
        String sql;
        PreparedStatement st = null;
        ResultSet r = null;
        boolean found =  false;

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
            e.printStackTrace();
        }

        return found;

    }//end of CheckWaitList

    public static String CheckoutBookWeb(String username, String isbn, Date today, Connection con)
    {
        String resultStr = "Hello World";
        String oldestuser = null;
        String time;
        String duedate;
        int month = 0, day = 0, year = 0;
        String currentdate;

        String oldestUserSQL = "SELECT username AS oldestuserforisbn FROM " + WaitListTable + " WHERE isbn = ? AND dateadded = (SELECT MIN(dateadded) from " + WaitListTable + " where isbn =  ? )";
        String decrementWaitCountSQL = "UPDATE " + RecordsTable + " SET waitcount = waitcount - 1 WHERE isbn = ?";
        String removeWaitListSQL = "DELETE FROM " + WaitListTable + " WHERE isbn = ? AND username = ?";

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar c = Calendar.getInstance();

        boolean availableforcheckout = false;
        boolean existingCheckout;


        PreparedStatement st = null;
        ResultSet results = null;

        int futureMonth;
        int futureDay;
        int futureYear;

        //get current time 
        time = timeFormat.format(today).toString();

        //compute 30 days from now
        c.setTime(today);

        //hold current date
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        year = c.get(Calendar.YEAR);

        currentdate = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) + " " + time;

        c.add(Calendar.DATE, 30);

        //save due date 

        futureMonth = c.get(Calendar.MONTH) + 1;
        futureDay = c.get(Calendar.DAY_OF_MONTH);
        futureYear = c.get(Calendar.YEAR);

        //create date string that will be inserted
        duedate = Integer.toString(futureYear) + "-" + Integer.toString(futureMonth) + "-" + Integer.toString(futureDay) + " " + time;

        //we need to check if there is a waitlist for this book

        try
        {
            st = con.prepareStatement(oldestUserSQL);
            st.setString(1, isbn);
            st.setString(2, isbn);
            results = st.executeQuery();

            if(!results.wasNull())
            {
                while(results.next())
                {
                    oldestuser = results.getString("oldestuserforisbn");
                }
            }


        } catch (Exception e) {


        }

        //test oldest user
        if(oldestuser == null)
        {
            //the user can check it out since no one is waiting
            availableforcheckout = true;
        }
        else
        {
            //if there is someone waiting, we need to see if it matches the user being passed in
            availableforcheckout = oldestuser.matches(username);
        }

        //we need to remove them from the waitlist
        if(oldestuser != null && oldestuser.matches(username))
        {

            //decrement waitcount in records table
            try{
                st = con.prepareStatement(decrementWaitCountSQL);
                st.setString(1, isbn);
                st.executeUpdate();
            } catch (Exception e) {

            }

            //remove waitlist entry
            try{
                st = con.prepareStatement(removeWaitListSQL);
                st.setString(1, isbn);
                st.setString(2, username);
                st.executeUpdate();
            } catch (Exception e) {

            }

        }

        //if the book isn't available to checkout
        if(!availableforcheckout)
        {
            resultStr = "You cannot check out this book because there is a waitlist and you are not at the top of the waitlist.<BR><a href=\"AddToWaitList.jsp\">You can add yourself to a wait list here.</a>";
            return resultStr;
        }

        //the book is available for checkout!

        else
        {
            //we need to see if the user already has the book checked out
            existingCheckout = CheckForCheckoutRecordWeb(username, isbn, con);
            

            //if the user doesn't have an existing checkout record, go create one
            if(!existingCheckout)
            {
                resultStr = CreateCheckoutRecordWeb(username, duedate, currentdate, isbn, con);
            }
            else
            {
                resultStr = "You already have a checkout record for this book.";
                return resultStr;
            }

        }



        return resultStr; 
    }

    public static boolean CheckForCheckoutRecordWeb(String username, String isbn, Connection con)
    {
        boolean found = false;
        String checkForRecordsql = "SELECT username, returndate FROM " + CheckoutRecordTable + " c WHERE isbn = ? AND username = ?";
        PreparedStatement st;
        ResultSet r = null;

        try{
            st = con.prepareStatement(checkForRecordsql);
            st.setString(1, isbn);
            st.setString(2, username);

            r = st.executeQuery();

            while(r.next())
            {
                if (r.getString("username").matches(username) && !r.getString("returndate").matches("9999-12-31 23:59:59") && found == false) {
                    found = true;
                    break;
                    
                }
            }

        } catch (Exception e) {

        }

        return found;
    }

    public static String CreateCheckoutRecordWeb(String username, String duedate, String today, String isbn, Connection con)
    {
        String resultStr = "";
        ResultSet r;
        PreparedStatement st = null;

        String getCopiesSQL = "SELECT copies FROM " + InventoryTable + " WHERE isbn = ?";
        String getTitleSQL = "SELECT title FROM " + RecordsTable + " WHERE isbn = ?";
        String insertRecordSQL = "INSERT INTO " + CheckoutRecordTable + " (username, isbn, title, checkoutdate, duedate) VALUES (?, ?, ?, ?, ?)";
        String incrementCheckoutSQL = "UPDATE " + RecordsTable + " SET checkoutcount = checkoutcount + 1 WHERE isbn = ?";
        String decrementCopiesSQL = "UPDATE " + InventoryTable + " SET copies = copies - 1 WHERE isbn = ?";

        int copies = 0;
        String title = "";

        //need to check if there are copies available to checkout

        try{
            st = con.prepareStatement(getCopiesSQL);
            st.setString(1, isbn);

            r = st.executeQuery();

            while(r.next())
            {
                if(r.getString("copies").matches("0"))
                {
                    resultStr = "There are no copies available of that book to checkout...";
                    return resultStr;
                }
                else
                    copies = Integer.parseInt(r.getString("copies"));

            }

        } catch (Exception e) {

        }

        //there are copies, begin bookkeeping to create a checkout record

        //get title of book according to isbn
        try{
            st = con.prepareStatement(getTitleSQL);
            st.setString(1, isbn);

            r = st.executeQuery();

            while(r.next())
            {
                title = r.getString("title");
            }

        } catch (Exception e) {

        }

        //insert checkout record into table
        try{
            st = con.prepareStatement(insertRecordSQL);
            st.setString(1, username);
            st.setString(2, isbn);
            st.setString(3, title);
            st.setString(4, today);
            st.setString(5, duedate);
            st.executeUpdate();

        } catch (Exception e) {

        }

        //we have to increment the number of times that the book has been checked out

        try{
            st = con.prepareStatement(incrementCheckoutSQL);
            st.setString(1, isbn);
            st.executeUpdate();

        } catch (Exception e) {

        }

        //decrement the number of copies of the book there is in the library
        try{
            st = con.prepareStatement(decrementCopiesSQL);
            st.setString(1, isbn);
            st.executeUpdate();
        } catch (Exception e) {

        }

        resultStr += "A checkout record has been made for:<BR>User: " + username + "<BR>Book Title: " + title + "<BR>ISBN: " + isbn;
        return resultStr;
    }


    /**
     * Allows a user to checkout a book specified by ISBN
     * @param today, today's date
     */
    public static void CheckoutBook(Date today)
    {
        String isbn, duedate, oldestuser = null, todayDate, time;
        boolean check = false, availableforcheckout = false;
        int thisMonth = 0, thisYear = 0, thisDay = 0;

        String sql;
        ResultSet r = null;
        PreparedStatement st = null;

        System.out.println("====================================================");
        System.out.println("                 CHECKOUT BOOK");
        System.out.println("====================================================");
        System.out.println();
        System.out.print("Please enter the ISBN of the book you wish to check out: ");

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

        //get the current time
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        time = sdf.format(date).toString();
//
        if(verbose)
            System.out.println("The time is " + time);

        todayDate = thisYear + "-" + thisMonth + "-" + thisDay + " " + time;

        //compute 30 days ahead
        c.add(Calendar.DATE, 30);

        //save duedate
        int futureMonth = c.get(Calendar.MONTH) + 1;
        int futureDay = c.get(Calendar.DAY_OF_MONTH);
        int futureYear = c.get(Calendar.YEAR);

        //create date string that will be inserted
        duedate = Integer.toString(futureYear) + "-" + Integer.toString(futureMonth) + "-" + Integer.toString(futureDay) + " " + time;

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

        //they will be removed from the waitlist count
        if(oldestuser != null && oldestuser.matches(loggedInUser))
        {
            //decrement waitcount in records table
            sql = "UPDATE " + RecordsTable + " SET waitcount = waitcount - 1 WHERE isbn = ?";

            try{
                st = con.prepareStatement(sql);
                st.setString(1, isbn);
                st.executeUpdate();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            //we also have to remove their waitlist entry
//            delete from _WaitList where isbn = 12 and username = 'garin'
            sql = "DELETE FROM " + WaitListTable + " WHERE isbn = ? AND username = ?";

            try
            {
                st = con.prepareStatement(sql);
                st.setString(1, isbn);
                st.setString(2, loggedInUser);
                st.executeUpdate();
            } catch (Exception e)
            {
                e.printStackTrace();
            }


        }

        //if the book isn't available to checkout, and they aren't at the top of the list to check out
        //prompt the user with the option to be added to the book's waitlist

        if(!availableforcheckout && oldestuser != null)
        {
            System.out.print("There is a waitlist for this book, and you are not at the top if the list" +
                " would you like to be added to the waitlist? [1] Yes [0] No: ");

            //ask user for choice, check for correctness
            do {
                userSelection = in.nextLine();

                if (!Console.IsNumber(userSelection)) {
                    System.out.print(userSelection + " is an invalid option, ");
                    System.out.print("Please make a selection: ");
                }

                //if the user did enter a number
                if (Console.IsNumber(userSelection)) {
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
                AddToWaitList(today);
            }
            else
            {
                //exit and return to main menu
                System.out.println("You will NOT be added to the waitlist, returning to main menu...\n");
                Console.MainMenu();
            }
        }

        //However, if the book IS available to checkout they were , create a checkout record for the user
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

        Console.MainMenu();


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
                    System.out.println("There are no copies of this book to check out! Returning to menu...");
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

        System.out.println("Thank you " + loggedInUser + " you now have a checkout record for " + title + " with a due date of " + due);
        System.out.println();

        Console.MainMenu();

    }//end of CreateCheckoutRecord

    /**
     * Gets the record of id of a specific isbn, used in making new entries
     * to the inventory table in AddBookCopy()
     * @param isbn, the isbn being looked up
     * @return the record ID to the corresponding isbn
     */
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

    public static String LeaveReviewWeb(String username, String isbn, String review, String score, Connection con)
    {
        String resultStr = "";
        String newAverage = "";

        String insertReviewSQL = "INSERT INTO " + ReviewTable + " (username, ISBN, score, opinion) VALUES(?, ?, ?, ?)";
        String getAvgSQL = "SELECT AVG(score) AS avg FROM " + ReviewTable + " WHERE isbn = ? ";
        String setAvgSQL = "UPDATE " + RecordsTable + " SET avgreviewscore = ? WHERE isbn = ?";

        boolean checkExistingISBN;

        ResultSet r = null;
        PreparedStatement st = null;

        checkExistingISBN = CheckForISBNWeb(isbn, con);
        //check to see if isbn exists
        if(!checkExistingISBN)
        {
            resultStr += "This isbn does not exist<BR>";
            return resultStr;
        }

        //check to see if user already left review
        if(CheckForReviewWeb(username, isbn, con))
        {
            resultStr += "You already have a review on record for this book. You cannot review a book more than once<BR>";
            return resultStr;
        }

        //insert review into review table
        try{
            st = con.prepareStatement(insertReviewSQL);
            st.setString(1, username);
            st.setString(2, isbn);
            st.setString(3, score);
            st.setString(4, review);
            
            st.executeUpdate();
        } catch (Exception e) {

        }

        //Get average score
        try{
            st = con.prepareStatement(getAvgSQL);
            st.setString(1, isbn);
            r = st.executeQuery();

            while(r.next())
            {
                newAverage = r.getString("avg");
            }
        } catch (Exception e) {

        }

        //set new average score

        try{
            st = con.prepareStatement(setAvgSQL);
            st.setString(1, newAverage);
            st.setString(2, isbn);
            st.executeUpdate();

        } catch (Exception e) {

        }

        resultStr +="<h2>Review Added</h2>A review with the following has been added to the database:<BR>Username: " + username + "<BR>ISBN: " + isbn + "<BR>Score: " + score + "<BR>Opinion: " + review + "<BR>";
        return resultStr;

    }

    public static boolean CheckForReviewWeb(String username, String isbn, Connection con)
    {
        boolean found = false;
        ResultSet r = null;
        PreparedStatement st = null;

        String checkReviewSQL = "SELECT username FROM " + ReviewTable + " WHERE username = ? AND isbn = ?";

        try{
            st = con.prepareStatement(checkReviewSQL);
            st.setString(1, username);
            st.setString(2, isbn);
            r = st.executeQuery();

            if(!r.next())
                return found;
            else
                found = true;
        
        } catch (Exception e) {

        }

        return found;


    }//end of CheckForReviewWeb


    /**
     * Allows a user to leave a review for a book, only one review per book per use allowed
     */
    public static void LeaveReview() {
        String isbnreview;
        int score;
        String userOpinion;
        boolean check;
        String newAverage = "";

        String sql;
        ResultSet r = null;
        PreparedStatement st = null;

        System.out.println("====================================================");
        System.out.println("                 REVIEW A BOOK");
        System.out.println("====================================================");
        System.out.println();

        System.out.print("What is the ISBN of the book you wish to leave a review for? :");
        isbnreview = in.nextLine();

        //check to see if isbn even exists
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
            Console.MainMenu();
        }

        System.out.print("Please enter a score [1 - 10]: ");
        try {
            do {
                userSelection = in.nextLine();

                if (!Console.IsNumber(userSelection)) {
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

            if (!Console.IsNumber(userSelection)) {
                System.out.print(userSelection + " is an invalid option, ");
                System.out.print("Please make a selection: ");
            }

            //if the user did enter a number
            if (Console.IsNumber(userSelection)) {
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

            sql = "INSERT INTO " + ReviewTable + " (username, ISBN, score, opinion) VALUES(?, ?, ?, ?)";

            try {

                //set sql parameters
                st = con.prepareStatement(sql);
                st.setString(1, loggedInUser);
                st.setString(2, isbnreview);
                st.setString(3, Integer.toString(score));
                st.setString(4, userOpinion);
                st.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
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
                e.printStackTrace();

            }
            if(verbose)
                PrintSQLStatement(st, sql);

        }


        sql = "SELECT AVG(score) AS avg FROM " + ReviewTable + " WHERE isbn = ? ";

        try
        {
            st = con.prepareStatement(sql);
            st.setString(1, isbnreview);
            r = st.executeQuery();

            while(r.next())
            {
                newAverage = r.getString("avg");
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        //add average to its corresponding record in Records table
        sql = "UPDATE " + RecordsTable + " SET avgreviewscore = ? WHERE isbn = ?";

        try
        {
            st = con.prepareStatement(sql);
            st.setString(1, newAverage);
            st.setString(2, isbnreview);
            st.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        //return to main menu
        Console.MainMenu();

    }//end of LeaveReview

    /**
     * Debugging method, prints out the raw sql query construted and the corresponding
     * final PreparedStatement
     * @param st
     * @param sql
     */
    public static void PrintSQLStatement(PreparedStatement st, String sql)
    {
        System.out.println();
        System.out.println("SQL RAW STRING: " + sql);
        System.out.println();
        System.out.println("PREPARED STATEMENT: " + st);
        System.out.println();
    }

    public static String PrintUserRecordWeb(String username, Connection con) throws Exception
    {

        String resultStr = "<h2> User Details: </h2>";
        String personalDataSql = "SELECT * FROM " + UserTable + " where username = ?";
        String checkoutHistorySql = "SELECT isbn, title, checkoutdate, returndate FROM " + CheckoutRecordTable + " WHERE username = ?";
        String lostBookSql = "SELECT isbn, title, checkoutdate, returndate FROM " + CheckoutRecordTable + " WHERE username = ? AND lost = 1";
        String bookRequestSql = "SELECT r.title, w.dateadded FROM " + RecordsTable + " r, " + WaitListTable + " w WHERE w.isbn = r.isbn AND w.username = ?";
        String reviewSql = "SELECT r.title, a.score, a.opinion FROM " + ReviewTable + " a, " + RecordsTable + " r WHERE r.isbn = a.isbn AND a.username = ?";
        
        ResultSet results;
        PreparedStatement st = null;


        //check to see if user exists
        if(!CheckForUserWeb(username, con))
        {
            resultStr = "User does not exist, please enter a valid username\n";
            return resultStr;
        }

        //get personal data

        try{
            st = con.prepareStatement(personalDataSql);
            st.setString(1, username);
            results = st.executeQuery();

            while(results.next()) {

                resultStr += "Full Name: " + results.getString("full_name") + "<BR>";
                resultStr += "Username: " + results.getString("username") + "<BR>";
                resultStr += "Idenfitication Number: " + results.getString("cardid") + "<BR>";
                resultStr += "Email: " + results.getString("email") + "<BR>";
                resultStr += "Address: " + results.getString("address") + "<BR>";
                resultStr += "Phone Number: " + results.getString("phonenumber" + "<BR>");
                resultStr += "<BR><BR>";

            }

        } catch (Exception e) {

        }

        resultStr += "<h2> Checkout History: </h2>";

        //get checkout history
        try{

            st = con.prepareStatement(checkoutHistorySql);

            //set sql parameters
            st.setString(1, username);
            results = st.executeQuery();

            while (results.next()) {
                resultStr += "ISBN: " + results.getString("isbn") + "<BR>";
                resultStr += "Title: " + results.getString("title") + "<BR>";
                resultStr += "Checkout Date: " + results.getString("checkoutdate") + "<BR>";
                resultStr += "Return Date: " + results.getString("returndate") + "<BR>";
            

            }

        } catch (Exception e) {

        }

        resultStr += "<h2> Books lost: </h2>";

        //get books lost by user
        try{

            st = con.prepareStatement(lostBookSql);
            st.setString(1,username);
            results = st.executeQuery();

            while(results.next())
            {
            
                resultStr += "ISBN: " + results.getString("isbn") + "<BR>";
                resultStr += "Title: " + results.getString("title") + "<BR>";
                resultStr += "Date Marked as Lost: " + results.getString("returndate") + "<BR>";
              

            }

        } catch (Exception e) {

        }

        resultStr += "<h2> Books Request for Future Checkout </h2>";

        //get books that the user is currently on the waitlist for

        try{
            st = con.prepareStatement(bookRequestSql);
            st.setString(1, username);
            results = st.executeQuery();

            while(results.next())
            {
                resultStr += "Title: " + results.getString("title") + "<BR>";
                resultStr += "Date Added to WaitList: " + results.getString("dateadded") + "<BR>";
                
            }

        } catch (Exception e) {

        }

        resultStr += "<h2> Reviews </h2>";

        try{
            st = con.prepareStatement(reviewSql);
            st.setString(1, username);
            results = st.executeQuery();

            while (results.next())
            {
                resultStr += "Title: " + results.getString("title") + "<BR>";
                resultStr += "Score: " + results.getString("score") + "<BR>";
                resultStr += "Opinion: " + results.getString("opinion") + "<BR>";
            }

        } catch (Exception e) {


        }

   
        return resultStr;
    }//end of PrintUserRecordWeb

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

        System.out.println("====================================================");
        System.out.println("            DISPLAY USER RECORD");
        System.out.println("====================================================");
        System.out.println();
        System.out.print("Username to view record of: ");
        lookedUpUser = in.nextLine();

        //check to see if user exists
        if (!CheckForUser(lookedUpUser)) {
            System.out.println("User does not exist, returning to main menu");
            System.out.println();
            Console.MainMenu();

        }

        System.out.println();
        //Printing personal data of user
        System.out.println("Personal data for " + lookedUpUser + ":");

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
        System.out.println("Checkout History for " + lookedUpUser + ":");
        System.out.println();

        //print book checkout history for user
        sql = "SELECT isbn, title, checkoutdate, returndate FROM " + CheckoutRecordTable + " WHERE username = ?";

        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, lookedUpUser);
            r = st.executeQuery();

            while (r.next()) {
                System.out.println("ISBN: " + r.getString("isbn") + "\t ");
                System.out.println("Title: " + r.getString("title") + "\t ");
                System.out.println("Checkout Date: " + r.getString("checkoutdate") + "\t ");
                System.out.println("Return Date: " + r.getString("returndate") + "\t ");
                System.out.println();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (verbose)
            PrintSQLStatement(st, sql);

        System.out.println("Books lost by: " + lookedUpUser + ":");

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
            e.printStackTrace();
        }
        System.out.println();

        if (verbose)
            PrintSQLStatement(st, sql);

        //print books books that user is on waitlist for
        System.out.println("Books requested for future checkout by " + lookedUpUser + ":");
        System.out.println();

        sql = "SELECT r.title, w.dateadded FROM " + RecordsTable + " r, " + WaitListTable + " w WHERE w.isbn = r.isbn AND w.username = ?";
        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, lookedUpUser);
            r = st.executeQuery();

            while (r.next()) {
                System.out.println("Title: " + r.getString("title") + "\t ");
                System.out.println("Date added to waitlist: " + r.getString("dateadded") + "\t ");
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (verbose)
            PrintSQLStatement(st, sql);

        System.out.println("Reviews by " + lookedUpUser + ":");

        //print reviews
        sql = "SELECT r.title, a.score, a.opinion FROM " + ReviewTable + " a, " + RecordsTable + " r WHERE r.isbn = a.isbn AND a.username = ?";
        try {

            //set sql parameters
            st = con.prepareStatement(sql);
            st.setString(1, lookedUpUser);
            r = st.executeQuery();

            while (r.next()) {
                System.out.println("Title: " + r.getString("title") + "\t ");
                System.out.println("Score: " + r.getString("score") + "\t ");
                System.out.println("Opinion: " + r.getString("opinion"));
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (verbose)
            PrintSQLStatement(st, sql);

        System.out.println();


        //return to main menu
        Console.MainMenu();

    }//end of PrintUserRecord

    public static String PrintUserStatisticsWeb(String amount, String selection, Connection con)
    {
        PreparedStatement st = null;
        ResultSet r = null;

        String resultStr = "";
        String topFrequency = "";
        String count = null;

        String getMaxCheckoutSQL = "SELECT COUNT(username) AS maxcheckoutcount FROM " + CheckoutRecordTable + " GROUP BY username ORDER BY maxcheckoutcount DESC LIMIT 1";
        String getUserSQL = "SELECT username, COUNT(username) AS count FROM " + CheckoutRecordTable + " GROUP BY username";
        String getUserReviewSQL = "SELECT username, COUNT(username) AS count FROM " + ReviewTable + " GROUP BY username";
        String getMaxReviewSQL = "SELECT COUNT(username) AS reviewcount FROM " + ReviewTable + " GROUP BY username ORDER BY reviewcount DESC LIMIT 1";
        String getMaxLostSQL =  "SELECT COUNT(username) AS lostcount FROM " + CheckoutRecordTable + " c WHERE c.lost = 1 GROUP BY username ORDER BY lostcount DESC LIMIT 1";
        //users who checked out the most books
        if(selection.matches("1"))
        {
            //get highest number of checkout
            try{
                st = con.prepareStatement(getMaxCheckoutSQL);
                r = st.executeQuery();

                while(r.next()){
                    topFrequency = r.getString("maxcheckoutcount");
                }
            } catch (Exception e) {

            }

            //get users who match that checkoutcount
            try {
                st = con.prepareStatement(getUserSQL);
                r = st.executeQuery();

                while(r.next()) {
                    if(r.getString("count").matches(topFrequency))
                    {
                        resultStr += "User: " + r.getString("username") + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Number of books checked out: " + r.getString("count") + "<BR><BR>";
                      
                    }
                }
            } catch (Exception e) {

            }

              resultStr += "The top user checked out " + topFrequency + " books";

        }//end of users who checked out the most books

        //users who rated the most number of books
        if(selection.matches("2"))
        {

             //get highest number of reviews
            try{
                st = con.prepareStatement(getMaxReviewSQL);
                r = st.executeQuery();

                while(r.next()){
                    topFrequency = r.getString("reviewcount");
                }
            } catch (Exception e) {

            }

            //get users who match that reviewcount
            try {
                st = con.prepareStatement(getUserReviewSQL);
                r = st.executeQuery();

                while(r.next()) {
                    if(r.getString("count").matches(topFrequency)){
                        resultStr += "User: " + r.getString("username") + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Books reviewed: " + r.getString("count") + "<BR><BR>";
                      
                    }
                }
            } catch (Exception e) {

            }

              resultStr += "The top user reviewed " + topFrequency + " books";

        }//end of users who have rated the most number of books

        //users who lost the most books
        if(selection.matches("3"))
        {
            //get highest number of loscount
            try{
                st = con.prepareStatement(getMaxLostSQL);
                r = st.executeQuery();

                while(r.next()){
                    topFrequency = r.getString("lostcount");
                }
            } catch (Exception e) {

            }

            //get users who match that checkoutcount
            try {
                st = con.prepareStatement(getUserSQL);
                r = st.executeQuery();

                while(r.next()) {
                    if(r.getString("count").matches(topFrequency))
                    {
                        resultStr += "User: " + r.getString("username") + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Books lost: " + r.getString("count") + "<BR><BR>";
                      
                    }
                }
            } catch (Exception e) {

            }

              resultStr += "The top user lost " + topFrequency + " books";
        } //end of users who have lost the most books


        return resultStr;
    }

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

        System.out.println("====================================================");
        System.out.println("               USER STATISTICS");
        System.out.println("====================================================");
        System.out.println();

        System.out.println("Do you want to..");
        System.out.println("List top users who have checked out the most books [1]");
        System.out.println("List top users who have rated the most number of books [2]");
        System.out.println("List top users who have lost the most books [3]");
        System.out.print("Please make a selection: ");

        do {
            userSelection = in.nextLine();

            if (!Console.IsNumber(userSelection)) {
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

        Console.MainMenu();

    }//end of PrintUserStatistics

    public static String PrintBookRecordWeb(String isbn, Connection con)
    {
        String resultStr = "<h2>Book record for " + isbn + " </h2><BR>";

        String getBookInfoSQL = "SELECT * FROM " + RecordsTable + " WHERE isbn = ? ";
        String getAuthorsSQL = "SELECT authorname FROM " + AuthorListTable + " WHERE isbn = ?";
        String getLocationSQL = "SELECT copies, location FROM " + InventoryTable + " WHERE isbn = ?";
        String getCheckoutInfoSQL = "SELECT username, checkoutdate, returndate FROM " + CheckoutRecordTable + " WHERE isbn = ?";
        String getReviewSQL = "SELECT username, score, opinion FROM " + ReviewTable + " WHERE isbn = ? "; 
        String getAvgSQL = "SELECT AVG(score) AS avg FROM " + ReviewTable + " WHERE isbn = ? ";

        String foundisbn = "";
        String title = "";
        String summary = "";
        String subject = "";
        String format = "";
        String pubyear = "";
        String publisher = "";
        String authors = "";
        String loc = "";

        PreparedStatement st = null;
        ResultSet r = null;

        boolean check;

        int copies;

        check = CheckForISBNWeb(isbn, con);

        if(!check)
        {
            resultStr ="That isbn does not exist in the database<BR>";
            return resultStr;
        }

        //get book information
        try{
            st = con.prepareStatement(getBookInfoSQL);
            st.setString(1, isbn);
            r = st.executeQuery();

            while(r.next())
            {
                foundisbn = r.getString("ISBN");
                title = r.getString("title");
                summary = r.getString("bookSummary");
                subject = r.getString("subject");
                format = r.getString("format");
                pubyear = r.getString("pubyear");
                publisher = r.getString("publisher");
            }

        } catch (Exception e) {

        }

        authors +="Author(s):<BR>";

        //get authors
        try{
            st = con.prepareStatement(getAuthorsSQL);
            st. setString(1, isbn);
            r = st.executeQuery();

            while(r.next()){
                authors += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + r.getString("authorname") + "<BR>";
            }

        } catch (Exception e) {

        }

        //put book information with authors
        resultStr +="Title: " + title + "<BR>";
        resultStr += authors;
        resultStr +="ISBN: " + foundisbn + "<BR>";
        resultStr +="Summary: " + summary + "<BR>";
        resultStr +="Subject: " + subject + "<BR>";
        resultStr +="Format: " + format + "<BR>";
        resultStr +="Publisher: " + publisher + "<BR><BR><BR>";


        //get location information
        resultStr +="<h2>Location Information: </h2><BR>";

        try{
            st = con.prepareStatement(getLocationSQL);
            st.setString(1,isbn);
            r = st.executeQuery();

            while(r.next())
            {
                resultStr += "There are " + r.getString("copies") + " copies located on shelf: " + r.getString("location");
            }

        } catch (Exception e) {

        }

        //get checkout information
        resultStr +="<BR><BR><h2>Checkout Information:</h2><BR>";

        try{
            st = con.prepareStatement(getCheckoutInfoSQL);
            st.setString(1, isbn);
            r = st.executeQuery();

            while(r.next())
            {
                resultStr += "User: " + r.getString("username");
                resultStr += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                resultStr += "Checkout Date: " + r.getString("checkoutdate") + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "Return Date: " + r.getString("returndate") + "<BR>";
            }

        } catch (Exception e) {

        }

        resultStr +="<BR><BR><h2>Reviews (If applicable):</h2> <BR>";

        //get review information
        try{
            st = con.prepareStatement(getReviewSQL);
            st.setString(1, isbn);
            r = st.executeQuery();

            while(r.next())
            {
                resultStr += "<BR>User: " + r.getString("username");
                resultStr += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                resultStr += "Score: " + r.getString("score") + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "Optional Opinion: " + r.getString("opinion") + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
            }
        } catch (Exception e) {

        }

        //get average score
        try{
            st = con.prepareStatement(getAvgSQL);
            st.setString(1, isbn);
            r = st.executeQuery();

            while(r.next())
            {
                resultStr += "Average Review Score: " + r.getString("avg");
            }

        } catch (Exception e) {

        }

        resultStr += "<BR><BR>";

        return resultStr;
    }

    /**
     * Prints record of a book:
     *
     * Which includes
     * book info
     * people who currently have the book checked out
     * location information of copies
     * reviews and average review score
     */
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

        //get location information for book from inventory table
        sql = "SELECT copies, location FROM " + InventoryTable + " WHERE isbn = ?";

        try
        {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            r = st.executeQuery();

            while(r.next())
            {
                System.out.println("There are " + r.getString("copies") + " copies located on shelf: " + r.getString("location"));
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
                    r.getString("returndate") + "\n");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        //get review information
        System.out.println("Book Reviews (if applicable): \n");

        sql = "SELECT username, score, opinion FROM " + ReviewTable + " WHERE isbn = ? ";

        try
        {
            st = con.prepareStatement(sql);
            st.setString(1, isbn);
            r = st.executeQuery();

            while(r.next())
            {
                System.out.println("User: " + r.getString("username"));
                System.out.println("\t\t\t" + "Score: " + r.getString("score") + "\t" + "Optional Opinion: " + r.getString("opinion") + "\n");
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


        //return to main menu  
        Console.MainMenu();
    }//end of PrintBookRecord

    public static String PrintLibraryStatisticsWeb(String amount, String selection, Connection con)
    {
        String resultStr = "";

        String mostRequestedSQL = "SELECT MAX(waitcount) AS maxwaitcount FROM " + RecordsTable;
        String getRecordSQL = "SELECT * FROM " + RecordsTable;
        String mostCheckedoutSQL = "SELECT MAX(checkoutcount) AS maxcheckoutcount FROM " + RecordsTable;
        String mostLostSQL = "SELECT MAX(lostcount) AS maxlostcount FROM " + RecordsTable;
        String mostPopularAuthorsSQL = "";

        PreparedStatement st = null;
        ResultSet r = null;
        String topFrequency = "";

        int counter = 0;
        int requestedAmount = Integer.parseInt(amount);

        //most requested books
        if(selection.matches("1"))
        {
            //get most requested number
            try{
                st = con.prepareStatement(mostRequestedSQL);
                r = st.executeQuery();

                while(r.next())
                {
                    topFrequency = r.getString("maxwaitcount");
                }
            } catch (Exception e) {

            }

            //get record

            try{
                st = con.prepareStatement(getRecordSQL);
                r = st.executeQuery();

                while(r.next())
                {
                    if(r.getString("waitcount").matches(topFrequency))
                    {
                        if(counter < requestedAmount){
                            resultStr +="Title: " + r.getString("title") + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Size of WaitList: " + topFrequency + "<BR>";
                            counter++;
                        }

                    }
                }
            } catch (Exception e) {

            }
        }//end of most requested books

        //most checked out books
        if(selection.matches("2"))
        {
            //get most checked out number

            try{
                st = con.prepareStatement(mostCheckedoutSQL);
                r = st.executeQuery();

                while(r.next())
                {
                    topFrequency = r.getString("maxcheckoutcount");
                }
            } catch (Exception e) {

            }

            //get record

            try{
                st = con.prepareStatement(getRecordSQL);
                r = st.executeQuery();

                while(r.next())
                {
                    if(r.getString("checkoutcount").matches(topFrequency))
                    {
                        if(counter < requestedAmount){
                            resultStr +="Title: " + r.getString("title") + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Checkout Count: " + topFrequency + "<BR>";
                            counter++;
                        }
                    }
                }
            } catch (Exception e) {

            }

        }//end of most checked out books

        //most lost books
        if(selection.matches("3"))
        {  
            //get highest lost count

            try{
                st = con.prepareStatement(mostLostSQL);
                r = st.executeQuery();

                while(r.next())
                {
                    topFrequency = r.getString("maxlostcount");
                }
            } catch (Exception e) {

            }

            //get record

            try{
                st = con.prepareStatement(getRecordSQL);
                r = st.executeQuery();

                while(r.next())
                {
                    if(r.getString("lostcount").matches(topFrequency))
                    {
                        if(counter  < requestedAmount)
                        {
                            resultStr +="Title: " + r.getString("title") + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Lost Count: " + topFrequency + "<BR>";
                            counter++;
                        }
                    }
                }
            } catch (Exception e) {

            }

        }//end of most lost books

        //most popular authors
        if(selection.matches("4"))
        {
            //TODO: MISSING Construct sql query for most popular authors (instances of checkout records with the most common authors)
        }//end of most popular authors

        return resultStr;
    }

    public static void PrintLibraryStatistics()
    {

        //TODO: Stretch Goal, Select * is expensive, have it return only those whom match that previosusly discovered max number
        int nbooks;
        int count = 0;
        String sql;
        PreparedStatement st = null;
        ResultSet r = null;
        String topFrequency = "";

        System.out.println("====================================================");
        System.out.println("             LIBRARY STATISTICS");
        System.out.println("====================================================");
        System.out.println();
        System.out.print("How many books would you like to request stats about?: ");

        do {
            userSelection = in.nextLine();

            if (!Console.IsNumber(userSelection)) {
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
        System.out.print("Please enter a number [1 - 4] for your selection: ");

        do {
            userSelection = in.nextLine();

            if (!Console.IsNumber(userSelection)) {
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

        System.out.println();


        //Most requested books
        if (choice == 1) {

            sql = "SELECT MAX(waitcount) AS maxwaitcount FROM " + RecordsTable;

            try {
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while (r.next()) {
                    topFrequency = r.getString("maxwaitcount");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

            sql = "SELECT * FROM " + RecordsTable;

            try{
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while(r.next())
                {
                    if(r.getString("waitcount").matches(topFrequency))
                    {
                        if(count < nbooks) {
                            System.out.println("Title: " + r.getString("title") + "\t Size of Waitlist: " + topFrequency);
                            count++;
                        }
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        //Most checked out books
        else if (choice == 2) {
            sql = "SELECT MAX(checkoutcount) AS maxcheckoutcount FROM " + RecordsTable;

            try {
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while (r.next()) {
                    topFrequency = r.getString("maxcheckoutcount");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

            sql = "SELECT * FROM " + RecordsTable;

            try{
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while(r.next())
                {
                    if(r.getString("checkoutcount").matches(topFrequency))
                    {
                        if(count < nbooks) {
                            System.out.println("Title: " + r.getString("title") + "\t Checkout Count: " + topFrequency);
                            count++;
                        }
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        //Most lost books
        else if (choice == 3) {
            sql = "SELECT MAX(lostcount) AS maxlostcount FROM " + RecordsTable;

            try {
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while (r.next()) {
                    topFrequency = r.getString("maxlostcount");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

            sql = "SELECT * FROM " + RecordsTable;

            try{
                st = con.prepareStatement(sql);
                r = st.executeQuery();

                while(r.next())
                {
                    if(r.getString("lostcount").matches(topFrequency))
                    {
                        if(count < nbooks) {
                            System.out.println("Title: " + r.getString("title") + "\t Lost Count: " + topFrequency);
                            count++;
                        }
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        //most popular authors
        else if (choice == 4) {
            //TODO: MISSING Construct sql query for most popular authors (instances of checkout records with the most common authors)
        }

        //display main menu again
        Console.MainMenu();
    }//end of PrintLibraryStatistics

    public static String ReturnBookWeb(String username, String isbn, boolean lost, String returnDate, Connection con)
    {
        PreparedStatement st = null;
        ResultSet r = null;

        String incrementLostCountSQL = "UPDATE " + RecordsTable + " SET lostcount = lostcount + 1 WHERE isbn = ?";
        String setBookLostSQL = "UPDATE " + CheckoutRecordTable + " SET returndate = ?, lost = 1 WHERE isbn = ? AND username = ?";
        String setReturnDateSQL =  "UPDATE " + CheckoutRecordTable + " SET returndate = ? WHERE isbn = ? AND username = ?";
        String incrementCopiesSQL =  "UPDATE " + InventoryTable + " SET copies = copies + 1 WHERE isbn = ?";
        String getWaitingUserSQL = "SELECT username FROM "+ WaitListTable +" WHERE isbn = ?";
        String resultStr = "";
        String time;

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Date today = new Date();

        time = timeFormat.format(today).toString();

        returnDate += " " + time;

        if(lost)
        {
            //have to set book lost in checkout record
            try{
                st = con.prepareStatement(setBookLostSQL);
                st.setString(1, returnDate);
                st.setString(2, isbn);
                st.setString(3, username);
                st.executeUpdate();

            } catch (Exception e) {

            }

            //increment lost count
            try{
                st = con.prepareStatement(incrementLostCountSQL);
                st.setString(1,isbn);
                st.executeUpdate();
            } catch (Exception e) {

            }

        }
        //book was not lost, properly return book
        else
        {
            //set return date
            try{
                st = con.prepareStatement(setReturnDateSQL);
                st.setString(1, returnDate);
                st.setString(2, isbn);
                st.setString(3, username);
                st.executeUpdate();

            } catch (Exception e) {

            }

            //put copy of book back
             try{
                st = con.prepareStatement(incrementCopiesSQL);
                st.setString(1, isbn);
                st.executeUpdate();
            } catch (Exception e){
                
            }
            resultStr += "<h2>Book Returned</h2>Waitlist:<BR>";
            //get list of users waiting on book
            try
            {
                st = con.prepareStatement(getWaitingUserSQL);
                st.setString(1, isbn);
                r = st.executeQuery();

                while(r.next())
                {
                    resultStr += r.getString("username") + "<BR>";
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        return resultStr;
    }

    /**
     * Marks a user's checkout record as returned. If the book was lost, the date returned is when the book was lost
     * and the book is marked as lost. The number of copies is not incremented.
     */
    public static void ReturnBook()
    {

        //TODO: Assuming that the user does have a currently open checkout record for given book, maybe check for if they haven't returned it later
        //TODO: Assuming that the waitlist isn't displayed when a book is marked as lost
        String isbn, month, day, year, returndate;

        ResultSet r = null;
        PreparedStatement st = null;
        String sql;

        boolean lost = false;

        System.out.println("====================================================");
        System.out.println("                 BOOK RETURN");
        System.out.println("====================================================");
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

        System.out.print("Was this book lost? [1] yes [0] no: ");

        do {
            userSelection = in.nextLine();

            if (!Console.IsNumber(userSelection)) {
                System.out.print(userSelection + " is an invalid option, ");
                System.out.print("Please make a selection: ");
            }

            //if the user did enter a number
            if (Console.IsNumber(userSelection)) {
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
                    System.out.print("\t" + r.getString("username" + "\n"));
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        System.out.println();

        //return to main menu
        Console.MainMenu();

    }//end of ReturnBook()

    /**
     * Sets the database connection to use in the appropriate functions
     * @param c
     */
    public static void SetConnection(Connection c)
    {
        try {

            //set statement
//            statem = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //store connection
            con = c;
        } catch (Exception e) {
            e.printStackTrace();
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
