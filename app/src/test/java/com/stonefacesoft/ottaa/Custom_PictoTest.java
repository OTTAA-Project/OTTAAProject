package com.stonefacesoft.ottaa;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

public class Custom_PictoTest  {
    private final Custom_Picto custom_picto = new Custom_Picto(null);

    @Test
    public void setCustom_Color() {
        custom_picto.setCustom_Color(R.color.color_midnight_blue);
        assertEquals(R.color.color_midnight_blue,custom_picto.getCustom_Color());
    }

    @Test
    public void setCustom_Texto() {
        custom_picto.setCustom_Texto("hello");
        assertEquals("hello",custom_picto.getCustom_Texto());
    }

    @Test
    public void getCustom_Texto() {
        custom_picto.setCustom_Texto("hi");
        assertEquals("hi",custom_picto.getCustom_Texto());
    }

    @Test
    public void getCustom_Color() {
        custom_picto.setCustom_Color(R.color.DarkRed);
        assertEquals(R.color.DarkRed,custom_picto.getCustom_Color());
    }


    @Test
    public void setIdPictogram() {
        custom_picto.setIdPictogram(5);
        custom_picto.setIdPictogram(7);
        custom_picto.setIdPictogram(9);
        custom_picto.setIdPictogram(20);
        assertEquals(20,custom_picto.getIdPictogram());
    }

    @Test
    public void getIdPictogram() {
        custom_picto.setIdPictogram(5);
        custom_picto.setIdPictogram(7);
        custom_picto.setIdPictogram(9);
        custom_picto.setIdPictogram(14);
        custom_picto.setIdPictogram(95);
        custom_picto.setIdPictogram(25);
        assertEquals(25,custom_picto.getIdPictogram());
    }

    @Test
    public void setCustom_description() {
        custom_picto.setCustom_description("hello");
        custom_picto.setCustom_description("hi");
        custom_picto.setCustom_description("hola");
        assertEquals("hola",custom_picto.getCustom_description());
    }

    @Test
    public void getCustom_description() {
        custom_picto.setCustom_description("hello");
        custom_picto.setCustom_description("hi");
        custom_picto.setCustom_description("hola");
        custom_picto.setCustom_description("want");
        assertEquals("want",custom_picto.getCustom_description());
    }
}