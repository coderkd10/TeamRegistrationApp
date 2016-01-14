package com.ashish.cop290assign0;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by arnavkansal on 10/01/16.
 */
public class InputValidator {
    // validates entries
    // validates name
    public static boolean validateName(String inputName){
        return inputName.matches(Config.REGEX_NAME);
    }
    // validates entrycode
    public static boolean validateEntryCode(String inputEntryNumber){
        return inputEntryNumber.matches(Config.REGEX_ENTRY_NUMBER);
    }
}