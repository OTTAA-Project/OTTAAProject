package com.stonefacesoft.ottaa.test.unitTesting;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UnitTestingGoogleTranslate extends TestCase  {

    @Test
     public void unitTestingGoogleTranslate(){

    }

//    private Context mContext= ApplicationProvider.getApplicationContext();
//    private traducirTexto translateText;
//    private SharedPreferences preferences;
//    private String result;
//    private boolean isTheSecondTest;
//    private boolean isTesting = true;
//    @Before
//    public void createUnitTesting(){
//        preferences= PreferenceManager.getDefaultSharedPreferences(mContext);
//        translateText=new traducirTexto(mContext,preferences);
//    }
//
//    @Test
//    public void UnitTestingGoogleTranslate(){
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//            translateText.traducirIdioma(this,"hola","es","en",true);
//            result="hello";
//    }
//
//    @Test
//    public void UnitTestingGoogleTranslate2(){
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        isTheSecondTest = true;
//        translateText.traducirIdioma(this,"uva","es","en",true);
//        result="grape";
//
//    }
//
//    @Override
//    public void onTextoTraducido(boolean traduccion) {
//        if(isTesting)
//            Assert.assertTrue(translateText.getTexto().equals(result));
//        if(!isTheSecondTest)
//            UnitTestingGoogleTranslate2();
//        if(isTheSecondTest)
//            isTesting = false;
//
//    }
//
//    @Override
//    protected TestResult createResult() {
//        return super.createResult();
//    }
//
//    @Override
//    public TestResult run() {
//        return super.run();
//    }
//
//    @Override
//    public int countTestCases() {
//        return testRunning++;
//    }


}
