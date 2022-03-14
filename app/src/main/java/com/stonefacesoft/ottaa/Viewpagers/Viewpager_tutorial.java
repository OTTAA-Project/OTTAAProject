package com.stonefacesoft.ottaa.Viewpagers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.customComponents.Custom_ImageDescription;

public class Viewpager_tutorial extends AppCompatActivity {

    private static final String TAG = "Viewpager_tutorial";
    private static ViewPager2 viewPager;
    private static final Custom_ImageDescription[] imageDescriptions = new Custom_ImageDescription[4];
    private static ScreenSlidePagerAdapter pagerAdapter;
    private static FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorialview);
        viewPager = findViewById(R.id.viewpager);
        //TODO hay dos colores color del titulo y color del texto
        setImageDescriptions(0, R.drawable.ic_tuto1,getResources().getString(R.string.tutorial01), getResources().getString(R.string.createyourphrases),R.color.NaranjaOTTAA,R.color.White);
        setImageDescriptions(1, R.drawable.ic_tuto2, getResources().getString(R.string.tutorial02), getResources().getString(R.string.talktotheworld),R.color.quantum_grey300,R.color.NaranjaOTTAA);
        setImageDescriptions(2, R.drawable.ic_tuto3,  getResources().getString(R.string.tutorial03),getResources().getString(R.string.accessthousandof) ,R.color.NaranjaOTTAA,R.color.White);
        setImageDescriptions(3, R.drawable.ic_tuto4,  getResources().getString(R.string.tutorial04), getResources().getString(R.string.playandlearn),R.color.quantum_grey300,R.color.NaranjaOTTAA);
        pagerAdapter = new Viewpager_tutorial.ScreenSlidePagerAdapter(this);
        floatingActionButton=findViewById(R.id.exit);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.canScrollVertically(View.SCROLL_INDICATOR_BOTTOM);
        viewPager.setPageTransformer(new ViewPagerTransformationEffects(1));

    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new Viewpager_tutorial.fragmentTutorial().newInstance(position);
        }

        @Override
        public int getItemCount() {
            try {
                return imageDescriptions.length;
            } catch (Exception ex){
                return 0;
            }
        }




    }



    public  static class fragmentTutorial extends Fragment implements View.OnClickListener {
        private View view;
        private  int position;
        private boolean editar;

        public fragmentTutorial(){

        }

        public fragmentTutorial newInstance(Integer position1){
            Viewpager_tutorial.fragmentTutorial fragmentGrupo=new Viewpager_tutorial.fragmentTutorial();
            Bundle args = new Bundle();
            args.putInt("position",position1);
            fragmentGrupo.setArguments(args);
            return fragmentGrupo;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(getArguments()!=null){
                position=getArguments().getInt("position");
            }
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = getView() != null ? getView() :
                    inflater.inflate(R.layout.viewpagertutorialclass, container, false);
            return view; }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            ImageView backgroundColor=view.findViewById(R.id.text_background);
            ImageView imageView = view.findViewById(R.id.icon);
            Button next=view.findViewById(R.id.btn_siguiente);
            Button previous=view.findViewById(R.id.btn_anterior);
            TextView textView = view.findViewById(R.id.description);
            TextView title = view.findViewById(R.id.title);
            imageView.setImageDrawable(view.getResources().getDrawable(imageDescriptions[position].getIdImage()));
            backgroundColor.setBackgroundColor(view.getResources().getColor(imageDescriptions[position].getBackgroundColor()));
            textView.setText(imageDescriptions[position].getDescription());
            title.setText(imageDescriptions[position].getTitle());
            title.setTextColor(view.getResources().getColor(imageDescriptions[position].getTextColor()));
            textView.setTextColor(view.getResources().getColor(imageDescriptions[position].getTextColor()));
            next.setTextColor(view.getResources().getColor(imageDescriptions[position].getTextColor()));
            previous.setTextColor(view.getResources().getColor(imageDescriptions[position].getTextColor()));
            view.setOnClickListener(this);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position==imageDescriptions.length-1)
                        floatingActionButton.callOnClick();
                    else
                        viewPager.setCurrentItem(position+1);

                }
            });
            if(position==0){
                previous.setVisibility(View.INVISIBLE);
            }else if(position==imageDescriptions.length-1)
                next.setText(getContext().getResources().getText(R.string.exit_general));
            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(position-1);

                }
            });

        }




        @Override
        public void onClick(View view) {
            try {

            }catch (Exception ex){

            }
        }

        @Nullable
        @Override
        public View getView() {
            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }
    }
    public void setImageDescriptions(int position,int id,String description,String title,int color,int textColot){
        imageDescriptions[position]=new Custom_ImageDescription(getApplicationContext(),title,description,id,color,textColot);
    }

    public  void finishActivity(){
        finish();
    }


}
