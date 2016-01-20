package com.ashish.cop290assign0.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashish.cop290assign0.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Networking Utility to fetch data for auto-form completion/ data verification, uses Volley.
 * @author Abhishek Kedia on 15/01/16
 */


public class LdapFetcher {
    /**
     * Uses a GET request to fetch ldap data of student.
     * @param
     * @throws
     * @return              
     */
    public interface studentJsonDataHandler {
        void onGetJson(JSONObject studentDataJson);
    }

    public interface ldapRequestErrorHandler{
        void handle(VolleyError error);
    }

    private RequestQueue ldapRequestQueue;

    public LdapFetcher(RequestQueue requestQueue){
        ldapRequestQueue = requestQueue;
    }

    public void getAndHandleStudentDetails(String inputEntryNumber,final studentJsonDataHandler jsonHandler, final ldapRequestErrorHandler errorHandler){
        StringRequest strReq = new StringRequest(
                Request.Method.GET,
                Config.LDAP_BASE_URL+"?uid="+entryNumToUserId(inputEntryNumber),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject parsedLdapData = parseLdapResponse(Jsoup.parse(response));
                            jsonHandler.onGetJson(parsedLdapData);
                        } catch (Exception e){
                            Log.e(LdapFetcher.class.getSimpleName(),"Exception occurred in getStudentDetails/onResponse:"+e.toString(),e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandler.handle(error);
                    }
                }
        );
        ldapRequestQueue.add(strReq);
    }

    public void getAndHandleStudentDetails(String inputEntryNumber,final studentJsonDataHandler jsonHandler){
        getAndHandleStudentDetails(inputEntryNumber, jsonHandler, new ldapRequestErrorHandler() {
            @Override
            public void handle(VolleyError error) {
                //do nothing
            }
        });
    }

    private String entryNumToUserId(String entryNum){
        String entryNumDataExtractRegEx = "20(\\d{2})([a-zA-z]{2}[a-zA-z\\d])(\\d{4})";
        Pattern entryNumDataExtractPattern = Pattern.compile(entryNumDataExtractRegEx);
        Matcher entryNumDataMatcher = entryNumDataExtractPattern.matcher(entryNum);
        if(!entryNumDataMatcher.find() || entryNumDataMatcher.groupCount() < 3)
            return "ee1130431";
        else {
            Log.d("group(1)=",entryNumDataMatcher.group(1));
            Log.d("group(0)=",entryNumDataMatcher.group(0));
            Log.d("group(2)=",entryNumDataMatcher.group(2));
            Log.d("group(3)=",entryNumDataMatcher.group(3));

            String userId = entryNumDataMatcher.group(2) + entryNumDataMatcher.group(1) + entryNumDataMatcher.group(3);
            Log.d("userID of " + entryNum,userId);
            return userId;
        }
    }

    /**
     * Parses the html returned by Ldap and retrieves the data contained.
     * @throws JSONException
     * @return
     */
    private JSONObject parseLdapResponse(Document ldapRes) throws JSONException{
        JSONObject toReturn = new JSONObject();
        String h1HeaderText = ldapRes.getElementsByTag("h1").first().text(); //can throw NullPointerException
        if(h1HeaderText.equals(" ()")) {
            toReturn.put("isValid",false);
        } else {
            String entryNumRegex = "\\((\\d{4}[a-zA-z]{2}[a-zA-z0-9]\\d{4})\\)";
            Pattern entryNumPattern = Pattern.compile(entryNumRegex);
            Matcher entryNumMatcher = entryNumPattern.matcher(h1HeaderText);
            if(!entryNumMatcher.find()){
                toReturn.put("isValid",false);
            } else {
                String entryNum = entryNumMatcher.group(0);
                entryNum = entryNum.substring(1,entryNum.length()-1);
                String fullName = h1HeaderText.replaceAll(entryNumRegex,"").trim();
                if(fullName.isEmpty()) {
                    toReturn.put("isValid",false);
                } else {
                    toReturn.put("isValid",true);
                    toReturn.put("entryNumber",entryNum);
                    toReturn.put("name",fullName);
                }
//                String[] nameParts = fullName.split(" ");
//                if(nameParts.length == 0) {
//                    toReturn.put("isValid",false);
//                } else {
//                    toReturn.put("isValid",true);
//                    toReturn.put("entryNumber",entryNum);
//                    if(nameParts.length == 1 || nameParts.length == 2) {
//                        toReturn.put("name",fullName);
//                    } else {
//                        StringBuilder shortName = new StringBuilder();
//                        for(int i=0; i<nameParts.length-2; i++){
//                            shortName.append(nameParts[i].charAt(0)).append(". ");
//                        }
//                        shortName.append(nameParts[nameParts.length-2]).append(" ");
//                        shortName.append(nameParts[nameParts.length-1]);
//                        toReturn.put("name", shortName.toString());
//                    }
//                }
                Elements imgElements = ldapRes.getElementsByTag("img");
                Log.d("imgElements", imgElements.toString());
                if(!imgElements.isEmpty()){
                    Log.d("img", "not empty");
                    String imgSrc = imgElements.first().attr("src");
                    Log.d("imgSrc", imgSrc);
                    String imgSrcStart = "data:image/gif;base64,";
                    if(imgSrc.startsWith(imgSrcStart)) {
                        toReturn.put("img", imgSrc.substring(imgSrcStart.length()));
                    }
                }
            }

        }
        return toReturn;
    }
}
