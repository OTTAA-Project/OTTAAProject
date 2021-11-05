package com.stonefacesoft.ottaa.utils;

import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;

import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.preferences.User;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PopupMenuUtils {
    public PopupMenuUtils(Context context, View view, PopupMenu.OnMenuItemClickListener listener){
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.setOnMenuItemClickListener(listener);
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
        if (!User.getInstance(context).isPremium()) {
            popupMenu.getMenu().getItem(0).setIcon(R.drawable.ic_baseline_lock_24);
        }
        popupMenu.show();
    }
}
