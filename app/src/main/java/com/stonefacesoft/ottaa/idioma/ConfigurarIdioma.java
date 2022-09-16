package com.stonefacesoft.ottaa.idioma;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import java.util.Locale;

//import android.support.constraint.solver.Metrics;

/**
 * Esta clase se encarga de traducir la aplicacion las opciones frontales de la aplicacion
 * **/

public class ConfigurarIdioma {
    private Context mContex;
    private static String language = "en";
    private static Locale locale;

    private static boolean enableScreenScanning;
    public static void setLanguage(String name){
        language = name;
    }
    public static String getLanguaje(){
        return language;
    }
    public ConfigurarIdioma(Context context,String loc)
    {

        Locale locale=new Locale(loc);
        Configuration config=new Configuration();
        Resources resources=context.getResources();
        config.locale=locale;
        this.locale = locale;
       // context.getApplicationContext().getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
        this.mContex=context;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        myContextWrapper wrapper=new myContextWrapper(context);
        myContextWrapper.wrap(mContex,loc);
        Log.d("ConfigurarIdioma._loc",loc);

    }

    public ConfigurarIdioma(){

    }



    public Context getmContex()
    {
        return mContex;
    }

    public static Locale getLocale() {
        return locale;
    }
}
