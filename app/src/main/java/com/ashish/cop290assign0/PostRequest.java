package com.ashish.cop290assign0;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 31/12/2015.
 */

public class PostRequest {
    //String url;
    //Map<String, String> data;
    private RequestQueue requestQueue;
    public PostRequest(Context context){
        //this.url = url;
        //this.data = data;
        this.requestQueue = Volley.newRequestQueue(context);
    }
    public interface ServerResponseHandler{
        void handle(String response);
    }
    public interface ErrorHandler{
        void handle(VolleyError error);
    }
    public void post(String url, final Map<String,String> data, final ServerResponseHandler responseHandler,final ErrorHandler errorHandler){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //show after successful response
                        responseHandler.handle(response);
                        Log.d("post","Got response:"+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandler.handle(error);
                        Log.e("post", "Volley error:" + error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                return new HashMap<>(data);
            }
        };
        requestQueue.add(stringRequest);
    }
}
