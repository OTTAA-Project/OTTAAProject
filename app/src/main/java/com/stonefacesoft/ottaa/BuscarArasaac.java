package com.stonefacesoft.ottaa;

import android.content.Context;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/**
 * Created by Cristian on 21/6/2016.
 */
public class BuscarArasaac {

    JSONObject arasaac;

    public JSONObject HacerBusqueda(String texto, String lang, Context context){

        HttpClient Client = new DefaultHttpClient();

        // Create URL string
        Log.d("BuscarAraasaac", "entre!!!!");
        String URL;

        //Todo remove key from galeriaAraasac
        URL = "http://arasaac.org/api/index.php?callback=json&language=" + lang.toUpperCase() + "&word=" + texto +
                "&catalog=colorpictos&thumbnailsize=150&TXTlocate=4&KEY="+context.getResources().getString(R.string.galeria_araasac_api_key);
        try {
            String SetServerString = "";
            // Create Request to server and get response
            HttpGet httpget = new HttpGet(URL);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            SetServerString = Client.execute(httpget, responseHandler);
            arasaac = new JSONObject(SetServerString);
        } catch (Exception ex) {
            Log.e("error_BuscarAraasac", ex.toString());
        }
        return arasaac;
    }

}
