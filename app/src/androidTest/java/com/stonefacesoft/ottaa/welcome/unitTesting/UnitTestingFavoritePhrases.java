package com.stonefacesoft.ottaa.welcome.unitTesting;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;


import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.Picto;
import com.stonefacesoft.ottaa.utils.DatosDeUso;
import com.stonefacesoft.ottaa.utils.TalkActions.Historial;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.welcome.Components.Pictograms;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

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
public class UnitTestingFavoritePhrases {
    private Context mContext= ApplicationProvider.getApplicationContext();
    private Json json;
    private JSONObject picto0,picto1,picto2,picto3,picto4,picto5,picto6,picto7,picto8,picto9;
    private Pictograms picto;
    private DatosDeUso datosDeUso;

    @Before
    public void prepareTesting(){
        json=Json.getInstance();
        json.setmContext(mContext);
        picto=new Pictograms(mContext,json);
        try {
            datosDeUso=new DatosDeUso(mContext);
        } catch (FiveMbException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void startTesting(){
        createPictograms();
        Historial historial=new Historial(mContext,json);
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
        System.out.println("Phrases list"+datosDeUso.getArrayListFrasesMasUsadas(2).toString()+"\n");
        System.out.println("Phrases list"+datosDeUso.getArrayListFrasesMasUsadas(5).toString()+"\n");
    }

    public void createPictograms(){
        picto0=picto.createPictograms(0,"es","hola","hello",5);
        picto1=picto.createPictograms(1,"es","Buenos Dias","good Morning",5);
        picto2=picto.createPictograms(2,"es","Buenas Tardes","good Afternoon",5);
        picto3=picto.createPictograms(3,"es","¿Como estas?","How are you?",5);
        picto4=picto.createPictograms(4,"es","Tengo","have",3);
        picto5=picto.createPictograms(5,"es","Quiero","want",3);
        picto6=picto.createPictograms(6,"es","Adios","bye",3);
        picto7=picto.createPictograms(7,"es","Hermano","brother",1);
        picto8=picto.createPictograms(8,"es","Mama","mom",1);
        picto9=picto.createPictograms(9,"es","Papa","dad",1);


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



}

