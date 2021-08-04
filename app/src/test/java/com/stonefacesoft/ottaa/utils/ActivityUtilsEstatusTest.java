package com.stonefacesoft.ottaa.utils;

import com.stonefacesoft.ottaa.Principal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActivityUtilsEstatusTest {
    ActivityUtilsEstatus activityUtilsEstatus = new ActivityUtilsEstatus();

    @Test
    public void testIsValidContext() {
        assertEquals(false,activityUtilsEstatus.isValidContext(null));
    }



}