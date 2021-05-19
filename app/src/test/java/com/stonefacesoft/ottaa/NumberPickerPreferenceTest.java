package com.stonefacesoft.ottaa;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberPickerPreferenceTest {
    private NumberPickerPreference numberPickerPreference = new NumberPickerPreference(new Principal(),"casa","casa");

    @Test
    public void setUp() throws Exception {
        numberPickerPreference = new NumberPickerPreference(new Principal(),"casa","casa");
    }
    @Test
    public void testSetmPicker() {
        numberPickerPreference.createDialog();
        numberPickerPreference.setmPicker(0,25);
        assertEquals(true,numberPickerPreference.getmNumberMin()==0&&numberPickerPreference.getmNumberMax()==25);

    }
    @Test
    public void testSetTipo() {
        numberPickerPreference.setTipo(2);
    }

}