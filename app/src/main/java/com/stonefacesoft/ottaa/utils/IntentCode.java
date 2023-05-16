package com.stonefacesoft.ottaa.utils;

import com.stonefacesoft.ottaa.Games.TellAStory;

/**
 * Created by Cristian on 3/4/2016.
 */
public enum IntentCode {
    MY_DATA_CHECK_CODE(0),
    CONFIG_SCREEN(1),
    EDITARPICTO(2),
    RC_SIGN_IN(3),
    LOGIN_ACTIVITY(4),
    PLAY_SERVICES_RESOLUTION_REQUEST(5),
    GALERIA_GRUPOS(6),
    CAMARA(7),
    GALERIA(8),
    ARASAAC(9),
    CALENDARIO(10),
    GALERIA_PICTOS(11),
    VINCULAR(12),
    ASIGN_TASK(13),
    NOTIGAMES(14),
    ORDENAR(15),
    CUSTOMPHRASES(16),
    PICK_IMAGE(17),
    SEARCH_ALL_PICTOGRAMS(18),
    AVATAR(19),
    VINCULAR_FRASES(20),
    TELL_A_STORY(21),

    SHAREACTION(25);

    private final int code;


    IntentCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
