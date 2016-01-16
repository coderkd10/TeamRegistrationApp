package com.ashish.cop290assign0;

/**
 * Created by Ashish on 10/01/2016.
 */
public class Config {
    final static String SERVER_URL = "http://agni.iitd.ernet.in/cop290/assign0/register/";
    final static String LDAP_BASE_URL = "http://10.7.172.112:80/search/ldap1.php";
    final static String REGEX_NAME = "^[\\p{L} .'-]+$";
    final static String REGEX_ENTRY_NUMBER ="(\\d{4})((?:[a-zA-Z]{2}[a-zA-z\\d]))(\\d{4})";
}
