package com.stonefacesoft.ottaa.Viewpagers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Edit_Picto_Visual;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.LicenciaExpirada;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.constants.ConstantsGroupGalery;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.ValidateContext;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Gonzalo Juarez
 * @since 23/06/2020
 * Viewpager to select the pictogram
 */
public class viewpager_galeria_pictos {

    private static final String TAG = "viewPager_GP";
    private static AppCompatActivity mActivity;
    private static final float MIN_SCALE = 0.85f;
    private static final boolean edit = false;
    private int positionObject;
    private static final float MIN_ALPHA = 0.5f;
    private static Json json;
    private static textToSpeech myTTS;
    private final ViewPager2 viewPager;
    private static int parent_button;
    private static JSONArray array;
    private final ScreenSlidePagerAdapter screenSlidePagerAdapter;
    private final fragmentPicto fragment;
    private boolean isSelectedItem;
    private final SharedPreferences sharedPrefDefault;




    public viewpager_galeria_pictos(AppCompatActivity mActivity, textToSpeech myTTS, int parent_button) {
        viewpager_galeria_pictos.mActivity = mActivity;
        viewpager_galeria_pictos.myTTS = myTTS;
        viewpager_galeria_pictos.parent_button = parent_button;
        this.sharedPrefDefault = PreferenceManager.getDefaultSharedPreferences(mActivity);
        this.viewPager = viewpager_galeria_pictos.mActivity.findViewById(R.id.viewPager_groups);
        json = Json.getInstance();
        json.setmContext(mActivity);
        array = json.getHijosGrupo2(viewpager_galeria_pictos.parent_button);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        fragment = new fragmentPicto();
        screenSlidePagerAdapter = new ScreenSlidePagerAdapter(viewpager_galeria_pictos.mActivity);
        viewPager.setAdapter(screenSlidePagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.canScrollVertically(View.SCROLL_INDICATOR_BOTTOM);
        viewPager.setPageTransformer(new ViewPagerTransformationEffects(1));

    }

    public ViewPager2 getViewPager() {
        return viewPager;
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new fragmentPicto().newInstance(position);
        }

        @Override
        public int getItemCount() {
            try {
                return array.length();
            } catch (Exception ex) {
                return 0;
            }
        }


    }

    public void setArray(JSONArray array) {
        json = Json.getInstance();
        json.setmContext(mActivity);
        viewpager_galeria_pictos.array = array;
        screenSlidePagerAdapter.notifyDataSetChanged();

    }


    public void scrollPosition(boolean add) {
        int position = viewPager.getCurrentItem();
        isSelectedItem = false;
        if (add)
            position++;
        else
            position--;
        if (position >= array.length())
            position = 0;
        else if (position < 0)
            position = array.length();
        viewPager.setCurrentItem(position);
    }

    public View getItem() {
        return viewPager.getChildAt(viewPager.getCurrentItem());
    }

    public void showViewPager(boolean show) {
        if (show)
            viewPager.setVisibility(View.VISIBLE);
        else
            viewPager.setVisibility(View.GONE);
    }

    public void editItem(boolean isPremium) {
        if (isPremium) {
            Intent intent = new Intent(mActivity, Edit_Picto_Visual.class);
            intent.putExtra("PositionPadre", parent_button);
            intent.putExtra("PositionHijo", viewPager.getCurrentItem());
            try {
                intent.putExtra("PictoID", json.getId(array.getJSONObject(viewPager.getCurrentItem())));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "editItem: Error: " + e.getMessage());
            }
            try {
                intent.putExtra("Texto", JSONutils.getNombre(array.getJSONObject(viewPager.getCurrentItem()),ConfigurarIdioma.getLanguaje()));
            } catch (JSONException e) {
                Log.e(TAG, "editItem: Error: " + e.getMessage());
            }
            intent.putExtra("esGrupo", false);
            myTTS.hablar(mActivity.getString(R.string.editar_pictogram));
            mActivity.startActivityForResult(intent, IntentCode.EDITARPICTO.getCode());

        } else {
            Intent i = new Intent(mActivity, LicenciaExpirada.class);
            mActivity.startActivity(i);
        }


    }


    public void OnClickItem() {
        if (array.length() > 0) {
            int position = viewPager.getCurrentItem();
            if (isSelectedItem) {
               finishApp(position);
            } else {
                sayName(position);
                isSelectedItem = true;
            }
        }
    }

    public final void finishApp(int position){
        int id = 0;
        try {
            id=json.getId(json.getJsonByPosition(array,position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mActivity.setResult(ConstantsGroupGalery.RETURNGALERIAPICTOSDATA, new Intent().putExtra("ID",id).putExtra("Boton",parent_button));
        mActivity.finish();
    }

    public void sayName(int position){
        String name = "";
        try {
            name = JSONutils.getNombre(array.getJSONObject(position),ConfigurarIdioma.getLanguaje());
        } catch (JSONException e) {
            Log.e(TAG, "OnClickItem: Error: " + e.getMessage());
        }
        myTTS.hablar(name);
    }

    public static class fragmentPicto extends Fragment {
        private View view;
        private int position;
        private boolean editar;
        private PictoView picto1;

        public fragmentPicto() {

        }


        public viewpager_galeria_pictos.fragmentPicto newInstance(Integer position1){
            viewpager_galeria_pictos.fragmentPicto fragmentPicto=new viewpager_galeria_pictos.fragmentPicto();
            Bundle args = new Bundle();
            args.putInt("position",position1);
            fragmentPicto.setArguments(args);
            return fragmentPicto;
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
            return inflater.inflate(R.layout.picto_components, container, false);
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }
        // creo la vista
        @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            picto1=view.findViewById(R.id.Option1);
            try {
                picto1.setUpContext(view.getContext());
                picto1.setUpGlideAttatcher(view.getContext());
                JSONObject object = array.getJSONObject(position);
                if(object != null)
                    picto1.setPictogramsLibraryPictogram(new Pictogram(object,ConfigurarIdioma.getLanguaje()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadPictogram(picto1);

        }

        @Override
        public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
            super.onViewStateRestored(savedInstanceState);
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
        }

        @Override
        public void onLowMemory() {
            super.onLowMemory();
            if(picto1 != null)
                picto1.getGlideAttatcher().clearMemory();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if(picto1 != null){
                if(ValidateContext.isValidContext(picto1.getContext()))
                    picto1.getGlideAttatcher().clearMemory();
            }
        }



        private void loadPictogram(PictoView custom_picto){
                custom_picto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(custom_picto.isClicked()){
                              Intent databack = new Intent();
                            try {
                                databack.putExtra("ID",array.getJSONObject(position).getInt("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG, "onClick: Error" + e.getMessage());
                            }
                            databack.putExtra("Boton", parent_button);
                            mActivity.setResult(IntentCode.GALERIA_PICTOS.getCode(), databack);
                            mActivity.finish();
                        }else{
                            custom_picto.setClicked(true);
                            myTTS.hablar(custom_picto.getCustom_Texto());
                        }
                    }
                });
        }
    }


}
