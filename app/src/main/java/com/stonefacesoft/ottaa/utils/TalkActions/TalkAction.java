package com.stonefacesoft.ottaa.utils.TalkActions;

import android.content.Context;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.NLG;

import org.json.JSONObject;

import java.util.ArrayList;

public class TalkAction {
    private boolean useNlg;
    private NLG  nlg;
    private final Context mContext;
    private ArrayList<JSONObject> history;
    private String Phrase;
    private final Json json;

    public TalkAction(Context mContext,Json json){
        this.mContext=mContext;
        this.json=json;
    }

    public void loadHistory(ArrayList<JSONObject> history){
        this.history=history;
    }

    public void talkWithNlg(){

    }


    public String getPhrase() {
        return Phrase;
    }
    public void borrarDeAuno(){
        if(history.size()>0)
        history.remove(history.size()-1);
    }
    public void eraseAll(){
        history.clear();
    }
    public JSONObject getParent(){
        if(history.size()>0)
            return history.get(history.size()-1);
        return json.getPictoFromId2(0);
    }
}
