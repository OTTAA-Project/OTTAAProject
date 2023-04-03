package com.stonefacesoft.ottaa.utils.TalkActions;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.NLG;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.RemoteConfigUtils;
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



    private ArrayList<JSONObject> listOfPictograms = new ArrayList<>();
    private JSONObject father;
    private final Json json;
    private final NLG nlg;
    private final String TAG = "Historial";
    private String chatGPTPrompt = "I am an {AGE} {SEX} and I need to nlg the following phrase or word '{PHRASE}' once without adding extra words, the result has to be in {LANG}, first person, without extra words and as precise as possible";


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

    public boolean hasPictogram(JSONObject object){
        return listOfPictograms.toString().contains(object.toString());
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
                String srcAux = JSONutils.getNombre(listOfPictograms.get(i),ConfigurarIdioma.getLanguaje()).toLowerCase();
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
            String promt=chatGPTPrompt.replace("{AGE}",json.obtenerEdad().toLowerCase().replace("nino","Niño")).replace("{SEX}",json.obtenerSexo()).replace("{PHRASE}",text).replace("{LANG}",ConfigurarIdioma.getNormalLanguage());
            Log.d(TAG, "chatGptObject: "+ promt);
            object.put("model","text-davinci-003");
            object.put("prompt",promt);
            object.put("temperature",0);
            object.put("max_tokens",word.length()*10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public void downloadPromt(){
       // chatGPTPrompt = RemoteConfigUtils.getInstance().getmFirebaseRemoteConfig().getString("ChatGPTPrompt");
        Log.d(TAG, "downloadPromt: "+ chatGPTPrompt);
        DatabaseReference ref = FirebaseUtils.getInstance().getmDatabase();
        ref.child("ChatGPTPrompt").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(ConfigurarIdioma.getLanguaje())){
                snapshot.child(ConfigurarIdioma.getLanguaje()).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chatGPTPrompt = snapshot.getValue().toString();
                        Log.e(TAG, "Download Prompt: "+ chatGPTPrompt );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
