package com.stonefacesoft.ottaa.utils;

public class ClickCounter {
    private int clickCounter=0;
    private static ClickCounter _ClickCounter;

    public static ClickCounter getInstance(){
        if(_ClickCounter == null)
            _ClickCounter = new ClickCounter();
        return _ClickCounter;
    }

    public void incrementValue(){
        clickCounter++;
    }

    public void decrementValue(){
        clickCounter--;
    }

    public int getClickCounter() {
        return clickCounter;
    }

    public void resetCounter(){
        clickCounter = 0;
    }


}
