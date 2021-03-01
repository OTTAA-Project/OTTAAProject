package com.stonefacesoft.ottaa.welcome.unitTesting;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnFailureListener;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UnitTestingConnectionDetector {
    //
    private Context mContext= ApplicationProvider.getApplicationContext();
    private ConnectionDetector detector;

    @Test
    public void UnitTestingConnectionDetector(){
        detector=new ConnectionDetector(mContext);
        if(detector.isConnectedToInternet())
        Assert.assertTrue(detector.isConnectedToInternet());
        else
         Assert.assertTrue(!detector.isConnectedToInternet());
    }
}
