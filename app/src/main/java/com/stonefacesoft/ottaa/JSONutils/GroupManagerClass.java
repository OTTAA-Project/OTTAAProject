package com.stonefacesoft.ottaa.JSONutils;

import org.json.JSONArray;

/**
 *
 * */
public class GroupManagerClass {
    private static volatile GroupManagerClass _GroupManagerClass;
    private JSONArray mGroup;

    private GroupManagerClass(){
        this.mGroup=new JSONArray();
    }

    public synchronized static GroupManagerClass getInstance() {
        if (_GroupManagerClass == null) {

            synchronized (Json.class) {
                //chequeamos por segunda vez si la instancia no es nula
                //Si no existe una instancia disponible  , creamos una
                if (_GroupManagerClass == null) {
                    _GroupManagerClass = new GroupManagerClass();
                }
            }
        }

        return _GroupManagerClass;
    }

    public void setmGroup(JSONArray mGroup) {
        this.mGroup = mGroup;
    }

    public JSONArray getmGroup() {
        return mGroup;
    }
}
