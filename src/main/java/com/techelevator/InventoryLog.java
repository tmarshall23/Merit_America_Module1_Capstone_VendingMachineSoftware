package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class InventoryLog {

    public static void log(String message) {
        String auditPath = "src/main/resources/Log.txt";
        File logFile = new File(auditPath);
        try (PrintWriter logOutput = new PrintWriter(new FileOutputStream(logFile, true))) {
            logOutput.println(message);

        } catch (FileNotFoundException e) {
            System.out.println("Unable to find the file: " + logFile.getAbsolutePath());
        }

    }
}


