package com.anil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import utility.ConfigPropertyReader;

public class MyAccount {

	String username = "";
	String password = "";

	public void myAccount() {

		new Exit().displayMessage("You must login to view your Account ");
		Login login = new Login();
		login.loginForm();

	}

	public void chooseActionsOnAccount(String username) {

		Scanner scanner = new Scanner(System.in);

		new Exit().displayMessage("Pick an action to perform : ");
		System.out.println("1. View my credentials");
		System.out.println("2. Add my credential ");
		System.out.println("3. Edit / Update my details");
		System.out.println("4. Delete a credential");
		System.out.println("5. Exit");

		int actionOnAccount = 0;
		while (actionOnAccount < 1 || actionOnAccount > 5) {
			try {
				System.out.println("Action (1 to 5)>> ");
				actionOnAccount = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("\nInput a valid action value");
			}
		}

		if (actionOnAccount >= 1 && actionOnAccount <= 4)
			try {
				actionOnAccount(actionOnAccount, username);
			} catch (Exception e) {
				e.printStackTrace();
			}
		else if (actionOnAccount == 5)
			new Exit().askFeedback();
		else {
			System.out.println("Please pick an action value from  1 to 5 ");
			chooseActionsOnAccount(username);
		}
		scanner.close();
	}

	private void actionOnAccount(int actionOnAcc, String username) throws SQLException, IOException {
		
		final String dburl = ConfigPropertyReader.getUrl(); 
		final String user = ConfigPropertyReader.getUsername();
		final String pswd = ConfigPropertyReader.getPassword();
		
		String query = "";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Connection con = DriverManager.getConnection(dburl, user, pswd);
		Statement stmt = con.createStatement();

		if (actionOnAcc == 1) {
			new Exit().displayMessage("ALL YOUR CREDENTIALS");

			query = "select * from " + username + ";";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				System.out.println("\n>> App or Website name :>> " + rs.getString(1) + "\n>> username :>> "
						+ rs.getString(2) + "\n>> Password :>> " + rs.getString(3));
				System.out.println();
			}
			chooseActionsOnAccount(username);
		}

		Scanner scanner = new Scanner(System.in);

		if (actionOnAcc == 2) {
			System.out.println("Enter name of App / Website name : ");
			String appOrWeb = scanner.nextLine();

			System.out.println("Enter username : ");
			String userName = scanner.nextLine();

			System.out.println("Enter password of account : ");
			String pass = scanner.nextLine();

			String queryToAdd = "insert into " + username + " values('" + appOrWeb + "','" + userName + "','" + pass
					+ "');";

			stmt.executeUpdate(queryToAdd);
			System.out.println("Credential added successfully..!!");
			chooseActionsOnAccount(username);

		}

		if (actionOnAcc == 3) {

			System.out.println("Enter the App/website name that uh wanna Edit / Update");
			String appOrWebName = scanner.nextLine();
			
//			System.out.println("username > "+username);
			String queryToSearch = "select * from " + username + " where AppOrWebSiteName = '" + appOrWebName + "';";
		
			if (!stmt.execute(queryToSearch)) {
				System.out.println("No such app Or Website exits in database");
			}
			else {
				System.out.println("\n\nPick an option to Edit or Update\n");
				System.out.println("1. Username \n2. Password ");

				String queryToEdit = "";

				int choice = 0;
				while (choice < 1 || choice > 2) {
					try {
						System.out.println("Action (1 to 5)>> ");
						choice = Integer.parseInt(scanner.nextLine());
					} catch (NumberFormatException nfe) {
						System.out.println("Input either 1 or 2");
					}
				}


				if (choice == 1) {
					System.out.println("Enter the username of "+appOrWebName +" to modify : ");
					String existingUserName = scanner.nextLine();

					System.out.println("Enter new username for your website  "+appOrWebName);
					String updateUserName = scanner.nextLine();

					queryToEdit = "update " + username + " set username = '" + updateUserName + ""
							+ "' where username ='" + existingUserName + "';";

					int rows = stmt.executeUpdate(queryToEdit);

					if (rows >= 1)
						System.out.println("credential updation successful..!!");
					else if (rows == 0)
						System.out.println("No Match found,please re-check and delete");

				}

				if (choice == 2) {
					String newPassword = "";
					while (newPassword.length() < 8) {
						System.out.println("Enter the new password of " + appOrWebName + " of length 8 atleast");
						newPassword = scanner.nextLine();
					}

					queryToEdit = "update " + username + " set password = '" + newPassword + ""
							+ "' where AppOrWebSiteName ='" + appOrWebName + "';";

					int rows = stmt.executeUpdate(queryToEdit);

					if (rows >= 1)
						System.out.println("credential updation successful..!!");
					else if (rows == 0)
						System.out.println("No Match found,please re-check and update");

				}
			}
			chooseActionsOnAccount(username);
		}

		if (actionOnAcc == 4) {

			System.out.println("Enter the App/website name that uh wanna delete");
			String appOrWebName = scanner.nextLine();

			String queryToSearch = "select * from " + username + " where AppOrWebSiteName = '" + appOrWebName + "';";
//			int rows = 
			if (!stmt.execute(queryToSearch)) {
				System.out.println("No such app Or Website exits in database");
			} else {

				String queryToDelete = "delete from " + username + " where  AppOrWebSiteName = '" + appOrWebName + "';";

				int rows = stmt.executeUpdate(queryToDelete);
				if (rows >= 1)
					System.out.println("credential deletion successful..!!");
				else if (rows == 0)
					System.out.println("No Match found,please re-check and delete");

			}
			chooseActionsOnAccount(username);
		}

		scanner.close();
	}
}
