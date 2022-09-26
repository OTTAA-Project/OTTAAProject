package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.Games.Juego;

import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class UnitTestingGame extends TestCase {
    private final Context mContext= ApplicationProvider.getApplicationContext();
    private FirebaseAuth mAuth;
    private Json json;
    private Juego game;

    @Before
    public void prepareTheTesting(){
        mAuth=FirebaseAuth.getInstance();
        json=Json.getInstance();
        json.setmContext(mContext);
        game=new Juego(mContext,0,1);
    }

    @Test
    public void unitTestingGames(){
        game.incrementCorrect();
        game.incrementCorrect();
        game.incrementWrong();
        game.agregarDatosConsulta();
        Assert.assertTrue("The score has been passed ",game.getScore()>0);
        if(game.getScore()>0)
        System.out.println("The score has been passed :"+game.getScore());
        else
            Assert.fail("The score is empty");
        game.saveJsonObjects();
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
