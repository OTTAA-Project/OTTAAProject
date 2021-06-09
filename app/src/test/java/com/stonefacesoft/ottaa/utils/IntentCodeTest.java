package com.stonefacesoft.ottaa.utils;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntentCodeTest {


    @Test
    public void getCode() {
        assertEquals(17,IntentCode.PICK_IMAGE.getCode());
    }

    @Test
    public void values() {
        assertEquals(18,IntentCode.SEARCH_ALL_PICTOGRAMS.getCode());
    }

    @Test
    public void valueOf() {
        assertEquals("PICK_IMAGE",IntentCode.valueOf("PICK_IMAGE").name());
    }
}