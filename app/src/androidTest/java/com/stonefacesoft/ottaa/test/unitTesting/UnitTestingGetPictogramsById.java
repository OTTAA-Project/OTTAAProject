package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.stonefacesoft.ottaa.Custom_Picto;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.test.Components.Pictograms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * In this class  I was testing the following classes
 * JSON ,CustomPicto and the preddiction algorithm
 * */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class UnitTestingGetPictogramsById {
    private Context context = ApplicationProvider.getApplicationContext();
    private JSONObject picto1,picto2,picto3,picto4;
    private JSONArray getOpciones;
    private Pictograms pictograms;
    private Json json;

    @Before
    public void createUnitTestingGetPictograms(){
        json=Json.getInstance();
        json.setmContext(context);
        pictograms=new Pictograms(context,json);

    }

    @Test
    public void createRelationShip(){
        picto1=pictograms.createPictograms(0,"es","Hola","Hello",5);
        picto2=pictograms.createPictograms(1,"es","Adios","Good Bye",5);
        picto3=pictograms.createPictograms(2,"es","Como estas?","How are you",5);
        picto4=pictograms.createPictograms(3,"es","Que queres comer","What do you want to eat?",5);
        Log.e("TAG", "createUnitTestingGetPictograms: "+picto1.toString() );

        pictograms.relacionarObjeto(picto1,picto2);
        pictograms.relacionarObjeto(picto1,picto2);
        pictograms.relacionarObjeto(picto1,picto3);
        pictograms.relacionarObjeto(picto1,picto3);
        pictograms.relacionarObjeto(picto1,picto3);
        pictograms.relacionarObjeto(picto1,picto3);
        pictograms.relacionarObjeto(picto1,picto3);
        pictograms.relacionarObjeto(picto1,picto4);

        pictograms.addPictogram(picto1);
        pictograms.addPictogram(picto2);
        pictograms.addPictogram(picto3);
        pictograms.addPictogram(picto4);
        Log.e("TAG", "createRelationShip: "+picto1.toString() );
        Log.e("TAG", "createRelationShip: "+pictograms.ordenarObjetos(picto1) );
        Assert.assertNotNull(pictograms.ordenarObjetos(picto1));//
        try {
            Custom_Picto picto=new Custom_Picto(context);
            picto.setIdPictogram(pictograms.getJson().getId(picto1));
            picto.setCustom_Texto(pictograms.getJson().getNombre(picto1,"es"));
            picto.setCustom_Color(pictograms.getJson().getTipo(picto1));

        } catch (JSONException e) {
            e.printStackTrace();
        }
     //   Log.e("TAG", "createRelationShip: "+pictograms.ordenarObjetos(picto1).toString() );

//                    Log.e("TAG", "createRelationShip: "+getOpciones.toString() );

    }
}
