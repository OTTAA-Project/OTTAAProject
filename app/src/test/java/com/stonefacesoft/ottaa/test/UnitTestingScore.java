package com.stonefacesoft.ottaa.test;

import android.content.Context;

import com.stonefacesoft.ottaa.utils.Games.CalculaPuntos;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Gonzalo Juarez
 * @version 0.1
 *<h1> Description </h1>
 * This class was created in order to make a testing about the score class
 * */
@RunWith(JUnit4.class)
public class UnitTestingScore extends TestCase {
    private Context context;
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
        score.sumarCantidadVecesCorrectas();//1
        score.sumarCantidVecesIncorretas();//01
        score.sumarCantidVecesIncorretas();//02
        score.sumarCantidadVecesCorrectas();//2
        score.sumarCantidadVecesCorrectas();//3
        score.sumarCantidadVecesCorrectas();//4
        score.sumarCantidadVecesCorrectas();//5
        System.out.println(" This is the score"+score.getResult());
        String text=score.getAciertos()+"";
        Assert.assertTrue(score.getAciertos()==5);
        Assert.assertTrue(score.getDesaciertos()==2);
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
