package com.flm.majorproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class JourneyBooking {
	private final UserDetails userDetails = new UserDetails(); // For database connection

	public JourneyBooking(Scanner scanner) {
		try {
			System.out.print("Enter the Source: ");
			String source = scanner.nextLine();
			System.out.print("Enter the Destination: ");
			String destination = scanner.nextLine();

			displayRoutes(source, destination, scanner);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	public void displayRoutes(String source, String destination, Scanner scanner) {
		String query = "SELECT * FROM routes_table WHERE Source = ? AND Destination = ?";
		try (Connection con = userDetails.getConnection();
				PreparedStatement pstmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY)) {

			pstmt.setString(1, source);
			pstmt.setString(2, destination);

			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (!resultSet.isBeforeFirst()) {
					System.out.println("No routes available between " + source + " and " + destination);
				} else {
					System.out.println("Available routes:");
					int routeCounter = 1;
					while (resultSet.next()) {
						System.out.println(routeCounter + ". Source: " + resultSet.getString("Source")
								+ ", Destination: " + resultSet.getString("Destination") + ", NoofSeats: "
								+ resultSet.getInt("NoofSeats") + ", Price: " + resultSet.getInt("Price"));
						routeCounter++;
					}

					selectRouteForBooking(scanner, resultSet);
				}
			}

		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	public void selectRouteForBooking(Scanner scanner, ResultSet resultSet) {
		try {
			System.out.print("Enter the route number to book: ");
			int routeNumber = scanner.nextInt();
			scanner.nextLine(); // Clear newline

			resultSet.absolute(routeNumber);

			String source = resultSet.getString("Source");
			String destination = resultSet.getString("Destination");
			int price = resultSet.getInt("Price");
			int noOfSeats = resultSet.getInt("NoofSeats");

			System.out.print("Enter your name: ");
			String name = scanner.nextLine();
			System.out.print("Enter the number of tickets you want to book: ");
			int ticketsToBook = scanner.nextInt();

			if (ticketsToBook <= noOfSeats) {
				System.out.println("Booking " + ticketsToBook + " tickets from " + source + " to " + destination);
				System.out.println("Price per ticket: " + price);
				int totalPrice = price * ticketsToBook;
				System.out.println("Total price: " + totalPrice);

				int updatedSeats = noOfSeats - ticketsToBook;
				updateSeatsInDatabase(resultSet.getInt("routes_id"), updatedSeats); // Assuming `routes_id` exists

				updateBookingInDatabase(name, source, destination, ticketsToBook, totalPrice);

				System.out.println(ticketsToBook + " tickets booked successfully!");
			} else {
				System.out.println("Sorry, only " + noOfSeats + " seats are available. Please try again.");
			}

		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	public void updateBookingInDatabase(String name, String source, String destination, int ticketsToBook,
			int totalPrice) {
		String updateQuery = "INSERT INTO booking_details (Name, noOfSeats, totalPrice, source, destination) VALUES (?, ?, ?, ?, ?)";
		try (Connection con = userDetails.getConnection();
				PreparedStatement pstmt = con.prepareStatement(updateQuery)) {

			pstmt.setString(1, name);
			pstmt.setInt(2, ticketsToBook);
			pstmt.setInt(3, totalPrice);
			pstmt.setString(4, source);
			pstmt.setString(5, destination);

			pstmt.executeUpdate(); 
			System.out.println("Booking details updated successfully!");
		} catch (Exception ex) {
			System.out.println("Error updating booking details: " + ex.getMessage());
		}
	}

	public void updateSeatsInDatabase(int routeId, int updatedSeats) {
		String updateQuery = "UPDATE routes_table SET NoofSeats = ? WHERE routes_id = ?";
		try (Connection con = userDetails.getConnection();
				PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
			pstmt.setInt(1, updatedSeats);
			pstmt.setInt(2, routeId);
			pstmt.executeUpdate();
		} catch (Exception ex) {
			System.out.println("Error updating seats: " + ex.getMessage());
		}
	}

}
