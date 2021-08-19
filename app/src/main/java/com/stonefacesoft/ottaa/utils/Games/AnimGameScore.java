package com.stonefacesoft.ottaa.utils.Games;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.stonefacesoft.ottaa.Custom_Picto;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

//Esta es una clase q se encarga de
public class AnimGameScore {
    private final Context context;
    private final ImageView animationSprite;
    final private String TAG = "AnimGameScore";

    public AnimGameScore(Context context, ImageView animationSprite) {
        super();
        this.context = context;
        this.animationSprite = animationSprite;
        init();
    }

    private void init() {


    }
/*
    public void setMenuView(Menu menu) {calculaPuntos.setIntentos(600);
        this.mMenu = menu;
    }*/


    public void animateCorrect(View picto, int smiley) {
        animationSprite.setVisibility(View.VISIBLE);

        animationSprite.setImageDrawable(getTintedSmiley(smiley));

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) animationSprite.getLayoutParams();
        params.startToStart = picto.getId();
        params.endToEnd = picto.getId();
        params.topToTop = picto.getId();
        params.bottomToBottom = picto.getId();
        animationSprite.setLayoutParams(params);
        animationSprite.requestLayout();

        AnimationSet set = new AnimationSet(true);

        Animation scaleAnimation = new ScaleAnimation(.7f, 1.3f, .7f, 1.3f,
                Animation.RELATIVE_TO_SELF, .5f,
                Animation.RELATIVE_TO_SELF, .5f);
        Animation alphaAnimation = new AlphaAnimation(0.0f, 1f);

        set.addAnimation(scaleAnimation);
        set.addAnimation(alphaAnimation);
        set.setDuration(800);
        set.setRepeatMode(Animation.ABSOLUTE);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet set = new AnimationSet(true);
                Animation scaleAnimation = new ScaleAnimation(1.3f, 0f, 1.3f, 0f,
                        Animation.RELATIVE_TO_SELF, .5f,
                        Animation.RELATIVE_TO_SELF, .5f);
                Animation alphaAnimation = new AlphaAnimation(1f, 0f);
                set.addAnimation(scaleAnimation);
                set.addAnimation(alphaAnimation);
                set.setDuration(800);
                set.setRepeatMode(Animation.ABSOLUTE);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.setFillAfter(true);
                animationSprite.startAnimation(set);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setFillAfter(true);
        animationSprite.startAnimation(set);

    }

    public void animateCorrect(PictoView picto, int smiley) {
        animationSprite.setVisibility(View.VISIBLE);

        animationSprite.setImageDrawable(getTintedSmiley(smiley));

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) animationSprite.getLayoutParams();
        params.startToStart = picto.getId();
        params.endToEnd = picto.getId();
        params.topToTop = picto.getId();
        params.bottomToBottom = picto.getId();
        animationSprite.setLayoutParams(params);
        animationSprite.requestLayout();


        AnimationSet set = new AnimationSet(true);

        Animation scaleAnimation = new ScaleAnimation(.7f, 1.3f, .7f, 1.3f,
                Animation.RELATIVE_TO_SELF, .5f,
                Animation.RELATIVE_TO_SELF, .5f);
        Animation alphaAnimation = new AlphaAnimation(0.0f, 1f);

        set.addAnimation(scaleAnimation);
        set.addAnimation(alphaAnimation);
        set.setDuration(800);
        set.setRepeatMode(Animation.ABSOLUTE);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet set = new AnimationSet(true);
                Animation scaleAnimation = new ScaleAnimation(1.3f, 0f, 1.3f, 0f,
                        Animation.RELATIVE_TO_SELF, .5f,
                        Animation.RELATIVE_TO_SELF, .5f);
                Animation alphaAnimation = new AlphaAnimation(1f, 0f);
                set.addAnimation(scaleAnimation);
                set.addAnimation(alphaAnimation);
                set.setDuration(800);
                set.setRepeatMode(Animation.ABSOLUTE);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.setFillAfter(true);
                animationSprite.startAnimation(set);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setFillAfter(true);
        animationSprite.startAnimation(set);

    }

    public void animateCorrect(PictoView picto, int smiley,ImageView sprite) {
        sprite.setVisibility(View.VISIBLE);

        sprite.setImageDrawable(getTintedSmiley(smiley));

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) sprite.getLayoutParams();
        params.startToStart = picto.getId();
        params.endToEnd = picto.getId();
        params.topToTop = picto.getId();
        params.bottomToBottom = picto.getId();
        sprite.setLayoutParams(params);
        sprite.requestLayout();


        AnimationSet set = new AnimationSet(true);

        Animation scaleAnimation = new ScaleAnimation(.7f, 1.3f, .7f, 1.3f,
                Animation.RELATIVE_TO_SELF, .5f,
                Animation.RELATIVE_TO_SELF, .5f);
        Animation alphaAnimation = new AlphaAnimation(0.0f, 1f);

        set.addAnimation(scaleAnimation);
        set.addAnimation(alphaAnimation);
        set.setDuration(800);
        set.setRepeatMode(Animation.ABSOLUTE);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet set = new AnimationSet(true);
                Animation scaleAnimation = new ScaleAnimation(1.3f, 0f, 1.3f, 0f,
                        Animation.RELATIVE_TO_SELF, .5f,
                        Animation.RELATIVE_TO_SELF, .5f);
                Animation alphaAnimation = new AlphaAnimation(1f, 0f);
                set.addAnimation(scaleAnimation);
                set.addAnimation(alphaAnimation);
                set.setDuration(800);
                set.setRepeatMode(Animation.ABSOLUTE);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.setFillAfter(true);
                sprite.startAnimation(set);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setFillAfter(true);
        sprite.startAnimation(set);

    }


    public Drawable getTintedSmiley(int drawable) {
        animationSprite.setImageDrawable(context.getResources().getDrawable(drawable));
        switch (drawable) {
            case R.drawable.ic_sentiment_very_dissatisfied_white_24dp:
                animationSprite.getDrawable().setTint(context.getResources().getColor(R.color.colorDarkRed));
                return animationSprite.getDrawable();
            case R.drawable.ic_sentiment_dissatisfied_white_24dp:
                animationSprite.getDrawable().setTint(context.getResources().getColor(R.color.colorRed));
                return animationSprite.getDrawable();
            case R.drawable.ic_sentiment_neutral_white_24dp:
                animationSprite.getDrawable().setTint(context.getResources().getColor(R.color.colorYellow));
                return animationSprite.getDrawable();
            case R.drawable.ic_sentiment_satisfied_white_24dp:
                animationSprite.getDrawable().setTint(context.getResources().getColor(R.color.colorGreen));
                return animationSprite.getDrawable();
            case R.drawable.ic_sentiment_very_satisfied_white_24dp:
                animationSprite.getDrawable().setTint(context.getResources().getColor(R.color.colorLightGreen));
                return animationSprite.getDrawable();
            default:
                return animationSprite.getDrawable();
        }

    }
}
