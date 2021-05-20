package com.stonefacesoft.ottaa;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NumberPickerPreferenceTest {
    private Context mContext= ApplicationProvider.getApplicationContext();
    private NumberPickerPreference numberPickerPreference = new NumberPickerPreference(mContext,"casa","casa");

    @Before
    public void setUp() throws Exception {
        numberPickerPreference = new NumberPickerPreference(mContext,"casa","casa");

    }
    @Test
    public void testSetmPicker() {
        numberPickerPreference.createDialog();
        numberPickerPreference.setmPicker(0,25);
        assertEquals(true,numberPickerPreference.getmNumberMin()==0&&numberPickerPreference.getmNumberMax()==25);
    }


    @Test
    public void testSetTipo() {
        numberPickerPreference.createDialog();
        numberPickerPreference.setTipo(2);
        assertEquals(2,numberPickerPreference.getTipo());
    }

}