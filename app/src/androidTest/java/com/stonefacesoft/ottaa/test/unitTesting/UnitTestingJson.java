package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class UnitTestingJson extends TestCase {
    Context context= ApplicationProvider.getApplicationContext();
    private Json json;
    private JSONArray pictos,Grupos;
    private JSONArray relacion;

    @Test
    public void JsonTesting(){
        json=Json.getInstance();
        json.setmContext(context);
        try {
            json.initJsonArrays();

            pictos=json.getmJSONArrayTodosLosPictos();
            //json.crearGrupo("hola","hello","",0);

            Log.e("TAG", "JsonTesting: "+json.getmJSONArrayTodosLosGrupos().toString() );
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("TAG", "JsonTesting: error" );
        } catch (FiveMbException e) {
            e.printStackTrace();
        }
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
