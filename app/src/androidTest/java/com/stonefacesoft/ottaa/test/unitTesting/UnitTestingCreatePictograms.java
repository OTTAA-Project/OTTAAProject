package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.stonefacesoft.ottaa.test.JUnitSuiteClasses.testRunning;


@RunWith(AndroidJUnit4.class)
public class UnitTestingCreatePictograms extends TestCase {
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
        if(mJsonArrayAllGroups==null)
            mJsonArrayAllGroups = new JSONArray();
        if(mJsonArrayAllPictograms == null)
            mJsonArrayAllPictograms = new JSONArray();
        json.setmJSONArrayTodosLosPictos(mJsonArrayAllPictograms);
        json.setmJSONArrayTodosLosGrupos(mJsonArrayAllGroups);
        addGroup();
    }
    @Test
    public void UnitTestingCreatePictograms(){
        try {
              mJsonArrayAllGroups=json.getmJSONArrayTodosLosGrupos();
              int idPadre=json.getId(mJsonArrayAllGroups.getJSONObject(0));
              //json.crearPicto(mJsonArrayAllGroups,json.getmJSONArrayTodosLosPictos(),idPadre,"hijo","son","ic_abeja",1,"","");
              idPadre=json.getId(mJsonArrayAllGroups.getJSONObject(1));
              //json.crearPicto(mJsonArrayAllGroups,json.getmJSONArrayTodosLosPictos(),idPadre,"primo","cousin","ic_logo",1,"","");
              json.setmJSONArrayTodosLosGrupos(mJsonArrayAllGroups);
              String text=getNombre(json.getmJSONArrayTodosLosPictos().getJSONObject(0),"es");
              Assert.assertTrue("Locale Pictograms",text!=null);
              text=getNombre(json.getmJSONArrayTodosLosPictos().getJSONObject(0),"en");
              Assert.assertTrue("English locale",text!=null);
              Assert.assertTrue(json.getHijosGrupo2(0)!=null);
         //     Assert.assertTrue("Group has child?",json.getHijosGrupo2(0).length()>0);

        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("Json object exception in the class addPictogram");
        }
    }


    public void preparePictograms(){

    }
    public String getNombre(JSONObject object, String idioma) {
        String name = null;
        try {
            name = object.getJSONObject("texto").getString(idioma);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Json object exception in the class addPictogram at line 95");
        }
        return name;
    }

    @Override
    protected TestResult createResult() {
        return super.createResult();
    }

    @Override
    public TestResult run() {
        return super.run();
    }

    @Override
    public int countTestCases() {
        return testRunning++;
    }
}
