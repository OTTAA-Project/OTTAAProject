package com.stonefacesoft.ottaa.utils;

import com.stonefacesoft.pictogramslibrary.view.PictoView;

public enum EnumPictoView {
    OPCION1,OPCION2,OPCION3,OPCION4;

    private PictoView pictoView;

    public void setPictoView(PictoView pictoView) {
        this.pictoView = pictoView;
    }

    public PictoView getPictoView() {
        return pictoView;
    }
}
