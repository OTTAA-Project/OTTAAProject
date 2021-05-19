package com.stonefacesoft.ottaa.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.stonefacesoft.ottaa.Principal;
import com.stonefacesoft.ottaa.R;

import junit.framework.TestCase;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class Item_adapterTest extends TestCase {
    private Context mContext= ApplicationProvider.getApplicationContext();
    private Dialog dialog = new Dialog(mContext);
    private Item_adapter adapter=new Item_adapter(R.layout.custom_item_select,new Principal(),dialog);

    public void testSetOnClickListener() {
        String[] names = new String[]{"paul","john"};
        adapter.setmArrayListItemsNames(names);
        adapter.setmArrayListValues(names);
        adapter.notifyDataSetChanged();
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        assertNotNull(adapter.getListener());

    }

    public void testOnCreateViewHolder() {
    }

    public void testOnBindViewHolder() {
    }

    public void testGetItemViewType() {
    }

    public void testGetItemId() {
    }

    public void testGetItemCount() {
    }
}