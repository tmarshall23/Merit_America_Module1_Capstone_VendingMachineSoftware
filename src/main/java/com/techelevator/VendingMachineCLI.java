package com.techelevator;

import com.techelevator.view.Menu;
import org.w3c.dom.ls.LSOutput;
//import com.techelevator.view.Purchasable;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.util.*;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT};

	private static final String FEED_MONEY = "Feed Money";
	private static final String SELECT_PRODUCT = "Select Product";
	private static final String FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = {FEED_MONEY, SELECT_PRODUCT, FINISH_TRANSACTION};

	private static final double QUARTERS_VALUE = 0.25;
	private static final double DIMES_VALUE = 0.10;
	private static final double NICKELS_VALUE = 0.05;

	private final File vendingFile = new File("vendingmachine.csv");
	private static Map<String, Integer> amountAvailable = new HashMap<>();
	private static double money = 0;
	private static String choice = "";
	private final Menu menu;


	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public File getVendingFile() {
		return vendingFile;
	}

	public void run() {
		while (true) {
			choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				productInventory(vendingFile);
				//runs product inventory from file location and displays in console

			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				purchaseMenu();
				// runs through purchase menu options

			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.out.println("Goodbye");
				System.exit(0);
				//exits program

			}
		}
	}

	public static void main(String[] args) {

		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.newMap(cli.getVendingFile());
		//creates new map of the index of the item and the amount available
		cli.run();

	}

	public void productInventory(File file) {
		int empty = 0;
		//opens file
		try (Scanner input = new Scanner(file)) {
			//reads file
			while (input.hasNextLine()) {

				String[] inputLine = input.nextLine().split("\\|");
				// while file has lines it splits each line into an array
				if (amountAvailable.get(inputLine[0]) <= empty) {
					System.out.println(inputLine[0] + " | " + inputLine[1] + " | " + inputLine[2] + " | " + "Remaining: SOLD OUT");
					// if item is sold out it displays item detail showing it is sold out
				} else {
					System.out.println(inputLine[0] + " | " + inputLine[1] + " | " + inputLine[2] + " | " + "Remaining: " + amountAvailable.get(inputLine[0]));
					// if item has stock, it shows how many are available with full selection detail.
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
			//catches if file doesnt exist
		}
	}

	public int purchaseMenu() {
		setPurchaseMenuOptions();
		//displays the purchase menu options
		int option = 0;
		boolean isNumber;

		if (menu.getIn().hasNextInt()) {
			option = menu.getIn().nextInt();
			isNumber = true;
			//makes sure user input is an Integer
			if (isNumber) {
				//if user input is a number it runs through options in menu
				if (option > 3) {
					System.out.println("Please Select a valid option.\n");
					option = purchaseMenu();
					//makes sure the selection is in bounds of the options.
				}
				while (option == 1) {

					money = feedMoney(money);
					//feeds the money provided into the machine via feedMoney method
					option = purchaseMenu();
					//returns to menu
				}
				while (option == 2) {
					productInventory(vendingFile);
					//displays product inventory for purchase
					selectProductMenu();
					// takes in the product selection as well as the quantity of purchase
					option = purchaseMenu();

				}
				if (option == 3) {
					System.out.println("Dispensing change ");
					money = Double.parseDouble(String.format("%.2f", money));

					getChange(money);

					//returns proper change to the user in Quarters Dimes and Nickels
					//sets balance to 0
					displayMenu();
					//returns to main menu
				}
			}
		}else{
			System.out.println("Please enter a valid selection.");
			displayMenu();
			purchaseMenu();
		}
		return 0;

	}

	public double feedMoney(double insertedMoney) {
		int amountFed = 0;
		double amountOutput = 0;
		try {
			System.out.print("How much would you like to add?: ");
			money = menu.getIn().nextInt();
			//money equals user input
			amountFed += money;
			//variable to hold money amount for processing
		} catch (InputMismatchException e) {
			System.out.println("Please enter a valid input");
			displayMenu();
			//Makes sure user inputs an Integer
		} finally {

			amountOutput = amountFed + insertedMoney;
			//adds the amount of money provided to the Current money inside machine
			System.out.println("Current Money Provided: " + "$" + amountOutput + "\n");

		}

		InventoryLog.log(getCurrentDate() + " FEED MONEY: $" + String.format("%.2f", (double) amountFed) + " $" + String.format("%.2f", amountOutput));
		return amountOutput;
		//logs transaction
	}

	public double testFeedMoney(double insertedMoney) {
		int amountFed = 0;
		double amountOutput = 0;
		try {
			amountFed += money;
			amountOutput = amountFed + insertedMoney;

		} catch (CustInputMismatchException e) {
			e.getMessage("Please enter a valid input");
			displayMenu();
		}
		return amountOutput;
	}


	public void selectProduct(String option, int amountPurchased) {
		if (!amountAvailable.containsKey(option)) {
			System.out.println("Product does not exist.");
			purchaseMenu();
			//returns user to purchase menu if they enter a non-valid product key
		} else {

			for (Map.Entry<String, Integer> newMap : amountAvailable.entrySet()) {
				// iterates through map to pull individual items
				if (option.equalsIgnoreCase(newMap.getKey())) {
					if (amountPurchased <= amountAvailable.get(option)) {
						//makes sure the item has enough items available

						double price = itemPrice(option, vendingFile);
						String type = itemOutput(option, vendingFile);
						String name = itemName(option, vendingFile);
						//sets a variable for each value of the item

						if (money < price) {
							System.out.println("Please insert more money to make that purchase.\n");
							//makes sure the customer has enough money for the item
						} else {
							amountAvailable.replace(option, amountAvailable.get(option) - amountPurchased);
							//changes the amount available inside the map for the option
							money -= price * amountPurchased;
							//subtracts the cost of item from total money
							System.out.printf("Dispensing: " + name + " | " + type + " | " + "Purchase amount: " + "%.2f", price * amountPurchased);
							System.out.printf(" | " + " Remaining balance: " + "%.2f", money);
							System.out.println("\n");
							InventoryLog.log(getCurrentDate() + " " + name + " " + option + " $" + String.format("%.2f", (money += price * amountPurchased)) + " $" + String.format("%.2f", money -= price * amountPurchased));
							//displays and logs the item, price, and remaining balance.
						}
					} else {
						System.out.println("There are not that many available\n");
						//tells them there are not enough

					}
				}
			}
		}

	}

	public String getChange(double currentMoney) {
		int numberOfQuarters = 0;
		int numberOfDimes = 0;
		int numberOfNickels = 0;
		double totalQuarterAmount = 0;
		double totalDimeAmount = 0;
		double totalNickelAmount = 0;
		double initialMoney = currentMoney;
		while (currentMoney >= QUARTERS_VALUE) {
			currentMoney -= QUARTERS_VALUE;
			currentMoney = Double.parseDouble(String.format("%.2f", currentMoney));
			numberOfQuarters++;
			//while the amount of money is more than a quarter, subtracts a quarter amount and adds one quarter to how many quarters
			if (currentMoney < QUARTERS_VALUE) {
				money = currentMoney;
				//adjusts money in machine
				totalQuarterAmount = numberOfQuarters * QUARTERS_VALUE;
				//amount of quarters dispensed in change amount
				System.out.println("Quarters: " + numberOfQuarters + " | $" + totalQuarterAmount);
				//returns how many and the amount of quarters
			}
		}
		while (currentMoney >= DIMES_VALUE) {
			currentMoney -= DIMES_VALUE;
			currentMoney = Double.parseDouble(String.format("%.2f", currentMoney));
			numberOfDimes++;
			//while the amount of money is more than a dime, subtracts a dime amount and adds one dime to how many dimes
			if (currentMoney < DIMES_VALUE) {
				money = currentMoney;
				//adjusts money in machine
				totalDimeAmount = numberOfDimes * DIMES_VALUE;
				//amount of dimes dispensed in change amount
				System.out.printf("Dimes: " + numberOfDimes + " | $" + "%.2f", totalDimeAmount);
				System.out.println("");
				//returns how many and the amount of dimes in change amount with a new line
			}
		}
		while (currentMoney >= NICKELS_VALUE) {
			currentMoney -= NICKELS_VALUE;
			currentMoney = Double.parseDouble(String.format("%.2f", currentMoney));
			numberOfNickels++;
			//while the amount of money is more than a nickel, subtracts a nickel amount and adds one nickel to how many nickel
			if (currentMoney >= 0.000 && currentMoney < NICKELS_VALUE) {
				totalNickelAmount = numberOfNickels * NICKELS_VALUE;
				//amount of nickels dispensed in change amount
				money = currentMoney;
				//adjusts money in machine
				System.out.println("Nickels: " + numberOfNickels + " | $" + totalNickelAmount);
				//returns how many and the amount of nickels in change amount
			}
		}
		InventoryLog.log(getCurrentDate() + " " + "GIVE CHANGE" + ": " + " $" + String.format("%.2f", (initialMoney)) + " $" + String.format("%.2f", money));
		System.out.printf("Current balance: $" + "%.2f", money);
		System.out.println("");
		//prints current balance after dispensing, which should be zero
		//logs the transaction with initial money and money left over
		return numberOfQuarters + " " + String.format("%.2f", totalQuarterAmount) + " " + numberOfDimes + " " + String.format("%.2f", totalDimeAmount) + " " + numberOfNickels + " " + String.format("%.2f", totalNickelAmount) + " " + String.format("%.2f", currentMoney);
	}

	public String getCurrentDate() {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US);
		String dateString = format.format(new Date());
		return dateString;
		//returns current date in string format
	}

	public void displayMenu() {
		//method used to call back to the original display menu with same functionality
		choice = menu.getIn().nextLine();
		if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
			productInventory(vendingFile);

		} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
			purchaseMenu();

		} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
			System.out.println("Goodbye");
			System.exit(0);
		}
	}

	public void setPurchaseMenuOptions() {
		int i = 1;
		for (Object purchaseMenuOption : VendingMachineCLI.PURCHASE_MENU_OPTIONS) {
			System.out.println(i + ") " + purchaseMenuOption);
			i++;
			//iterates through an array of purchase options and displays them in number sequence
		}
		System.out.print("\nPlease choose an option >>> ");
	}


	public Map<String, Integer> newMap(File file) {
		//reads the file and creates map for index and gives original amount available
		int available = 5;

		try (Scanner input = new Scanner(file)) {
			while (input.hasNextLine()) {
				//creates a substring of the first two characters in the file line to create a proper index for the machine
				amountAvailable.put(input.nextLine().substring(0, 2), available);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
		}
		return amountAvailable;
	}

	public Double itemPrice(String index, File file) {
		//reads file and splits lines into and array a the "|"regex,
		double price = 0.0;

		try (Scanner input = new Scanner(file)) {

			while (input.hasNextLine()) {
				String thisInput = input.nextLine();

				if (thisInput.contains(index)) {
					String[] purchasedItem = thisInput.split("\\|");
					price = Double.parseDouble(purchasedItem[2]);
					//grabs the price from the location in the created array
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
		}
		return price;
		//returns the grabbed price
	}

	public String itemOutput(String index, File file) {
		//reads file and splits lines into and array a the "|"regex,
		String output = "";

		try (Scanner input = new Scanner(file)) {

			while (input.hasNextLine()) {
				String thisInput = input.nextLine();

				if (thisInput.contains(index)) {
					String[] purchasedItem = thisInput.split("\\|");
					String foodType = purchasedItem[3];
					if (foodType.equalsIgnoreCase("chip")) {
						output = "Crunch Crunch Yum";
					} else if (foodType.equalsIgnoreCase("candy")) {
						output = "Munch Munch Yum";
					} else if (foodType.equalsIgnoreCase("drink")) {
						output = "Glug Glug Yum";
					} else if (foodType.equalsIgnoreCase("gum")) {
						output = "Chew Chew Yum";
					}
					//grabs the proper line for the type of food from the location in the created array
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
		}
		return output;
	}

	public String itemName(String index, File file) {
		//reads file and splits lines into and array a the "|"regex,
		String name = "";
		try (Scanner input = new Scanner(file)) {
			while (input.hasNextLine()) {
				String thisInput = input.nextLine();
				if (thisInput.contains(index)) {
					String[] purchasedItem = thisInput.split("\\|");
					name = purchasedItem[1];
					//grabs the name of the items from the proper index in the array
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
		}
		return name;
	}

	public void selectProductMenu() {
		int purchaseAmount = 0;
		String purchaseOption = "";
		boolean isNumber;

		try {
			//displays the current amount of money in the machine
			System.out.printf("$" + "%.2f", money);
			System.out.print(" available\n");
			//prompts the user to input a key for an item

			System.out.println("Please select an item >>> ");
			purchaseOption = menu.getIn().next().toUpperCase();

			if (amountAvailable.containsKey(purchaseOption)) {
				System.out.println("How many would you like to purchase >>> ");
				//prompts the user to input how many of that item they want to purchase

				if (menu.getIn().hasNextInt()) {
					purchaseAmount = menu.getIn().nextInt();
					isNumber = true;

					if (isNumber) {
						selectProduct(purchaseOption, purchaseAmount);
					}
				}else{
					System.out.println("Please enter a valid input.");
					displayMenu();
					displayMenu();
				}
			}else {
					System.out.println("Please enter a valid menu option.");
				}

				//puts the amount and which item into the select product method
			}catch(NumberFormatException e){
				System.out.println("Please enter a valid input.");

			}
		}
	}
