package com.stonefacesoft.ottaa;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FavModelTest {

    private final FavModel favModel =  new FavModel();
    @Test
    public void testGetTexto() {
        favModel.setTexto("hello");
        assertEquals("hello",favModel.getTexto());
    }

    @Test
    public void testSetTexto() {
        favModel.setTexto("hello");
        assertNotNull(favModel.getTexto());
    }
}