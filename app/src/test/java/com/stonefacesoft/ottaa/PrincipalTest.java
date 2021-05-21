package com.stonefacesoft.ottaa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class PrincipalTest  {

    private Principal principal = new Principal();

    @Test
    public void cargarOracion() {
        JSONArray array =createPictogramJSONArray();
        try {
            principal.CargarOracion(array.getJSONObject(0),"es");
            principal.CargarOracion(array.getJSONObject(1),"es");
            principal.CargarOracion(array.getJSONObject(2),"es");
            principal.CargarOracion(array.getJSONObject(3),"es");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertEquals("Yo quiero comer manzana  ",principal.getOracion());
    }

    @Test
    public void cargarSeleccion() {

    }


    @Test
    public void setOracion() {
        String oracion = "hello";
        principal.setOracion(oracion);
        assertEquals("hello",principal.getOracion());
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

    private ArrayList<JSONObject> createListOfPictograms(ArrayList<JSONObject> list){

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


    public NLG createNLG(){
        NLG nlg = new NLG();
        return nlg;
    }
}