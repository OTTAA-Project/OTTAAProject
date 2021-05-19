package com.stonefacesoft.ottaa.Adapters;

import android.app.Dialog;

import com.stonefacesoft.ottaa.Principal;
import com.stonefacesoft.ottaa.R;

import junit.framework.TestCase;

public class Item_adapterTest extends TestCase {
    private Dialog dialog;
    private Item_adapter adapter=new Item_adapter(R.layout.custom_item_select,new Principal(),dialog);


    public void testSetmArrayListItemsNames() {
        String[] names = new String[]{"paul","john"};
        adapter.setmArrayListItemsNames(names);
        assertEquals(names.toString(),adapter.getmArrayListItemsnames().toString());
    }

    public void testSetmArrayListValues() {
        String[] names = new String[]{"paul","john"};
        adapter.setmArrayListValues(names);
        assertEquals(names.toString(),adapter.getmArrayListValues().toString());
    }

    public void testSetKey() {
        adapter.setKey("default");
        assertEquals("default",adapter.getKey());
    }

    public void testSetDefaultValue() {
        adapter.setDefaultValue("Hello");
        assertEquals("Hello",adapter.getDefaultValue());
    }

    public void testSetOnClickListener() {
    }

    public void testOnCreateViewHolder() {
    }

    public void testOnBindViewHolder() {
    }

    public void testSelecItem() {
    }

    public void testGetBooleanValue() {
    }

    public void testGetItemViewType() {
    }

    public void testGetItemId() {
    }

    public void testGetItemCount() {
    }

    public void testGetmArrayListItemsnames() {
    }

    public void testGetmArrayListValues() {
    }

    public void testDevolverPosition() {
    }
}