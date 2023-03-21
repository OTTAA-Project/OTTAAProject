package com.stonefacesoft.ottaa;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseDatabaseRequest;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.Interfaces.CalendarChangeEvent;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.Interfaces.LoadUserInformation;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.DateTextWatcher;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.InmersiveMode;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.preferences.DataUser;
import com.stonefacesoft.ottaa.utils.preferences.PreferencesUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class LoginActivity2Step2 extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener, FirebaseSuccessListener, LoadUserInformation, CalendarChangeEvent {
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
    private StorageReference mStorageRef;

    //User variables
    private FirebaseAuth mAuth;
    private FirebaseDatabaseRequest databaseRequest;
    private AnalyticsFirebase mAnalyticsFirebase;
    private FirebaseUtils firebaseUtils;
    private DateTextWatcher dateTextWatcher;
    private BajarJsonFirebase mBajarJsonFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new InmersiveMode(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_2);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesUtil = new PreferencesUtil(preferences);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseUtils = FirebaseUtils.getInstance();
        firebaseUtils.setmContext(this);
        firebaseUtils.setUpFirebaseDatabase();
        mAnalyticsFirebase = new AnalyticsFirebase(this);



        bindUI();

        animateEntrance();

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
        buttonPrevious.setVisibility(View.INVISIBLE);

        editTextName = findViewById(R.id.editTextName);
        editTextName.setInputType(InputType.TYPE_NULL);
        editTextBirthday = findViewById(R.id.editTextBirthday);
        dateTextWatcher = new DateTextWatcher(editTextBirthday,this);
        genderSelector = findViewById(R.id.selectorGender);
        genderSelector.setOnItemSelectedListener(this);
        databaseRequest = new FirebaseDatabaseRequest(this);
        convert = true;
        databaseRequest.FillUserInformation(this);
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
        switch (id) {
            case R.id.nextButton:
                if (availableUserData()) {
                    setUpUserData();
                    BajarJsonFirebase bajarJsonFirebase = new BajarJsonFirebase(preferencesUtil.getPreferences(), mAuth, this);
                    bajarJsonFirebase.setInterfaz(this);
                    if (ConnectionDetector.isNetworkAvailable(this)) {
                        final StorageReference mPredictionRef = mStorageRef.child("Archivos_Sugerencias").child("pictos_" + preferencesUtil.getStringValue("prefSexo", "FEMENINO") + "_" + preferencesUtil.getStringValue("prefEdad", "JOVEN") + ".txt");
                        bajarJsonFirebase.descargarPictosDatabase(mPredictionRef);
                    }else{
                        onPictosSugeridosBajados(true);
                    }

                } else {
                    Toast.makeText(this, this.getResources().getText(R.string.prediction_data), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.backButton:
                /*
                Log.e(TAG, "onClick: BackButton stept 1" );
                mAnalyticsFirebase.customEvents("Touch", "LoginActivityStep2", "Back1");
                Intent intent2 = new Intent(LoginActivity2Step2.this, LoginActivity2Step2.class);
                startActivity(intent2);
                finishAfterTransition();*/
                break;
            case R.id.buttonCalendarDialog:
                mAnalyticsFirebase.customEvents("Touch", "LoginActivityStep2", "calendarButton");
                calendarDialog();
                break;
        }
    }

    @Override
    public void getUserInformation(DataUser dataUser) {
            userData = dataUser;
            fillUserData();
    }

    @Override
    public void fillUserData() {
        if (mAuth.getCurrentUser() != null) {
            getUserData();
            if(!userData.getFirstAndLastName().isEmpty())
                editTextName.setText(userData.getFirstAndLastName());
            else
                editTextName.setText(mAuth.getCurrentUser().getDisplayName());
            if(userData.getBirthDate()>0)
                setUpDateUser(userData.getBirthDate());
            if(!userData.getGender().isEmpty()){
                selectIndicator(userData.getGender());
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    editTextName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                }
            }, 2500);

            }

    }

    private void selectIndicator(String gender) {
        switch (gender){
            case "Masculino":
                genderSelector.setSelection(1);
            break;
            case "Femenino":
                genderSelector.setSelection(2);
            break;
            case "Fluid":
                genderSelector.setSelection(3);
            break;
            case "Binary":
                genderSelector.setSelection(4);
            break;
            case "Other":
                genderSelector.setSelection(5);
            break;
            default:
                genderSelector.setSelection(0);
        }
    }

    public void setUpDateUser(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        String value =getValue(day)+""+getValue(month)+""+year;
        editTextBirthday.setText(value);
        dateTextWatcher.validateTextWatcher();
    }



    public String  getValue(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    public void calendarDialog() {
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        editTextBirthday.setText(getValue(dayOfMonth)+""+getValue(month + 1)+""+year);
        dateTextWatcher.validateTextWatcher();
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
        String regex = "^(1[0-9]|0[1-9]|3[0-1]|2[0-9])/(0[1-9]|1[0-2])/[0-9]{4}$";
        String aux = strDate.replaceAll("٠","0").replaceAll("١","1").replaceAll("٢","2").
                replaceAll("٣","3").replaceAll("٤","4").replaceAll("٥","5").replaceAll("٦","6")
                .replaceAll("٧","7").replaceAll("٨","8").replaceAll("٩","9");
        Pattern pattern = Pattern.compile(regex);

        if (aux.isEmpty())
            return false;
        else
            return pattern.matcher(aux).matches();
    }

    @Override
    public void onDescargaCompleta(int descargaCompleta) {

    }

    @Override
    public void onDatosEncontrados(int datosEncontrados) {

    }

    @Override
    public void onFotoDescargada(int fotosDescargadas) {

    }

    @Override
    public void onArchivosSubidos(boolean subidos) {

    }

    @Override
    public void onPictosSugeridosBajados(boolean descargado) {
        downloadFiles();
    }

    public void toogleKeyBoard(TextView view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    @Override
    public void setCalendarDate(long value) {
        if(userData != null)
            userData.setBirthDate(value);
    }

    public DataUser getUserData() {
        if(userData == null)
            userData = new DataUser();
        return userData;
    }
    public void downloadFiles() {
        String locale = Locale.getDefault().getLanguage();
        File rootPath = new File(getApplicationContext().getCacheDir(), "Archivos_OTTAA");
        if (!rootPath.exists()) {
            rootPath.mkdirs();//si no existe el directorio lo creamos
        }
        if(mBajarJsonFirebase == null)
            mBajarJsonFirebase = new BajarJsonFirebase(preferencesUtil.getPreferences(),mAuth,this);
        ObservableInteger observableInteger = new ObservableInteger();
        observableInteger.setOnIntegerChangeListener(new ObservableInteger.OnIntegerChangeListener() {
            @Override
            public void onIntegerChanged(int newValue) {
                if(newValue == 1) {
                    Intent intent = new Intent(LoginActivity2Step2.this, LoginActivity2Step3.class);
                    startActivity(intent);
                    finishAfterTransition();
                    mAnalyticsFirebase.customEvents("Touch", "LoginActivityStep2", "Next1 ");
                }
            }
        });
        mBajarJsonFirebase.bajarPictos(locale, observableInteger);


    }

}



