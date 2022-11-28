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
import com.stonefacesoft.ottaa.utils.StringFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

import kotlin.Result;

/**
 * Created by Cristian on 21/6/2016.
 */
public class BuscarArasaac {
    private final String TAG = "BuscarArasaac";
    SearchAraasacPictogram searchAraasacPictogram;

    public void setSearchAraasacPictogram(SearchAraasacPictogram searchAraasacPictogram) {
        this.searchAraasacPictogram = searchAraasacPictogram;
    }

    public void HacerBusqueda(String texto, String lang, Context context){
        String direction;
        String key = context.getResources().getString(R.string.galeria_araasac_api_key);
        String aux =texto.replaceAll(" ","_");
        String globalsymbol = "https://globalsymbols.com/api/v1/labels/search?query="+texto+"&symbolset=arasaac&language="+changeLanguage(lang)+"&limit=100";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.start();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, globalsymbol, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray object = new JSONArray(response);
                    searchAraasacPictogram.findPictograms(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("BuscarAraasac","Error :" + error.getMessage());
            }
        });

        requestQueue.add(stringRequest);
    }

    private String changeLanguage(String lang){
        String language = "eng";
        switch (lang){
            case "es":
                return "spa";
            case "pt":
                return "por";
            case "ca":
                return "cat";
            case "fr":
                return "fra";
            case "it":
                return "ita";
            case "ar":
                return "ara";
            case "da":
                return "dan";
            default:
                return language;
        }
    }

}
