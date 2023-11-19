package com.anil;


import utility.ConfigPropertyReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

public class DeleteAccount {

	public void deleteMyAccount() throws IOException {
		Scanner scanner = new Scanner(System.in);

		new Exit().displayMessage("You must login to Delete your Account ");
		Login login = new Login();
		login.loginForm();

		String userName = login.getUserName();

		System.out.println("Please enter your security answer to delete : ");
		String secAns = scanner.nextLine();

//		url to your database 
//		example for local databse ==> dburl = "jdbc:mysql://localhost:portnumber/database name"
		
		final String dburl = ConfigPropertyReader.getUrl(); 
		final String user = ConfigPropertyReader.getUsername();
		final String pswd = ConfigPropertyReader.getPassword();
		
		String query = "";

		query = "select * from accountstable where username ='" + userName + "';";

		String secAnsInDB = "";

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(dburl, user, pswd);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			rs.next();
			secAnsInDB = rs.getString(5);

		} catch (Exception e) {
			e.printStackTrace();
		}

		scanner.close();

		boolean validAns = BCrypt.checkpw(secAns, secAnsInDB);

		if (validAns) {
			String query1 = "delete from accountstable where username = '" + userName + "';";
			String query2 = "drop table " + userName + ";";

			try {

				boolean q1Status = stmt.execute(query1);

				boolean q2Status = stmt.execute(query2);

				if (!q1Status && !q2Status) {
					System.out.println("\nAccount Deletion  Successful...!!!");
					new Exit().displayMessage("");
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {

			System.out.println("please provide your response with case sensitive as given before");
			new Exit().displayMessage("");
		}

	}

}
