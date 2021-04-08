package com.stonefacesoft.ottaa.welcome.unitTesting;

import android.content.Context;
import android.content.SharedPreferences;

import com.stonefacesoft.ottaa.Interfaces.translateInterface;
import com.stonefacesoft.ottaa.utils.traducirTexto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class UnitTestingGoogleTranslate implements translateInterface {
    private Context mContext= ApplicationProvider.getApplicationContext();
    private traducirTexto translateText;
    private SharedPreferences preferences;
    private String result;
    @Before
    public void createUnitTesting(){
        preferences= PreferenceManager.getDefaultSharedPreferences(mContext);
        translateText=new traducirTexto(mContext,preferences);
    }

    @Test
    public void UnitTestingGoogleTranslate(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        translateText.traducirIdioma(this,"hola","es","hello",true);
        result="hello";
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        translateText.traducirIdioma(this,"uva","es","hello",true);
        result="grape";
    }

    @Override
    public void onTextoTraducido(boolean traduccion) {
        Assert.assertTrue(translateText.getTexto().equals(result));
    }
}