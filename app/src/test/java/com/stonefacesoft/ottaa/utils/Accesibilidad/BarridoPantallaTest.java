package com.stonefacesoft.ottaa.utils.Accesibilidad;

import android.view.View;

import com.stonefacesoft.ottaa.Principal;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BarridoPantallaTest {

    BarridoPantalla barridoPantalla;

    @Before
    public void setUp() throws Exception {
        barridoPantalla = new BarridoPantalla();
    }

    @Test
    public void setmListadoVistas(){
        Principal principal = new Principal();
        PictoView pictoView =new PictoView(principal);
        PictoView pictoView2 =new PictoView(principal);
        PictoView pictoView3 =new PictoView(principal);
        PictoView pictoView4 =new PictoView(principal);
        ArrayList<View> list =  new ArrayList<>();
        barridoPantalla.setmListadoVistas(list);
        barridoPantalla.addView(pictoView);
        barridoPantalla.addView(pictoView2);
        barridoPantalla.addView(pictoView3);
        barridoPantalla.addView(pictoView4);
        assertEquals(4,barridoPantalla.getmListadoVistas().size());
    }

    @Test
    public void isBarridoPantalla() {
        barridoPantalla.enableDisableScreenScanning(true,true,false,false,false,false);
        assertEquals(true,barridoPantalla.isBarridoPantalla());
    }

    @Test
    public void isBarridoActivado() {
        barridoPantalla.enableDisableScreenScanning(true,true,false,false,false,false);
        assertEquals(true,barridoPantalla.isBarridoActivado());
    }


    @Test
    public void isScrollMode() {
        barridoPantalla.enableDisableScreenScanning(true,false,false,false,true,false);
        assertEquals(true,barridoPantalla.isScrollMode());
    }

    @Test
    public void isScrollModeClicker() {
        barridoPantalla.enableDisableScreenScanning(true,false,false,false,false,true);
        assertEquals(true,barridoPantalla.isScrollModeClicker());
    }

    @Test
    public void setmEstaActivado() {
        barridoPantalla.setmEstaActivado(false);
        assertEquals(false,barridoPantalla.isBarridoActivado());
    }

    @Test
    public void isAvanzarYAceptar() {
        barridoPantalla.enableDisableScreenScanning(true,false,true,false,false,false);
        assertEquals(true,barridoPantalla.isAvanzarYAceptar());
    }

    @Test
    public void setAvanzarYAceptar() {
        barridoPantalla.setAvanzarYAceptar(false);
        assertEquals(false,barridoPantalla.isAvanzarYAceptar());
    }


    @Test
    public void isSipAndPuff() {
        barridoPantalla.enableDisableScreenScanning(true,false,false,true,false,false);
        assertEquals(true,barridoPantalla.isSipAndPuff());
    }

    public View createView(Principal principal,int visibility){
        View  view =  new View(principal);
        view.setVisibility(visibility);
        return view;
    }
}