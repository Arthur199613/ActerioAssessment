package com.example.acterioexercise.Util;

public class Utility {

    public static String extractDomainFromEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex != -1) {
            return email.substring(atIndex + 1);
        } else {
            // Handle invalid email format
            return "";
        }
    }
}
