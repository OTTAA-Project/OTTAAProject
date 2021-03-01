package com.stonefacesoft.ottaa.utils;

import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

class PopUpMenuUtils implements PopupMenu.OnMenuItemClickListener {
    private AppCompatActivity activity;
    public PopUpMenuUtils(){

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (activity.getClass().getName()){
            case "":
        }
        return false;
    }
}
