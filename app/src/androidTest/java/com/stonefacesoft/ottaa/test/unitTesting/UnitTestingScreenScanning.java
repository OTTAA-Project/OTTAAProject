package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Principal;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UnitTestingScreenScanning extends TestCase {
    private FirebaseAuth mAuth;
    private final Context mContext= ApplicationProvider.getApplicationContext();
    private SharedPreferences preferences;
    @Rule
    public ActivityTestRule<Principal> testRule= new ActivityTestRule<>(Principal.class);

    @Before
    public void prepareTheTesting(){
        mAuth=FirebaseAuth.getInstance();
        preferences=PreferenceManager.getDefaultSharedPreferences(mContext);
        preferences.edit().putInt("premium",1);

    }

    @Test
    public void screenScanning(){

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
