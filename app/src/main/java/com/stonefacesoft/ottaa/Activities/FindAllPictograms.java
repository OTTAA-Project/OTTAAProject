package com.stonefacesoft.ottaa.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.RecyclerViews.FindAllPictograms_Recycler_View;
import com.stonefacesoft.ottaa.utils.preferences.User;
import com.stonefacesoft.ottaa.utils.textToSpeech;

public class FindAllPictograms extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FindAllPictograms_Recycler_View findAllPictograms_recycler_view;
    private User mUser;
    private SearchView searchView;
    private SharedPreferences sharedPrefsDefault;
    private textToSpeech myTTS;
    private SubirArchivosFirebase uploadFile;
    private FirebaseUtils firebaseUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findallpictograms);
        mUser= new User(this);
        initComponents();
    }

    public void initComponents(){
        sharedPrefsDefault= PreferenceManager.getDefaultSharedPreferences(this);
        recyclerView=findViewById(R.id.recyclerView);
        searchView=findViewById(R.id.searchView);
        myTTS = textToSpeech.getInstance(this);
        firebaseUtils=FirebaseUtils.getInstance();
        firebaseUtils.setmContext(this);
        uploadFile=new SubirArchivosFirebase(this);
        setUpFindAllPictograms();
    }

    public void setUpFindAllPictograms(){
        findAllPictograms_recycler_view= new FindAllPictograms_Recycler_View(this,mUser.getmAuth());
        findAllPictograms_recycler_view.setSearchView(searchView);
        findAllPictograms_recycler_view.setSharedPrefsDefault(sharedPrefsDefault);
        findAllPictograms_recycler_view.setMyTTS(myTTS);
        findAllPictograms_recycler_view.setArray();
        findAllPictograms_recycler_view.setUploadFirebaseFile(uploadFile);
        findAllPictograms_recycler_view.showRecyclerView(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        findAllPictograms_recycler_view.getProgress_dialog_options().destruirDialogo();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
