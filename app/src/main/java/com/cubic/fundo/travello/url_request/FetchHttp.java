package com.cubic.fundo.travello.url_request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FetchHttp  {
    private RequestQueue mRequestQueue;
    private DataFetchListener mDataFetchListener = null;
    private Context mContext;
    public final static int POST = Request.Method.POST;
    public final static int GET = Request.Method.GET;
    private Map<String, String> paramMap = new HashMap<String, String>();;
    private Map<String, String> headerMap = new HashMap<String, String>();;

    public FetchHttp(Context mContext){
        this.mContext = mContext;
        this.mRequestQueue =   Volley.newRequestQueue(mContext);
    }

    public void url(String urlString,int requestType){
        StringRequest stringRequest = new StringRequest(requestType, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      if(FetchHttp.this.mDataFetchListener!=null){
                          FetchHttp.this.mDataFetchListener.dataString(response);
                      }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(FetchHttp.this.mDataFetchListener!=null){
                    FetchHttp.this.mDataFetchListener.fetchingError(error.getMessage());
                }
            }
        }) {

            //Access Token as Header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return headerMap;
            }
            //Access Token as Header
            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                return paramMap;
            }


        };
        this.mRequestQueue.add(stringRequest);

    }
    public FetchHttp param(String key, String value){
         // paramMap = new HashMap<String, String>();
          paramMap.put(key,value);

          return this;

    }
    public FetchHttp header(String key,String value){
           //headerMap  = new HashMap<String, String>();
           headerMap.put(key,value);
           return this;
    }
    public void setFetchDataListener(DataFetchListener mDataFetchListener){
        this.mDataFetchListener = mDataFetchListener;

    }
    public  interface DataFetchListener{
        public void dataString(String  responseData);
        public void fetchingError(String  errorMessage);
    }


}
