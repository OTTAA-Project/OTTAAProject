package com.stonefacesoft.ottaa;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.perf.metrics.AddTrace;
import com.stonefacesoft.ottaa.Bitmap.GestionarBitmap;
import com.stonefacesoft.ottaa.Interfaces.CloudFunctionResponse;
import com.stonefacesoft.ottaa.Interfaces.LoadOnlinePictograms;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.CircularProgressBar;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ActivityInformes extends AppCompatActivity implements CloudFunctionResponse {

    private static final String TAG = "ActivityInformes";
    ImageView imgProfilePic, imageInternet, imgMostUsedSentences;
    CircularProgressBar progressBarScore, progressBarVocabulario1, progressBarVocabulario2, progressBarVocabulario3;
    TextView textViewVocabulario1, textViewVocabulario2, textViewVocabulario3, textViewFrasesUltimo7dias, textViewPromedioPictoPorFrase, textViewScore;
    GlideAttatcher glideAttatcher;
    LineChart lineChart;
    CardView cardViewScore, cardViewUsoGrupo, cardViewActividad, cardViewUltimos7Dias, cardViewPromedioFrase, cardViewFraseMasUsada;
    ConnectionDetector connectionDetector;
    CloudFunctionResponse cloudFunctionResponse;
    private Handler handler;
    private int pos = 0;
    private GestionarBitmap gestionarBitmap;
    ScoreHelper scoreHelper;

    @AddTrace(name = "ActivityInformes", enabled = true /* optional */)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe_2);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        binding();
        if (connectionDetector.isConnectedToInternet()) {
            imageInternet.setVisibility(View.INVISIBLE);
            doHTTPRequestFirst();
            doHTTPRequestSecond();
            glideAttatcher = new GlideAttatcher(this);

            loadProfilePic();
            //loadAnimateProgressBar(progressBarScore, 65);

        } else {

            cardViewScore.setVisibility(View.INVISIBLE);
            cardViewActividad.setVisibility(View.INVISIBLE);
            cardViewFraseMasUsada.setVisibility(View.INVISIBLE);
            cardViewPromedioFrase.setVisibility(View.INVISIBLE);
            cardViewUltimos7Dias.setVisibility(View.INVISIBLE);
            cardViewUsoGrupo.setVisibility(View.INVISIBLE);

            imageInternet.setVisibility(View.VISIBLE);
            CustomToast customToast = CustomToast.getInstance(this);
            customToast.mostrarFrase(getResources().getString(R.string.problema_inet));

        }


    }

    private void loadProfilePic() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Uri photoUrl = user.getPhotoUrl();
//            String str = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Pierre-Person.jpg/1024px-Pierre-Person.jpg";
            String str = photoUrl.toString();
