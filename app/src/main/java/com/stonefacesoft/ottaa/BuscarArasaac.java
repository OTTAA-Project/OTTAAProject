package com.stonefacesoft.ottaa;

import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.datatransport.cct.internal.LogEvent;
import com.google.api.LogDescriptor;
import com.stonefacesoft.ottaa.Interfaces.SearchAraasacPictogram;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import kotlin.Result;

/**
 * Created by Cristian on 21/6/2016.
 */
public class BuscarArasaac {
    SearchAraasacPictogram searchAraasacPictogram;
    JSONObject arasaac;

    public void setSearchAraasacPictogram(SearchAraasacPictogram searchAraasacPictogram) {
        this.searchAraasacPictogram = searchAraasacPictogram;
    }

    public void HacerBusqueda(String texto, String lang, Context context){

        //HttpClient Client = new DefaultHttpClient();

        // Create URL string
        Log.d("BuscarAraasaac", "entre!!!!");
        String direction;

        direction = "http://old.arasaac.org/api/index.php?callback=json&language=" + lang.toUpperCase() + "&word=" + texto.replaceAll(" ","+") +
                "%&catalog=colorpictos&thumbnailsize=150&TXTlocate=4&KEY="+context.getResources().getString(R.string.galeria_araasac_api_key);
        RequestQueue requestQueue = Volley.newRequestQueue(context,new HurlStack());
        requestQueue.start();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, direction, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    arasaac = new JSONObject(response);
                    searchAraasacPictogram.findPictograms(arasaac);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("BuscarAraasac", response );
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("BuscarAraasac","Error :" + error.getMessage());
            }
        });

        requestQueue.add(stringRequest);
    }

}
