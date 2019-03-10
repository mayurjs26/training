package com.capgemini.citi.ui;

import java.sql.Connection;
import java.util.Scanner;

import com.capgemini.citi.bean.Customer;
import com.capgemini.citi.exception.InsuffiecientBalanceException;
import com.capgemini.citi.exception.SenderReceiverSameException;
import com.capgemini.citi.service.ServiceClass;

public class MainUI {

	public static void main(String[] args) {
		//DaoClass dao = new DaoClass();
		Scanner scan = new Scanner(System.in);
		int choice = 0;
		Customer validate;
		boolean logOut = false;
		String name;
		String mobile;
		String email;
		String username;
		String password;
		ServiceClass service = new ServiceClass();
		Customer customer;
		Connection connection = null;
		System.out
				.println("*******************Welcome To CiTi Bank Wallet********************");
		while (true) {
			System.out.println("Choose suitable option:");
			System.out.print("1.Register\n2.Login\n3.Exit\n");
			choice = scan.nextInt();
			if (choice == 1) {

				customer = new Customer();
				System.out
						.println("Please Enter The Following Details to Register in the Wallet");

				while (true) {
					System.out.println("Enter your Name");
					name = scan.next();
					boolean isValid = service.validateName(name);
					if (isValid)
						customer.setName(name);
					break;


				}

				while (true) {
					System.out.println("Enter Mobile:");
					mobile = scan.next();
					boolean isValid = service.validateMobile(mobile);
					if (isValid) {
						customer.setMobileNo(Long.parseLong(mobile));
						break;
					} else
						System.out.println("enter correct mobile no");

				}
				while (true) {
					System.out.println("Enter Email:");
					email = scan.next();
					boolean isValid = service.validateEmail(email);
					if (isValid) {
						customer.setEmail(email);
						break;
					}
				}

				while (true) {
					System.out.println("Enter Password");
					password = scan.next();
					boolean isValid = service.validatePassword(password);
					if (isValid) {
						customer.setPassword(password);
						break;
					}
				}

				service.insertCustomer(customer);

				System.out
						.println("Customer Registered Successfully\n Customer ID "
								+ customer.getCustId());
				System.out.println(customer.toString());
			}

			else {

				System.out.println("Enter MobileNo And Password:");
				while (true) {
					System.out.println("Enter mob no");
					mobile = scan.next();
					boolean isValid = service.validateMobile(mobile);
					if (isValid)
						break;
				}

				while (true) {
					System.out.println("Enter Password");
					password = scan.next();
					boolean isValid = service.validatePassword(password);
					if (true)
						break;
				}

				validate = service.login(Long.parseLong(mobile), password);

				if (validate != null) {
					System.out.println("login successful");
					while (true) {

						System.out
								.println("***************************Menu for User to use Banking Facilities***********************");
						System.out
								.println("1.Show Balance\n 2.Deposit\n 3.Withdraw\n 4.Fund Transfer\n 5.Print Transaction\n 6.log out");
						choice = scan.nextInt();
						switch (choice) {
						case 1: {
							System.out.println("Account Balance is:"
									+ service.showBalance(Long
											.parseLong(mobile)));
							break;
						}
						case 2: {
							System.out.println("amount to deposit:");
							double amount = scan.nextDouble();
							service.deposit(amount, customer);
							System.out.println("Current Balance"
									+ service.showBalance(Long
											.parseLong(mobile)));
							break;
						}
						case 3: {
							System.out
									.println("Enter the amount to be withdrawn");
							double amount = scan.nextDouble();
							
							try {
								service.withdraw(amount, customer);
							} catch (NumberFormatException
									| InsuffiecientBalanceException e) {
								e.printStackTrace();
							}
							System.out.println("Current Balance"
									+ service.showBalance(Long
											.parseLong(mobile)));
							
							break;
						}
						case 4: {
							System.out
									.println("Enter the Mobile no of the receiver:");
							long receiverMobileNo = scan.nextLong();
							Customer validateReceiver = service
									.checkCredentials(receiverMobileNo);
							if (validateReceiver!=null) {
								System.out
										.println("Enter the Amount to transfer");
								double amount = scan.nextDouble();
								try {
									service.fundTransfer(
											Long.parseLong(mobile),
											receiverMobileNo, amount);
								} catch (NumberFormatException
										| InsuffiecientBalanceException e) {
									e.printStackTrace();
								} catch (SenderReceiverSameException e) {
									e.printStackTrace();
								}
								System.out
										.println("Amount Transferred Successfull");
							}

							break;
						}
						case 5: {
							service.printTransaction(Long.parseLong(mobile));
							break;
						}
						case 6:
							logOut = true;
							break;

						default:
							break;
						}
						if (logOut == true) {
							logOut = false;
							break;
						} else
							continue;

					}

				}

			}

		}

	}

}