//            glideAttatcher.attachedOnImaView(imgProfilePic,photoUrl);
            Glide.with(this).load(str).placeholder(R.drawable.ic_baseline_person_24).dontAnimate().error(R.drawable.ic_baseline_person_24).circleCrop().into(imgProfilePic);
        }
    }

    private void loadAnimateProgressBar(CircularProgressBar progressBar, float progressValue) {
        //progressBar.setProgress(progress);
        ObjectAnimator.ofFloat(progressBar, "progress", progressValue)
                .setDuration(3000)
                .start();
    }

    private void loadAnimateTextView(TextView textView, String string) {
        textView.setText(string);
        ScaleAnimation scal = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scal.setDuration(1000);
        scal.setFillAfter(true);
        textView.setAnimation(scal);
        textView.startAnimation(scal);
    }

    private void loadAnimateImageView(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        ScaleAnimation scal = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scal.setDuration(800);
        scal.setFillAfter(true);
        imageView.setAnimation(scal);
        imageView.startAnimation(scal);
    }


    private void loadAnimateLineGraph(JSONObject jsonObject) {
        List<Entry> mesActual = new ArrayList<>();
        List<Entry> mesAnterior = new ArrayList<Entry>();
        String[] xLabels = new String[7];
        DateFormat df = new SimpleDateFormat("MMM-dd", Locale.ENGLISH);

        JSONArray jsonArray = jsonObject.names();
        if (jsonArray != null) {
            for (int i = 0; i <= jsonArray.length(); i++) {
                try {
                    Entry sem = new Entry(i, jsonObject.getInt(String.valueOf(jsonArray.get(jsonArray.length() - i - 1))));
                    xLabels[i] = df.format(new Date(Long.parseLong(jsonArray.get(jsonArray.length() - i - 1).toString())));
                    mesActual.add(sem);
                    //Log.e(TAG, "loadAnimateLineGraph: name: "+ new Date(Long.parseLong(jsonArray.get(i).toString())) + " value: "+ jsonObject.getInt(String.valueOf(jsonArray.get(i))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        //TODO modificar la cloud function y mandar mas datos para tiempo pasado
//        Entry sem0 = new Entry(0, 10);
//        Entry sem1 = new Entry(1, 24);
//        Entry sem2 = new Entry(2, 20);
//        Entry sem3 = new Entry(3, 45);
//        Entry sem4 = new Entry(4, 5);
//        Entry sem5 = new Entry(5, 9);
//        Entry sem6 = new Entry(6, 13);
//        mesActual.add(sem0);
//        mesActual.add(sem1);
//        mesActual.add(sem2);
//        mesActual.add(sem3);
//        mesActual.add(sem4);
//        mesActual.add(sem5);
//        mesActual.add(sem6);
        Entry sem0 = new Entry(0, 2);
//        sem1 = new Entry(1, 11);
//        sem2 = new Entry(2, 7);
//        sem3 = new Entry(3, 15);
//        sem4 = new Entry(4, 9);
//        sem5 = new Entry(5, 1);
//        sem6 = new Entry(6, 12);
        mesAnterior.add(sem0);
//        mesAnterior.add(sem1);
//        mesAnterior.add(sem2);
//        mesAnterior.add(sem3);
//        mesAnterior.add(sem4);
//        mesAnterior.add(sem5);
//        mesAnterior.add(sem6);

        LineDataSet setComp1 = new LineDataSet(mesActual, "Mes Actual");
        LineDataSet setComp2 = new LineDataSet(mesAnterior, "Mes Anterior");
        setComp1.setColor(getResources().getColor(R.color.NaranjaOTTAA));
        //Drawable drawable = ContextCompat.getDrawable(this,R.drawable.fade_color);
        //setComp1.setGradientColor(R.color.Moccasin,R.color.NaranjaOTTAA);
        setComp1.setDrawCircles(false);
        setComp1.setLineWidth(4f);
        setComp1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        setComp2.setColor(getResources().getColor(R.color.colorLightGray));
        setComp2.setDrawCircles(false);
        setComp2.setLineWidth(4f);
        setComp2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        setComp2.enableDashedLine(5, 5, 1);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);


        // use the interface ILineDataSet
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);


        //ValueFormatter xAxisFormatter = new DayAxisValueFormatter(lineChart);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);

        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);

        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        //xAxis.setValueFormatter(xAxisFormatter);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

        YAxis left = lineChart.getAxisLeft();
        left.setDrawLabels(true); // no axis labels
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(true); // no grid lines
        left.setDrawZeroLine(true); // draw a zero line
        lineChart.getAxisRight().setEnabled(false);

        LineData data = new LineData(dataSets);

        data.setDrawValues(false);
        Description description=new Description();
        description.setText("");
        lineChart.setDescription(description);
        lineChart.setData(data);
        lineChart.animateX(2500, Easing.EaseInElastic);

    }

    private void binding() {

        imgProfilePic = findViewById(R.id.imageViewProfilePic);
        imageInternet = findViewById(R.id.imageViewNoInternet);
        imgMostUsedSentences = findViewById(R.id.frasesMasUsadas);

        progressBarScore = findViewById(R.id.progressBar);
        progressBarVocabulario1 = findViewById(R.id.progressBar1);
        progressBarVocabulario2 = findViewById(R.id.progressBar2);
        progressBarVocabulario3 = findViewById(R.id.progressBar3);

        textViewVocabulario1 = findViewById(R.id.textViewVocabulario1);
        textViewVocabulario2 = findViewById(R.id.textViewVocabulario2);
        textViewVocabulario3 = findViewById(R.id.textViewVocabulario3);
        textViewScore = findViewById(R.id.textViewNivel);

        textViewFrasesUltimo7dias = findViewById(R.id.textViewFrasesUltimos7Dias);
        textViewPromedioPictoPorFrase = findViewById(R.id.textViewPromedioPictoPorFrase);

        lineChart = findViewById(R.id.lineChartUsoFrases);

        connectionDetector = new ConnectionDetector(this);

        gestionarBitmap = new GestionarBitmap(this);

        handler = new Handler();

        cardViewScore = findViewById(R.id.cardViewScore);
        cardViewUsoGrupo = findViewById(R.id.cardViewUsoGrupos);
        cardViewActividad = findViewById(R.id.cardViewActividad);
        cardViewUltimos7Dias = findViewById(R.id.cardViewUltimo7dias);
        cardViewPromedioFrase = findViewById(R.id.cardViewPromedioFrase);
        cardViewFraseMasUsada = findViewById(R.id.cardViewFrasesMasUsadas);

        cloudFunctionResponse = this;

        scoreHelper = new ScoreHelper();

    }
    //https://firebase.google.com/docs/functions/callable

    private void doHTTPRequestFirst() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://us-central1-ottaa-project.cloudfunctions.net/readFile";

        // Request a string response from the provided URL.
        // Display the first 500 characters of the response string.
        //Log.e(TAG, "onResponse: "+response);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                this::
                        parseReponse, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onResponse: Volley Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                params.put("UserID", user.getEmail());
                params.put("Language",ConfigurarIdioma.getLanguaje());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseReponse(String response) {
        try {
            Log.d(TAG, "parseResponse: OK");
            JSONObject jsonObject = new JSONObject(response);
            loadAnimateTextView(textViewFrasesUltimo7dias, String.valueOf(jsonObject.getInt("frases7days")));
            loadAnimateTextView(textViewPromedioPictoPorFrase, String.valueOf(jsonObject.getDouble("averagePictoFrase")).substring(0, 3));
            loadAnimateLineGraph(jsonObject.getJSONObject("frecLast7days"));
            scoreHelper.setFrases7days(jsonObject.getInt("frases7days"));
            scoreHelper.setAveragePictoFrase(jsonObject.getDouble("averagePictoFrase"));
            scoreHelper.setLast7DaysUsageJson(jsonObject.getJSONObject("frecLast7days"));
            cloudFunctionResponse.getCloudFunctionResponse(1);
        } catch (JSONException err) {
            Log.e(TAG, "parseResponse: Error: " + err);
        }

    }

    private void doHTTPRequestSecond() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://us-central1-ottaa-project.cloudfunctions.net/onReqFunc";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e(TAG, "onResponse2: ");
                        parseReponse2(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onResponse2: Volley Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //TODO revisar que este assert este bien
                assert user != null;
                params.put("UserID", user.getEmail());
                params.put("Language", ConfigurarIdioma.getLanguaje());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseReponse2(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray mostUsedSentencesArray = jsonObject.getJSONArray("most_used_sentences");
            JSONArray groupUsageArray = jsonObject.getJSONArray("picto_usage_per_group");
            gestionarBitmap.getBitmapDeFrase(mostUsedSentencesArray.getJSONObject(0), new LoadOnlinePictograms() {
                @Override
                public void preparePictograms() {}
                @Override
                public void loadPictograms(Bitmap bitmap) {
                    imgMostUsedSentences.setImageBitmap(bitmap);
                }

                @Override
                public void FileIsCreated() {

                }

                @Override
                public void FileIsCreated(Bitmap bitmap) {

                }

            });
            MyRunnable obj = new MyRunnable(mostUsedSentencesArray);
            handler.post(obj);

            Log.d(TAG, "parseReponse2: "+groupUsageArray.toString());
            loadGroupUsage(groupUsageArray);
            scoreHelper.setGroupUsage(groupUsageArray);
            cloudFunctionResponse.getCloudFunctionResponse(2);
            Log.d(TAG, "parseResponse2: OK");
        } catch (JSONException err) {
            Log.e(TAG, "parseResponse2: Error: " + err);
        }

    }

    /**
     *
     * This method loads the most used sentences to an Imageview that will switch images automatically
     */
    private void loadMostUsedSentences(JSONArray jsonArray) {
        if (jsonArray.length() > 0) {
            try {
                gestionarBitmap.getBitmapDeFrase(jsonArray.getJSONObject(pos), new LoadOnlinePictograms() {
                    @Override
                    public void preparePictograms() { }
                    @Override
                    public void loadPictograms(Bitmap bitmap) {
                        loadAnimateImageView(imgMostUsedSentences, bitmap);
                        }

                    @Override
                    public void FileIsCreated() {

                    }

                    @Override
                    public void FileIsCreated(Bitmap bitmap) {

                    }
                });
                //txtViewFraseUsada.setText(mDatosDeUso.getFrasesOrdenadas().get(pos).getString("frase"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (pos++ >= jsonArray.length())
                pos = 0;

        }
    }

    private void loadGroupUsage(JSONArray jsonArray) {
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonList.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(jsonList, new Comparator<JSONObject>() {
            public int compare(JSONObject a, JSONObject b) {
                double valA = 0.0;
                double valB = 0.0;
                try {
                    //Log.e(TAG, "compare: " + a.get("percentage"));
                    valA = Double.parseDouble(a.getString("percentage"));
                    valB = Double.parseDouble(b.getString("percentage"));
                } catch (JSONException e) {
                    Log.e(TAG, "compare: Error sorting the arrayList");
                }

                return compareTo(valA, valB);
            }
        });

        try {
            loadAnimateProgressBar(progressBarVocabulario1, (float) jsonList.get(0).getDouble("percentage"));
            loadAnimateTextView(textViewVocabulario1, jsonList.get(0).getJSONObject("name").getString(ConfigurarIdioma.getLanguaje()));
            loadAnimateProgressBar(progressBarVocabulario2, (float) jsonList.get(1).getDouble("percentage"));
            loadAnimateTextView(textViewVocabulario2, jsonList.get(1).getJSONObject("name").getString(ConfigurarIdioma.getLanguaje()));
            loadAnimateProgressBar(progressBarVocabulario3, (float) jsonList.get(2).getDouble("percentage"));
            loadAnimateTextView(textViewVocabulario3, jsonList.get(2).getJSONObject("name").getString(ConfigurarIdioma.getLanguaje()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private int compareTo(double frec1, double frec2) {
//        // descending order
        if (frec1 > frec2)
            return -1;
        if (frec2 > frec1)
            return 1;
        return (int) (frec2 - frec1);
    }

    //Interface to liosten and wait for the responses.
    @Override
    public void getCloudFunctionResponse(int responseNumber) {
        Log.d(TAG, "getCloudFunctionResponse: OK");
        if (responseNumber == 1)
            scoreHelper.setFlagResponse1(true);
        if (responseNumber == 2)
            scoreHelper.setFlagResponse2(true);

        if (scoreHelper.isScoreReady()) {
            int score = scoreHelper.getScore();
            loadAnimateTextView(textViewScore, getString(R.string.str_level) + " " + score);
            loadAnimateProgressBar(progressBarScore, score * 10);
        }

    }

    /**
     * This runnable change the position and select automatically the phrase when callback this method
     */

    private class MyRunnable implements Runnable {
        private final JSONArray jsonArray;

        public MyRunnable(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        @Override
        public void run() {
            loadMostUsedSentences(jsonArray);
            handler.postDelayed(this, 4000);
        }

    }


    protected class ScoreHelper {
        private JSONArray groupUsage;
        private int frases7days;
        private double averagePictoFrase;
        private JSONObject last7DaysUsageJson;
        private boolean flagResponse1, flagResponse2;


        public void setFlagResponse1(boolean flagResponse1) {
            this.flagResponse1 = flagResponse1;
        }

        public void setFlagResponse2(boolean flagResponse2) {
            this.flagResponse2 = flagResponse2;
        }

        public void setGroupUsage(JSONArray groupUsage) {
            this.groupUsage = groupUsage;
        }

        public void setFrases7days(int frases7days) throws JSONException {
            this.frases7days = frases7days;
        }

        public void setAveragePictoFrase(double averagePictoFrase) throws JSONException {
            this.averagePictoFrase = averagePictoFrase;

        }

        public void setLast7DaysUsageJson(JSONObject jsonObject) throws JSONException {
            this.last7DaysUsageJson = jsonObject;
        }

        //Hacer un listener que modifique una variable global y lanzarlo 2 veces, cuando las variables digan que se puede ejecutar se ejecuta.
        public int getScore() {
            //Formula para calcular los niveles
            // score = usoUltimos7dias * a + frasesUltimos7Dias * b + promedioPictoPorFrase * c + gruposUsados * d + totalFrases * 300
            //maxScore = 7 * a + 700 * b + 5 * c + 44 * d = 10000
            //1000 puntos son un nivel.

            int a = 500, b = 3, c = 500, d = 44, level = 0, last7DaysUsage = 0, usedGroups = 0;
            double score = 0;

            //Recuento de frases en los ultimos 7 dias
            JSONArray jsonArray = last7DaysUsageJson.names();
            if (jsonArray != null) {
                for (int i = 0; i <= jsonArray.length(); i++) {
                    try {
                        if (last7DaysUsageJson.getInt(String.valueOf(jsonArray.get(jsonArray.length() - i - 1))) != 0)
                            last7DaysUsage++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            //Recuento de grupos utilizados
            for (int i = 0; i < groupUsage.length(); i++) {
                try {
                    if (groupUsage.getJSONObject(i).getDouble("percentage") != 0)
                        usedGroups++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            score = last7DaysUsage * a + frases7days * b + averagePictoFrase * c + usedGroups * d;
            Log.d(TAG, "getScore: " + score);
            return (int) (score / 1000);
        }

        public boolean isScoreReady() {
            return flagResponse1 && flagResponse2;
        }

    }

}