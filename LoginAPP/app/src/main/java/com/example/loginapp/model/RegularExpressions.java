package com.example.loginapp.model;

import java.util.regex.Pattern;

public class RegularExpressions {
    public RegularExpressions() {
    }
    public boolean validateName(String cadena) {
        String regex = "^(?=(?:[^\\w]*\\w){3,})[\\w\\s]{3,36}$";
        Pattern patter = Pattern.compile(regex);
        return patter.matcher(cadena).matches();
    }
    public boolean validateEmail(String cadena) {
        String regex = "^(\\w|\\.|_)+@(gmail|email|outlook)\\.com$";
        Pattern patter = Pattern.compile(regex);
        return patter.matcher(cadena).matches();
    }

    public boolean validatePhone(String cadena) {
        String regex = "^\\d{8}$";
        Pattern patter = Pattern.compile(regex);
        return patter.matcher(cadena).matches();
    }
    public boolean validatePassword(String cadena) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-=_+{};':,./<>?])[A-Za-z\\d!@#$%^&*()\\-=_+{};':,./<>? ]{8,24}$";
        Pattern patter = Pattern.compile(regex);
        return patter.matcher(cadena).matches();
    }
}
