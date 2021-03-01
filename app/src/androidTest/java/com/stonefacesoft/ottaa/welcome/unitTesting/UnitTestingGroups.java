package com.stonefacesoft.ottaa.welcome.unitTesting;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.welcome.Components.Groups;
import com.stonefacesoft.ottaa.welcome.Components.Pictograms;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class UnitTestingGroups {
    private Context mContext= ApplicationProvider.getApplicationContext();
    private Groups groups;
    private Pictograms pictograms;
    private Json json;
    private JSONObject group1,group2,group3,groupAll;
    private JSONObject picto0,picto1,picto2,picto3,picto4,picto5;


    @Before
    public void createClass(){
        json=Json.getInstance();
        json.setmContext(mContext);
        groups=new Groups(json);
        pictograms=new Pictograms(mContext,json);

    }

    @Test
    public void UnitTestingGroups(){
        createObjects();
        try {
            groups.joinGroups(json.getId(group1),json.getId(picto3));
            groups.joinGroups(json.getId(group2),json.getId(picto0));
            groups.joinGroups(json.getId(group3),json.getId(picto1));
            groups.joinGroups(json.getId(group3),json.getId(picto0));
            groups.joinGroups(json.getId(group3),json.getId(picto2));
            Log.e("TAG", "UnitTestingGroups: "+json.getmJSONArrayTodosLosGrupos().toString() );

            groups.deleteRelationship(group3,json.getId(picto2));
            Log.e("TAG", "UnitTestingGroups: "+json.getmJSONArrayTodosLosGrupos().toString() );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createObjects(){
        group1=groups.createGroup(0,"es","animales","animals",0);
        group2=groups.createGroup(1,"es","conjunciones","conjunctions",0);
        group3=groups.createGroup(2,"es","animales","animals",0);
        groupAll=groups.createGroup(24,"es","Todo","all",0);

        picto0=pictograms.createPictograms(0,"es","yo","I",1);
        picto1=pictograms.createPictograms(1,"es","Quiero","Want",3);
        picto2=pictograms.createPictograms(2,"es","Jugar con","play with",3);
        picto3=pictograms.createPictograms(3,"es","juguete","toy",2);

        json.getmJSONArrayTodosLosGrupos().put(group1);
        json.getmJSONArrayTodosLosGrupos().put(group2);
        json.getmJSONArrayTodosLosGrupos().put(group3);
        json.getmJSONArrayTodosLosGrupos().put(groupAll);

        json.getmJSONArrayTodosLosPictos().put(picto0);
        json.getmJSONArrayTodosLosPictos().put(picto1);
        json.getmJSONArrayTodosLosPictos().put(picto2);
        json.getmJSONArrayTodosLosPictos().put(picto3);


    }

}
