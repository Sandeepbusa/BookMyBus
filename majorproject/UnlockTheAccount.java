package com.flm.majorproject;

import java.util.Scanner;

public class UnlockTheAccount {

	public void unlockAccount() {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Enter the Email id: ");
			scanner.next();
			System.out.print("Enter the new password: ");
			scanner.next();
			System.out.println("Account unlocked successfully.");

		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}
}
