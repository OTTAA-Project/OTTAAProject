package com.stonefacesoft.ottaa.test;

import android.content.SharedPreferences;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.Principal;
import com.stonefacesoft.ottaa.test.utils.JsonTesting;

import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class CreateGroup {
   private Principal principal;
   private JsonTesting testing;
   private Json json;
   SharedPreferences preferences;

   @Before
   public void createClass(){
       principal=Robolectric.buildActivity(Principal.class).create().resume().get();
       testing=new JsonTesting();
       json=testing.getJson();
       json.setmContext(principal);

   }

   @Test
   public  void  runTesting(){
       json.setmJSONArrayTodosLosGrupos(new JSONArray());
       json.crearGrupo("hola","hello","",0);
       Assert.assertTrue(json.getmJSONArrayTodosLosGrupos().length()==1);
   }
}
