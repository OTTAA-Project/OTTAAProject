package com.stonefacesoft.ottaa.Games.Model;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryGameModelModelTest  {

    private final MemoryGameModelModel model = new MemoryGameModelModel();

    @Test
    public void getMatrixIdPictogram() {
        model.setSize(4);
        model.createArray();
        assertTrue(model.getMatrixIdPictogram()[0].length==4);
    }

    @Test
    public void refresMatrix() {
        model.setSize(4);
        model.createArray();
        model.addRandomIndex();
        model.refresMatrix();
        assertTrue(model.getMatrixIdPictogram()[1][1]==-1);
    }

    @Test
    public void addRandomIndex() {
        model.setSize(4);
        model.createArray();
        model.addRandomIndex();
        assertTrue(model.getMatrixIdPictogram()[1][1]!=-1);
    }

    @Test
    public void resetMatrix() {
        model.setSize(4);
        model.createArray();
        model.addRandomIndex();
        model.resetMatrix();
        assertTrue(model.getMatrixIdPictogram()[1][1]==0);
    }

    @Test
    public void resetHistory() {
    }

    @Test
    public void createArray() {
    }

    @Test
    public void addHistoryValue() {
    }

    @Test
    public void theViewHasBeenSelected() {
    }
}