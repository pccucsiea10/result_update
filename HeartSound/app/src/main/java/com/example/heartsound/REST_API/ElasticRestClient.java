package com.example.heartsound.REST_API;
import android.content.Context;
import android.util.Log;

import com.example.heartsound.Result_analysis.ResultDepartment;
import com.github.mikephil.charting.data.Entry;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class ElasticRestClient {

    private static final String BASE_URL = "http://140.137.41.81:9200/"; //http://localhost:9200/
    private static final String CLASS_NAME = ElasticRestClient.class.getSimpleName();


    private static AsyncHttpClient client = new AsyncHttpClient();

    public static ArrayList<Entry> get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        ArrayList<Entry> eArray = new ArrayList<>();
        client.get(getAbsoluteUrl(url), params, new JsonHttpResponseHandler() { // instead of 'get' use twitter/tweet/1

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.i(CLASS_NAME, "onSuccess: " + response.toString());
                try {
                    JSONArray responses = response.getJSONObject("hits").getJSONArray("hits");

                    for(int i = 0 ;  i< responses.length() ; i++){

                        String s = responses.getJSONObject(i).getJSONObject("_source").get("pulse").toString();
                        Entry e = new Entry(i,Float.valueOf(s));
                        eArray.add(e);
                    }
                    ResultDepartment.enties = eArray;
                    System.out.println(eArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                System.out.println(response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e(CLASS_NAME, "onFailure");
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.i(CLASS_NAME, "onRetry " + retryNo);
                // called when request is retried
            }
        });
        return eArray;

    }

    public static void post(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }



    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public void getHttpRequest() {
        try {


            ElasticRestClient.get("get", null, new JsonHttpResponseHandler() { // instead of 'get' use twitter/tweet/1
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Log.i(CLASS_NAME, "onSuccess: " + response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i(CLASS_NAME, "onSuccess: " + response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.e(CLASS_NAME, "onFailure");
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                }

                @Override
                public void onRetry(int retryNo) {
                    Log.i(CLASS_NAME, "onRetry " + retryNo);
                    // called when request is retried
                }
            });
        }
        catch (Exception e){
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        }
    }
}