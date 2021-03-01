package com.stonefacesoft.ottaa.welcome.unitTesting;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.LoginActivity;
import com.stonefacesoft.ottaa.Principal;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UnitTestingCreatePictograms {
    private Context mContext= ApplicationProvider.getApplicationContext();
    private Json json;
    private JSONArray mJsonArrayAllGroups,mJsonArrayAllPictograms;
    private SharedPreferences sharedPreferences;

    @Before
    public void createTest(){
        json=Json.getInstance();
        json.setmContext(mContext);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
        sharedPreferences.edit().putString(mContext.getResources().getString(R.string.str_idioma),"es").apply();
        mJsonArrayAllGroups=json.getmJSONArrayTodosLosGrupos();
        mJsonArrayAllPictograms=json.getmJSONArrayTodosLosPictos();
        addGroup();
    }
    @Test
    public void addPictogram(){
        try {
              mJsonArrayAllGroups=json.getmJSONArrayTodosLosGrupos();
              int idPadre=json.getId(mJsonArrayAllGroups.getJSONObject(0));
              json.crearPicto(mJsonArrayAllGroups,json.getmJSONArrayTodosLosPictos(),idPadre,"hijo","son","ic_abeja",1,"","");
              idPadre=json.getId(mJsonArrayAllGroups.getJSONObject(1));
              json.crearPicto(mJsonArrayAllGroups,json.getmJSONArrayTodosLosPictos(),idPadre,"primo","cousin","ic_logo",1,"","");
              json.setmJSONArrayTodosLosGrupos(mJsonArrayAllGroups);
              System.out.println(json.getmJSONArrayTodosLosPictos().toString());
              System.out.println(json.getmJSONArrayTodosLosGrupos());
              String text=getNombre(json.getmJSONArrayTodosLosPictos().getJSONObject(0),"es");
              Assert.assertTrue("Locale Pictograms",text!=null);
              text=getNombre(json.getmJSONArrayTodosLosPictos().getJSONObject(0),"en");
              Assert.assertTrue("English locale",text!=null);
              Assert.assertTrue("Group has child?",json.getHijosGrupo2(0).length()>0);
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("Json object exception in the class addPictogram");
        }
    }
    public void addGroup(){
        try {
            mJsonArrayAllGroups=json.getmJSONArrayTodosLosGrupos();
            json.crearGrupo(mJsonArrayAllGroups,"animales","animals","Ic_Abeja",0,"","");
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            json.crearGrupo(mJsonArrayAllGroups,"casa","house","Ic_mama",0,"","");
            System.out.println(mJsonArrayAllGroups.toString());
            json.setmJSONArrayTodosLosGrupos(mJsonArrayAllGroups);
        } catch (JSONException e) {

        }
    }

    public void preparePictograms(){

    }
    public String getNombre(JSONObject object, String idioma) {
        try {
            return object.getJSONObject("texto").getString(idioma);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error_Json_getNombre", "2 :" + e.toString());
        }
        return null;
    }
}
