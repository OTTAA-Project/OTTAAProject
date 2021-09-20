package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.test.Components.Groups;
import com.stonefacesoft.ottaa.test.Components.Pictograms;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)

public class UnitTestingGroups extends TestCase {
    private final Context mContext= ApplicationProvider.getApplicationContext();
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

    }

    private void createObjects(){
        group1=groups.createGroup(0,"es","animales","animals",0);
        group2=groups.createGroup(1,"es","conjunciones","conjunctions",0);
        group3=groups.createGroup(2,"es","animales","animals",0);
        groupAll=groups.createGroup(24,"es","Todo","all",0);

        picto0=pictograms.generatePictogram(0,"es","yo","I",1);
        picto1=pictograms.generatePictogram(1,"es","Quiero","Want",3);
        picto2=pictograms.generatePictogram(2,"es","Jugar con","play with",3);
        picto3=pictograms.generatePictogram(3,"es","juguete","toy",2);

        json.getmJSONArrayTodosLosGrupos().put(group1);
        json.getmJSONArrayTodosLosGrupos().put(group2);
        json.getmJSONArrayTodosLosGrupos().put(group3);
        json.getmJSONArrayTodosLosGrupos().put(groupAll);

        json.getmJSONArrayTodosLosPictos().put(picto0);
        json.getmJSONArrayTodosLosPictos().put(picto1);
        json.getmJSONArrayTodosLosPictos().put(picto2);
        json.getmJSONArrayTodosLosPictos().put(picto3);


    }

    @Override
    protected TestResult createResult() {
        return super.createResult();
    }

    @Override
    public TestResult run() {
        return super.run();
    }


}
