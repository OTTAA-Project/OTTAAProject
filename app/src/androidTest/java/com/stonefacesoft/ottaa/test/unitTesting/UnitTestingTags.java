package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.stonefacesoft.ottaa.AsignTags;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.Prediction.Edad;
import com.stonefacesoft.ottaa.Prediction.Horario;
import com.stonefacesoft.ottaa.Prediction.Posicion;
import com.stonefacesoft.ottaa.test.Components.Groups;
import com.stonefacesoft.ottaa.test.Components.Pictograms;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;



@RunWith(JUnit4.class)
public class UnitTestingTags extends TestCase {
    private Context mContext= ApplicationProvider.getApplicationContext();
    private Groups group;
    private Pictograms pictograms;
    private Json json;
    private AsignTags tags;
    private JSONObject picto0,picto1,picto2,picto3;
    private JSONObject grupo;

    @Before
    public  void prepareTesting(){
        json=Json.getInstance();
        json.setmContext(mContext);
        group=new Groups(json);
        pictograms=new Pictograms(mContext,json);
        tags=new AsignTags(mContext);
    }

    @Test public void runTagsTesting(){
        picto0 = pictograms.createPictograms(0,"es","yo","I",1);
        picto1 = pictograms.createPictograms(1,"es","Quiero","Want",3);
        picto2 = pictograms.createPictograms(2,"es","Jugar con","play with",3);
        picto3 = pictograms.createPictograms(3,"es","juguete","toy",2);

        json.setEdad(picto0, Edad.JOVEN);
        json.setHorario(picto0, Horario.TARDE);
        json.setPosicion(picto0, Posicion.CASA);

        pictograms.relacionarObjeto(picto1,picto0);
        pictograms.relacionarObjeto(picto1,picto2);
        pictograms.relacionarObjeto(picto1,picto3);
        pictograms.relacionarObjeto(picto1,picto3);
        pictograms.relacionarObjeto(picto1,picto3);
        pictograms.relacionarObjeto(picto1,picto3);



        json.getmJSONArrayTodosLosPictos().put(picto0);
        json.getmJSONArrayTodosLosPictos().put(picto1);
        json.getmJSONArrayTodosLosPictos().put(picto2);
        json.getmJSONArrayTodosLosPictos().put(picto3);

        JSONArray array=pictograms.ordenarObjetos(picto1);
        System.out.println(pictograms.ordenarObjetos(picto1));

        try {
            assertThat(json.getId(array.getJSONObject(0)),is(3));
        } catch (JSONException e) {
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
