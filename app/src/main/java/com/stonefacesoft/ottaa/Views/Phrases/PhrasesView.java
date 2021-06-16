package com.stonefacesoft.ottaa.Views.Phrases;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.ReturnPositionItem;
import com.stonefacesoft.ottaa.utils.preferences.User;

public class PhrasesView extends AppCompatActivity implements View.OnClickListener {
    protected ReturnPositionItem returnPositionItem;
    protected User firebaseUser;
    protected ViewPager2 viewPager2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_grupos2);
        viewPager2=findViewById(R.id.viewPager_groups);
        viewPager2.setVisibility(View.GONE);
        firebaseUser =  new User(this);
        initComponents();
    }


    public  void initComponents(){
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {

    }
}
