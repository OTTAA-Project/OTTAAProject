package com.stonefacesoft.ottaa;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
    SharedPreferences sharedPrefsDefault;

    public JSONObject HacerBusqueda(Context context, String texto){

        // Create http cliient object to send request to server
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(context);
        HttpClient Client = new DefaultHttpClient();

        // Create URL string
        Log.d("BuscarAraasaac","entre!!!!");
        String URL;

        String lang = sharedPrefsDefault.getString(context.getString(R.string.str_idioma),"en").toUpperCase();
        //Todo remove key from galeriaAraasac
        URL = "http://arasaac.org/api/index.php?callback=json&language="+ lang +"&word=" + texto +
                "&catalog=colorpictos&thumbnailsize=150&TXTlocate=4&KEY=GaArpYjNXFr2bJXuQcCT";

//        if(sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma),"en")) {
//            URL = "http://arasaac.org/api/index.php?callback=json&language=ES&word=" + texto + "&catalog=colorpictos&thumbnailsize=150&TXTlocate=4&KEY=GaArpYjNXFr2bJXuQcCT";
//        }else {
//            URL = "http://arasaac.org/api/index.php?callback=json&language=EN&word=" + texto + "&catalog=colorpictos&thumbnailsize=150&TXTlocate=4&KEY=GaArpYjNXFr2bJXuQcCT";
//        }

        //Log.i("httpget", URL);

        try
        {
            String SetServerString = "";

            // Create Request to server and get response

            HttpGet httpget = new HttpGet(URL);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            SetServerString = Client.execute(httpget, responseHandler);

            arasaac = new JSONObject(SetServerString);
        }
        catch(Exception ex) {
            Log.e("error_BuscarAraasac", ex.toString());}
        return arasaac;
    }

}
