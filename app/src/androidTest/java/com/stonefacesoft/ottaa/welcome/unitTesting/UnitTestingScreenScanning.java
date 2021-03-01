package com.stonefacesoft.ottaa.welcome.unitTesting;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Principal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UnitTestingScreenScanning {
    private FirebaseAuth mAuth;
    private Context mContext= ApplicationProvider.getApplicationContext();
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
}
