package com.anil;

import java.util.Scanner;

public class Exit {

	public void displayMessage(String msg) {
		for (int i = 0; i < 80; i++)
			System.out.print("=");
		if (!msg.isEmpty())
			System.out.println("\n" + msg + "\n");
		else
			System.out.println("\nThank you for using Password Locker ..!!");
		for (int i = 0; i < 80; i++)
			System.out.print("=");
		System.out.println();

	}

	public void askFeedback() {

		System.out.println("Would you like to provide us feedback ( Y / N ) ");

		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();

		if (input.equalsIgnoreCase("Y")) {
			System.out.println("Provide your feedback : ");
			String feedback = scanner.nextLine();
			System.out.println("thank you ..!!  For your valuable feedback..!!\n");
		}
		scanner.close();
		displayMessage("\nThank you for using Password Vault..!!");

	}
}
