package com.stonefacesoft.ottaa;

import org.junit.Assert;
import org.junit.Test;

public class NumberPickerPreferenceTest {
    private final NumberPickerPreference numberPickerPreference = new NumberPickerPreference();

   @Test
    public void setNumberPicker(){
       numberPickerPreference.setmPicker(0,10);
       Assert.assertTrue(numberPickerPreference.getmNumberMax() == 10 && numberPickerPreference.getmNumberMin() == 0);
   }
   @Test
    public void setMax(){
       numberPickerPreference.setmNumberMax(10);
       Assert.assertTrue(numberPickerPreference.getmNumberMax() == 10);
   }
   @Test
    public void setMin(){
       numberPickerPreference.setmNumberMin(0);
       Assert.assertTrue(numberPickerPreference.getmNumberMin() == 0);
   }

}