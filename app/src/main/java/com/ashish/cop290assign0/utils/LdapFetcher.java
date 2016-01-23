package com.ashish.cop290assign0.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.os.Process;

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
    private static final String TAG = LdapFetcher.class.getSimpleName();
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

    private class GetUsingJsoup extends AsyncTask<Void,Void,Document> {
        String url;
        studentJsonDataHandler jsonHandler;
        GetUsingJsoup(String url, studentJsonDataHandler jsonHandler) {
            this.url = url;
            this.jsonHandler = jsonHandler;
        }
        @Override
        protected Document doInBackground(Void... v) {
            try {
                return Jsoup.connect(url).get();
            } catch (Exception e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(Document responseAsHTML) {
            try {
                JSONObject parsedLdapData = parseLdapResponse(responseAsHTML);
                jsonHandler.onGetJson(parsedLdapData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ParseAndHandleResponse extends Thread {
        String inputEntryNumber;
        String response;
        studentJsonDataHandler jsonHandler;
        ParseAndHandleResponse(String inputEntryNumber, String response, studentJsonDataHandler jsonHandler){
            this.inputEntryNumber = inputEntryNumber;
            this.response = response;
            this.jsonHandler = jsonHandler;
            //Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            //Process.setThreadPriority(Process.THREAD_PRIORITY_LESS_FAVORABLE);
            Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
        }
        public void run() {
            try {
//                Document responseAsHTML = Jsoup.parse(response);
//                Document responseAsHTML = Jsoup.connect(Config.LDAP_BASE_URL+"?uid="+entryNumToUserId(inputEntryNumber)).get();
//                JSONObject parsedLdapData = parseLdapResponse(responseAsHTML);
//                jsonHandler.onGetJson(parsedLdapData);
                new GetUsingJsoup(Config.LDAP_BASE_URL+"?uid="+entryNumToUserId(inputEntryNumber),jsonHandler).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private RequestQueue ldapRequestQueue;

    public LdapFetcher(RequestQueue requestQueue){
        ldapRequestQueue = requestQueue;
    }

    public void getAndHandleStudentDetails(final String inputEntryNumber,final studentJsonDataHandler jsonHandler, final ldapRequestErrorHandler errorHandler){
        StringRequest strReq = new StringRequest(
                Request.Method.GET,
                Config.LDAP_BASE_URL+"?uid="+entryNumToUserId(inputEntryNumber),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
//                            JSONObject parsedLdapData = parseLdapResponse(Jsoup.parse(response));
                            JSONObject parsedLdapData = parseLdapUsingRegex(response);
                            Log.d(TAG,"parsedLdapData:"+parsedLdapData);
                            if(parsedLdapData != null)
                                jsonHandler.onGetJson(parsedLdapData);
//                            new ParseAndHandleResponse(inputEntryNumber,response,jsonHandler).run();
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

    private JSONObject parseLdapUsingRegex(String response) throws JSONException{
        JSONObject toReturn = new JSONObject();
//        Log.d(TAG,"--> response = " + response );
        String h1HeaderRegexString = "<H1>(.*?)<\\/H1>";
        Pattern h1HeaderRegex = Pattern.compile(h1HeaderRegexString);
        Matcher h1HeaderMatcher = h1HeaderRegex.matcher(response);
//        if(!response.matches(h1HeaderRegex))
//            return null;
//        String r1 = "<html>\n" +
//                "                                                                       <head>\n" +
//                "                                                                       <style type=\"text/css\">\n" +
//                "                                                                       <!-- BODY {background:none transparent;}-->\n" +
//                "                                                                       </style>\n" +
//                "                                                                       </head>\n" +
//                "                                                                       <body>\n" +
//                "                                                                       <H1>Abhishek Kedia (2013EE10431)</H1>";
//        Log.d(TAG,"in r1       : " + h1Regex.matcher(r1).find() + "; text : ");
//        Log.d(TAG,"in response : " + h1Regex.matcher(response).find());
//        String h1HeaderText = Pattern.compile("H1").matcher(response).group(1);
        if(!h1HeaderMatcher.find())
            return null;
        String h1HeaderText = h1HeaderMatcher.group(1);
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
                entryNum = entryNum.substring(1, entryNum.length() - 1);
                String fullName = h1HeaderText.replaceAll(entryNumRegex, "").trim();
                if (fullName.isEmpty()) {
                    toReturn.put("isValid", false);
                } else {
                    toReturn.put("isValid", true);
                    toReturn.put("entryNumber", entryNum);
                    toReturn.put("name", fullName);
                }
                Pattern imgBase64SrcRegex = Pattern.compile("<img.*src='data:image\\/gif;base64,(.*?)' \\/>");
                Matcher imgBas64Matcher = imgBase64SrcRegex.matcher(response);
                if(imgBas64Matcher.find()) {
                    toReturn.put("img",imgBas64Matcher.group(1));
                }
            }
        }
        
        return toReturn;
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
