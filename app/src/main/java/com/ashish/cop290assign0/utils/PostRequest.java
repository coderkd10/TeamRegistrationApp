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
 * Networking Utility to use Volley for data posting to server.
 * @author Arnav Kansal
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
    /**
     * POSTS a StringRequest to the server.
     * The format of the request is :
     *  {
     *       “teamname” : “ &lt; value &gt; ”
     *       “entry1” : “ &lt; value &gt; ”
     *       “name1” : “ &lt; value &gt; ”
     *       “entry2” : “ &lt; value &gt; ”
     *       “name2” : “ &lt; value &gt; ”
     *       “entry3” : “ &lt; value &gt; ”
     *       “name3” : “ &lt; value &gt; ”
     *   }
     *
     * source: <a href="http://stackoverflow.com/questions/15805555/java-regex-to-validate-full-name-allow-only-spaces-and-letters">Stack Overflow answer</a>
     * @param url   the url of the server
     * @param data  the data to be posted in above format
     * @param responseHandler   the section which takes over for successful response
     * @param errorHandler    the section which takes over for invalid reponse
     * @return              <code>void</code>
     */
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
