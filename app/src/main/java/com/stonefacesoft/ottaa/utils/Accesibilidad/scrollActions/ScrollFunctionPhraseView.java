package com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions;

import android.content.Context;
import android.os.Message;

import androidx.annotation.NonNull;

import com.stonefacesoft.ottaa.GaleriaGrupos2;
import com.stonefacesoft.ottaa.Views.Phrases.PhrasesView;

import org.jetbrains.annotations.NotNull;

public class ScrollFunctionPhraseView extends ScrollFunction {
    private final PhrasesView phraseView;
    public ScrollFunctionPhraseView(PhrasesView phrasesView) {
        super(phrasesView);
        this.phraseView = phrasesView;
    }

    @Override
    public void handleMessage(@NonNull @NotNull Message msg) {
        switch (msg.what){
            case HACER_CLICK:
                phraseView.OnClickBarrido();
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
