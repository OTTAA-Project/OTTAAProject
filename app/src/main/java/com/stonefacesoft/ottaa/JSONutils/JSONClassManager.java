package com.stonefacesoft.ottaa.JSONutils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONClassManager {

    public JSONClassManager(){

    }

    public JSONObject createJsonObject(){
        JSONObject object=new JSONObject();
        return object;
    }
    /**
     * Insert an string object
     * */
    public void JsonObjectloadString(JSONObject object,String name,String value,String className){
        try {
            object.put(name,value);
        } catch (JSONException e) {
            Log.e(className, "JsonObjectloadString: "+ e.getMessage() );
        }
    }
    /**
     *
     *
     */
    public void JsonObjectloadInt(JSONObject object,String name,int value,String className){
        try {
            object.put(name,value);
        } catch (JSONException e) {
            Log.e(className, "JsonObjectloadString: "+ e.getMessage() );
        }
    }
    /**
     *
     * */
    public void JsonObjectloadValue(JSONObject object,String name,boolean value,String className){
        try {
            object.put(name,value);
        } catch (JSONException e) {
            Log.e(className, "JsonObjectloadString: "+ e.getMessage() );
        }
    }
    /**
     *
     * */
    public void JsonObjectloadObject(JSONObject object,String name,Object value,String className){
        try {
            object.put(name,value);
        } catch (JSONException e) {
            Log.e(className, "JsonObjectloadString: "+ e.getMessage() );
        }
    }
    /**
     *
     * */
    public JSONObject JsonObjectGetJsonObject(JSONObject object,String name,Object value,String className){
        try {
            return  object.getJSONObject(name);
        } catch (JSONException e) {
            Log.e(className, "JsonObjectloadString: "+ e.getMessage() );
        }
        return createJsonObject();
    }
    /**
     *
     * */
    public String JsonObjectGetString(JSONObject object,String value,String className){
        try {
            return object.getString(value);
        } catch (JSONException e) {
            Log.e(className, "JsonObjectGetString: "+e.getMessage() );
        }
        return "";
    }
    /**
     *
     * */
    public JSONObject JsonObjectGetJSONObject(JSONObject object,String value,String className){
        try {
            return   object.getJSONObject(value);
        } catch (JSONException e) {
            Log.e(className, "JsonObjectGetJSONObject: "+ e.getMessage() );
        }
        return null;
    }
    /**
     *
     * */
    public int JsonObjectGetInt(JSONObject object,String value,String className){
        try {
            return object.getInt(value);
        } catch (JSONException e) {
            Log.e(className, "JsonObjectGetString: "+e.getMessage() );
        }
        return -1;
    }

}
