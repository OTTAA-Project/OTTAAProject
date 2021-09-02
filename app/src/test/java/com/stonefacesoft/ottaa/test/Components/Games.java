package com.stonefacesoft.ottaa.test.Components;

import com.stonefacesoft.ottaa.JSONutils.Json;

import org.json.JSONObject;

public class Games {
    private final Json json;
    public Games(Json json){
        this.json=json;
    }

    public JSONObject createGame(int id,int levelId){
        JSONObject game=new JSONObject();
        return game;
    }

}
