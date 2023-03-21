package com.stonefacesoft.ottaa.utils.Accesibilidad;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScreenScanningSettingsTest {
    private final ScreenScanningSettings settings =  new ScreenScanningSettings();
    @Test
    public void setScreenScanning() {
        settings.setScreenScanning(false);
        assertTrue(!settings.isScreenScanning());
    }

    @Test
    public void setScroll() {
        settings.setScroll(false);
        assertTrue(!settings.isScroll());
    }

    @Test
    public void setScrollWithClick() {
        settings.setScrollWithClick(false);
        assertTrue(!settings.isScrollWithClick());
    }

    @Test
    public void setSipAndPuff() {
        settings.setSipAndPuff(false);
        assertTrue(!settings.isSipAndPuff());
    }

    @Test
    public void setAvanzarYAceptar() {
        settings.setAvanzarYAceptar(false);
        assertTrue(!settings.isAvanzarYAceptar());
    }

    @Test
    public void setEnabled() {
        settings.setEnabled(false);
        assertTrue(!settings.isEnabled());
    }

    @Test
    public void setBarrido() {
        settings.setBarrido(false);
        assertTrue(!settings.isBarrido());
    }

    @Test
    public void isAvanzarYAceptar() {
        settings.setAvanzarYAceptar(true);
        assertTrue(settings.isAvanzarYAceptar());
    }

    @Test
    public void isScreenScanning() {
        settings.setScreenScanning(true);
        assertTrue(settings.isScreenScanning());
    }

    @Test
    public void isScroll() {
        settings.setScroll(true);
        assertTrue(settings.isScroll());
    }

    @Test
    public void isScrollWithClick() {
        settings.setScrollWithClick(true);
        assertTrue(settings.isScrollWithClick());
    }

    @Test
    public void isSipAndPuff() {
        settings.setSipAndPuff(true);
        assertTrue(settings.isSipAndPuff());
    }

    @Test
    public void isEnabled() {
        settings.setEnabled(true);
        assertTrue(settings.isEnabled());
    }

    @Test
    public void isBarrido() {
        settings.setBarrido(true);
        assertTrue(settings.isBarrido());
    }
}