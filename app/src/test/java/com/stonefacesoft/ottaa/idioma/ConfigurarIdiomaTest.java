package com.stonefacesoft.ottaa.idioma;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class ConfigurarIdiomaTest {
    private final ConfigurarIdioma configurarIdioma = new ConfigurarIdioma();
    @Test
    public void testSetLanguage() {
        ConfigurarIdioma.setLanguage("en");
        Assert.assertTrue(ConfigurarIdioma.getLanguaje().equals("en"));
    }
    @Test
    public void testGetLanguaje() {
        Assert.assertTrue(ConfigurarIdioma.getLanguaje().equals("en"));
    }

    @Test
    public void testGetLanguajeEn() {
        ConfigurarIdioma.setLanguage("en");
        Assert.assertTrue(ConfigurarIdioma.getNormalLanguage().equals("english"));
    }
    @Test
    public void testGetLanguajeCa() {
        ConfigurarIdioma.setLanguage("ca");
        Assert.assertTrue(ConfigurarIdioma.getNormalLanguage().equals("catalan"));
    }
    @Test
    public void testGetLanguajePT() {
        ConfigurarIdioma.setLanguage("pt");
        Assert.assertTrue(ConfigurarIdioma.getNormalLanguage().equals("portuguese"));
    }
    @Test
    public void testGetLanguajeEs() {
        ConfigurarIdioma.setLanguage("es");
        Assert.assertTrue(ConfigurarIdioma.getNormalLanguage().equals("spanish"));
    }
    @Test
    public void testGetLanguajeFr() {
        ConfigurarIdioma.setLanguage("fr");
        Assert.assertTrue(ConfigurarIdioma.getNormalLanguage().equals("french"));
    }
    @Test
    public void testGetLanguajeIt() {
        ConfigurarIdioma.setLanguage("it");
        Assert.assertTrue(ConfigurarIdioma.getNormalLanguage().equals("italian"));
    }
    @Test
    public void testGetLanguajeAr() {
        ConfigurarIdioma.setLanguage("ar");
        Assert.assertTrue(ConfigurarIdioma.getNormalLanguage().equals("arabic"));
    }
    @Test
    public void testGetLanguajeDa() {
        ConfigurarIdioma.setLanguage("da");
        Assert.assertTrue(ConfigurarIdioma.getNormalLanguage().equals("danish"));
    }
    @Test
    public void testGetLanguajeIso6393En() {
        String result = ConfigurarIdioma.getLanguageIso6393("eb");
        Assert.assertTrue(result.equals("eng"));
    }
    @Test
    public void testGetLanguajeIso6393Es() {
        String result = ConfigurarIdioma.getLanguageIso6393("es");
        Assert.assertTrue(result.equals("spa"));
    }
    @Test
    public void testGetLanguajeIso6393Pt() {

        String result = ConfigurarIdioma.getLanguageIso6393("pt");
        Assert.assertTrue(result.equals("por"));
    }
    @Test
    public void testGetLanguajeIso6393Ca() {
        String result = ConfigurarIdioma.getLanguageIso6393("ca");
        Assert.assertTrue(result.equals("cat"));
    }
    @Test
    public void testGetLanguajeIso6393Fr() {
        String result = ConfigurarIdioma.getLanguageIso6393("fr");
        Assert.assertTrue(result.equals("fra"));
    }
    @Test
    public void testGetLanguajeIso6393Ita() {
        String result = ConfigurarIdioma.getLanguageIso6393("it");
        Assert.assertTrue(result.equals("ita"));
    }
    @Test
    public void testGetLanguajeIso6393Ara() {
        String result = ConfigurarIdioma.getLanguageIso6393("ar");
        Assert.assertTrue(result.equals("ara"));
    }
    @Test
    public void testGetLanguajeIso6393Da() {
        String result = ConfigurarIdioma.getLanguageIso6393("da");
        Assert.assertTrue(result.equals("dan"));
    }



}