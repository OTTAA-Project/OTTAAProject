package com.stonefacesoft.ottaa.welcome.unitTesting;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.stonefacesoft.ottaa.utils.Games.CalculaPuntos;
import com.stonefacesoft.ottaa.utils.Games.Juego;
import com.stonefacesoft.ottaa.welcome.SplashActivity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
/**
 * @author Gonzalo Juarez
 * @version 0.1
 *<h1> Description </h1>
 * This class was created in order to make a testing about the score class
 * */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestingScore {
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
