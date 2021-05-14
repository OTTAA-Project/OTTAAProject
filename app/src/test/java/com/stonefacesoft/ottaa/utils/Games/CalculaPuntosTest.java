package com.stonefacesoft.ottaa.utils.Games;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculaPuntosTest {
    private CalculaPuntos calculaPuntos;
    @Before
    public void setUp() throws Exception {
        calculaPuntos = new CalculaPuntos();
        calculaPuntos.setAciertos(0);
        calculaPuntos.setDesaciertos(0);
        calculaPuntos.setIntentos(0);
    }

    @Test
    public void sumarCantidadVecesCorrectas() {
        calculaPuntos.sumarCantidadVecesCorrectas();
        calculaPuntos.sumarCantidadVecesCorrectas();
        calculaPuntos.sumarCantidadVecesCorrectas();
        assertEquals(3,calculaPuntos.getAciertos());
    }

    @Test
    public void sumarCantidVecesIncorretas() {
        calculaPuntos.sumarCantidVecesIncorretas();
        assertEquals(1,calculaPuntos.getDesaciertos());
    }

    @Test
    public void calcularValor() {
        calculaPuntos.sumarCantidadVecesCorrectas();
        calculaPuntos.sumarCantidadVecesCorrectas();
        calculaPuntos.sumarCantidadVecesCorrectas();
        calculaPuntos.sumarCantidVecesIncorretas();
        System.out.println(calculaPuntos.calcularValor());
        assertEquals(75.0,calculaPuntos.calcularValor(),0);
    }

    @Test
    public void setAciertos() {
        calculaPuntos.setAciertos(3);
        assertEquals(3,calculaPuntos.getAciertos());
    }

    @Test
    public void setDesaciertos() {
        calculaPuntos.setDesaciertos(3);
        assertEquals(3,calculaPuntos.getDesaciertos());
    }

    @Test
    public void setIntentos() {
        calculaPuntos.setAciertos(400);
        calculaPuntos.setDesaciertos(100);
        assertEquals(500,calculaPuntos.getIntentos());
    }

    @Test
    public void getIntentos() {
        calculaPuntos.setAciertos(400);
        calculaPuntos.setDesaciertos(200);
        assertEquals(600,calculaPuntos.getIntentos());
    }

    @Test
    public void getScore() {
        calculaPuntos.setIntentos(4);
        calculaPuntos.setAciertos(3);
        calculaPuntos.setDesaciertos(1);
        calculaPuntos.calcularValor();
        assertEquals(75,calculaPuntos.getScore(),0);
    }

    @Test
    public void getAciertos() {
        calculaPuntos.setAciertos(5);
        assertEquals(5,calculaPuntos.getAciertos());
    }

    @Test
    public void getDesaciertos() {
        calculaPuntos.setDesaciertos(5);
        assertEquals(5,calculaPuntos.getDesaciertos());
    }

    @Test
    public void getPuntaje() {

    }
}