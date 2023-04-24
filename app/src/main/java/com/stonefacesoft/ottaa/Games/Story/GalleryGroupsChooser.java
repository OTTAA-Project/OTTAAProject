package com.stonefacesoft.ottaa.Games.Story;

import android.os.Bundle;

import com.stonefacesoft.ottaa.GaleriaGrupos2;
import com.stonefacesoft.ottaa.Viewpagers.viewpager_galeria_grupo;
import com.stonefacesoft.ottaa.Viewpagers.viewpager_game_filter_view;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.GaleriaGruposControls;

public class GalleryGroupsChooser extends GaleriaGrupos2 {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initViewPager() {
        iniciarBarrido();
        crearRecyclerView();
        changeEditButtonIcon();
        viewpager=new viewpager_game_filter_view(this,myTTS);
        viewpager.showViewPager(showViewPager);
        //showView(editButton,showViewPager);
        deviceControl=new GaleriaGruposControls(this);
    }
}
