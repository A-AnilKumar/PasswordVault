package com.anil;



import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

import utility.ConfigPropertyReader;

public class SignUp {

	ArrayList<String> securityQuestions = new ArrayList<String>();

	SignUp() {
		securityQuestions.add("Birth place ");
		securityQuestions.add("Secret code ");
		securityQuestions.add("Favourite food");
		securityQuestions.add("Favourite Sport ");
		securityQuestions.add("Other");
	}

	public void signUpForm() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nEnter your name : ");

		String username = scanner.nextLine();

		String password = "";
		while (password.length() < 8) {
			System.out.println("\nEnter your Password : ");
			System.out.println("Password should be of length 8 atleat");

			password = scanner.nextLine();

		}

		String mailId = "";
		while (!testUsingStrictRegex(mailId)) {
			System.out.println("\nEnter your mail id : ");
			mailId = scanner.nextLine();

		}

		new Exit().displayMessage("CHOOSE YOUR SECURITY QUESTION : ");

		int number = 1;
		for (String option : securityQuestions) {
			System.out.println(number + ". " + option);
			number++;
		}

		int securityOption = 0;
		while (securityOption > 5 || securityOption < 1) {
			try {
				System.out.println("Enter your choice between 1 to 5 : ");
				securityOption = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Input a valid number.");
			}
		}

		System.out.println("Enter your security answer for " + securityQuestions.get(securityOption - 1) + ": ");
		String securityAns = scanner.nextLine();

		boolean accCreation = false;
		try {
			accCreation = insertIntoAccountsDB(username, password, mailId, securityQuestions.get(securityOption - 1),
					securityAns);
		} catch (Exception e) {

			System.out.println("Please provide correct form of values ");
			signUpForm();

		}

		if (accCreation) {
			try {
				createTableForUser(username);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			System.out.println("You've an account with us ..!!");
			System.out.println("Let me help you to fetch Your Account (true / false) :");
			String help = scanner.next();
			if (help.equalsIgnoreCase("true")) {
				fetchMyAccount(username);
			} else {
				new Exit().askFeedback();
			}
			scanner.close();
		}

	}

	private void createTableForUser(String username) throws IOException {

		final String dburl = ConfigPropertyReader.getUrl(); 
		final String user = ConfigPropertyReader.getUsername();
		final String pswd = ConfigPropertyReader.getPassword();
		

		String createTableQuery = "create table " + username
				+ "(AppOrWebSiteName varchar(20), username varchar(20),Password varchar(60));";

		try {

//    		"com.mysql.cj.jdbc.Driver"
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(dburl, user, pswd);
			Statement stmt = con.createStatement();
			stmt.execute(createTableQuery);
			
//			msg to display for creation of account
			System.out.println("Account created successfully...!!");

			new MyAccount().chooseActionsOnAccount(username);
		} catch (Exception e) {
			System.out.println("Failed to create Account ...!!");
			e.printStackTrace();
		}
	}

	// @Test
	private boolean testUsingStrictRegex(String emailAddress) {
//	    example ==> emailAddress = "username@domain.com

		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

		// assertTrue(EmailValidation.patternMatches(emailAddress, regexPattern));

		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(emailAddress);

		return matcher.matches();

	}

	private boolean insertIntoAccountsDB(String username, String password, String mailId, String securityOption,
			String securityAns) throws SQLException, IOException {
		
		final String dburl = ConfigPropertyReader.getUrl(); 
		final String user = ConfigPropertyReader.getUsername();
		final String pswd = ConfigPropertyReader.getPassword();
		

		securityAns = BCrypt.hashpw(securityAns, BCrypt.gensalt(12));

		String query2 = "insert into accountstable values('" + username + "','" + password + "','" + mailId + "','"
				+ securityOption + "','" + securityAns + "');";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(dburl, user, pswd);
			PreparedStatement ps = con.prepareStatement(query2);
			ps.executeUpdate(query2);
			con.createStatement();

			con.close();
		} catch (Exception e) {
			for (int i = 0; i < 80; i++)
				System.out.print("#");
			System.out.println("  \n OUR SYSTEM DETECTED   ");
			return false;
		}
		return true;
	}

	private void fetchMyAccount(String userName) {
		final String dburl = "jdbc:mysql://localhost:3306/passwordvault";
		final String user = "root";
		final String pswd = "123456";

		String username = "";
		String password = "";
		String secQn = "";
		String secAnsInDB = "";
		String mailIdInDB = "";
		String mailId = "";

		String query = "select * from accountstable where username = '" + userName + "';";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(dburl, user, pswd);
			PreparedStatement stmt = con.prepareStatement(query);

			ResultSet rs = stmt.executeQuery();
			rs.next();

			username = rs.getString(1);
			password = rs.getString(2);
			mailIdInDB = rs.getString(3);
			secQn = rs.getString(4);
			secAnsInDB = rs.getString(5);

			stmt.close();
			con.close();

		} catch (Exception e) {
			System.out.println("\nOops something went wrong on our side ");
			System.out.println("Please Try after some time..\n");
			new Exit().displayMessage("Thank you for using Password Vault..!!");
		}

		Scanner scanner = new Scanner(System.in);

		new Exit().displayMessage(" FETCHING YOUR ACCOUNT DETAILS ...!!");

		System.out.println("\nPlease give details that you remember");
		System.out.println("1.Username ");
		System.out.println("2.MailId \n");

		int choice = 0;
		while (choice < 1 || choice > 5) {
			try {
				System.out.println("Action : ");
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Input a valid value");
			}
		}

		boolean inputGiven = false;

		if (choice == 1) {

			System.out.println("\nPlease enter your username : ");
			username = scanner.nextLine();

			inputGiven = true;
		} else if (choice == 2) {

			System.out.println("\n Please enter your mailid : ");
			mailId = scanner.nextLine();

			inputGiven = true;
		} else if (choice > 2 || choice < 1) {
			System.out.println("\nPlease choose a valid option");
			System.out.println("Come again..!!\n");

		}

		if (inputGiven) {

			System.out.println("Please provide answer to your security question : \n" + secQn + " : ");
			String userGivenSecAns = scanner.nextLine();

			if (BCrypt.checkpw(userGivenSecAns, secAnsInDB)
					&& (userName.equals(username) || mailIdInDB.equalsIgnoreCase(mailId))) {
				System.out.println("These are your Account details");
				System.out.println("USERNAME : " + username);
				System.out.println("PASSWORD : " + password);

				Homepage.main(null);

			}

			else {
				System.out.println("Sorry we dont find you in our Database..!!");
				System.out.println("Try Again..!!");
				new Exit().askFeedback();

			}
		}

		scanner.close();
	}
}
