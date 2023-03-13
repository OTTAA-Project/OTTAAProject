package com.stonefacesoft.ottaa.utils;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CloudFunctionHTTPRequest {
    private final Activity mActivity;
    private String TAG ="";
    public CloudFunctionHTTPRequest(Activity mActivity,String tag){
        this.mActivity = mActivity;
        this.TAG = tag;
    }
    public void doHTTPRequest(String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mActivity);

        // Request a string response from the provided URL.
        // Display the first 500 characters of the response string.
        //Log.e(TAG, "onResponse: "+response);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                this::parseReponse, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onResponse: Volley Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //TODO revisar que este assert este bien
                assert user != null;
                params.put("UserID", user.getEmail());
                params.put("first_name", user.getDisplayName());
                if (user.getPhoneNumber()!=null)
                    params.put("phone", user.getPhoneNumber());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseReponse(String response) {
        try {
            Log.d(TAG, "parseResponse: OK");
            JSONObject jsonObject = new JSONObject(response);

        } catch (JSONException err) {
            Log.e(TAG, "parseResponse: Error: " + err);
        }

    }

}
