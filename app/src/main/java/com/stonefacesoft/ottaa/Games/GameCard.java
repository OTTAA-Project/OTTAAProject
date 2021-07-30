package com.stonefacesoft.ottaa.Games;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.stonefacesoft.ottaa.R;

/**
 *
 */
public class GameCard extends ConstraintLayout {
    TextView textViewTitle;
    TextView textViewDescription;
    ImageView imageView;
    Button btnPuntaje, btnJugar;
    private String mTxtTitle;
    private String mTxtDescription;
    private Drawable mImage;
    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    public GameCard(Context context) {
        super(context);
        init(null, 0);
    }

    public GameCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        inflate(getContext(), R.layout.game_card, this);
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.GameCard, defStyle, 0);

        mTxtTitle = a.getString(R.styleable.GameCard_exampleString);


        if (a.hasValue(R.styleable.GameCard_exampleDrawable)) {
            mImage = a.getDrawable(
                    R.styleable.GameCard_exampleDrawable);
            mImage.setCallback(this);
        }

        this.textViewTitle = findViewById(R.id.gamecard_title);
        this.textViewDescription = findViewById(R.id.gamecard_description);
        this.imageView = findViewById(R.id.imgJuego);
        btnJugar = new Button(getContext());
        this.btnJugar = findViewById(R.id.btnJugar);
        btnPuntaje = new Button(getContext());
        this.btnPuntaje = findViewById(R.id.btnPuntaje);

        a.recycle();
    }

    public void prepareCardView(int name,int description,int drawable,OnClickListener onClickListener){
        setmTxtTitle(getResources().getString(name));
        setmTxtDescription(getResources().getString(description));
        setmImage(getResources().getDrawable(drawable));
        setOnClickListenerGeneral(onClickListener);
    }

    public void prepareGameScore(String text){
        btnPuntaje.setText(text);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getmTxtTitle() {
        return mTxtTitle;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param title The example string attribute value to use.
     */
    public void setmTxtTitle(String title) {
        this.mTxtTitle = title;
        this.textViewTitle.setText(mTxtTitle);
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param description The example string attribute value to use.
     */
    public void setmTxtDescription(String description) {
        this.mTxtDescription = description;
        this.textViewDescription.setText(mTxtDescription);
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getmImage() {
        return mImage;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setmImage(Drawable exampleDrawable) {
        this.mImage = exampleDrawable;
        this.imageView.setImageDrawable(mImage);
    }

    public void setBtnJugarClickListener(OnClickListener listener) {
        btnJugar.setOnClickListener(listener);
    }

    public void setBtnPuntajeClickListener(OnClickListener listener) {
        btnPuntaje.setOnClickListener(listener);
    }

    public void setOnClickListenerGeneral(OnClickListener listener){
        this.imageView.setOnClickListener(listener);
        this.textViewTitle.setOnClickListener(listener);
        this.textViewDescription.setOnClickListener(listener);
        this.btnJugar.setOnClickListener(listener);
        this.btnPuntaje.setOnClickListener(listener);
    }

    public void setRatingBar(float puntaje){
        this.setRatingBar(puntaje);
    }

    //this set the number of games
    public void setmTxtScore(String mTxtDescription) {
        this.btnPuntaje.setText(getResources().getString(R.string.str_level)+" "+mTxtDescription);
    }
}
