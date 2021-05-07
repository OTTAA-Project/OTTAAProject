package com.stonefacesoft.ottaa.Games.Model;

public class Settings {
    private boolean repeat;
    private boolean soundOn;
    private boolean helpFunction;


    public Settings(){

    }

    public Settings enableRepeatFunction(boolean enable){
        this.repeat = enable;
        return this;
    }
    public Settings enableSound(boolean enable){
        this.soundOn = enable;
        return this;
    }
    public Settings enableHelpFunction(boolean enable){
        helpFunction = enable;
        return this;
    }

    public boolean isHelpFunction() {
        return helpFunction;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    public boolean changeStatus(boolean value){
        return !value;
    }
}
