package com.stonefacesoft.ottaa.idioma;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;
/**
 * @author gonzalo Fecha: 2/8/2018
 *Esta clase se encarga de tomar el contexto de la aplicacion y cambiarle el idioma
 *
 * **/

public class myContextWrapper extends ContextWrapper{
    /**
     * Este metodo es el constructor
     * */
    public myContextWrapper(Context base) {
        super(base);
    }

    /**
     * Metodo encargado de tomar el contexto y el lenguaje elegido
     * @param context Contexto de la aplicacion
     * @param languaje idioma a traducir
     * se setea la configuracion desde el contexto
     * @params config configuracion de la aplicacion
     * @params mSysLocale Locale de la aplicacion
     * **/

    public static  ContextWrapper wrap(Context context,String languaje)
    {

        Configuration config=context.getResources().getConfiguration();
        Locale mSysLocale=null;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            mSysLocale=getSystemLocaleLegacy(config);
        }
        else
        {
            mSysLocale=getSystemLocaleLegacy(config);

        }
        if(!languaje.equals("")&&!mSysLocale.getLanguage().equals(languaje))
        {
                Locale locale=new Locale(languaje);
                Locale.setDefault(locale);
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
                {
                    setSystemLocale(config,locale);
                }
                else
                {
                    setSystemLocaleLegacy(config, locale);
                }
            if (Build.VERSION.SDK_INT >= 21)
                {
                    context=context.createConfigurationContext(config);
                }
                else
                {
                    context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
                }
        }
        return new myContextWrapper(context);
    }

    /**
     *Este metodo se encarga de tomar la configuracion de la aplicacion y establecerle el idioma
     * **/
    private static  void setSystemLocaleLegacy(Configuration config,Locale locale)
    {
        config.locale=locale;
    }
    //devuelvo el idioma de la configuracion
    private static Locale getSystemLocaleLegacy(Configuration config)
    {
        return  config.locale;
    }
    @TargetApi(Build.VERSION_CODES.N)
    public static void setSystemLocale(Configuration config, Locale locale){
        config.setLocale(locale);
    }
    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }



}
