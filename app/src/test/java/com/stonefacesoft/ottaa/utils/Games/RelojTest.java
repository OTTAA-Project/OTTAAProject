package com.stonefacesoft.ottaa.utils.Games;

import com.stonefacesoft.pictogramslibrary.JsonUtils.JSONObjectManager;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RelojTest  {

    private Reloj reloj ;

    @Before
    public void setUp() throws Exception {
        reloj = new Reloj();
    }

    @Test
    public void setHorafin() {
        long time = System.currentTimeMillis();
        reloj.setHorafin(time);
        assertNotNull(reloj.getHorafin());

    }

    @Test
    public void setHorainicio() {
        long time = System.currentTimeMillis();
        reloj.setHorainicio(time);
        assertNotNull(reloj.getHorainicio());
    }

    @Test
    public void getHorafin() {
        long time = System.currentTimeMillis();
        reloj.setHorafin(time);
        assertEquals(time,reloj.getHorafin());
    }

    @Test
    public void getHorainicio() {
        long time = System.currentTimeMillis();
        reloj.setHorainicio(time);
        assertEquals(time,reloj.getHorainicio());
    }

    @Test
    public void setId() {
        reloj.setId(0);
        assertEquals(0,reloj.getId());
    }

    @Test
    public void getId() {
        reloj.setId(1);
        assertEquals(1,reloj.getId());
    }

    @Test
    public void getTiempoUso() {
        long initTime = 1621281600000L; //Monday 17 may 17:00
        reloj.setHorainicio(initTime);
        long endTime = 1621282994000L; //Monday 17 may 17:23
        reloj.setHorafin(endTime);
        assertEquals(endTime-initTime,reloj.getTiempoUso());
    }

    @Test
    public void getRelojToJsonObject() {
        JSONObjectManager manager = new JSONObjectManager();
        JSONObject object = reloj.getRelojToJsonObject();
        assertEquals(object.toString(),reloj.getRelojToJsonObject().toString());
    }

    @Test
    public void setLastClock() {
        reloj.setLastClock(System.currentTimeMillis());
        reloj.getLastUsedTime();
    }

    @Test
    public void getLastUsedTime() {
    }

    @Test
    public void timeToHour() {
        long initTime = 1621281600000L; //Monday 17 may 17:00
        reloj.setHorainicio(initTime);
        long endTime = 1621282994000L; //Monday 17 may 17:23
        reloj.setHorafin(endTime);

        assertEquals(0,reloj.timeToHour(reloj.getTiempoUso()));
    }

    @Test
    public void timeToMinutes() {
        long initTime = 1621281600000L; //Monday 17 may 17:00
        reloj.setHorainicio(initTime);
        long endTime = 1621282994000L; //Monday 17 may 17:23
        reloj.setHorafin(endTime);
        assertEquals(23,reloj.timeToMinutes(reloj.getTiempoUso(),reloj.timeToHour(reloj.getTiempoUso())));
    }

    @Test
    public void timeToSeconds() {
        long initTime = 1621281600000L; //Monday 17 may 17:00
        reloj.setHorainicio(initTime);
        long endTime = 1621282994000L; //Monday 17 may 17:23
        reloj.setHorafin(endTime);
        long hour = reloj.timeToHour(reloj.getTiempoUso());
        long minutes = reloj.timeToMinutes(reloj.getTiempoUso(),hour);
        assertEquals(14,reloj.timeToSeconds(reloj.getTiempoUso(),hour,minutes));
    }

    @Test
    public void timeToMiliseconds() {
        long initTime = 1621281600000L; //Monday 17 may 17:00
        reloj.setHorainicio(initTime);
        long endTime = 1621282994079L; //Monday 17 may 17:23:14 : 79
        reloj.setHorafin(endTime);
        long hour = reloj.timeToHour(reloj.getTiempoUso());
        long minutes = reloj.timeToMinutes(reloj.getTiempoUso(),hour);
        long seconds = reloj.timeToSeconds(reloj.getTiempoUso(),hour,minutes);
        System.out.printf(reloj.timeToMiliseconds(reloj.getTiempoUso(),hour,minutes,seconds)+"");
        assertEquals(79,reloj.timeToMiliseconds(reloj.getTiempoUso(),hour,minutes,seconds));
    }


    @Test
    public void getTimeInMinutes() {
        long initTime = 1621281600000L; //Monday 17 may 17:00
        reloj.setHorainicio(initTime);
        long endTime = 1621282994000L; //Monday 17 may 17:23
        reloj.setHorafin(endTime);
        System.out.println(reloj.getTimeInMinutes());
    }




}