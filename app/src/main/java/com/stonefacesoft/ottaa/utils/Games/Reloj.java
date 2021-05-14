package com.stonefacesoft.ottaa.utils.Games;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
/**
 * @author  Gonzalo Juarez
 * <h3>Objetive</h3>
 * Take the using time in the class.
* <h3>How to declare</h3>
 * <code>Reloj clock=new Reloj();</code>
 * <h3>Examples of implementation</h3>
 * <h4>Return the used time</h4>
 * clock.getTiempoUso();
 * <h4>Convert To Json Object</h4>
 * clock.getRelojToJsonObject();
 * <h4>Return the time in minutes</h4>
 * clock.getTimeInMinutes();
 * */
public class Reloj {

    private final String TAG = "Reloj";

    private int id;
    private long horainicio;
    private long horafin;
    private long lastClock = 0;

    public Reloj() {

    }

    public void setHorafin(long horafin) {
        this.horafin = horafin;
    }

    public void setHorainicio(long horainicio) {
        this.horainicio = horainicio;
    }

    public long getHorafin() {
        return horafin;
    }

    public long getHorainicio() {
        return horainicio;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public long getTiempoUso(){
        return horafin-horainicio;
    }

    public JSONObject getRelojToJsonObject(){
        JSONObject object=new JSONObject();
        try {
            object.put("horaInicio",horainicio);
            object.put("horaFin",horafin);
            object.put("tiempoUso",getTiempoUso());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public void setLastClock(long timeInMilis){
        this.lastClock+=timeInMilis;
    }

    public long getLastUsedTime(){
        return lastClock+getTiempoUso();
    }


    public long timeToHour(long time){
        long value= TimeUnit.MILLISECONDS.toHours(time);
        Log.d(TAG, "timeToHour: " + value);
        return value;
    }

    public long timeToMinutes(long time,long hours){
       return TimeUnit.MILLISECONDS.toMinutes(time-TimeUnit.HOURS.toMillis(hours));
    }

    public long timeToSeconds(long time,long hours,long minutes){

        return TimeUnit.MILLISECONDS
                .toSeconds(time - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));
    }

    public long timeToMiliseconds(long time,long hours,long minutes,long seconds){
        return time - TimeUnit.HOURS.toMillis(hours)
                - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds);
    }

    public String getTimeInMinutes(){
        long time,hours,h1,m1,s1;
        time=getLastUsedTime();
        hours=Calendar.getInstance().getTimeInMillis();
        h1=timeToHour(time+hours);
        m1=timeToMinutes(time+hours,h1);
        s1=timeToSeconds(time+hours,h1,m1);
        return m1+" m "+s1 +" s";
    }




}
