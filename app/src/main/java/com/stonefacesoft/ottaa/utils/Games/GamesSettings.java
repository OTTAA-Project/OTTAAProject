package com.stonefacesoft.ottaa.utils.Games;

public class GamesSettings {
    private boolean repeat;
    private boolean soundOn;
    private boolean helpFunction;


    public GamesSettings(){

    }

    public GamesSettings enableRepeatFunction(boolean enable){
        this.repeat = enable;
        return this;
    }
    public GamesSettings enableSound(boolean enable){
        this.soundOn = enable;
        return this;
    }
    public GamesSettings enableHelpFunction(boolean enable){
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
