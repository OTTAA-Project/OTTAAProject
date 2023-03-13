package com.stonefacesoft.ottaa.test.unitTesting;

import static org.junit.Assert.assertEquals;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.NLG;
import com.stonefacesoft.ottaa.utils.TalkActions.Historial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

public class HistorialTest  {
    private final Json json = Json.getInstance();
    private final Historial historial =  new Historial(Json.getInstance());

    @Test
    public void getListadoPictos() {
       createTestPhrase();
        assertEquals(4, historial.getListadoPictos().size());
    }

    @Test
    public void addPictograma() {
        try {
            historial.addPictograma(createPictogramJSONArray().getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertEquals(1,historial.getListadoPictos().size());
    }

    @Test
    public void removePictogram() {
      createTestPhrase();
      historial.removePictogram();
      assertEquals(historial.getListadoPictos().get(2),historial.getFather());
    }

    @Test
    public void clear() {
        createGrupoJSONArray();
        historial.clear();
        assertEquals(0,historial.getListadoPictos().size());
    }

    @Test
    public void getFather() {
        createTestPhrase();
        System.out.println("Ideal Father : " + historial.getListadoPictos().get(3));
        System.out.println("Real Father : " + historial.getFather());
        assertEquals(historial.getListadoPictos().get(3),historial.getFather());
    }

    @Test
    public void removePictograms() {
        createTestPhrase();
        historial.removePictograms(true);
        assertEquals(0,historial.getListadoPictos().size());
    }


    @Test
    public void talkWithtNLG() {
        historial.clear();
        createTestPhrase();
        assertEquals("I want to eat an apple.",historial.talkWithtNLG());
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
            JSONObject relatedId1 = new JSONObject();
            relatedId1.put("id",22);
            JSONObject relatedId2 = new JSONObject();
            relatedId2.put("id",118);
            JSONObject relatedId3 = new JSONObject();
            relatedId3.put("id",474);
            jsonArray.put(relatedId1);
            jsonArray.put(relatedId2);
            jsonArray.put(relatedId3);
            jsonObject.put("relacion",jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONArray createPictogramJSONArray(){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(createPictograms(643,"es","Yo","I",1));
        jsonArray.put(createPictograms(22,"es","quiero","want",3));
        jsonArray.put(createPictograms(118,"es","comer","eat",3));
        jsonArray.put(createPictograms(474,"es","manzana","apple",2));
        return jsonArray;
    }

    private ArrayList< JSONObject> createListOfPictograms(ArrayList<JSONObject> list){

            ArrayList<JSONObject> listado = null;
            if(list == null)
                listado = new ArrayList<>();
        return listado;
    }
    private ArrayList<JSONObject> addChild(ArrayList arrayList,JSONObject object){
        arrayList.add(object);
        return arrayList;
    }




    private JSONArray createGrupoJSONArray(){
        try {
            return new JSONArray("[{\"id\":0,\"texto\":{\"en\":\"Actions\",\"es\":\"ACCIONES\"},\"tipo\":0,\"imagen\":{\"picto\":\"verbos\"},\"relacion\":[{\"id\":1,\"texto\":{\"en\":\"escort\",\"es\":\"acompañar\"},\"tipo\":3,\"imagen\":{\"picto\":\"ic_acompanar\"},\"relacion\":[],\"agenda\":0,\"gps\":0},{\"id\":2,\"texto\":{\"en\":\"turn off\",\"es\":\"apagar\"},\"tipo\":3,\"imagen\":{\"picto\":\"ic_apagar_television\"},\"relacion\":[{\"id\":1016,\"frec\":2},{\"id\":1019,\"frec\":1},{\"id\":773,\"frec\":2},{\"id\":774,\"frec\":2}],\"agenda\":0,\"gps\":0},{\"id\":3,\"texto\":{\"en\":\"turn the volume down\",\"es\":\"bajar volumen\"},\"tipo\":3,\"imagen\":{\"picto\":\"ic_volumen_menos\"},\"relacion\":[],\"agenda\":0,\"gps\":0},{\"id\":4,\"texto\":{\"en\":\"erase\",\"es\":\"borrar\"},\"tipo\":3,\"imagen\":{\"picto\":\"ic_borrar\"},\"relacion\":[],\"agenda\":0,\"gps\":0}],\"frecuencia\":1},{\"id\":1,\"texto\":{\"en\":\"Adjectives\",\"es\":\"ADJETIVOS\"},\"tipo\":0,\"imagen\":{\"picto\":\"descripcion\"},\"relacion\":[{\"id\":118,\"frec\":1},{\"id\":121,\"frec\":1},{\"id\":122,\"frec\":1}],\"frecuencia\":1}]");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createTestPhrase(){
        try {
            historial.addPictograma(createPictogramJSONArray().getJSONObject(0));
            historial.addPictograma(createPictogramJSONArray().getJSONObject(1));
            historial.addPictograma(createPictogramJSONArray().getJSONObject(2));
            historial.addPictograma(createPictogramJSONArray().getJSONObject(3));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public NLG createNLG(){
        NLG nlg = new NLG();
        return nlg;
    }
}