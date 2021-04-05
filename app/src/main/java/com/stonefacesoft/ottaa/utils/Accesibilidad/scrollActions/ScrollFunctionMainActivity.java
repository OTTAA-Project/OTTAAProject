package com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions;

import android.content.Context;
import android.os.Message;

import androidx.annotation.NonNull;

import com.stonefacesoft.ottaa.Principal;

public class ScrollFunctionMainActivity extends ScrollFunction {
    private final Principal principal;

    public ScrollFunctionMainActivity(Context mContext, Principal principal) {
        super(mContext);
        this.principal = principal;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case HACER_CLICK:
                principal.OnClickBarrido();
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
