package com.stonefacesoft.ottaa.Viewpagers;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.stonefacesoft.ottaa.Edit_Picto_Visual;
import com.stonefacesoft.ottaa.GaleriaPictos3;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.LicenciaExpirada;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.customComponents.Custom_Grupo;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
/**
 * @author Gonzalo Juarez
 * @since 23/06/2020
 * Viewpager to select the group
 * */
public class viewpager_galeria_grupo {

    private static final String TAG = "viewpager_GG";
    private static AppCompatActivity mActivity;
    private static final float MIN_SCALE = 0.85f;
    private static Json json;
    private int positionObject;
    private static final float MIN_ALPHA = 0.5f;
    private static final boolean edit = false;
    private static textToSpeech myTTS;
    private static JSONArray array;
    private final ViewPager2 viewPager;
    private final ScreenSlidePagerAdapter pagerAdapter;


    public viewpager_galeria_grupo(AppCompatActivity mActivity, textToSpeech myTTS) {
        viewpager_galeria_grupo.mActivity = mActivity;
        viewpager_galeria_grupo.myTTS = myTTS;
        this.viewPager = viewpager_galeria_grupo.mActivity.findViewById(R.id.viewPager_groups);
        json = Json.getInstance();
        json.setmContext(mActivity);
        array = json.getmJSONArrayTodosLosGrupos();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        pagerAdapter = new ScreenSlidePagerAdapter(viewpager_galeria_grupo.mActivity);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.canScrollVertically(View.SCROLL_INDICATOR_BOTTOM);
        viewPager.setPageTransformer(new ViewPagerTransformationEffects(0));

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
           return new fragmentGrupo().newInstance(position);
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
            myTTS.hablar(json.getNombre(array.getJSONObject(viewPager.getCurrentItem())));
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

    public void updateData() {
        json = Json.getInstance();
        json.setmContext(mActivity);
        array = json.getmJSONArrayTodosLosGrupos();
    }

    public void OnClickItem() {
        Intent intent = new Intent(mActivity, GaleriaPictos3.class);
        intent.putExtra("Boton", viewPager.getCurrentItem());
        try {
            myTTS.hablarSinMostrarFrase(json.getNombre(array.getJSONObject(viewPager.getCurrentItem())));
        } catch (JSONException e) {
            Log.e(TAG, "OnClickItem: Error:" + e.getMessage());
        }
        mActivity.startActivityForResult(intent, IntentCode.GALERIA_PICTOS.getCode());
    }

    public static class fragmentGrupo extends Fragment implements View.OnClickListener {
        private View view;
        private int position;
        private boolean editar;

        public fragmentGrupo() {

        }

        public   fragmentGrupo newInstance(Integer position1){
             fragmentGrupo fragmentGrupo=new fragmentGrupo();
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
            Custom_Grupo grupo=view.findViewById(R.id.grupo_1);
            try {
                grupo.setCustom_Texto(json.getNombre(array.getJSONObject(position)));
                Pictogram pictogram=new Pictogram(array.getJSONObject(position),json.getIdioma());
                GlideAttatcher attatcher=new GlideAttatcher(mActivity);
                loadDrawable(attatcher,pictogram,grupo.getImg());
                grupo.setTagDrawable(0,json.tieneTag(array.getJSONObject(position),Constants.UBICACION)?R.drawable.ic_location_on_black_24dp:R.drawable.ic_location_off_black_24dp);
                grupo.setTagDrawable(1,json.tieneTag(array.getJSONObject(position), Constants.HORA) ? R.drawable.ic_timer_black_24dp : R.drawable.ic_baseline_timer_off_gray_24);
                grupo.setTagDrawable(3,json.tieneTag(array.getJSONObject(position), Constants.EDAD) ? R.drawable.ic_face_black_on_24dp : R.drawable.ic_face_black_24dp);
                grupo.setTagDrawable(2,json.tieneTag(array.getJSONObject(position), Constants.SEXO) ? R.drawable.ic_wc_black_24dp : R.drawable.ic_wc_block_24dp);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(view.getContext(), GaleriaPictos3.class);
                        intent.putExtra("Boton", position);
                        try {
                            myTTS.hablarSinMostrarFrase(json.getNombre(array.getJSONObject(position)));
                        } catch (JSONException e) {
                            Log.e(TAG, "onClick: Error: " + e.getMessage());
                        }
                        mActivity.startActivityForResult(intent,IntentCode.GALERIA_PICTOS.getCode());
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
