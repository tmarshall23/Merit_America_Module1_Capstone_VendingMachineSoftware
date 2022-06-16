package com.techelevator;

import com.techelevator.view.Menu;
//import com.techelevator.view.Purchasable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT};

	private static final String FEED_MONEY = "Feed Money";
	private static final String SELECT_PRODUCT = "Select Product";
	private static final String FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { FEED_MONEY, SELECT_PRODUCT, FINISH_TRANSACTION};

	Map<String, Integer> amountAvailable = new HashMap<>();
	double money = 0;
	double amountFed = 0;

	private Menu menu;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			amountAvailable = newMap();

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				productInventory();

			}


			else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				// do purchase
				int selection = purchaseMenu();

				selection = purchaseMenu();

			}
		}
		}






	public static void main(String[] args) {

		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();

	}

	public void productInventory() {
		int empty = 0;
		File vendingMenu = new File("vendingmachine.csv");
		try (Scanner input = new Scanner(vendingMenu)) {
			while (input.hasNextLine()) {
				String[] inputLine = input.nextLine().split("\\|");
				if (amountAvailable.get(inputLine[0]) <= empty) {
					System.out.println( inputLine[0] + " | " + inputLine[1] + " | " + inputLine[2] + " | " + "Remaining: SOLD OUT");
				} else {
					System.out.println(inputLine[0] + " | " + inputLine[1] + " | " + inputLine[2] + " | " + "Remaining: " + amountAvailable.get(inputLine[0]));
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
		}
	}

	public int purchaseMenu(){
		setPurchaseMenuOptions();
		int option = menu.getIn().nextInt();
		if(option > 3){
			System.out.println("Please Select a valid option.\n");
			option = purchaseMenu();
		}
		while(option == 1){
			money = feedMoney(money);
			option = purchaseMenu();
		}
		while(option == 2){
			productInventory();
			System.out.println("Please select an item >>> ");
			String purchaseOption = menu.getIn().next().toUpperCase();
			System.out.println("How many would you like to purchase >>> ");
			int purchaseAmount = menu.getIn().nextInt();
			selectProduct(purchaseOption,purchaseAmount);
			option = purchaseMenu();
		}

		return option;
	}


	public double feedMoney(double insertedMoney){
		int amountFed = 0;
		double amountOutput = 0;
		try {
			System.out.print("How much would you like to add?: ");
			money = menu.getIn().nextInt();

			amountFed += money;
			amountOutput = amountFed + insertedMoney;
			System.out.println("Current Money Provided: " + "$" + amountOutput);



		} catch (InputMismatchException e) {
			System.out.println("Please enter a valid input");
		}
			return amountOutput;
	}

	public void selectProduct(String option, int amount){
		if (!amountAvailable.containsKey(option)) {
						System.out.println("Product does not exist.");
						purchaseMenu();
					} else {

						for (Map.Entry<String, Integer> newMap : amountAvailable.entrySet()) {

							if (option.equalsIgnoreCase(newMap.getKey())) {
								if (amount <= newMap().get(option)) {
									amountAvailable.replace(option, newMap().get(option) - amount);
									double price = itemPrice(option); //printf
									String type = itemOutput(option);
									String name = itemName(option);
									money -= price*amount;
									System.out.println(name + "|" + type + "|" + (money));
								} else {
									System.out.println("There are not that many available");

								}
							}
						}
					}

				}


















public void setPurchaseMenuOptions() {
	int i = 1;
	for (Object purchaseMenuOption : VendingMachineCLI.PURCHASE_MENU_OPTIONS) {
		System.out.println(i + ") " + purchaseMenuOption);
		i++;
	}
	System.out.print("\nPlease choose an option >>> ");
}


public Map<String, Integer> newMap(){
	int available = 5;
		File vendingMenu = new File("vendingmachine.csv");
		try (Scanner input = new Scanner(vendingMenu)) {
			while (input.hasNextLine()) {
				amountAvailable.put(input.nextLine().substring(0,2), available);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
		}
return amountAvailable;
}





public Double itemPrice(String index){
	File vendingMenu = new File("vendingmachine.csv");
	double price = 0.0;
	try (Scanner input = new Scanner(vendingMenu)) {
		while (input.hasNextLine()) {
			String thisInput = input.nextLine();
			if(thisInput.contains(index)) {
				String[] purchasedItem = thisInput.split("\\|");
				price = Double.parseDouble(purchasedItem[2]);
			}
		}
	} catch (FileNotFoundException e) {
		System.out.println("File Not Found.");
	}
		return price;
}





public String itemOutput(String index){
	File vendingMenu = new File("vendingmachine.csv");
	String output = "";
	try (Scanner input = new Scanner(vendingMenu)) {
		while (input.hasNextLine()) {
			String thisInput = input.nextLine();
			if(thisInput.contains(index)) {
				String[] purchasedItem = thisInput.split("\\|");
				String foodType = purchasedItem[3];
				if(foodType.equalsIgnoreCase("chip")){
					output = "Crunch Crunch Yum";
				}else if (foodType.equalsIgnoreCase("candy")){
					output = "Munch Munch Yum";
				}else if (foodType.equalsIgnoreCase("drink")){
					output = "Glug Glug Yum";
				}else if (foodType.equalsIgnoreCase("gum")){
					output = "Chew Chew Yum";
				}
			}
		}
	} catch (FileNotFoundException e) {
		System.out.println("File Not Found.");
	}
	return output;
}




public String itemName(String index){
	File vendingMenu = new File("vendingmachine.csv");
	String name = "";
	try (Scanner input = new Scanner(vendingMenu)) {
		while (input.hasNextLine()) {
			String thisInput = input.nextLine();
			if(thisInput.contains(index)) {
				String[] purchasedItem = thisInput.split("\\|");
				name = purchasedItem[1];
			}
		}
	} catch (FileNotFoundException e) {
		System.out.println("File Not Found.");
	}
	return name;
}

//	public String itemCost(String index){
//		File vendingMenu = new File("vendingmachine.csv");
//		String name = "";
//		try (Scanner input = new Scanner(vendingMenu)) {
//			while (input.hasNextLine()) {
//				String thisInput = input.nextLine();
//				if(thisInput.contains(index)) {
//					String[] purchasedItem = thisInput.split("\\|");
//					name = purchasedItem[1];
//				}
//			}
//		} catch (FileNotFoundException e) {
//			System.out.println("File Not Found.");
//		}
//		return name;
//	}








}
