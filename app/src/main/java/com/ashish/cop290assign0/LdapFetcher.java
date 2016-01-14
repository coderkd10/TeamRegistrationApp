package com.ashish.cop290assign0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by kedia-abhishek on 15/1/16.
 */
public class LdapFetcher {

    public interface studentJsonDataHandler {
        void onGetJson(JSONObject studentDataJson);
    }

    private RequestQueue ldapRequestQueue;
    LdapFetcher(Context context){
        ldapRequestQueue = Volley.newRequestQueue(context);
    }

    public void getAndHandleStudentDetails(String inputEntryNumber,final studentJsonDataHandler jsonHandler){
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
                    }
                }
        );
        ldapRequestQueue.add(strReq);
    }

    private String entryNumToUserId(String entryNum){
        String entryNumDataExtractRegEx = "20(\\d{2})([a-zA-z]{2}[a-zA-z\\d])(\\d{4})";
        Pattern entryNumDataExtractPattern = Pattern.compile(entryNumDataExtractRegEx);
        Matcher entryNumDatMatcher = entryNumDataExtractPattern.matcher(entryNum);
        if(!entryNumDatMatcher.find() || entryNumDatMatcher.groupCount() < 3)
            return "ee1130431";
        else {
            Log.d("group(1)=",entryNumDatMatcher.group(1));
            Log.d("group(0)=",entryNumDatMatcher.group(0));
            Log.d("group(2)=",entryNumDatMatcher.group(2));
            Log.d("group(3)=",entryNumDatMatcher.group(3));

            String userId = entryNumDatMatcher.group(2) + entryNumDatMatcher.group(1) + entryNumDatMatcher.group(3);
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
                String[] nameParts = fullName.split(" ");
                if(nameParts.length == 0) {
                    toReturn.put("isValid",false);
                } else {
                    toReturn.put("isValid",true);
                    toReturn.put("entryNumber",entryNum);
                    if(nameParts.length == 1 || nameParts.length == 2) {
                        toReturn.put("name",fullName);
                    } else {
                        StringBuilder shortName = new StringBuilder();
                        for(int i=0; i<nameParts.length-2; i++){
                            shortName.append(nameParts[i].charAt(0)).append(". ");
                        }
                        shortName.append(nameParts[nameParts.length-2]);
                        shortName.append(nameParts[nameParts.length-1]);
                        toReturn.put("name", shortName.toString());
                    }
                }
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
