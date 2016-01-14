package com.ashish.cop290assign0;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by arnavkansal on 10/01/16.
 */
public class InputValidator {
    // validates entries
    // validates name
    private boolean validateName(String testString){
        String nameTypeRegex = "^[\\p{L} .'-]+$";
        return isValid(testString, nameTypeRegex);
    }
    // validates entrycode
    private boolean validateEntryCode(String testString){
        String entryNoRegex="(\\d{4})((?:[a-zA-Z][a-zA-Z]\\d))(\\d{4})";
        return isValid(testString, entryNoRegex);
    }

    // validates a given string using a regex pattern
    private boolean isValid(String test, String regexPattern){
        Pattern regx = Pattern.compile(regexPattern);
        Matcher match = regx.matcher(test);
        return match.matches();
    }
}