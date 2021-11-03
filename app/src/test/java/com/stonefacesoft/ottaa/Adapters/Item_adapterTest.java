package com.stonefacesoft.ottaa.Adapters;

import android.app.Dialog;

import com.stonefacesoft.ottaa.Principal;
import com.stonefacesoft.ottaa.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Item_adapterTest {
    private Dialog dialog;
    private Item_adapter adapter=new Item_adapter(R.layout.custom_item_select,dialog);


   @Test
    public void testSetmArrayListItemsNames() {
        String[] names = new String[]{"paul","john"};
        adapter.setmArrayListItemsNames(names);
        assertEquals(names.toString(),adapter.getmArrayListItemsnames().toString());
    }
    @Test
    public void testSetmArrayListValues() {
        String[] names = new String[]{"paul","john"};
        adapter.setmArrayListValues(names);
        assertEquals(names.toString(),adapter.getmArrayListValues().toString());
    }

    @Test
    public void testSetKey() {
        adapter.setKey("default");
        assertEquals("default",adapter.getKey());
    }

    @Test
    public void testSetDefaultValue() {
        adapter.setDefaultValue("Hello");
        assertEquals("Hello",adapter.getDefaultValue());
    }




    @Test
    public void testGetItemId() {
        String[] names = new String[]{"paul","john"};
        adapter.setmArrayListItemsNames(names);
        adapter.setmArrayListValues(names);
       assertNotNull(adapter.getItemId(0));
    }
    @Test
    public void testGetItemCount() {
        String[] names = new String[]{"paul","john"};
        adapter.setmArrayListItemsNames(names);
        adapter.setmArrayListValues(names);
        assertEquals(names.length,adapter.getItemCount());
    }
    @Test
    public void testGetmArrayListItemsnames() {
       String[] names = new String[]{"paul","john"};
       adapter.setmArrayListItemsNames(names);
       assertEquals(names[1].toString(),adapter.getmArrayListItemsnames()[1].toString());
    }
    @Test
    public void testGetmArrayListValues() {
        String[] names = new String[]{"paul","john","adrian"};
        adapter.setmArrayListValues(names);
        assertEquals(names[2].toString(),adapter.getmArrayListValues()[2].toString());
    }
    @Test
    public void testDevolverPosition() {

   }
}