Welcome to the Book My Bus project! This Java-based application simulates an online bus booking system, providing users with functionalities such as user registration, login, journey booking, rescheduling bookings, and unlocking accounts. The project demonstrates various Java concepts, including file handling, database integration, exception handling, and object-oriented programming.

Features:
User Registration: Users can register by providing their details.
Login System: Login functionality with account locking after multiple failed attempts.
Journey Booking: Users can search for available routes, view pricing, and book tickets.
Reschedule Booking: Allows users to reschedule their booked journeys.
Account Unlocking: Users can unlock their accounts by resetting their passwords.
Database Integration: Data is stored in and retrieved from a MySQL database.
File Handling: Display project logo from a file.

Technologies Used:
Programming Language: Java
Database: MySQL
JDBC: For database connectivity
File Handling: Read logo file during application startup

Code Overview
Main Components
VolvoTravels:
Entry point for the application.
Reads and displays the project logo from a file.
Invokes the UserDetails menu.

UserDetails:
Manages user registration and menu display.
Provides connection to the MySQL database.

Login:
Implements user login functionality with account lock after 3 failed attempts.

JourneyBooking:
Handles ticket booking by querying available routes.
Updates seat availability and stores booking details in the database.

RescheduleBookingTicket:
Validates bookings and reschedules them to new routes.

UnlockTheAccount:
Allows users to reset their passwords and unlock their accounts.

