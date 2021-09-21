package com.stonefacesoft.ottaa.idioma;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class ConfigurarIdiomaTest {
    private ConfigurarIdioma configurarIdioma = new ConfigurarIdioma();
    @Test
    public void testSetLanguage() {
        configurarIdioma.setLanguage("en");
        Assert.assertTrue(configurarIdioma.getLanguaje().equals("en"));
    }
    @Test
    public void testGetLanguaje() {
        Assert.assertTrue(configurarIdioma.getLanguaje().equals("en"));
    }
}