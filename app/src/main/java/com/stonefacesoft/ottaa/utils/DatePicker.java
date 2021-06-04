package com.stonefacesoft.ottaa.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

import com.stonefacesoft.ottaa.R;

import java.util.Calendar;

public class DatePicker extends Dialog {

    private final CalendarView calendarView;

    public DatePicker(@NonNull Context context, CalendarView.OnDateChangeListener dateChangeListener) {
        super(context);
        setContentView(R.layout.datepicker_dialog);
        calendarView=this.findViewById(R.id.calendar);
        calendarView.setDate(Calendar.getInstance().getTimeInMillis());
        calendarView.setOnDateChangeListener(dateChangeListener);
    }

}
