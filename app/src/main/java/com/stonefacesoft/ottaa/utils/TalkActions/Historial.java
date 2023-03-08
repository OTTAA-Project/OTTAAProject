package com.stonefacesoft.ottaa.utils.TalkActions;

import android.util.Log;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.NLG;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.preferences.DataUser;
import com.stonefacesoft.ottaa.utils.preferences.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * Make a follow up about the selected pictograms
 */
public class Historial {

    private ArrayList<JSONObject> listOfPictograms;
    private JSONObject father;
    private final Json json;
    private final NLG nlg;
    private final String TAG = "Historial";


    public Historial(Json json) {
        this.json = json;
        listOfPictograms = new ArrayList<>();
        nlg = new NLG();
    }

    public ArrayList<JSONObject> getListadoPictos() {
        return listOfPictograms;
    }
    public void addPictograma(JSONObject object){
        listOfPictograms.add(object);
    }
    public void removePictogram(){
        if(listOfPictograms.size()>0) {
            listOfPictograms.remove(listOfPictograms.size() - 1);
        }
    }


    public void clear(){
        listOfPictograms=new ArrayList<>();
    }



    public JSONObject getFather(){
        try {
            if(!listOfPictograms.isEmpty())
            father=listOfPictograms.get(listOfPictograms.size()-1);
            else
             father=json.getPictoFromId2(0);

        }catch (Exception ex){
            father=json.getPictoFromId2(0);
        }
        return father;
    }

    public JSONObject removePictograms(boolean clearAll){
        if(clearAll)
            clear();
        else
            removePictogram();

        return getFather();
    }

    public String talkWithtNLG(){
        String Phrase="";
        nlg.NuevaFrase();
        for (int i = 0; i < listOfPictograms.size(); i++) {
            nlg.CargarFrase(listOfPictograms.get(i), JSONutils.getTipo(listOfPictograms.get(i)));
        }
        Phrase=nlg.ArmarFrase();
        Log.d(TAG, "talkWithtNLG: "+ Phrase );
        return Phrase;
    }

    public JSONObject nlgObject(){
        JSONObject object = new JSONObject();
        JSONArray word = new JSONArray(),types = new JSONArray();
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < listOfPictograms.size(); i++) {
            try {
                String srcAux = JSONutils.getNombre(listOfPictograms.get(i),"es").toLowerCase();
                text.append(srcAux+" ");
                word.put(i,srcAux);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                types.put(i,JSONutils.getType(listOfPictograms.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(json.useChatGPT())
            object = chatGptObject(word,text.toString());
        else
            object = viterbyObject(word,types);
        return object;
    }

    private JSONObject viterbyObject(JSONArray word,JSONArray types){
        JSONObject object= new JSONObject();
        try {
            object.put("words",word);
            object.put("types",types);
            object.put("language","es");
            object.put("props",new JSONObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private JSONObject chatGptObject(JSONArray word,String text){
        JSONObject object= new JSONObject();
        try {
            object.put("model","text-davinci-001");
            object.put("prompt","soy un "+json.obtenerEdad()+" "+json.obtenerSexo().toLowerCase().replace("nino","niño")+" y necesito aplicar nlg a la siguiente frase o palabra '"+text+"' una sola vez sin agregar palabras de màs,el resultado tiene que ser en español, primera persona, sin palabras de mas y lo mas preciso posible");
            object.put("temperature",0);
            object.put("max_tokens",word.length()*10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }


}
