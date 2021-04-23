package com.stonefacesoft.ottaa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.RecyclerViews.PhrasesRecyclerView;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.preferences.User;

public class VincularFrases extends AppCompatActivity implements View.OnClickListener {
    private ViewPager2 viewPager2;
    private PhrasesRecyclerView recyclerView;
    private User firebaseUser;
    private SubirArchivosFirebase subirArchivos;
    private AnalyticsFirebase mAnalyticsFirebase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        subirArchivos=new SubirArchivosFirebase(this);

        boolean status_bar = intent.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_galeria_grupos2);
        firebaseUser=new User(this);
        initComponents();
    }
    public void initComponents(){
        mAnalyticsFirebase=new AnalyticsFirebase(this);
        viewPager2=findViewById(R.id.viewPager_groups);
        viewPager2.setVisibility(View.GONE);
        recyclerView=new PhrasesRecyclerView(this,firebaseUser.getmAuth());
        ImageButton foward=findViewById(R.id.down_button);
        ImageButton previous=findViewById(R.id.up_button);
        ImageButton exit=findViewById(R.id.back_button);
        ImageButton btnEditar=findViewById(R.id.edit_button);
        btnEditar.setVisibility(View.VISIBLE);
        btnEditar.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_save_white_24));
        btnEditar.setOnClickListener(this);
        foward.setOnClickListener(this);
        previous.setOnClickListener(this);
        exit.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_button:
                mAnalyticsFirebase.customEvents("Touch","VincularFrases","Favorite Phrases UpButton");
                recyclerView.scrollTo(false);
                break;
            case R.id.down_button:
                mAnalyticsFirebase.customEvents("Touch","VincularFrases","Favorite Phrases DownButton");
                recyclerView.scrollTo(true);
                break;
            case R.id.back_button:
                mAnalyticsFirebase.customEvents("Touch","VincularFrases","Favorite Phrases BackButton");
                onBackPressed();
                break;
            case R.id.edit_button:
                mAnalyticsFirebase.customEvents("Touch","VincularFrases","SaveFavoritePhrases");
                recyclerView.savePhrases();
                subirArchivos.uploadFavoritePhrases(subirArchivos.getmDatabase(firebaseUser.getmAuth(), Constants.FrasesFavoritas),subirArchivos.getmStorageRef(firebaseUser.getmAuth(),Constants.FrasesFavoritas));
                onBackPressed();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent databack = new Intent();
        setResult(IntentCode.GALERIA_GRUPOS.getCode(), databack);
    }
}
