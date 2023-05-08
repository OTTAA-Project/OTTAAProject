package com.stonefacesoft.ottaa.Viewpagers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.stonefacesoft.ottaa.Games.GameCard;
import com.stonefacesoft.ottaa.Games.GameSelector;
import com.stonefacesoft.ottaa.Games.TellAStory;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.Games.TellAStoryUtils;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.ReturnPositionItem;

import org.json.JSONObject;

//import com.stonefacesoft.ottaa.Games.DescribirPictograma;

/**
 * @author Gonzalo Juarez
 * @since 23/06/2020
 * Viewpager to select game
 */
public class viewpager_galeria_juegos {

    private static AppCompatActivity mActivity;
    private static Json json;
    private final ViewPager2 viewPager;
    private JSONObject gameData;
    private ReturnPositionItem positionItem;

    private int size = 4;


    public viewpager_galeria_juegos(AppCompatActivity mActivity) {
        viewpager_galeria_juegos.mActivity = mActivity;
        if(!ConnectionDetector.isNetworkAvailable(mActivity))
            size = 3;
        json = Json.getInstance();
        json.setmContext(viewpager_galeria_juegos.mActivity);
        viewPager = mActivity.findViewById(R.id.viewpager);
        viewPager.setAdapter(new ScreenSlidePagerAdapter(viewpager_galeria_juegos.mActivity));
        viewPager.setCurrentItem(0);
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new viewpager_galeria_juegos.fragmentJuego().newInstance(position);
        }

        @Override
        public int getItemCount() {
                return size;
        }
    }


    public static class fragmentJuego extends Fragment {
        private View view;
        private int position;
        private boolean editar;

        public fragmentJuego() {

        }

        public viewpager_galeria_juegos.fragmentJuego newInstance(Integer position1) {
            viewpager_galeria_juegos.fragmentJuego fragmentJuego = new viewpager_galeria_juegos.fragmentJuego();
            Bundle args = new Bundle();
            args.putInt("position", position1);
            fragmentJuego.setArguments(args);
            return fragmentJuego;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                position = getArguments().getInt("position");
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
            return inflater.inflate(R.layout.grid_game_card_container, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            GameCard card=view.findViewById(R.id.card1);
            switch (position) {
                case 0:
                    card.prepareCardView( R.string.whichpictogram, R.string.which_description_name, R.drawable.whats_picto, createOnClickListener(mActivity, GameSelector.class, "notigames"));
                    card.setmTxtScore(json.devolverCantidadGruposUsados(0)+"/"+json.getmJSONArrayTodosLosGrupos().length());
                break;
                case 1:
                    card.prepareCardView( R.string.join_pictograms, R.string.join_pictograms_description, R.drawable.join_pictograms, createOnClickListener(mActivity, GameSelector.class, "seleccionar_palabras"));
                    card.setmTxtScore(json.devolverCantidadGruposUsados(1)+"/"+json.getmJSONArrayTodosLosGrupos().length());//todo in recycler fill with the position
                    break;
                case 2:
                    card.prepareCardView(R.string.memory_game, R.string.memory_game_string, R.drawable.memory_game, createOnClickListener(mActivity, GameSelector.class, "descripciones"));
                    card.setmTxtScore(json.devolverCantidadGruposUsados(2)+"/"+json.getmJSONArrayTodosLosGrupos().length());//todo in recycler fill with the position
                    break;
                case 3:
                    card.prepareCardView(R.string.TellStory, R.string.TellStory_Description, R.drawable.tell_story, createOnClickListener(mActivity, TellAStory.class, "history"));
                    card.setmTxtScore(json.devolverCantidadGruposUsados(3)+"/"+ TellAStoryUtils.getInstance().getChildCounts());//todo in recycler fill with the position
                    break;
            }
        }


        private View.OnClickListener createOnClickListener(Context context,Class clase,String value){
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,clase);
                    intent.putExtra("name_game",value);
                   mActivity.startActivityForResult(intent, IntentCode.NOTIGAMES.getCode());
                }
            };
        }
    }



    public void setUpPositionItem(int size){
        positionItem=new ReturnPositionItem(size);
    }

    public void scrollPosition(boolean add){
        int position=0;
        if(add){
            position=positionItem.add();
        }
        else
           position=positionItem.subtract();
        viewPager.setCurrentItem(position);
    }

    public void updateAdapter(){
        viewPager.invalidate();
    }

    public void actionClick(){
        Intent intent=null;
         switch (viewPager.getCurrentItem())
         {
             case 0:
                 intent=new Intent(mActivity,GameSelector.class);
                 intent.putExtra("name_game","notigames");
                 break;
             case 1:
                 intent=new Intent(mActivity,GameSelector.class);
                 intent.putExtra("name_game","seleccionar_palabras");
                 break;
             case 2:
                 intent=new Intent(mActivity, GameSelector.class);
                 intent.putExtra("name_game","descripciones");
                break;
             case 3:
                 intent=new Intent(mActivity, TellAStory.class);
                 intent.putExtra("name_game","history");
                 break;
             default:
                 intent=new Intent(mActivity,GameSelector.class);
                 intent.putExtra("name_game","notigames");
                 break;
         }
        mActivity.startActivityForResult(intent, IntentCode.NOTIGAMES.getCode());
    }


}