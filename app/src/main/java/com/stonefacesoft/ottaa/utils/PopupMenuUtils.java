package com.stonefacesoft.ottaa.utils;

import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;

import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.preferences.User;
import com.stonefacesoft.pictogramslibrary.utils.ValidateContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PopupMenuUtils {

    private PopupMenu popupMenu;
    private Context mContext;

    public  PopupMenuUtils(Context context,View view){
        this.mContext = context;
        popupMenu = new PopupMenu(mContext,view);
    }

    public void inflateIt(){
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        if (!User.getInstance(mContext).isPremium()) {
            popupMenu.getMenu().getItem(0).setIcon(R.drawable.ic_padlock);
        }
        popupMenu.show();
    }

    public PopupMenuUtils addClickListener(PopupMenu.OnMenuItemClickListener listener){
        popupMenu.setOnMenuItemClickListener(listener);
        return this;
    }

    public PopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void dismissPopupMenu() {
        // If the PopupMenu object is not null, then dismiss it
        if (popupMenu != null) {
            popupMenu.dismiss();
        }
    }
}
