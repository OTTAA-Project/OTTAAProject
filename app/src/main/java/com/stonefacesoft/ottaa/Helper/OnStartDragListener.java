package com.stonefacesoft.ottaa.Helper;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by GastonSaillen on 1/3/2018.
 */

public interface OnStartDragListener {
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
    void  isDroped(boolean isDroped);
}