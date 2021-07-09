import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class toys {
    public static void main(String[] ags) {

        //try connect to MySQL database, user and password is "root"
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/toys",
                    "root",
                    "root"
            );

            //pass connection into the menu method
            menu(connection);

            //error handling for connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //main menu for the database
    public static void menu(Connection connection)  throws SQLException {

        //scanner to get menu number from the user
        Scanner scan = new Scanner(System.in);
        System.out.println("TOY STORE DATABASE MENU:\n1) Enter new toy\n2) Update toy information\n3) Delete item from stock\n4) Search Database\n5) Print Inventory\n0) Exit");
        String opt = scan.nextLine();


        //switch and cases for the menu 0 is exit, the others are sent to other methods (add/update/delete/search/print)
            switch (opt) {
                case "0":
                    //the program remains running until user escapes from main menu
                    System.exit(0);
                    break;
                case "1":
                    add(connection);
                    break;
                case "2":
                    update(connection);
                    break;
                case "3":
                    delete(connection);
                    break;
                case "4":
                    search(connection);
                    break;
                case "5":
                    Statement statement = connection.createStatement();
                    printAllFromTable(statement, connection);
                    break;
                // if the user enters an invalid menu number then the error message is displayed and the menu is repeated
                default:
                    System.out.println("Error!!!!! Please enter a valid option from the menu (0-5)\n\n");
                    menu(connection);
                    break;

        }
        scan.close();
    }


    //add toy section
    public static void add(Connection connection) throws SQLException {
        //display what is required, create a scanner and get input from user
        System.out.println("ADD ENTRY\nFormat(id, toy, company, qty) example: 1234, Barbie, Mattel, 55");
        Scanner scan = new Scanner(System.in);

        Statement statement = connection.createStatement();
        String entry = scan.nextLine();


        //input is stored in an array and then converted to the required
        //variable types for the database (id(INT), toy(string), company(string, qty(INT))
        String[] array = entry.split(", ");
        int id = Integer.parseInt(array[0]);
        String toy = array[1];
        String company = array[2];
        int qty = Integer.parseInt(array[3]);

        //add the new book with the variable types above inserted as SQL
        statement.executeUpdate("INSERT INTO toy_stock VALUES (" + id  + ", '" + toy + "', '" + company + "', " + qty + ")");
        System.out.println("Row Successfully added: " + Arrays.toString(array) + "\n");

        //returns to main menu
        menu(connection);
    }



    //update toy quantity by toy name
    public static void update(Connection connection) throws SQLException {

        //display what is required, create a scanner and get input from user
        System.out.println("UPDATE STOCK QTY\nSelect the toy you would like to update:\nFormat (Toy, Qty) example: Barbie, 55");
        Scanner scan = new Scanner(System.in);

        //input is stored in an array and then converted to the required
        //variable types for the database (toy(string), qty(INT))
        Statement statement = connection.createStatement();
        String update = scan.nextLine();


        String[] updateArray = update.split(", ");
        String toy = updateArray[0];
        int amount = Integer.parseInt(updateArray[1]);

        //update the the qty where the toy in the database is = to the toy given by the user
        statement.executeUpdate("UPDATE toy_stock SET qty=" + amount + " WHERE toy='" + toy + "'");
        System.out.println("Row Successfully updated: " + Arrays.toString(updateArray) + "\n");

        //return to the main menu
        menu(connection);

    }


    //delete a toy from the database
    public static void delete(Connection connection) throws SQLException {

        //display what is required, create a scanner and get input from user
        System.out.println("DELETE ITEM\nSelect the toy you would like to delete:\nFormat (Toy) example: Barbie");
        Scanner scan = new Scanner(System.in);

        Statement statement = connection.createStatement();
        String delete = scan.nextLine();

        //delete the toy from the database where the toy is = to the toy given by the user
        statement.executeUpdate("DELETE FROM toy_stock WHERE toy='" + delete + "'");
        System.out.println("Toy Deleted From Database\n");

        //back to main menu
        menu(connection);

    }

    //search for a toy row in the database
    public static void search(Connection connection) throws SQLException {

        //search for toy by giving only the name of the toy
        System.out.println("SEARCH FOR ITEM\nFormat (Toy) example: Barbie");
        Scanner scan = new Scanner(System.in);
        Statement statement = connection.createStatement();

        //store toy from user as a string
        String toy = scan.nextLine();

        //result is where the toy is = to the same name in MySQL database
        ResultSet results = statement.executeQuery("SELECT id, toy, company, qty FROM toy_stock WHERE toy='" + toy + "'");

        //print the entry of that given toy from the MySQL database
            if (results.next()) {
                System.out.println(results.getInt("id") + ", "
                        + results.getString("toy") + ", "
                        + results.getString("company") + ", "
                        + results.getInt("qty"));
            } else {
                //else the toy the user search for is not in the database
            System.out.println("'" + toy + "' unfortunately doesn't exist in the Database\n");
        }
        //return to main menu
        menu(connection);
    }



    //print inventory/database
    public static void printAllFromTable(Statement statement, Connection connection) throws SQLException {

        //results all info (id, toy, company, qty) from toy_stock
        ResultSet results = statement.executeQuery("SELECT id, toy, company, qty FROM toy_stock");

        //while there is another row in the database print it
        while(results.next()) {
            System.out.println(results.getInt("id") + ", "
                    + results.getString("toy") + ", "
                    + results.getString("company") + ", "
                    + results.getInt("qty"));
        }
        System.out.println("\n");

        //return to main menu
        menu(connection);
    }
}