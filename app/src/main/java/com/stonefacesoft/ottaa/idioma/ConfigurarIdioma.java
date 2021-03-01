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
    public ConfigurarIdioma(Context context,String loc)
    {

        Locale locale=new Locale(loc);
        Configuration config=new Configuration();
        Resources resources=context.getResources();
        config.locale=locale;
       // context.getApplicationContext().getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
        mContex=context;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        myContextWrapper wrapper=new myContextWrapper(context);
        wrapper.wrap(mContex,loc);
        Log.d("configurarIdioma_loc",loc);

    }
    public Context getmContex()
    {
        return mContex;
    }


}
