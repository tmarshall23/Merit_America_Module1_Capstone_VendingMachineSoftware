package com.techelevator;

import com.techelevator.view.Menu;
import org.w3c.dom.ls.LSOutput;
//import com.techelevator.view.Purchasable;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
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

	private static final double QUARTERS_VALUE = 0.25;
	private static final double DIMES_VALUE = 0.10;
	private static final double NICKELS_VALUE = 0.05;

	Map<String, Integer> amountAvailable = new HashMap<>();
	double money = 0;
	double amountFed = 0;
	String choice = "";
	private Menu menu;





	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		while (true) {
			choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			newMap();



			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				productInventory();
			}


			else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				// do purchase
				purchaseMenu();


			}
			else if (choice.equals(MAIN_MENU_OPTION_EXIT)){
				System.out.println("Goodbye");
				System.exit(0);
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
			System.out.printf("$" + "%.2f",money );
			System.out.print(" available\n");
			System.out.println("Please select an item >>> ");
			String purchaseOption = menu.getIn().next().toUpperCase();
			System.out.println("How many would you like to purchase >>> ");
			int purchaseAmount = menu.getIn().nextInt();
			selectProduct(purchaseOption,purchaseAmount);
			option = purchaseMenu();
		}
		if(option == 3) {
			System.out.println("Dispensing change ");
			getChange(money);
			Menu menu = new Menu(System.in, System.out);
			VendingMachineCLI cli = new VendingMachineCLI(menu);
			cli.run();
		}
		return 0;
	}


	public double feedMoney(double insertedMoney){
		int amountFed = 0;
		double amountOutput = 0;
		try {
			System.out.print("How much would you like to add?: ");
			money = menu.getIn().nextInt();

			amountFed += money;
			amountOutput = amountFed + insertedMoney;
			System.out.println("Current Money Provided: " + "$" + amountOutput + "\n");

		} catch (InputMismatchException e) {
			System.out.println("Please enter a valid input");
		}

			InventoryLog.log(getCurrentDate() + " FEED MONEY: $" + String.format("%.2f",(double)amountFed) + " $" + String.format("%.2f",amountOutput));
			return amountOutput;
	}

	public void selectProduct(String option, int amountPurchased){
		if (!amountAvailable.containsKey(option)) {
						System.out.println("Product does not exist.");
						purchaseMenu();
					} else {

						for (Map.Entry<String, Integer> newMap : amountAvailable.entrySet()) {

							if (option.equalsIgnoreCase(newMap.getKey())) {
								if (amountPurchased <= amountAvailable.get(option)) {


									double price = itemPrice(option);
									String type = itemOutput(option);
									String name = itemName(option);


									if(money < price){
										System.out.println("Please insert more money to make that purchase.\n");
									}else {
										amountAvailable.replace(option, amountAvailable.get(option) - amountPurchased);
										money -= price*amountPurchased;
//									System.out.println(name + "|" + type + "|" + " Remaining balance: " +(money)); //change code to account for change.
										System.out.printf("Dispensing: " + name + " | " + type + " | " + "Purchase amount: " + "%.2f",price*amountPurchased);
										System.out.printf(" | " + " Remaining balance: " + "%.2f", money);
										System.out.println("\n");
										InventoryLog.log(getCurrentDate() + " " + name + " " + option + " $" + String.format("%.2f",(money += price*amountPurchased )) + " $" + String.format("%.2f",money -= price*amountPurchased));

									}
								} else {
									System.out.println("There are not that many available\n");

								}
							}
						}
					}

				}


public void getChange(double currentMoney){
int numberOfCoins = 0;
double initialMoney = currentMoney;
        while (currentMoney >= QUARTERS_VALUE) {
			currentMoney -= QUARTERS_VALUE;
			numberOfCoins++;
			if (currentMoney < QUARTERS_VALUE) {
				money = currentMoney;
				double totalQuarterAmount = numberOfCoins * QUARTERS_VALUE;
				System.out.println("Quarters: " + numberOfCoins + " | $" + totalQuarterAmount);
				numberOfCoins = 0;

				while (currentMoney >= DIMES_VALUE) {
					currentMoney -= DIMES_VALUE;
					numberOfCoins++;
					if (currentMoney < DIMES_VALUE) {
						money = currentMoney;
						double totalDimeAmount = numberOfCoins * DIMES_VALUE;
						System.out.printf("Dimes: " + numberOfCoins + " | $" + "%.2f",totalDimeAmount);
						System.out.println("");
						numberOfCoins = 0;
						while (currentMoney >= NICKELS_VALUE) {
							currentMoney -= NICKELS_VALUE;
							numberOfCoins++;
							if (currentMoney < NICKELS_VALUE) {
								double totalNickelAmount = numberOfCoins * NICKELS_VALUE;
								money = currentMoney;
								System.out.println("Nickels: " + numberOfCoins + " | $"  + totalNickelAmount);

							}
						}
					}

				}
			}

		}
	InventoryLog.log(getCurrentDate() + " " + "GIVE CHANGE" + ": " + " $" + String.format("%.2f",(initialMoney)) + " $" + String.format("%.2f",money));
		System.out.printf("Current balance: $" + "%.2f",money);
		System.out.println("");

		}

public String getCurrentDate(){
	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US);
	String dateString = format.format(new Date());
	return dateString;
}












public void setPurchaseMenuOptions() {
	int i = 1;
	for (Object purchaseMenuOption : VendingMachineCLI.PURCHASE_MENU_OPTIONS) {
		System.out.println(i + ") " + purchaseMenuOption);
		i++;
	}
	System.out.print("\nPlease choose an option >>> ");
}


public void newMap(){
	int available = 5;
		File vendingMenu = new File("vendingmachine.csv");
		try (Scanner input = new Scanner(vendingMenu)) {
			while (input.hasNextLine()) {
				amountAvailable.put(input.nextLine().substring(0,2), available);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
		}

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
