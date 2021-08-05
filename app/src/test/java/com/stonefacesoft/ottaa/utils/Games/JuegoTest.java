package com.stonefacesoft.ottaa.utils.Games;

import com.stonefacesoft.ottaa.Games.WhichIsThePicto;
import com.stonefacesoft.ottaa.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JuegoTest {

    private Juego juego = new Juego(null,1,1);

    @Test
    public void testStartUseTime() {
        juego.startUseTime();
        assertNotNull(juego.getReloj().getHorainicio());
    }

    @Test
    public void testEndUseTime() {
        juego.endUseTime();
        assertNotNull(juego.getReloj().getHorafin());
    }

    @Test
    public void testIncrementCorrect() {
        juego.incrementCorrect();
        juego.incrementCorrect();
        assertEquals(2,juego.getCorrectStreak());
    }

    @Test
    public void testIncrementWrong() {
        juego.incrementWrong();
        juego.incrementWrong();
        assertEquals(2,juego.getScoreClass().getDesaciertos());
    }

    @Test
    public void testGetCorrectStreak() {
        juego.incrementCorrect();
        assertEquals(1,juego.getCorrectStreak());
    }

    @Test
    public void testGetBestStreak() {
        juego.incrementCorrect();
        juego.incrementCorrect();
        juego.incrementCorrect();
        juego.incrementCorrect();
        juego.incrementWrong();
        assertEquals(4,juego.getBestStreak());
    }

    @Test
    public void testGetGame() {
        assertEquals(1,juego.getGame());
    }
    @Test
    public void testGetIconArrayActive() {
        assertEquals(R.drawable.ic_sentiment_dissatisfied_white_24dp,juego.getSmiley(1));
    }

    @Test
    public void testDevolverCaritaInt() {
        juego.incrementCorrect();
        juego.incrementCorrect();
        juego.incrementCorrect();
        juego.incrementCorrect();
        juego.incrementCorrect();
        juego.incrementWrong();
        juego.incrementWrong();
        juego.incrementWrong();
        juego.incrementWrong();
        juego.incrementWrong();
        juego.incrementWrong();
        System.out.println(" Return face nº :"+juego.devolverCaritaInt());
        assertEquals(juego.getSmiley(2),juego.devolverCaritaInt());
    }

    public void testGetObject() {
        assertNotNull(juego.getJson());
    }

    public void testGetScoreClass() {
        assertNotNull(juego.getScoreClass());
    }

    public void testGetScore() {
    }

    public void testGetReloj() {
        assertNotNull(juego.getReloj());
    }

    public void testAgregarDatosConsulta() {
        juego.agregarDatosConsulta();

    }


    public void testGetSmiley() {
        assertEquals(R.drawable.ic_sentiment_neutral_white_24dp,juego.getSmiley(2));
    }
}