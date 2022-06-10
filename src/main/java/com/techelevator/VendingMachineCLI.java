package com.techelevator;

import com.techelevator.view.Menu;
import com.techelevator.view.Purchasable;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT};

	private static final String FEED_MONEY = "Feed Money";
	private static final String SELECT_PRODUCT = "Select Product";
	private static final String FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { FEED_MONEY, SELECT_PRODUCT, FINISH_TRANSACTION};


	private Menu menu;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}
	Menu purchasable = new Purchasable(System.in,System.out);
	public void run() {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {

				File vendingMenu = new File("vendingmachine.csv");
				try(Scanner input = new Scanner(vendingMenu)){
					while(input.hasNextLine()){
						System.out.println(input.nextLine());
					}
				}catch(FileNotFoundException e){
					System.out.println("File Not Found.");
				}

			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				// do purchase
				int amountFed = 0;
				double amountOutput = 0;


				String purchaseMenuChoice = (String) purchasable.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
					if(purchaseMenuChoice.equals(FEED_MONEY)) {
						System.out.print("How much would you like to add?: ");
//						Scanner moneyFed = new Scanner(System.in);
						String money = purchasable.getIn().nextLine();
						if (purchasable.getIn().hasNextInt()) {
							amountFed += Integer.parseInt(money);
							amountOutput = amountFed;
							System.out.println("Current Money Provided: " + "$" + amountOutput);


						}else{
							System.out.println("Please enter a dollar amount.");

						}

					}
					purchasable.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);










			}else if(choice.equals(MAIN_MENU_OPTION_EXIT)){
				//exit

			}
		}
	}

	public static void main(String[] args) {

		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();

	}



}
