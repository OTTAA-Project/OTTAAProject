package com.stonefacesoft.ottaa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseDatabaseRequest;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.InmersiveMode;
import com.stonefacesoft.ottaa.utils.preferences.DataUser;
import com.stonefacesoft.ottaa.utils.preferences.PreferencesUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoginActivity2Step2 extends AppCompatActivity implements View.OnClickListener ,DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
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
    Spinner genderSelector;
    private String gender;
    boolean convert;
    DataUser userData;
    private PreferencesUtil preferencesUtil;

    //User variables
    private FirebaseAuth mAuth;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private FirebaseDatabaseRequest databaseRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new InmersiveMode(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_2);

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        preferencesUtil=new PreferencesUtil(preferences);
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
        editTextBirthday.setInputType(InputType.TYPE_CLASS_DATETIME);

        genderSelector=findViewById(R.id.selectorGender);
        genderSelector.setOnItemSelectedListener(this);

        new DateInputMask(editTextBirthday);
        databaseRequest=new FirebaseDatabaseRequest(this);
        convert=true;
        userData=new DataUser();

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
            setUpUserData();
            Intent intent = new Intent(LoginActivity2Step2.this, LoginActivity2Step3.class);
            startActivity(intent);
        } else if (id == R.id.backButton) {
            Intent intent2 = new Intent(LoginActivity2Step2.this, LoginActivity2Step2.class);
            startActivity(intent2);
        } else if (id == R.id.buttonCalendarDialog) {
            calendarDialog();
        }
    }

    private void fillUserData(){
        if (mAuth.getCurrentUser() != null){
            //TODO get user data, birthdate and gender is quite tricky. Probably best to ask the user to insert it
            editTextName.setText(mAuth.getCurrentUser().getDisplayName());

        }
    }

    public void calendarDialog(){
        Calendar calendar=Calendar.getInstance();
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,this,year,month,day);
        datePickerDialog.show();

    }

    public void setUpUserData(){
        userData.setFirstAndLastName(editTextName.getText().toString());
        preferencesUtil.applyStringValue("name",userData.getFirstAndLastName());
        userData.setGender(gender);
        preferencesUtil.applyStringValue(Constants.GENERO,userData.getGender());
        preferencesUtil.applyLongValue(Constants.FECHACUMPLE,userData.getBirthDate());
        databaseRequest.UploadUserData(userData);
    }



    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        convert=false;
        editTextBirthday.setText(dayOfMonth+"/"+(month+1)+"/"+year);
        Calendar calendar=Calendar.getInstance();
        calendar.set(year,month,dayOfMonth);
        userData.setBirthDate(calendar.getTime().getTime());
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position!=0)
            gender=genderSelector.getSelectedItem().toString();
        else {
            try {
                gender=preferencesUtil.getStringValue(Constants.GENERO,genderSelector.getItemAtPosition(position+1).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class DateInputMask implements TextWatcher {
        //Source https://stackoverflow.com/questions/16889502/how-to-mask-an-edittext-to-show-the-dd-mm-yyyy-date-format

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
            if(convert) {
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

                if (clean.length() < 8) {
                    clean = clean + ddmmyyyy.substring(clean.length());
                } else {
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day = Integer.parseInt(clean.substring(0, 2));
                    int mon = Integer.parseInt(clean.substring(2, 4));
                    int year = Integer.parseInt(clean.substring(4, 8));

                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                    cal.set(Calendar.MONTH, mon - 1);
                    year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                    clean = String.format("%02d%02d%02d", day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                input.setText(current);
                input.setSelection(sel < current.length() ? sel : current.length());
                userData.setBirthDate(cal.getTime().getTime());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            convert=true;
        }


    }
}



