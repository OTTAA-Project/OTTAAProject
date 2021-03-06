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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Games.MatchPictograms;
import com.stonefacesoft.ottaa.Games.MemoryGame;
import com.stonefacesoft.ottaa.Games.WhichIsThePicto;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.customComponents.Custom_Grupo_Juego;
import com.stonefacesoft.ottaa.utils.Games.Juego;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

//import com.stonefacesoft.ottaa.Games.DescribirPictograma;

/**
 * @author Gonzalo Juarez
 * @since 23/06/2020
 * Viewpager to select game level
 */
public class ViewPager_Game_Grupo {

    private static final String TAG = "viewPager_GameG";
    private static AppCompatActivity mActivity;
    private static final float MIN_SCALE = 0.85f;
    private static Json json;
    private int positionObject;
    private static final float MIN_ALPHA = 0.5f;
    private static final boolean edit = false;
    private static textToSpeech myTTS;
    private final ViewPager2 viewPager;
    private static JSONArray array;
    private static int id;
    private final FloatingActionButton actionButton;
    private final ImageView editPicto;
    private static int positionItem;


    public ViewPager_Game_Grupo(AppCompatActivity mActivity, textToSpeech myTTS, int id) {
        ViewPager_Game_Grupo.mActivity = mActivity;
        ViewPager_Game_Grupo.myTTS = myTTS;
        this.viewPager = ViewPager_Game_Grupo.mActivity.findViewById(R.id.viewPager_groups);
        ViewPager_Game_Grupo.id = id;
        json = Json.getInstance();
        json.setmContext(mActivity);
        array = json.getmJSONArrayTodosLosGrupos();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        viewPager.setAdapter(new com.stonefacesoft.ottaa.Viewpagers.ViewPager_Game_Grupo.ScreenSlidePagerAdapter(ViewPager_Game_Grupo.mActivity));
        viewPager.setCurrentItem(0);
        viewPager.canScrollVertically(View.SCROLL_INDICATOR_BOTTOM);
        viewPager.setPageTransformer(new ViewPagerTransformationEffects(2));
        actionButton = ViewPager_Game_Grupo.mActivity.findViewById(R.id.btnTalk);
        editPicto = ViewPager_Game_Grupo.mActivity.findViewById(R.id.edit_button);
        editPicto.setVisibility(View.INVISIBLE);
        actionButton.setImageDrawable(ViewPager_Game_Grupo.mActivity.getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hablar();
            }
        });

    }

        public void setId(int id){
            ViewPager_Game_Grupo.id = id;
        }

        private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
            public ScreenSlidePagerAdapter(FragmentActivity fa) {
                super(fa);
            }

            @Override
            public Fragment createFragment(int position) {
                return new com.stonefacesoft.ottaa.Viewpagers.ViewPager_Game_Grupo.fragmentGrupo().newInstance(position);
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

    /**
     * Talk action
     */
    public void hablar() {
        try {
            myTTS.hablar(json.getNombre(array.getJSONObject(viewPager.getCurrentItem())));
            OnClickItem();
        } catch (JSONException e) {
            Log.e(TAG, "hablar: Error: " + e.getMessage());
        }
    }

    /**
     * This method increment or decrement the viewpager position
     *
     * @param add value true increment
     * @param add value false decrement
     */
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

    /**
     * Return the viewpager selected item
     */
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

    /**
     *
     */
    public void OnClickItem() {
        Intent intent = null;
        int position = viewPager.getCurrentItem();
        try {
            myTTS.hablarSinMostrarFrase(json.getNombre(array.getJSONObject(position)));
            switch (id) {
                case 0:
                    intent = new Intent(mActivity, WhichIsThePicto.class);
                    break;
                case 1:
                    intent = new Intent(mActivity, MatchPictograms.class);
                    break;
                case 2:
                    intent = new Intent(mActivity, MemoryGame.class);
                    break;
            }

            try {
                intent.putExtra("PictoID", array.getJSONObject(position).getInt("id"));
                intent.putExtra("PositionPadre", position);
                mActivity.startActivityForResult(intent, IntentCode.NOTIGAMES.getCode());
            } catch (Exception e) {
                Log.e(TAG, "OnClickItem: Error: " + e.getMessage());
            }
        } catch (JSONException e) {
            Log.e(TAG, "OnClickItem: Error: " + e.getMessage());
        }

    }

    public void refreshView() {
        viewPager.refreshDrawableState();
        viewPager.setCurrentItem(positionItem);
    }

    public static class fragmentGrupo extends Fragment {
        private View view;
        private int position;
        private boolean editar;

        public fragmentGrupo() {

        }

        public com.stonefacesoft.ottaa.Viewpagers.ViewPager_Game_Grupo.fragmentGrupo newInstance(Integer position1){
                com.stonefacesoft.ottaa.Viewpagers.ViewPager_Game_Grupo.fragmentGrupo fragmentGrupo=new com.stonefacesoft.ottaa.Viewpagers.ViewPager_Game_Grupo.fragmentGrupo();
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
                return inflater.inflate(R.layout.groups_components_juegos, container, false);
            }
            @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                Custom_Grupo_Juego grupo=view.findViewById(R.id.grupo_1);
                ImageView cancelButton=view.findViewById(R.id.edit_button);

                try {
                    Pictogram pictogram=new Pictogram(array.getJSONObject(position),json.getIdioma());
                    GlideAttatcher attatcher=new GlideAttatcher(mActivity);
                    loadDrawable(attatcher,pictogram,grupo.getImg());
                    grupo.setCustom_Texto(json.getNombre(array.getJSONObject(position)));
                    int levelId=json.getId(array.getJSONObject(position));
                    Juego juego=new Juego(mActivity,id,levelId);
                    Drawable drawable=juego.devolverCarita();
                    drawable.setTint(mActivity.getResources().getColor(R.color.NaranjaOTTAA));
                    if(juego.getScoreClass().getIntentos()>0)
                        grupo.setTagDrawable(drawable);
                    else{
                        grupo.setTagDrawable(mActivity.getResources().getDrawable(R.drawable.ic_remove_orange_24dp));
                    }

                    grupo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=null;
                           // intent.putExtra("Boton", position);
                            positionItem=position;
                            try {
                                myTTS.hablarSinMostrarFrase(json.getNombre(array.getJSONObject(position)));
                                switch (id){
                                    case 0:
                                        intent = new Intent(mActivity, WhichIsThePicto.class);
                                        break;
                                    case 1:
                                        intent = new Intent(mActivity, MatchPictograms.class);
                                        break;
                                  case 2:
                                        intent=new Intent(mActivity, MemoryGame.class);
                                        break;
                                }
                                try {
                                    intent.putExtra("PictoID", array.getJSONObject(position).getInt("id"));
                                    intent.putExtra("PositionPadre", position);
                                    startActivityForResult(intent, IntentCode.NOTIGAMES.getCode());
                                } catch (Exception e) {
                                    Log.e(TAG, "onClick: Error: " + e.getMessage());
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "onClick: Error: " + e.getMessage());
                            }

                        }
                    });
                } catch (JSONException e) {
                    Log.e(TAG, "onViewCreated: Error: " + e.getMessage());
                }
            }

    }

    public static void loadDrawable(GlideAttatcher attatcher, Pictogram pictogram, ImageView imageView){
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


