package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.test.Components.Pictograms;
import com.stonefacesoft.ottaa.utils.TalkActions.Historial;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Phrases
 * hello how are you
 * hello good morning
 * how are you felling?
 * I have Hungry
 * I have cold
 * I am really bad
 * hello good afternoon
 * Mom I want to eat
 * Mom I want to play with the toy
 * */

@RunWith(AndroidJUnit4.class)
public class UnitTestingFavoritePhrases extends TestCase {
    private final Context mContext= ApplicationProvider.getApplicationContext();
    private Json json;
    private JSONObject picto0,picto1,picto2,picto3,picto4,picto5,picto6,picto7,picto8,picto9;
    private Pictograms picto;


    @Before
    public void prepareTesting(){
        json=Json.getInstance();
        json.setmContext(mContext);
        picto=new Pictograms(mContext,json);

    }

    @Test
    public void unitTestingPhrases(){
        addPictograms();
        Historial historial=new Historial(json);
        historial.addPictograma(picto0);
        historial.addPictograma(picto1);
        addFrase(1,historial);
        historial.removePictograms(true);
        historial.addPictograma(picto0);
        historial.addPictograma(picto2);
        addFrase(2,historial);
        historial.removePictograms(true);
        historial.addPictograma(picto0);
        historial.addPictograma(picto3);
        addFrase(6,historial);
        historial.removePictograms(true);
        historial.addPictograma(picto6);
        historial.addPictograma(picto7);
        addFrase(8,historial);

    }

    public void addPictograms(){
        picto0=picto.generatePictogram(0,"es","hola","hello",5);
        picto1=picto.generatePictogram(1,"es","Buenos Dias","good Morning",5);
        picto2=picto.generatePictogram(2,"es","Buenas Tardes","good Afternoon",5);
        picto3=picto.generatePictogram(3,"es","¿Como estas?","How are you?",5);
        picto4=picto.generatePictogram(4,"es","Tengo","have",3);
        picto5=picto.generatePictogram(5,"es","Quiero","want",3);
        picto6=picto.generatePictogram(6,"es","Adios","bye",3);
        picto7=picto.generatePictogram(7,"es","Hermano","brother",1);
        picto8=picto.generatePictogram(8,"es","Mama","mom",1);
        picto9=picto.generatePictogram(9,"es","Papa","dad",1);


        json.getmJSONArrayTodosLosPictos().put(picto0);
        json.getmJSONArrayTodosLosPictos().put(picto1);
        json.getmJSONArrayTodosLosPictos().put(picto2);
        json.getmJSONArrayTodosLosPictos().put(picto3);
        json.getmJSONArrayTodosLosPictos().put(picto4);
        json.getmJSONArrayTodosLosPictos().put(picto5);
        json.getmJSONArrayTodosLosPictos().put(picto6);
        json.getmJSONArrayTodosLosPictos().put(picto7);
        json.getmJSONArrayTodosLosPictos().put(picto8);
        json.getmJSONArrayTodosLosPictos().put(picto9);
    }
    public void createPrase(Historial history){
        try {
            json.crearFrase(getOracion(history),history.getListadoPictos(),System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getOracion(Historial history){
        String phrase="";
        for (int i = 0; i <history.getListadoPictos().size() ; i++) {
            phrase+=getName(history.getListadoPictos().get(i))+" ";
        }
        return phrase;
    }

    public String getName(JSONObject object){
        try {
            return object.getJSONObject("texto").getString("es");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void addFrase(int size,Historial historial){
        for (int i = 0; i <size ; i++) {
            createPrase(historial);
        }
    }

    @Override
    protected TestResult createResult() {
        return super.createResult();
    }

    @Override
    public TestResult run() {
        return super.run();
    }



}

