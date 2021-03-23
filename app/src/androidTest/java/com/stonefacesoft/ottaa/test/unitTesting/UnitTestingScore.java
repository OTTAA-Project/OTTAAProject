package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.stonefacesoft.ottaa.utils.Games.CalculaPuntos;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Gonzalo Juarez
 * @version 0.1
 *<h1> Description </h1>
 * This class was created in order to make a testing about the score class
 * */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class UnitTestingScore {
    private Context context = ApplicationProvider.getApplicationContext();
    private CalculaPuntos score;
    /**
     * Declaration
     * */
    @Before
    public  void createGame(){
       score=new CalculaPuntos();
    }
    /**
     * Testing block
     * */
    @Test
    public void UnitTestingPhrases(){
        score.sumarCantidadVecesCorrectas();
        score.sumarCantidVecesIncorretas();
        score.sumarCantidVecesIncorretas();
        score.sumarCantidadVecesCorrectas();
        Log.e("TAG", "UnitTestingPhrases: "+score.calcularValor() );
        score.sumarCantidadVecesCorrectas();
        score.sumarCantidadVecesCorrectas();
        score.sumarCantidadVecesCorrectas();
        Log.e("TAG", "UnitTestingPhrases: "+score.calcularValor() );
        System.out.println(" This is the score"+score.calcularValor());


    }
}
