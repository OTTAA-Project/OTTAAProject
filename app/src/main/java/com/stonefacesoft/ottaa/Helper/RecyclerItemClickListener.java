package com.stonefacesoft.ottaa.Helper;

/**
 * Created by GastonSaillen on 2/3/2018.
 */

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener{

    private final OnItemClickListener mListener;
    private final GestureDetector mGestureDetector;
    private boolean isDouble;//boolean para saber si entro en el double tap
    private boolean isRunning;//boolean para saber si el handler esta corriendo
    private boolean isLongPress; //para saber si se esta presionan
    private RecyclerView recyclerView;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onDoubleTap(View view, int position);
        void onLongClickListener(View view,int position);
    }

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e)
            {
                isLongPress=false;
                isDouble=false;//si toco una sola vez
                return true;
            }
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                //llama a la funcion cuando se deja de tocar el boton
                return true;
            }


            @Override
            public boolean onDoubleTap(MotionEvent e) {

                isDouble=true;// si entra en el double tap
                isLongPress=false;

                return true;
            }


        });



    }
    public RecyclerItemClickListener(RecyclerView recyclerView1,Context context, OnItemClickListener listener) {
        mListener = listener;
        recyclerView=recyclerView1;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e)
            {
                isLongPress=false;
                isDouble=false;//si toco una sola vez
                return true;
            }
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                //llama a la funcion cuando se deja de tocar el boton
                return true;
            }


            @Override
            public boolean onDoubleTap(MotionEvent e) {

                isDouble=true;// si entra en el double tap
                isLongPress=false;

                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                isLongPress = true;
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    mListener.onLongClickListener(child, recyclerView.getChildAdapterPosition(child));
                }
            }

        });



    }

    @Override
    public boolean onInterceptTouchEvent(final RecyclerView view, MotionEvent e) {

        final View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            Handler handler=new Handler();
            if(!isRunning)//si no esta corriendo el tiempo
            {
                isRunning = true;// se inicia y se da un tiempo de 1,7 segundos
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //si no hay double click entra en la primera opcion
                        if(isDouble){
                            mListener.onDoubleTap(childView,view.getChildAdapterPosition(childView));
                        }else if(!isLongPress&&!isDouble){
                            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                        }

                       isRunning = false;
                    }
                }, 300);

            }


        }


        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent e) {
        //onInterceptTouchEvent(view, motionEvent);

        final View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            Handler handler=new Handler();
            if(!isRunning)//si no esta corriendo el tiempo
            {
                isRunning = true;// se inicia y se da un tiempo de 1,7 segundos
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //si no hay double click entra en la primera opcion
                        if(isLongPress){
                            mListener.onLongClickListener(childView, view.getChildAdapterPosition(childView));
                        }else if(isDouble){
                            mListener.onDoubleTap(childView,view.getChildAdapterPosition(childView));
                        }else{
                            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                        }

                        isRunning = false;
                    }
                }, 700);

            }


        }


    }



    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }



}