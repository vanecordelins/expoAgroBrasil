package com.expoagro.expoagrobrasil.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fabricio on 7/3/2017.
 */

public class Regex {

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.length() <= 15;
    }

    public static boolean isNameValid(String name) {
        String expression = "^[[ ]|\\p{L}*]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isTelephoneValid(String telefone) {
        String expression = "^[1-9]{2} (?:[2-8]|9[1-9])[0-9]{3}\\-[0-9]{4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(telefone);
        return matcher.matches();
    }
}
