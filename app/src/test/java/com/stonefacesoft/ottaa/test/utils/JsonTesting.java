package com.stonefacesoft.ottaa.test.utils;


import com.stonefacesoft.ottaa.JSONutils.Json;

public class JsonTesting {
    private Json json;
    public JsonTesting(){
        json=Json.getInstance();
    }
    public Json getJson(){
        return json;
    }
}
