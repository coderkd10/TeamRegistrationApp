package com.ashish.cop290assign0.utils;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ashish.cop290assign0.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Networking Utility to fetch data for auto-form completion/ data verification, uses Volley.
 * @author Abhishek Kedia on 15/01/16
 */
public class LdapFetcher {
    private static final String TAG = LdapFetcher.class.getSimpleName();

    public interface StudentJsonDataHandler {
        void handle(JSONObject studentDataJson);
    }

    private RequestQueue ldapRequestQueue;

    public LdapFetcher(RequestQueue requestQueue){
        ldapRequestQueue = requestQueue;
    }

    private Response.Listener<String> ldapResponseHandler(final StudentJsonDataHandler resultHandler) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject parsedLdapData = parseLdapUsingRegex(response);
                    if(parsedLdapData != null)
                        resultHandler.handle(parsedLdapData);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

    //fetching student details from LDAP using entered entry number
    public void getAndHandleStudentDetails(final String inputEntryNumber, final StudentJsonDataHandler resultHandler, final Response.ErrorListener errorHandler){
        String studentUserId;
        try{
            studentUserId = entryNumberToUserId(inputEntryNumber);
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
            return;
        }
        String requestUrl = Config.LDAP_BASE_URL+"?uid="+studentUserId;
        StringRequest strReq = new StringRequest(Request.Method.GET, requestUrl, ldapResponseHandler(resultHandler), errorHandler);
        Log.d(TAG,String.format("Sending request for entryNumber:%s",inputEntryNumber));
        ldapRequestQueue.add(strReq);
    }

    //fetching student details from LDAP using entered entry number
    public void getAndHandleStudentDetails(final String inputEntryNumber, final StudentJsonDataHandler resultHandler){
        getAndHandleStudentDetails(inputEntryNumber, resultHandler, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Cannot connect to LDAP. error:"+error);
            }
        });
    }

    //converts entry number to userId for fetching details from LDAP
    private String entryNumberToUserId(String entryNumber) throws IllegalArgumentException{
        Pattern entryNumberDataExtractPattern = Pattern.compile(Config.REGEX_ENTRY_NUMBER);
        Matcher entryNumDataMatcher = entryNumberDataExtractPattern.matcher(entryNumber);
        if(!entryNumDataMatcher.find() || entryNumDataMatcher.groupCount() < 3)
            throw new IllegalArgumentException("Input entry number structure invalid");
        else {
            String userId = entryNumDataMatcher.group(2) + entryNumDataMatcher.group(1) + entryNumDataMatcher.group(3);
            Log.v(TAG,String.format("uid(%s)=%s",entryNumber,userId));
            return userId;
        }
    }

    //gets entry number from the html using regex pattern matching
    private Map<String,String> getEntryNumberAndNameFromLdapH1Text(String h1Text) {
        Pattern h1TextFormatRegEx = Pattern.compile("^(.+?) \\((20\\d{2}[a-zA-z]{2}[a-zA-z\\d]\\d{4})\\)$");
        Matcher h1TextFormatMatcher = h1TextFormatRegEx.matcher(h1Text);
        if(!h1TextFormatMatcher.find() || h1TextFormatMatcher.groupCount() < 2)
            return null;
        Map<String,String> entryNumberAndName = new HashMap<>();
        entryNumberAndName.put("name",h1TextFormatMatcher.group(1));
        entryNumberAndName.put("entryNumber",h1TextFormatMatcher.group(2));
        return entryNumberAndName;
    };

    //gets base64 image from the html using regex pattern matching
    private String getImageSrcInBase64(String htmlSrc) {
        Pattern imgBase64SrcRegex = Pattern.compile("<img.*src='data:image/gif;base64,(.*?)' />");
        Matcher imgBas64Matcher = imgBase64SrcRegex.matcher(htmlSrc);
        if(!imgBas64Matcher.find() || imgBas64Matcher.groupCount() < 1)
            return null;
        return imgBas64Matcher.group(1);
    }

    /**
     * Parses the html returned by Ldap and retrieves the data contained.
     * @throws JSONException
     * @return
     */
    private JSONObject parseLdapUsingRegex(String response) throws JSONException{
        JSONObject dataAsJson = new JSONObject();
        Pattern h1HeaderRegex = Pattern.compile("<H1>(.*?)</H1>");
        Matcher h1HeaderMatcher = h1HeaderRegex.matcher(response);
        if(!h1HeaderMatcher.find())
            return null;
        String h1HeaderText = h1HeaderMatcher.group(1);
        if(h1HeaderText.equals(" ()")) {
            dataAsJson.put("isValid", false);
            return dataAsJson;
        }
        Map<String,String> entryNumberAndName = getEntryNumberAndNameFromLdapH1Text(h1HeaderText);
        if(entryNumberAndName == null)
            return null;
        dataAsJson.put("isValid", true);
        dataAsJson.put("entryNumber", entryNumberAndName.get("entryNumber"));
        dataAsJson.put("name", entryNumberAndName.get("name"));
        String imgSrcInBase64 = getImageSrcInBase64(response);
        if(imgSrcInBase64 != null) {
            dataAsJson.put("img", imgSrcInBase64);
        }
        return dataAsJson;
    }
}
