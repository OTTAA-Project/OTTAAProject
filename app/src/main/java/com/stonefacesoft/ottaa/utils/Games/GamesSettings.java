package com.stonefacesoft.ottaa.utils.Games;

/**
 * @author Gonzalo Juarez
 * <h1> Objective </h1>
 * this class was created in order to manage the games behavior
 * <h1> How to Declare </h1>
 * <code>GameSettings settings = new GameSettings();</code>
 * <br>
 * <h2> How to set up the flag initial status</h2>
 * <code>settings.enableRepeatFunction(status)</code> <br> <code>settings.enableSound(status)</code> <br> <code>settings.enableHelpFunction(status)</code>
 * <br>
 * <h2> How to get the flag status </h2>
 * <code>settings.isHelpFunction()</code> <br> <code>settings.isRepeat</code> <br> <code>settings.isSoundOn</code>
 * @version 1
 */
public class GamesSettings {
    private boolean repeat;
    private boolean soundOn;
    private boolean helpFunction;
    private boolean repeatLection;


    public GamesSettings() {

    }

    /**
     * This method was created in order to enable the repeat function on the game
     */
    public GamesSettings enableRepeatFunction(boolean enable) {
        this.repeat = enable;
        return this;
    }

    /**
     * This method was created in order to enable or disable the game sound
     */
    public GamesSettings enableSound(boolean enable) {
        this.soundOn = enable;
        return this;
    }

    /**
     * This method was created in order to enable or disable the help function
     */
    public GamesSettings enableHelpFunction(boolean enable) {
        helpFunction = enable;
        return this;
    }

    /**
     * this method returns the help function status
     *
     * @return helpFunction
     */
    public boolean isHelpFunction() {
        return helpFunction;
    }

    /**
     * this method returns the repeat function status
     *
     * @return repeat
     */
    public boolean isRepeat() {
        return repeat;
    }

    /**
     * this method returns the sound status
     *
     * @return soundOn
     */
    public boolean isSoundOn() {
        return soundOn;
    }

    /**
     * this method change the variable status and return the value
     *
     * @return !value
     */
    public boolean changeStatus(boolean value) {
        return !value;
    }

    /**
     * this method returns the flag status about the lection
     *
     * @return repeatLection
     */
    public boolean isRepeatLection() {
        return repeatLection;
    }

    /**
     * this method changes the flag status about the lection and returns that flag
     *
     * @return repeatLection
     */
    public boolean changeRepeatLectionStatus() {
        return repeatLection = changeStatus(repeatLection);
    }
}
