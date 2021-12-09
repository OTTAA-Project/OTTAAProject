package com.stonefacesoft.ottaa.utils;

public class ReturnPositionItem {
    private int position;
    private int size;

    public ReturnPositionItem(int size) {
        this.size = size;
        if (size != 0)
            position = 0;
        else
            position = -1;
    }

    public int add() {
        position++;
        if (position >= size)
            position = 0;
        return position;
    }

    public int subtract() {
        position--;
        if (position < 0)
            position = size - 1;
        if (position == -1)
            return 0;
        return position;
    }

    public int getPosition() {
        return position;
    }

    public void updateSize(int size) {
        if (this.size != size)
            this.size = size;
    }
}
