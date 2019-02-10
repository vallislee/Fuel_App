package com.example.vallislee.fuel_consumption;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sanitization {

    //email_sanitization
    public boolean email_sanitization(String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
