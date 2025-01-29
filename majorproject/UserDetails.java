package com.flm.majorproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class UserDetails {

    private Connection conn = null;

    // Method to establish and reuse a single database connection
    public Connection getConnection() {
        String driver = "com.mysql.cj.jdbc.Driver"; // Correct MySQL driver
        String user = "root";
        String password = "Sandeep@3014"; // Ensure this is secured in production
        String url = "jdbc:mysql://localhost:3306/major_project"; // Ensure database name matches your setup

        if (conn == null) {
            try {
                Class.forName(driver); 
            } catch (ClassNotFoundException ex) {
                System.out.println("Error: MySQL Driver not found - " + ex.getMessage());
            }
            try {
                conn = DriverManager.getConnection(url, user, password); 
            } catch (SQLException ex) {
                System.out.println("Error: Unable to establish connection - " + ex.getMessage());
            }
        }
        return conn;
    }

    public void displayMenu() {
    
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("\n=== Volvo Travels Menu ===");
                System.out.println("1. Register New User");
                System.out.println("2. Login");
                System.out.println("3. Book a Journey");
                System.out.println("4. Reschedule Your Journey");
                System.out.println("5. Unlock Account");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        enterUserDetails(scanner);
                        break;
                    case "2":
                        Login login = new Login(); 
                        login.logindetails();
                        break;
                    case "3":
                        JourneyBooking booking = new JourneyBooking(scanner);
                        break;
                    case "4":
                        RescheduleBookingTicket rt = new RescheduleBookingTicket();
                        rt.reschedule();
                        break;
                    case "5":
                        UnlockTheAccount unlock = new UnlockTheAccount();
                        unlock.unlockAccount();
                        break;
                    case "6":
                        System.out.println("Exiting... Thank you for using Volvo Travels!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
        
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private void enterUserDetails(Scanner scanner) {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter Mobile Number: ");
        String mobileNumber = scanner.nextLine();

        System.out.print("Enter Gender (M/F): ");
        String gender = scanner.nextLine();

        System.out.print("Enter Email ID: ");
        String emailId = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        String query = "INSERT INTO user_details (First_Name, Last_Name, Mobile_Number, Gender, Email_id, Password) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, mobileNumber);
            pstmt.setString(4, gender);
            pstmt.setString(5, emailId);
            pstmt.setString(6, password);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User details added successfully!");
            } else {
                System.out.println("Failed to add user details. Please try again.");
            }
        } catch (SQLException ex) {
            System.out.println("Error: Unable to execute query - " + ex.getMessage());
        }
    }
}
