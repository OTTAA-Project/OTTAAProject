package com.stonefacesoft.ottaa.test;

import com.stonefacesoft.ottaa.NLG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NLGTest {
    private NLG nlg;
    @Before
    public void setUp(){
        nlg = new NLG();
    }

    @Test
    public void nuevaFraseTest() {
        nlg.NuevaFrase();
        String frase = nlg.ArmarFrase();
        assertEquals(frase,"");
    }

    @Test
    public void cargarFraseTest() {

        JSONObject picto1 = null;
        assertFalse(nlg.CargarFrase(picto1, 3));

        JSONObject picto2 = createPictograms(1,"es","acompañar","escort",3);
        assertTrue(nlg.CargarFrase(picto2,3));

    }

    @Test
    public void makePhraseTest() {
        nlg.NuevaFrase();

        JSONObject picto1 = createPictograms(643,"es","Yo","I",1);
        JSONObject picto2 = createPictograms(22,"es","estoy","be",3);
        JSONObject picto3 = createPictograms(819,"es","enfermo","sick",4);

        nlg.CargarFrase(picto1,1);
        nlg.CargarFrase(picto2,3);
        nlg.CargarFrase(picto3,4);

        String actual = nlg.ArmarFrase();
        assertEquals("I am sick.", actual);

        picto1 = createPictograms(643,"es","Yo","I",1);
        picto2 = createPictograms(22,"es","quiero","want",3);
        picto3 = createPictograms(118,"es","comer","eat",3);
        JSONObject picto4 = createPictograms(474,"es","manzana","apple",2);
        JSONObject picto5 = createPictograms(414,"es","frutilla","strawberry",2);

        nlg.NuevaFrase();
        nlg.CargarFrase(picto1,1);
        nlg.CargarFrase(picto2,3);
        nlg.CargarFrase(picto3,3);
        nlg.CargarFrase(picto4,2);
        nlg.CargarFrase(picto5,2);
        actual = nlg.ArmarFrase();
        assertEquals("I want to eat an apple and a strawberry.", actual);

    }

    @Test
    public void makeVerbPhrase() {
        nlg.NuevaFrase();

        JSONObject picto1 = createPictograms(643,"es","Yo","I",1);
        JSONObject picto3 = createPictograms(819,"es","ir a","go to",3);
        JSONObject picto4 = createPictograms(820,"es","doctor","doctor",1);

        nlg.CargarFrase(picto1,1);
        nlg.CargarFrase(picto3,3);
        nlg.CargarFrase(picto4,1);

        String actual = nlg.ArmarFrase();
        System.out.println(actual);
        assertEquals("I go to.", actual);

    }


    public JSONObject createPictograms(int id, String locale, String localeName, String englishName, int tipo) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("tipo",tipo);
            JSONObject texto=new JSONObject();
            texto.put("en",englishName);
            texto.put(locale,localeName);
            jsonObject.put("texto",texto);
            JSONObject imagen=new JSONObject();
            imagen.put("picto","ic_action_previous");
            jsonObject.put("imagen",imagen);
            jsonObject.put("relacion",new JSONArray());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}