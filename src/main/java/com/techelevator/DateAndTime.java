package com.techelevator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateAndTime {

        public static void dateAndTime() {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(dtf.format(now));
        }
    }
