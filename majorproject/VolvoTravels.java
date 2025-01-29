package com.flm.majorproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

public class VolvoTravels {
    public static void main(String[] args) {
        // Try-with-resources block to automatically close resources
        try (BufferedReader bReader = new BufferedReader(
                new FileReader(new File("C:\\Users\\Naresh\\OneDrive\\Desktop\\MajorProject\\Logo.txt")))) {
            String line;
            while ((line = bReader.readLine()) != null) {
                System.out.println(line); // Print each line from the file
            }
        } catch (IOException ex) {
            System.out.println("Error message: " + ex.getMessage());
        }

        UserDetails menu = new UserDetails();
        menu.displayMenu();
    }
}
