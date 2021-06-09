package com.stonefacesoft.ottaa.utils;

import com.stonefacesoft.ottaa.Principal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActivityUtilsEstatusTest {
    ActivityUtilsEstatus activityUtilsEstatus = new ActivityUtilsEstatus();

    @Test
    public void testIsValidContext() {
        assertEquals(true,activityUtilsEstatus.isValidContext(new Principal()));
    }

    @Test
    public void testGetmActivityFromContext() {
        Principal principal = new Principal();
        assertEquals(principal,activityUtilsEstatus.getmActivityFromContext(principal));
    }
    @Test
    public void testIsActivityDestroyed() {
        Principal principal = new Principal();
        assertEquals(false,activityUtilsEstatus.isActivityDestroyed(principal));
    }
}