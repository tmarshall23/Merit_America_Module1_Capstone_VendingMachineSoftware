package com.techelevator;

import java.util.InputMismatchException;

public class CustInputMismatchException extends InputMismatchException {




    public CustInputMismatchException() {
    }

    public String getMessage(String message) {
        return message;
    }
}
