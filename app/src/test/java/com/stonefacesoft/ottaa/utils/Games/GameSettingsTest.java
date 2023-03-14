package com.stonefacesoft.ottaa.utils.Games;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class GameSettingsTest {

    private GamesSettings gamesSettings = new GamesSettings();

    @Test
    public void EnableRepeatFunction(){
        gamesSettings.enableRepeatFunction(true);
        Assert.assertTrue(gamesSettings.isRepeat());
    }

    @Test
    public void EnableHelpFunction(){
        gamesSettings.enableHelpFunction(true);
        Assert.assertTrue(gamesSettings.isHelpFunction());
    }

    @Test
    public void EnableSound(){
        gamesSettings.enableSound(true);
        Assert.assertTrue(gamesSettings.isSoundOn());
    }

    @Test
    public void ChangeRepeatLection(){
        boolean repeatLection = gamesSettings.changeRepeatLectionStatus();
        Assert.assertTrue(repeatLection);
    }

    @Test
    public void RepeatLection(){
        boolean repeatLection = gamesSettings.isRepeatLection();
        Assert.assertFalse(repeatLection);
    }
}
