package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.stonefacesoft.ottaa.Interfaces.SortPictogramsInterface;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.test.Components.Pictograms;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * In this class  I was testing the following classes
 * JSON ,CustomPicto and the preddiction algorithm
 * */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class UnitTestingGetPictogramsById extends TestCase implements SortPictogramsInterface {
    private final Context context = ApplicationProvider.getApplicationContext();
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
    public void unitTestingPictograms(){
        picto1=pictograms.generatePictogram(0,"es","Hola","Hello",5);
        picto2=pictograms.generatePictogram(1,"es","Adios","Good Bye",5);
        picto3=pictograms.generatePictogram(2,"es","Como estas?","How are you",5);
        picto4=pictograms.generatePictogram(3,"es","Que queres comer","What do you want to eat?",5);
        Log.e("TAG", "createUnitTestingGetPictograms: "+picto1.toString() );



        pictograms.addPictogram(picto1);
        pictograms.addPictogram(picto2);
        pictograms.addPictogram(picto3);
        pictograms.addPictogram(picto4);
        Log.e("TAG", "createRelationShip: "+picto1.toString() );
        assertTrue(!pictograms.getJson().getHijosGrupo2(0).toString().isEmpty());

     //   Log.e("TAG", "createRelationShip: "+pictograms.ordenarObjetos(picto1).toString() );

//                    Log.e("TAG", "createRelationShip: "+getOpciones.toString() );

    }

    @Override
    protected TestResult createResult() {
        return super.createResult();
    }

    @Override
    public TestResult run() {
        return super.run();
    }


    @Override
    public void pictogramsAreSorted(JSONArray array) {

    }
}
