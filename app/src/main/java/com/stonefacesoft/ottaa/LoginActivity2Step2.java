package com.stonefacesoft.ottaa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class LoginActivity2Step2 extends Activity implements View.OnClickListener {
    private static final String TAG = "LoginActivity2Step2";

    //UI elemetns
    ImageView imageViewOrangeBanner;
    ImageView imageViewThreePeople;
    TextView textViewLoginBig;
    TextView textViewLoginSmall;
    Button buttonNext;
    Button buttonPrevious;
    EditText editTextName;
    EditText editTextBirthday;

    //User variables
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO hacer que sea fullscreen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_2);

        mAuth = FirebaseAuth.getInstance();

        bindUI();

        animateEntrance();

        fillUserData();

    }

    private void bindUI(){
        imageViewOrangeBanner = findViewById(R.id.orangeBanner2);
        imageViewThreePeople = findViewById(R.id.imagen3personas);
        textViewLoginBig = findViewById(R.id.textLoginBig);
        textViewLoginSmall = findViewById(R.id.textLoginSmall);

        buttonNext = findViewById(R.id.nextButton);
        buttonNext.setOnClickListener(this);
        buttonPrevious = findViewById(R.id.backButton);
        buttonPrevious.setOnClickListener(this);

        editTextName = findViewById(R.id.editTextName);
        editTextBirthday = findViewById(R.id.editTextBirthday);
        new DateInputMask(editTextBirthday);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private  void animateEntrance(){
        TranslateAnimation translateAnimation = new TranslateAnimation(-700, 0, 0, 0);
        translateAnimation.setRepeatMode(Animation.ABSOLUTE);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation.setDuration(1000);
        translateAnimation.setFillAfter(true);
        imageViewOrangeBanner.startAnimation(translateAnimation);
        textViewLoginBig.startAnimation(translateAnimation);
        textViewLoginSmall.startAnimation(translateAnimation);

        TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, 700, 0);
        translateAnimation2.setRepeatMode(Animation.ABSOLUTE);
        translateAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation2.setDuration(1000);
        translateAnimation2.setFillAfter(true);
        imageViewThreePeople.startAnimation(translateAnimation2);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.nextButton) {//TODO save user data on preferences and on Firebase
            Intent intent = new Intent(LoginActivity2Step2.this, LoginActivity2Step3.class);
            startActivity(intent);
        } else if (id == R.id.backButton) {
            Intent intent2 = new Intent(LoginActivity2Step2.this, LoginActivity2Step2.class);
            startActivity(intent2);
        } else if (id == R.id.buttonCalendarDialog) {
            //TODO open dialog with calendar view and choose date.

        }
    }

    private void fillUserData(){
        if (mAuth.getCurrentUser() != null){
            //TODO get user data, birthdate and gender is quite tricky. Probably best to ask the user to insert it
            editTextName.setText(mAuth.getCurrentUser().getDisplayName());

        }
    }
}

//Source https://stackoverflow.com/questions/16889502/how-to-mask-an-edittext-to-show-the-dd-mm-yyyy-date-format

class DateInputMask implements TextWatcher {
    private String current = "";
    private String ddmmyyyy = "DDMMYYYY";
    private Calendar cal = Calendar.getInstance();
    private EditText input;

    public DateInputMask(EditText input) {
        this.input = input;
        this.input.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //TODO check if the double press happens on a device as well.

        if (s.toString().equals(current)) {
            return;
        }

        String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
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
            // ^ first set year for the line below to work correctly
            //with leap years - otherwise, date e.g. 29/02/2012
            //would be automatically corrected to 28/02/2012

            day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
            clean = String.format("%02d%02d%02d",day, mon, year);
        }

        clean = String.format("%s/%s/%s", clean.substring(0, 2),
                clean.substring(2, 4),
                clean.substring(4, 8));

        sel = sel < 0 ? 0 : sel;
        current = clean;
        input.setText(current);
        input.setSelection(sel < current.length() ? sel : current.length());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
