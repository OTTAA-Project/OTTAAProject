package com.stonefacesoft.ottaa.Viewpagers;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;
/**
 * A class that make an animation in a viewpager.
 * For example:
 * <pre>
 * viewPager.setPageTransformer(new ViewPagerTransformationEffects(position));
 *<ul>
 * <li>Position 0: CubeInRotatingTransformation</li>
 * <li>Position 1: ZoomOutPageTransformer</li>
 *<li>Position 2:  SpinnerTransformation</li>
 *<li>Position 3:  TossTransformation</li>
 *</ul>
 * </pre>
 *
 * @author  Gonzalo Juarez
 * @version 1.0 23/06/2020
 */
public class ViewPagerTransformationEffects  implements ViewPager2.PageTransformer {
    private final int idAnimation;

    public ViewPagerTransformationEffects(int idAnimation){
        this.idAnimation=idAnimation;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        switch (idAnimation){
            case 0:
                CubeInRotatingTransformation(page,position);
                break;
            case 1:
                ZoomOutPageTransformer(page,position);
                break;
            case 2:
                SpinnerTransformation(page,position);
                break;
            case 3:
                TossTransformation(page,position);
                break;
        }
    }

    private void CubeInRotatingTransformation(View page,float position){
        if (position < -1) {    // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        } else if (position <= 0) {    // [-1,0]
            page.setAlpha(1);
            page.setPivotX(page.getWidth());
            page.setRotationY(-90 * Math.abs(position));

        } else if (position <= 1) {    // (0,1]
            page.setAlpha(1);
            page.setPivotX(0);
            page.setRotationY(90 * Math.abs(position));

        } else {    // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);
        }
    }

    private void  ZoomOutPageTransformer(View page,float position){
        final float MIN_SCALE = 0.85f;
        final float MIN_ALPHA = 0.5f;
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0f);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                page.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                page.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            page.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0f);
        }

    }
    public void SpinnerTransformation(View page,float position){

        page.setTranslationX(-position * page.getWidth());
        page.setCameraDistance(12000);

        if (position < 0.5 && position > -0.5) {
            page.setVisibility(View.VISIBLE);
        } else {
            page.setVisibility(View.INVISIBLE);
        }



        if (position < -1){     // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        }
        else if (position <= 0) {    // [-1,0]
            page.setAlpha(1);
            page.setRotationY(900 *(1-Math.abs(position)+1));

        }
        else if (position <= 1) {    // (0,1]
            page.setAlpha(1);
            page.setRotationY(-900 *(1-Math.abs(position)+1));

        }
        else {    // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);

        }
    }
    public void TossTransformation(View page,float position){
        page.setTranslationX(-position * page.getWidth());
        page.setCameraDistance(20000);


        if (position < 0.5 && position > -0.5) {
            page.setVisibility(View.VISIBLE);

        } else {
            page.setVisibility(View.INVISIBLE);

        }


        if (position < -1) {     // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        }
        else if (position <= 0) {    // [-1,0]
            page.setAlpha(1);
            page.setScaleX(Math.max(0.4f, (1 - Math.abs(position))));
            page.setScaleY(Math.max(0.4f, (1 - Math.abs(position))));
            page.setRotationX(1080 * (1 - Math.abs(position) + 1));
            page.setTranslationY(-1000*Math.abs(position));

        }
        else if (position <= 1) {    // (0,1]
            page.setAlpha(1);
            page.setScaleX(Math.max(0.4f, (1-Math.abs(position))));
            page.setScaleY(Math.max(0.4f, (1-Math.abs(position))));
            page.setRotationX(-1080 * (1 - Math.abs(position) + 1));
            page.setTranslationY(-1000*Math.abs(position));

        }
        else {    // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);
        }
    }
}
