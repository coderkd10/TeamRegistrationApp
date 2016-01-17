package com.ashish.cop290assign0.utils;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 31/12/2015.
 */

public class PostRequest {
    private RequestQueue postRequestQueue;
    public PostRequest(RequestQueue requestQueue){
        this.postRequestQueue = requestQueue;
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
        postRequestQueue.add(stringRequest);
    }
}
