package com.stonefacesoft.ottaa.utils.Accesibilidad;

import android.view.View;

import com.stonefacesoft.ottaa.Principal;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class BarridoPantallaTest {

    BarridoPantalla barridoPantalla;

    @Before
    public void setUp() throws Exception {
        barridoPantalla = new BarridoPantalla();
    }

    @Test
    public void setmListadoVistas(){

    }

    @Ignore
    public void recorrerBarridoAutomatico() {
    }

    @Test
    public void isBarridoPantalla() {

    }

    @Test
    public void isBarridoActivado() {

    }


    @Test
    public void isScrollMode() {
    }

    @Test
    public void isScrollModeClicker() {
    }

    @Test
    public void setmEstaActivado() {
    }

    @Test
    public void isAvanzarYAceptar() {
    }

    @Test
    public void setAvanzarYAceptar() {
    }

    @Test
    public void getPosicionBarrido() {
    }

    @Test
    public void activarDesactivarBarrido() {
    }

    @Test
    public void devolverpago() {
    }

    @Test
    public void avanzarBarrido() {
    }

    @Test
    public void volverAtrasBarrido() {
    }

    @Test
    public void getmListadoVistas() {
    }

    @Test
    public void cambiarEstadoBarrido() {
    }

    @Test
    public void addView() {
    }

    @Test
    public void removieView() {
    }

    @Test
    public void changeButtonVisibility() {
    }

    @Test
    public void isButtonVisible() {
    }

    @Test
    public void isSipAndPuff() {
    }

    public View createView(Principal principal,int visibility){
        View  view =  new View(principal);
        view.setVisibility(visibility);
        return view;
    }
}