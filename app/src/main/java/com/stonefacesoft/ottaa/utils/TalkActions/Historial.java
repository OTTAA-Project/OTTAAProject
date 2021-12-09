package com.stonefacesoft.ottaa.utils.TalkActions;

import android.util.Log;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.NLG;
import com.stonefacesoft.ottaa.utils.JSONutils;

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


}
