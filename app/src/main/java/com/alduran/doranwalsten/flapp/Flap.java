package com.alduran.doranwalsten.flapp;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by doranwalsten on 4/13/16.
 * This is an interface for all objects which are draggable and allow for editing with
 */
public interface Flap {

    //Touchpoint stuff needed for dragging
    void setTouchpoint(float x, float y);

    //View Methods that are needed
    int getWidth();//Call superclass View method
    int getHeight(); //Call superclass View method


    float[] getTouchpoint();
    float[] getDisplacement();

    boolean isActivated();
    void setActivated(boolean b);
    void setScaleActivated(boolean b);
    boolean isScaleActivated();

    //Points for onDraw method, which all Views have
    ArrayList<Point> calculatePoints();

}
