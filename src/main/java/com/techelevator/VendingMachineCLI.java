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
	int money = 0;

	private Menu menu;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			amountAvailable = newMap();

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {

				File vendingMenu = new File("vendingmachine.csv");
				try (Scanner input = new Scanner(vendingMenu)) {
					while (input.hasNextLine()) {

//							if ava = 0 then so out of stock
						System.out.println(input.nextLine());
					}
				} catch (FileNotFoundException e) {
					System.out.println("File Not Found.");
				}

			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				// do purchase
				int amountFed = 0;
				double amountOutput = 0;
				setPurchaseMenuOptions();
				int option = menu.getIn().nextInt();


				while (option == 1) {
					try {
						System.out.print("How much would you like to add?: ");
						 money = menu.getIn().nextInt();

						amountFed += money;
						amountOutput = amountFed;
						System.out.println("Current Money Provided: " + "$" + amountOutput);

						setPurchaseMenuOptions();
						option = menu.getIn().nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Please enter a valid input");
						option = 0;

					}

				}
				if (option == 2) {
					File vendingMenu = new File("vendingmachine.csv");
					try (Scanner input = new Scanner(vendingMenu)) {
						while (input.hasNextLine()) {
							System.out.println(input.nextLine());
						}
					} catch (FileNotFoundException e) {
						System.out.println("File Not Found.");
					}


					int totalAvailable = 5;
					System.out.println("Please select an item >>> ");
					String purchaseOption = menu.getIn().next().toUpperCase();

					System.out.println("How many would you like to purchase >>> ");
					int purchaseAmount = menu.getIn().nextInt();

					if (!amountAvailable.containsKey(purchaseOption)) {
						System.out.println("Product does not exist.");
						setPurchaseMenuOptions();
						option = menu.getIn().nextInt();
					} else {

						for (Map.Entry<String, Integer> newMap : amountAvailable.entrySet()) {

							if (purchaseOption.equalsIgnoreCase(newMap.getKey())) {
								if (purchaseAmount <= totalAvailable) {
									amountAvailable.replace(purchaseOption, totalAvailable - purchaseAmount);
									double price = itemPrice(purchaseOption); //printf
									String type = itemOutput(purchaseOption);
									String name = itemName(purchaseOption);
									System.out.println(name + "|" + type + "|" + (money - (price * purchaseAmount)));
								} else {
									System.out.println("There are not that many available");

								}
								setPurchaseMenuOptions();
								option = menu.getIn().nextInt();
							}
						}
					}

				}
			}
		}
		}






	public static void main(String[] args) {

		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();

	}

private void setPurchaseMenuOptions() {
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
}
