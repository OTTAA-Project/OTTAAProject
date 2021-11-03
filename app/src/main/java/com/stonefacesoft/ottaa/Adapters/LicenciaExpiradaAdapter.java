package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stonefacesoft.ottaa.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by Hector on 30/8/2018.
 */
public class LicenciaExpiradaAdapter extends PagerAdapter {
    int[] images;
    int[] textos;
    LayoutInflater layoutInflater;
    Context context;


    public LicenciaExpiradaAdapter(Context context, int[] images, int[] textos) {
        this.context = context;
        this.images = images;
        this.textos = textos;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item_licencia_expirada, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageViewLicenciaExpirada);
        final TextView textView = itemView.findViewById(R.id.textViewLicenciaExpirada);
        imageView.setImageResource(images[position]);
        textView.setText(context.getResources().getString(textos[position]));

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(v -> {
            //Al hacer click pasar al siguiente

        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
