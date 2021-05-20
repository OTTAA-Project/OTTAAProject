package com.stonefacesoft.ottaa;

import org.junit.Test;

public class NumberPickerPreferenceTest {
    private NumberPickerPreference numberPickerPreference = new NumberPickerPreference(new Principal(),"casa","casa");

    @Test
    public void setUp() throws Exception {
        numberPickerPreference = new NumberPickerPreference(new Principal(),"casa","casa");
    }

    @Test
    public void testSetTipo() {
        numberPickerPreference.setTipo(2);
    }

}