package com.stonefacesoft.ottaa;

import static org.junit.Assert.assertEquals;

public class NumberPickerPreferenceTest {
    private NumberPickerPreference numberPickerPreference;

    public void setUp() throws Exception {
        numberPickerPreference = new NumberPickerPreference(new Principal(),"casa","casa");
    }

    public void testSetmPicker() {
        numberPickerPreference.setmPicker(0,25);
        assertEquals(true,numberPickerPreference.getmNumberMin()==0&&numberPickerPreference.getmNumberMax()==25);

    }

    public void testSetTipo() {
        numberPickerPreference.setTipo(2);
    }

}