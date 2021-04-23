package com.stonefacesoft.ottaa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseDatabaseRequest;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.InmersiveMode;
import com.stonefacesoft.ottaa.utils.preferences.DataUser;
import com.stonefacesoft.ottaa.utils.preferences.PreferencesUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

public class LoginActivity2Step2 extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "LoginActivity2Step2";
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
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
    boolean convert;
    DataUser userData;
    private String gender;
    private PreferencesUtil preferencesUtil;
    //User variables
    private FirebaseAuth mAuth;
    private FirebaseDatabaseRequest databaseRequest;
    private AnalyticsFirebase mAnalyticsFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new InmersiveMode(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_2);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesUtil = new PreferencesUtil(preferences);
        mAuth = FirebaseAuth.getInstance();
        mAnalyticsFirebase = new AnalyticsFirebase(this);


        bindUI();

        animateEntrance();

        fillUserData();

    }

    private void bindUI() {
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

        genderSelector = findViewById(R.id.selectorGender);
        genderSelector.setOnItemSelectedListener(this);

        new DateInputMask(editTextBirthday);
        databaseRequest = new FirebaseDatabaseRequest(this);
        convert = true;
        userData = new DataUser();


        //TODO remove the keyboard when entering the screen

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void animateEntrance() {
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
        switch (view.getId()) {
            case R.id.nextButton:
                if (availableUserData()) {
                    setUpUserData();
                    Intent intent = new Intent(LoginActivity2Step2.this, LoginActivity2Step3.class);
                    startActivity(intent);
                    finish();
                    mAnalyticsFirebase.customEvents("Touch", "LoginActivityStep2", "Next1 ");
                } else {
                    Toast.makeText(this, "Estos datos son necesarios para la prediccion", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.back_button:
                mAnalyticsFirebase.customEvents("Touch", "LoginActivityStep2", "Back1");
                Intent intent2 = new Intent(LoginActivity2Step2.this, LoginActivity2Step2.class);
                startActivity(intent2);
                break;
            case R.id.buttonCalendarDialog:
                mAnalyticsFirebase.customEvents("Touch", "LoginActivityStep2", "calendarButton");
                calendarDialog();
                break;
        }
    }

    private void fillUserData() {
        if (mAuth.getCurrentUser() != null) {
            editTextName.setText(mAuth.getCurrentUser().getDisplayName());
            //TODO el teclado no permite ver lo que se esta escribiendo
        }
    }

    public void calendarDialog() {
        Calendar calendar = Calendar.getInstance();
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MyDatePickerStyle, this, year, month, day);
        datePickerDialog.show();

    }

    public void setUpUserData() {
        userData.setFirstAndLastName(editTextName.getText().toString());
        preferencesUtil.applyStringValue("name", userData.getFirstAndLastName());
        userData.setGender(gender);
        preferencesUtil.applyStringValue(Constants.GENERO, userData.getGender());
        preferencesUtil.applyLongValue(Constants.FECHACUMPLE, userData.getBirthDate());
        databaseRequest.UploadUserData(userData);
        setUserAgePrediction();
    }


    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        convert = false;
        editTextBirthday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        userData.setBirthDate(calendar.getTime().getTime());
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0)
            gender = genderSelector.getSelectedItem().toString();
        else {
            try {
                gender = preferencesUtil.getStringValue(Constants.GENERO, genderSelector.getItemAtPosition(position + 1).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setUserAgePrediction() {
        int age = userData.getUserAge();
        Log.d(TAG, "setUserAgePrediction: " + age);
        String[] ageArray = getResources().getStringArray(R.array.list_Edad_valores);
        if (age < 12) {
            preferencesUtil.applyStringValue(Constants.EDAD, ageArray[0]);
            databaseRequest.subirEdadUsuario(ageArray[0], mAuth);
        } else if (age >= 12 && age < 20) {
            preferencesUtil.applyStringValue(Constants.EDAD, ageArray[1]);
            databaseRequest.subirEdadUsuario(ageArray[1], mAuth);
        } else {
            preferencesUtil.applyStringValue(Constants.EDAD, ageArray[2]);
            Log.d(TAG, "setUserAgePrediction: " + ageArray[2]);
            databaseRequest.subirEdadUsuario(ageArray[2], mAuth);
        }
    }

    public boolean availableUserData() {
        return !editTextName.getText().toString().isEmpty() && validateGenderSelection() && validateJavaDate(editTextBirthday.getText().toString());
    }

    private boolean validateGenderSelection() {
        return genderSelector.getSelectedItemPosition() != 0;
    }

    /**
     * https://howtodoinjava.com/java/regex/java-regex-date-format-validation/
     */
    private boolean validateJavaDate(String strDate) {
        /* Check if date is 'null' */
        String regex = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
        Pattern pattern = Pattern.compile(regex);
        if (strDate.isEmpty())
            return false;
        else
            return pattern.matcher(strDate).matches();
    }

    class DateInputMask implements TextWatcher {
        //Source https://stackoverflow.com/questions/16889502/how-to-mask-an-edittext-to-show-the-dd-mm-yyyy-date-format
        private final String ddmmyyyy = "DDMMYYYY";
        private final Calendar cal = Calendar.getInstance();
        private final EditText input;
        private String current = "";

        public DateInputMask(EditText input) {
            this.input = input;
            this.input.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (convert) {
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
            convert = true;
        }
    }
}



