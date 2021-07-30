package com.stonefacesoft.ottaa.customComponents;

import com.stonefacesoft.ottaa.R;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

public class Custom_ImageDescriptionTest {
    Custom_ImageDescription imageDescription = new Custom_ImageDescription("Casa","Esta es una casa", R.drawable.ic_casa,R.color.color_silver,R.color.colorGrayTransparent);

    @Test
    public void getDescription() {
        assertEquals("Esta es una casa",imageDescription.getDescription());
    }

    @Test
    public void getIdImage() {
        assertEquals(R.drawable.ic_casa,imageDescription.getIdImage());
    }

    @Test
    public void getTitle() {
        assertEquals("Casa",imageDescription.getTitle());
    }

    @Test
    public void getBackgroundColor() {
        assertEquals(R.color.color_silver,imageDescription.getBackgroundColor());
    }

    @Test
    public void getTextColor() {
        assertEquals(R.color.colorGrayTransparent,imageDescription.getTextColor());
    }
}