package com.flm.majorproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Login {

	
	UserDetails userDetails = new UserDetails();

	public void logindetails() {
		try (Scanner scanner = new Scanner(System.in)) {
			int maxAttempts = 3; // Maximum allowed attempts
			int attemptCount = 0; // Counter for failed attempts

			while (attemptCount < maxAttempts) {
				
				System.out.print("Enter the Email ID: ");
				String email = scanner.nextLine();

				System.out.print("Enter the Password: ");
				String password = scanner.nextLine();

			
				if (validateUser(email, password)) {
					System.out.println("Login successful! Welcome to Volvo Travels.");
					break; 
				} else {
					attemptCount++;
					System.out
							.println("Invalid email or password. Attempt " + attemptCount + " of " + maxAttempts + ".");
				}

				// Lock account if maximum attempts are reached
				if (attemptCount == maxAttempts) {
					System.out.println("Account locked due to too many failed attempts.");
				}
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	private boolean validateUser(String email, String password) {
		String query = "SELECT * FROM user_details WHERE Email_id = ? AND Password = ?";
		try (Connection con = userDetails.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

			pstmt.setString(1, email);
			pstmt.setString(2, password);

		
			try (ResultSet resultSet = pstmt.executeQuery()) {
				
				return resultSet.next();
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			return false;
		}
	}
}
