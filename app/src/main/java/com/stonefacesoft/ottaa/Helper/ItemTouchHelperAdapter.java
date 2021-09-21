package com.stonefacesoft.ottaa.Helper;

/**
 * Created by GastonSaillen on 1/3/2018.
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromIndex, int toIndex);
    void onItemDismiss(int position);
    void onDropItem();

}