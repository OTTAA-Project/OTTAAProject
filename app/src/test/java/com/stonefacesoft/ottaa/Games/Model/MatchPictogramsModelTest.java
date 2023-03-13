package com.stonefacesoft.ottaa.Games.Model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatchPictogramsModelTest {

    private final MatchPictogramsModel matchPictogramsModel =  new MatchPictogramsModel();



    @Test
    public void setCorrectValue() {
    }

    @Test
    public void restartValue() {
        matchPictogramsModel.setSize(2);
        matchPictogramsModel.createArray();
        matchPictogramsModel.setCorrectValue(0,1);
        matchPictogramsModel.setCorrectValue(1,1);
        assertTrue(matchPictogramsModel.restartValue());
    }
    @Test
    public void restartValueFalse() {
        matchPictogramsModel.setSize(2);
        matchPictogramsModel.createArray();
        matchPictogramsModel.setCorrectValue(0,-1);
        matchPictogramsModel.setCorrectValue(1,-1);
        assertFalse(matchPictogramsModel.restartValue());
    }

    @Test
    public void selectRandomPictogram() {
        MatchPictogramsModel matchPictogramsModel =  new MatchPictogramsModel();
        matchPictogramsModel.setSize(5);
        ArrayList<Integer> number= new ArrayList<>();
        System.out.println(number.size());
        matchPictogramsModel.selectRandomPictogram(number);
        assertTrue(number.contains(4));
    }
}