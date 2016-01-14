package com.ashish.cop290assign0;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Ashish on 31/12/2015.
 */
public class PostRequest {
    String url;
    ArrayList<NameValuePair> data;
    public PostRequest(String u,ArrayList<NameValuePair> d){
        this.url = u;
        this.data = d;
    }
    public String post(){
        String result;
        try {// client, request, response.. in that order.
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);
            request.setEntity(new UrlEncodedFormEntity(data));
            HttpResponse response = null;
            response = httpClient.execute(request);
            result = EntityUtils.toString(response.getEntity());
        }catch (UnknownHostException e) {
            result = "internetNotAvailable";
        } catch (Exception e) {
            if(e.getMessage()==null)
                result = e.toString();
            else
                result = e.getMessage();
        }
        return result;
    }
}
