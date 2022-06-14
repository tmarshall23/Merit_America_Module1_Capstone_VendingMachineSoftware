//package com.techelevator.view;
//
//import java.io.InputStream;
//import java.io.OutputStream;
//
//public class Purchasable extends Menu{
//
//
//
//    public Purchasable(InputStream input, OutputStream output) {
//        super(input, output);
//    }
//
//    @Override
//    public Object getChoiceFromOptions(Object[] options) {
//        Object choice = null;
//        while (choice == null) {
//            displayMenuOptions(options);
//            choice = getChoiceFromUserInput(options);
//        }
//        return choice;
//    }
//
//    private Object getChoiceFromUserInput(Object[] options) {
//        Object choice = null;
//        String userInput = getIn().nextLine();
//        try {
//            int selectedOption = Integer.valueOf(userInput);
//            if (selectedOption > 0 && selectedOption <= options.length) {
//                choice = options[selectedOption - 1];
//            }
//        } catch (NumberFormatException e) {
//            // eat the exception, an error message will be displayed below since choice will be null
//        }
//        if (choice == null) {
//            getOut().println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
//        }
//        return choice;
//    }
//
//    private void displayMenuOptions(Object[] options) {
//        getOut().println();
//        for (int i = 0; i < options.length; i++) {
//            int optionNum = i + 1;
//            getOut().println(optionNum + ") " + options[i]);
//        }
//        getOut().print(System.lineSeparator() + "Please choose an option >>> ");
//        getOut().flush();
//    }
//}
