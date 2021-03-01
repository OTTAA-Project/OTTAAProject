package com.stonefacesoft.ottaa.welcome.Components;

import com.stonefacesoft.ottaa.JSONutils.Json;

import org.json.JSONObject;

public class Games {
    private Json json;
    public Games(Json json){
        this.json=json;
    }

    public JSONObject createGame(int id,int levelId){
        JSONObject game=new JSONObject();
        return game;
    }

}
