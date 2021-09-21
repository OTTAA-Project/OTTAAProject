package com.stonefacesoft.ottaa.utils;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObservableIntegerTest implements ObservableInteger.OnIntegerChangeListener {

    private ObservableInteger observableInteger = new ObservableInteger();

    @Before
    public void setUp() throws Exception {
        observableInteger = new ObservableInteger();
    }


    @Test
    public void setOnIntegerChangeListener() {
        observableInteger.setOnIntegerChangeListener(this);
        assertNotNull(observableInteger.getListener());
    }

    @Test
    public void get() {
        observableInteger.setOnIntegerChangeListener(this);
        observableInteger.set(2);
        assertEquals(2,observableInteger.get());
    }

    @Test
    public void set() {
        observableInteger.setOnIntegerChangeListener(this);
        observableInteger.set(1);
        assertEquals(1,observableInteger.get());
    }

    @Override
    public void onIntegerChanged(int newValue) {
        System.out.println("Observable Integer Value :"+newValue);
    }
}