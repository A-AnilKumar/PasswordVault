package com.anil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Scanner;

import utility.ConfigPropertyReader;


public class Login {
	private String user = "";
	private int attempts = 3;

	public String getUserName() {
		return user;
	}

	public void loginForm() {

		Scanner scanner = new Scanner(System.in);
		while (attempts > 0) {

			new Exit().displayMessage(" Welcome to Login Page ");

			System.out.println("\n>>>> Enter your details >>>>\n");

			System.out.println("Enter Username : ");
			String username = scanner.nextLine();
			user = username;

			System.out.println("Enter Password : ");
			String password = scanner.nextLine();

			boolean isValid = false;
			try {
				isValid = validateUser(username, password);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (isValid) {
				System.out.println("Log in Successful...!!!");
				System.out.println("Do u want to perform actions on account credentials(Y/N) ");
				String answer = scanner.nextLine();
				if (answer.equalsIgnoreCase("Y"))
					new MyAccount().chooseActionsOnAccount(username);
				else
					new Exit().displayMessage("");
				return;

			} 
			else {
				if (attempts > 0) {
					attempts--;
					System.out.println("Please check username and password . \n Try Again");
				} 
				
			}
		}
		scanner.close();
		if(attempts == 0)
			new Exit().displayMessage(
					"\nYou've exceeded number of trials to login ...\n Please Come back again  ..!!");
		

	}

	private boolean validateUser(String username, String password) throws IOException {
		
		final String dburl = ConfigPropertyReader.getUrl(); 
		final String user = ConfigPropertyReader.getUsername();
		final String pswd = ConfigPropertyReader.getPassword();
		
		boolean returnValue = false;
		String query = "select * from accountstable where username ='" + username + "'  and password = '" + password
				+ "';";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(dburl, user, pswd);
			PreparedStatement stmt = con.prepareStatement(query);

			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			
			returnValue = rs.isLast();

			stmt.close();
			con.close();

		} catch (Exception e) {
			System.out.println("Oops something went wrong...!!");
			System.out.println("Please Try after some time..");
		}
		return returnValue;
	}
}
