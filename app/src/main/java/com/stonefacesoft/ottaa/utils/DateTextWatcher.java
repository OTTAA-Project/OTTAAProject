package com.stonefacesoft.ottaa.utils;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.stonefacesoft.ottaa.Interfaces.CalendarChangeEvent;

import java.util.Calendar;

public class DateTextWatcher  {
    private String current = "";
    private final String ddmmyyyy = "DDMMYYYY";
    private final Calendar cal = Calendar.getInstance();
    private final EditText input;
    private final CalendarChangeEvent calendarChangeEvent;


    public DateTextWatcher(EditText input, CalendarChangeEvent calendarChangeEvent) {
        this.input = input;

        this.input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                    validateText(input.getText().toString());
                return false;
            }
        });
        this.calendarChangeEvent = calendarChangeEvent;
    }



    private void validateText(String s){
        if (!s.equals(current)) {
            String clean = s.replaceAll("[^\\d.]|\\.", "");
            String cleanC = current.replaceAll("[^\\d.]|\\.", "");

            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }
            //Fix for pressing delete next to a forward slash
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 8){
                clean = clean + ddmmyyyy.substring(clean.length());
            }else{
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                int day  = Integer.parseInt(clean.substring(0,2));
                int mon  = Integer.parseInt(clean.substring(2,4));
                int year = Integer.parseInt(clean.substring(4,8));
                mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                cal.set(Calendar.MONTH, mon-1);
                year = (year<1900)?1900:(year>2100)?2100:year;
                cal.set(Calendar.YEAR, year);

                day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                cal.set(Calendar.DAY_OF_MONTH,day);
                clean = String.format("%02d%02d%02d",day, mon, year);
            }

            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8));

            sel = sel < 0 ? 0 : sel;
            current = clean;
            input.setText(current);
            input.setSelection(sel < current.length() ? sel : current.length());
            calendarChangeEvent.setCalendarDate(cal.getTimeInMillis());
        }
    }

    public void validateTextWatcher(){
        validateText(input.getText().toString());
    }
}
