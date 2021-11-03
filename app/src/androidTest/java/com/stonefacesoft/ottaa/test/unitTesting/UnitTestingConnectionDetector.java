package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.stonefacesoft.ottaa.utils.ConnectionDetector;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class UnitTestingConnectionDetector extends TestCase {
    //
    private final Context mContext= ApplicationProvider.getApplicationContext();
    private ConnectionDetector detector;

    @Test
    public void UnitTestingConnectionDetector(){
        detector=new ConnectionDetector(mContext);
        if(detector.isConnectedToInternet())
        Assert.assertTrue(detector.isConnectedToInternet());
        else
         Assert.assertTrue(!detector.isConnectedToInternet());

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
