package com.stonefacesoft.ottaa.test.unitTesting;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.LicenciaUsuario;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.preferences.User;
import com.stonefacesoft.ottaa.test.Components.Preferences;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class UnitTestingPremiumUser extends TestCase {
    private final Context mContext= ApplicationProvider.getApplicationContext();
    private FirebaseUtils firebaseUtils;
    private User user;
    private Preferences preferences;
    private LicenciaUsuario licenciaUsuario;
    private BarridoPantalla barridoPantalla;
    @Before
    public void setUpUnitTesting(){
            user=new User(mContext);
            user.connectClient();
            preferences=new Preferences(mContext);
            firebaseUtils=FirebaseUtils.getInstance();
            firebaseUtils.setmContext(mContext);
            firebaseUtils.setUpFirebaseDatabase();
            preferences.editBoolean(Constants.BARRIDO_BOOL,true);
            preferences.editBoolean(Constants.SUGERENCIA_BOOL,true);
            licenciaUsuario=new LicenciaUsuario(mContext);
            //activar Sugerencias
            //activar barrido
            //activar editar pictos

    }
    @Test
    public void runUnitTestingPremiunUser(){

        System.out.println("Preferences:"+ preferences.getPreferences().getInt(Constants.PREMIUM,0));
        if(preferences.getPreferences().getInt(Constants.PREMIUM,0)==0){
            preferences.editBoolean(Constants.BARRIDO_BOOL,false);
            preferences.editBoolean(Constants.SUGERENCIA_BOOL,false);
        }
        if(preferences.getPreferences().getInt(Constants.PREMIUM,0)==1) {
            Assert.assertTrue("IsEnabled", preferences.getPreferences().getBoolean(Constants.BARRIDO_BOOL, true));
            Assert.assertTrue("IsEnabled", preferences.getPreferences().getBoolean(Constants.SUGERENCIA_BOOL, true));
        }
        else {
            Assert.assertTrue("IsEnabled", !preferences.getPreferences().getBoolean(Constants.BARRIDO_BOOL, false));
            Assert.assertTrue("IsEnabled", !preferences.getPreferences().getBoolean(Constants.SUGERENCIA_BOOL, false));
        }
        if(preferences.getPreferences().getInt(Constants.PREMIUM,0)==1)
        inhabilitarUsuario();

    }

    public void inhabilitarUsuario(){
        preferences.editInteger(Constants.PREMIUM,0);
        runUnitTestingPremiunUser();
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
