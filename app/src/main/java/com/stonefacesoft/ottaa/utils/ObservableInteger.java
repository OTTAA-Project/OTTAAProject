package com.stonefacesoft.ottaa.utils;
/*
* Use this class to download an number of specifics files from firebase
* this files likes phrases, pictograms or groups from firebase
* */
public class ObservableInteger {

    private OnIntegerChangeListener listener;

    private int value;
    private int cantMax;

    public void setOnIntegerChangeListener(OnIntegerChangeListener listener) {
        this.listener = listener;
    }

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
        if (listener != null) {
            listener.onIntegerChanged(value);
        }
    }

    public interface OnIntegerChangeListener {
        void onIntegerChanged(int newValue);
    }

    public OnIntegerChangeListener getListener() {
        return listener;
    }

    public void incrementValue(){
        value++;
        if(listener!=null)
            listener.onIntegerChanged(value);
    }
}