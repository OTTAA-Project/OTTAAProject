package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.NLG;
import com.stonefacesoft.ottaa.utils.TalkActions.Historial;
import com.stonefacesoft.ottaa.test.Components.Pictograms;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertTrue;

/**
 * @author Gonzalo Juarez
 * @version 0.1
 *<h1>Description</h1>
 * This class was created in order to make a testing about the Json Phrases
 * In this class I was testing the following classes
 * Historial,NLG, JSON
 * */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class UnitTestingPhrases {
    private Context context = ApplicationProvider.getApplicationContext();
    private Historial historial;
    private NLG nlg;
    private JSONObject picto0,picto1,picto2,picto3;
    private Pictograms pictograms;
    private StringBuilder phrase;
    private Json json;

    @Before
    public void createUnitTestingClass(){
        nlg=new NLG(context);
        json= Json.getInstance();
        json.setmContext(context);
        pictograms=new Pictograms(context,json);
        historial=new Historial(context,pictograms.getJson());
        phrase=new StringBuilder();


    }
    @Test
    public void UnitTestingPhrases(){

        try {
            picto0=pictograms.createPictograms(0,"es","yo","I",1);
            picto1=pictograms.createPictograms(0,"es","Quiero","Want",3);
            picto2=pictograms.createPictograms(1,"es","Jugar con","play with",3);
            picto3=pictograms.createPictograms(2,"es","juguete","toy",2);

            historial.addPictograma(picto0);
            historial.addPictograma(picto1);
            historial.addPictograma(picto2);
            historial.addPictograma(picto3);

            for (int i = 0; i <historial.getListadoPictos().size() ; i++) {
                if(historial.getListadoPictos().get(i)!=null)
                cargarOracion(historial.getListadoPictos().get(i));
            }


            pictograms.getJson().crearFrase(phrase.toString(),historial.getListadoPictos(),System.currentTimeMillis());
            String nlgPhrase=talkWithtNLG();
            pictograms.getJson().crearFrase(nlgPhrase,historial.getListadoPictos(),System.currentTimeMillis());
            System.out.println(pictograms.getJson().getmJSONArrayTodasLasFrases().toString());
        //    Log.e("UnitTesting", "UnitTestingPhrases: "+pictograms.getJson().getmJSONArrayTodasLasFrases().toString() );
            assertTrue(pictograms.getJson().getmJSONArrayTodasLasFrases().toString().length()>0);
            assertTrue(!nlgPhrase.isEmpty());
            assertEquals(nlgPhrase,"I Want play with a toy.");

            historial.removePictograms(false);
            removeItem(picto3);

            pictograms.getJson().crearFrase(phrase.toString(),historial.getListadoPictos(),System.currentTimeMillis());
            nlgPhrase=talkWithtNLG();
            pictograms.getJson().crearFrase(nlgPhrase,historial.getListadoPictos(),System.currentTimeMillis());
            System.out.println("UnitTestingPhrases: "+pictograms.getJson().getmJSONArrayTodasLasFrases().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void cargarOracion(JSONObject object){
        phrase.append(getName(object,"es") +" ");
    }

    public String getName(JSONObject object,String locale){
        try {
            return object.getJSONObject("texto").getString(locale);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void removeItem(JSONObject object){
        String name=getName(object,"es");
        int index=phrase.toString().indexOf(name);
        int end=phrase.toString().lastIndexOf(name);
        phrase.delete(index,end);
    }


    public String talkWithtNLG(){
        String Phrase="";

        nlg.NuevaFrase();
        for (int i = 0; i < historial.getListadoPictos().size(); i++) {
            nlg.CargarFrase(historial.getListadoPictos().get(i));
        }
        Phrase=nlg.ArmarFrase();
        Log.e("Historial", "talkWithtNLG: "+ Phrase );
        return Phrase;
    }

}
