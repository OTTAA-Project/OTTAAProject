package com.stonefacesoft.ottaa.Viewpagers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.perf.metrics.AddTrace;
import com.stonefacesoft.ottaa.Edit_Picto_Visual;
import com.stonefacesoft.ottaa.GaleriaPictos3;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.LicenciaExpirada;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.Views.PictogramsGalleryGames;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.Games.TellAStoryUtils;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.pictogramslibrary.Classes.Group;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;
import com.stonefacesoft.pictogramslibrary.utils.ValidateContext;
import com.stonefacesoft.pictogramslibrary.view.GroupView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class viewpager_game_filter_view {

    protected static  String TAG = "viewpager_GG";
    protected static AppCompatActivity mActivity;
    protected static  float MIN_SCALE = 0.85f;
    protected static Json json;
    protected int positionObject;
    protected static  float MIN_ALPHA = 0.5f;
    protected static  boolean edit = false;
    protected static textToSpeech myTTS;
    protected static JSONArray array;
    protected  ViewPager2 viewPager;
    protected viewpager_game_filter_view.ScreenSlidePagerAdapter pagerAdapter;

    protected SharedPreferences sharedPrefsDefault;


    public viewpager_game_filter_view(AppCompatActivity mActivity, textToSpeech myTTS) {
        viewpager_game_filter_view.mActivity = mActivity;
        viewpager_game_filter_view.myTTS = myTTS;
        this.viewPager = mActivity.findViewById(R.id.viewPager_groups);
        json = Json.getInstance();
        json.setmContext(mActivity);
        initArray();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        pagerAdapter = new ScreenSlidePagerAdapter(viewpager_game_filter_view.mActivity);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.canScrollVertically(View.SCROLL_INDICATOR_BOTTOM);
        viewPager.setPageTransformer(new ViewPagerTransformationEffects(1));
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(mActivity);

    }

    protected void initArray(){
        array= new JSONArray();
        int[] idPictos = TellAStoryUtils.getInstance().getItem().getOptions();
        for (int i = 0; i < idPictos.length; i++) {
            array.put(json.getGrupoFromId(idPictos[i]));
        }

    }


    public ViewPager2 getViewPager() {
        return viewPager;
    }

    protected class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new viewpager_game_filter_view.fragmentGrupo().newInstance(position);
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

    public void editItem(boolean isPremium) {

        if (isPremium) {
            Intent intent = new Intent(mActivity, Edit_Picto_Visual.class);
            int id = 0;
            try {
                id = array.getJSONObject(viewPager.getCurrentItem()).getInt("id");
            } catch (JSONException e) {
                Log.e(TAG, "editItem: Error: " + e.getMessage());
            }
            Log.d(TAG, "editItem: " + positionObject);
            intent.putExtra("PositionPadre", viewPager.getCurrentItem());
            intent.putExtra("PictoID", id);
            intent.putExtra("esGrupo", true);
            Log.d(TAG, "editItem: Editando un picto");
            myTTS.hablar(mActivity.getString(R.string.editar_group));
            mActivity.startActivityForResult(intent, IntentCode.EDITARPICTO.getCode());
        } else {
            Intent i = new Intent(mActivity, LicenciaExpirada.class);
            mActivity.startActivity(i);
        }
    }


    public void scrollPosition(boolean add) {
        int position = viewPager.getCurrentItem();
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

    public void hablar() {
        try {
            myTTS.hablar(JSONutils.getNombre(array.getJSONObject(viewPager.getCurrentItem()),sharedPrefsDefault.getString(mActivity.getString(R.string.str_idioma), "en")));
        } catch (JSONException e) {
            Log.e(TAG, "hablar: Error" + e.getMessage());
        }
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
    @AddTrace(name = "updateDataViewPager", enabled = true /* optional */)
    public void updateData() {
        json = Json.getInstance();
        json.setmContext(mActivity);
        array= new JSONArray();
        int[] idPictos = TellAStoryUtils.getInstance().getItem().getOptions();
        for (int i = 0; i < idPictos.length; i++) {
            array.put(json.getGrupoFromId(idPictos[i]));
        }
    }

    public void OnClickItem() {
        Intent intent = new Intent(mActivity, PictogramsGalleryGames.class);
        JSONObject object = null;
        try {
             object = array.getJSONObject(viewPager.getCurrentItem());
        } catch (JSONException e) {
           e.printStackTrace();
        }
        if(object!=null) {
            try {
                int id = json.getId(object);
                TellAStoryUtils.getInstance().setGame(mActivity,id);
                intent.putExtra("Boton", json.getPosPicto(json.getmJSONArrayTodosLosGrupos(), json.getId(object)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            myTTS.hablarSinMostrarFrase(JSONutils.getNombre(object, sharedPrefsDefault.getString(mActivity.getString(R.string.str_idioma), "en")));
        }
        mActivity.startActivityForResult(intent, IntentCode.GALERIA_PICTOS.getCode());
    }

    public static class fragmentGrupo extends Fragment implements View.OnClickListener {
        private View view;
        private int position;
        private boolean editar;

        public fragmentGrupo() {

        }

        public viewpager_game_filter_view.fragmentGrupo newInstance(Integer position1){
            viewpager_game_filter_view.fragmentGrupo fragmentGrupo=new viewpager_game_filter_view.fragmentGrupo();
            Bundle args = new Bundle();
            args.putInt("position",position1);
            args.putBoolean("edit",edit);
            fragmentGrupo.setArguments(args);
            return fragmentGrupo;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(getArguments()!=null){
                position=getArguments().getInt("position");
                editar=getArguments().getBoolean("edit");
            }
        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            mActivity.setResult(resultCode,data);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.groups_components, container, false);
        }
        @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            GroupView grupo=view.findViewById(R.id.grupo_1);
            try {
                SharedPreferences sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(mActivity);
                JSONObject aux = array.getJSONObject(position);
                if(aux.has("imagen")&& ValidateContext.isValidContext(mActivity)) {
                    Group group = new Group(aux, ConfigurarIdioma.getLanguaje());
                    grupo.setUpContext(mActivity);
                    grupo.setUpGlideAttatcher(mActivity);
                    grupo.setPictogramsLibraryGroup(group);
                    grupo.loadAgeIcon(json.tieneTag(aux, Constants.EDAD));
                    grupo.loadGenderIcon(json.tieneTag(aux, Constants.SEXO));
                    grupo.loadLocationIcon(json.tieneTag(aux, Constants.UBICACION));
                    grupo.loadHourIcon(json.tieneTag(aux, Constants.HORA));
                }


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject object = aux;

                        Intent intent=new Intent(view.getContext(), PictogramsGalleryGames.class);
                        if(object!=null) {
                            try {
                                int id = json.getId(object);
                                TellAStoryUtils.getInstance().setGame(mActivity,id);
                                intent.putExtra("Boton", json.getPosPicto(json.getmJSONArrayTodosLosGrupos(),id));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            myTTS.hablarSinMostrarFrase(JSONutils.getNombre(object, sharedPrefsDefault.getString(mActivity.getString(R.string.str_idioma), "en")));
                            intent.putExtra("Nombre", JSONutils.getNombre(object, sharedPrefsDefault.getString(mActivity.getString(R.string.str_idioma), "en")));
                            mActivity.startActivityForResult(intent, IntentCode.GALERIA_PICTOS.getCode());
                        }
                    }
                });
            } catch (JSONException e) {
                Log.e(TAG, "onViewCreated: Error:" + e.getMessage());
            }
        }



        @Override
        public void onClick(View view) {
            try {
                view.callOnClick();
            }catch (Exception ex){
                Log.e(TAG, "onClick: Error: " + ex.getMessage());

            }
        }

        @Nullable
        @Override
        public View getView() {
            return view;
        }
    }

    public static  void loadDrawable(GlideAttatcher attatcher, Pictogram pictogram, ImageView imageView){
        if(pictogram.getEditedPictogram().isEmpty()){
            JSONObject picto=pictogram.toJsonObject();
            Log.d(TAG, "loadDrawable: "+ picto.toString());
            Drawable drawable=json.getIcono(picto);
            if(drawable!=null)
                attatcher.UseCornerRadius(true).loadDrawable(drawable,imageView);
            else
                attatcher.UseCornerRadius(true).loadDrawable(mActivity.getResources().getDrawable(R.drawable.ic_cloud_download_orange),imageView);
        }else{
            File picto=new File(pictogram.getEditedPictogram());
            if(picto.exists())
                attatcher.UseCornerRadius(true).loadDrawable(picto,imageView);
            else
                attatcher.UseCornerRadius(true).loadDrawable(Uri.parse(pictogram.getUrl()),imageView);
        }
    }



}
