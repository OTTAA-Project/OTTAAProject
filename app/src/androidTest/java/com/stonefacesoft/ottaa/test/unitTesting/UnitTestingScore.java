package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.stonefacesoft.ottaa.utils.Games.CalculaPuntos;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Gonzalo Juarez
 * @version 0.1
 *<h1> Description </h1>
 * This class was created in order to make a testing about the score class
 * */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class UnitTestingScore extends TestCase {
    private final Context context = ApplicationProvider.getApplicationContext();
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
    public void UnitTestingScore(){
        score.sumarCantidadVecesCorrectas();//1
        score.sumarCantidVecesIncorretas();//01
        score.sumarCantidVecesIncorretas();//02
        score.sumarCantidadVecesCorrectas();//2
        Log.e("TAG", "UnitTestingPhrases: "+score.getResult() );
        score.sumarCantidadVecesCorrectas();//3
        score.sumarCantidadVecesCorrectas();//4
        score.sumarCantidadVecesCorrectas();//5
        Log.e("TAG", "UnitTestingPhrases: "+score.getResult() );
        System.out.println(" This is the score"+score.getResult());
        String text=score.getAciertos()+"";
        assertThat(score.getAciertos(),is(5));
        assertThat(score.getDesaciertos(),is(2));
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
