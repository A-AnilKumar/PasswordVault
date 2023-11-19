package com.anil;

import java.io.IOException;
import java.util.Scanner;

public class Menu {

	Menu() {
		menu();
	}

	private static void menu() {

		int userChoice = 0;
		Scanner scan = new Scanner(System.in);

		menuOptions();
		while (userChoice > 5 || userChoice < 1) {
			try {
				System.out.println("Enter your choice between 1 to 5 : ");
				userChoice = Integer.parseInt(scan.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("\nInput a valid number.");
			}
		}

		selectedOption(userChoice);
		scan.close();

	}

	private static void menuOptions() {
		for (int counter = 0; counter < 80; counter++)
			System.out.print("#");

		System.out.println();
		System.out.println("\n\tWELCOME TO PASSWORD VAULT ");
		System.out.println();

		for (int counter = 0; counter < 80; counter++)
			System.out.print("#");

		System.out.println();
		System.out.println("\n OPT A CHOICE FROM BELOW LIST : ");

		for (int counter = 0; counter < 80; counter++)
			System.out.print("*");

		System.out.println("\n1. CREATE ACCOUNT ");
		System.out.println("2. LOGIN ");
		System.out.println("3. VIEW MY ACCOUNT");
		System.out.println("4. DELETE ACCOUNT ");
		System.out.println("5. EXIT \n");

		for (int counter = 0; counter < 80; counter++)
			System.out.print("*");

		System.out.println("\n");
	}

	private static void selectedOption(int userChoice) {
		switch (userChoice) {
		case 1:
			new SignUp().signUpForm();
			break;
		case 2:
			new Login().loginForm();
			break;
		case 3:
			new MyAccount().myAccount();
			break;
		case 4:
			try {
				new DeleteAccount().deleteMyAccount();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 5:
			new Exit().askFeedback();
			break;
		}
	}

}
