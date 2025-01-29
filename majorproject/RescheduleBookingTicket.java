package com.flm.majorproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RescheduleBookingTicket {
    UserDetails userDetails = new UserDetails();

    public void reschedule() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the Email ID: ");
            String email = scanner.nextLine();

            System.out.print("Enter the Password: ");
            String password = scanner.nextLine();

            // Validate user credentials
            if (userBookings(email, password)) {
                System.out.println("User validated successfully!");

                // Get current booking details
                System.out.print("Enter the current booking ID: ");
                int bookingId = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                // Check if the booking exists
                if (validateBooking(email, bookingId)) {
                    // Get new rescheduling details
                    System.out.print("Enter the new Source: ");
                    String newSource = scanner.nextLine();

                    System.out.print("Enter the new Destination: ");
                    String newDestination = scanner.nextLine();

                    // Display available routes
                    displayAvailableRoutes(newSource, newDestination, bookingId, scanner);
                } else {
                    System.out.println("No booking found with the given ID.");
                }
            } else {
                System.out.println("Invalid Email ID or Password.");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private boolean userBookings(String email, String password) {
        String query = "SELECT * FROM user_details WHERE Email_id = ? AND Password = ?";
        try (Connection con = userDetails.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next(); // If a record is found, the credentials are valid
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }

    private boolean validateBooking(String email, int bookingId) {
        String query = "SELECT * FROM booking_details WHERE Booking_ID = ? AND Email_id = ?";
        try (Connection con = userDetails.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, bookingId);
            pstmt.setString(2, email);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next(); // Check if the booking exists
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }

    private void displayAvailableRoutes(String source, String destination, int bookingId, Scanner scanner) {
        String query = "SELECT * FROM routes_table WHERE Source = ? AND Destination = ?";
        try (Connection con = userDetails.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, source);
            pstmt.setString(2, destination);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No routes available between " + source + " and " + destination);
                } else {
                    System.out.println("Available routes:");
                    int routeCounter = 1;
                    while (resultSet.next()) {
                        System.out.println(routeCounter + ". Route ID: " + resultSet.getInt("routes_id") +
                                           ", Price: " + resultSet.getInt("Price") +
                                           ", Seats Available: " + resultSet.getInt("NoofSeats"));
                        routeCounter++;
                    }

                    System.out.print("Select the route ID to reschedule: ");
                    int newRouteId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    updateBooking(bookingId, newRouteId);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void updateBooking(int bookingId, int newRouteId) {
        String updateQuery = "UPDATE booking_details SET Route_ID = ? WHERE Booking_ID = ?";
        String updateSeatsQuery = "UPDATE routes_table SET NoofSeats = NoofSeats - 1 WHERE routes_id = ?";
        try (Connection con = userDetails.getConnection();
             PreparedStatement updateBookingPstmt = con.prepareStatement(updateQuery);
             PreparedStatement updateSeatsPstmt = con.prepareStatement(updateSeatsQuery)) {

            // Update booking details
            updateBookingPstmt.setInt(1, newRouteId);
            updateBookingPstmt.setInt(2, bookingId);
            int rowsUpdated = updateBookingPstmt.executeUpdate();

            // Decrease seat count for the new route
            updateSeatsPstmt.setInt(1, newRouteId);
            updateSeatsPstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Booking rescheduled successfully!");
            } else {
                System.out.println("Failed to reschedule the booking. Please try again.");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
