package com.stonefacesoft.ottaa.test;




import com.stonefacesoft.ottaa.Prediction.Clima;
import com.stonefacesoft.ottaa.Prediction.Edad;
import com.stonefacesoft.ottaa.Prediction.Horario;
import com.stonefacesoft.ottaa.Prediction.Posicion;
import com.stonefacesoft.ottaa.Prediction.Sexo;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.JSONutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class JSONutilsTest {

    @Test
    public void getIDfromNombreTest() {
        JSONArray jsonArray = createJSONArray();
        try {
            assertEquals(22,JSONutils.getIDfromNombre("want",jsonArray));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void stringToArrayListTest() {
        String string = "[{\"id\":0,\"tipo\":111},{\"id\":1,\"tipo\":222},{\"id\":2,\"tipo\":333},{\"id\":3,\"tipo\":444},{\"id\":4,\"tipo\":555},{\"id\":5,\"tipo\":666}]";
        ArrayList<JSONObject> arrayList = JSONutils.stringToArrayList(string);

        try {
            assertEquals(0,arrayList.get(0).getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getNombreTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        assertEquals("apple",JSONutils.getNombre(jsonObject,"en"));
        assertEquals("manzana",JSONutils.getNombre(jsonObject,"es"));
        assertEquals("error",JSONutils.getNombre(jsonObject,"ec"));

    }

    @Test
    public void getTipoTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        assertEquals(2,JSONutils.getTipo(jsonObject));
    }

    @Test
    public void getWordTypeTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        try {
            jsonObject.put("wordTYPE",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertEquals(1,JSONutils.getWordType(jsonObject));
    }

    @Test
    public void setHijosGrupo2Test() {
        JSONArray arrayPadre = createJSONArray();
        JSONArray arrayHijo = createJSONArray();
        JSONutils.setHijosGrupo2(arrayPadre,arrayHijo,1);
        try {
            assertEquals(arrayHijo,arrayPadre.getJSONObject(1).getJSONArray("relacion"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setPosicionTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setPosicion(jsonObject, Posicion.BANCO);

        try {
            assertEquals(Posicion.BANCO,jsonObject.getJSONArray(Constants.UBICACION).get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONutils.setPosicion(jsonObject, Posicion.PANADERIA);

        try {
            assertEquals(Posicion.PANADERIA,jsonObject.getJSONArray(Constants.UBICACION).get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void setNombreTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setNombre(jsonObject,"Manzana Rica","Mela rica","es","it");

        assertEquals("Mela rica",JSONutils.getNombre(jsonObject,"it"));
    }

    @Test
    public void setTipoTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setTipo(jsonObject,6);

        assertEquals(6,JSONutils.getTipo(jsonObject));
    }

    @Test
    public void setImagenTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setImagen(jsonObject,"img","url","pushKey");

        try {
            assertEquals("url",jsonObject.getJSONObject("imagen").getString("urlFoto"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getImagenTest(){
        JSONObject jsonObjectActual = createPictograms(474,"es","manzana","apple",2);
        //Test picto original
        try {
            JSONObject jsonObjectExpected = new JSONObject().put("type", 2).put("picto", "ic_action_previous");
            assertEquals(jsonObjectExpected.getInt("type"),JSONutils.getImagen(jsonObjectActual).getInt("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Test picto editado
        try {
            JSONutils.setImagen(jsonObjectActual,"img","url","pushKey");
            JSONObject jsonObjectExpected = new JSONObject().put("type", 1).put("pictoEditado", "img");
            assertEquals(jsonObjectExpected.getInt("type"),JSONutils.getImagen(jsonObjectActual).getInt("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Test null
        try {
            jsonObjectActual.remove("imagen");
            JSONObject jsonObjectExpected = new JSONObject().put("type", 3);
            assertEquals(jsonObjectExpected.getInt("type"),JSONutils.getImagen(jsonObjectActual).getInt("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void desvincularJsonTest() {
        JSONObject jsonObject = createPictograms(643,"es","Yo","I",1);
        try {
            int lenghtExpected = jsonObject.getJSONArray("relacion").length();
            JSONutils.desvincularJson(jsonObject,118);
            assertEquals(lenghtExpected-1,jsonObject.getJSONArray("relacion").length());

            //Test without relacion
            jsonObject.remove("relacion");
            JSONutils.desvincularJson(jsonObject,1);
            assertNull(jsonObject.getJSONObject("relacion"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setHorarioTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setHorario(jsonObject, Horario.MANANA);
        try {
            assertEquals(Horario.MANANA,jsonObject.getJSONArray(Constants.HORA).get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONutils.setHorario(jsonObject, Horario.MEDIODIA);
        try {
            assertEquals(Horario.MEDIODIA,jsonObject.getJSONArray(Constants.HORA).get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setSexoTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setSexo(jsonObject, Sexo.MASCULINO);
        try {
            assertEquals(Sexo.MASCULINO,jsonObject.getJSONArray(Constants.SEXO).get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONutils.setSexo(jsonObject, Sexo.BINARIO);
        try {
            assertEquals(Sexo.BINARIO,jsonObject.getJSONArray(Constants.SEXO).get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setEdadTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setEdad(jsonObject, Edad.ADULTO);
        try {
            assertEquals(Edad.ADULTO,jsonObject.getJSONArray(Constants.EDAD).get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONutils.setEdad(jsonObject, Edad.NINO);
        try {
            assertEquals(Edad.NINO,jsonObject.getJSONArray(Constants.EDAD).get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setClimaTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setClima(jsonObject, Clima.LLUVIA);
        try {
            assertEquals(Clima.LLUVIA,jsonObject.getJSONArray(Constants.CLIMA).get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONutils.setClima(jsonObject, Clima.DESPEJADO);
        try {
            assertEquals(Clima.DESPEJADO,jsonObject.getJSONArray(Constants.CLIMA).get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            JSONArray jsonArray = new JSONArray();
            JSONObject relatedId1 = new JSONObject().put("id",22);
            JSONObject relatedId2 = new JSONObject().put("id",118);
            JSONObject relatedId3 = new JSONObject().put("id",474);
            jsonArray.put(relatedId1).put(relatedId2).put(relatedId3);
            jsonObject.put("relacion",jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }



    private JSONArray createJSONArray(){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(createPictograms(643,"es","Yo","I",1));
        jsonArray.put(createPictograms(22,"es","quiero","want",3));
        jsonArray.put(createPictograms(118,"es","comer","eat",3));
        jsonArray.put(createPictograms(474,"es","manzana","apple",2));
        return jsonArray;
    }






}