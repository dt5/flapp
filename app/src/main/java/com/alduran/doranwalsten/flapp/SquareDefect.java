package com.alduran.doranwalsten.flapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by doranwalsten on 4/12/16.
 * This class will be used to allow the user to define the square defect that they
 * will be working with for an advancement flap
 */

public class SquareDefect extends View { //Still a View

    private int x; //width
    private int y; //height
    private Point center;
    private float[] touchpoint = new float[2];
    private float[] displacement = {0,0};

    public SquareDefect(Context context) {
        super(context);
        x = 100;
        y = 100;
        this.center = new Point(300,300);
    }

    public void setWidth(int w) {
        this.x = w;
    }
    public void setHeight(int h) {
        this.y = h;
    }

    public void setTouchpoint(float x, float y) {
        touchpoint[0] = x;
        touchpoint[1] = y;
        displacement[0] = x - this.getX() - 300;
        displacement[1] = y - this.getY() - 450;
    }

    public float[] getTouchpoint() {
        float[] toReturn = {touchpoint[0], touchpoint[1]};
        return toReturn;
    }

    public float[] getDisplacement() {
        float[] toReturn = {displacement[0],displacement[1]};
        return toReturn;
    }

    @Override
    public void onDraw(Canvas canvas) {
        //Get the points
        canvas.save();
        //No scaling this time, want the slider outputs to be correct to the dimensions needed
        ArrayList<Point> ref = calculatePoints();
        //Set the style
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(android.graphics.Color.BLACK);

        //Design the outline
        Path path = new Path();
        //Define my path here given my ArrayList
        path.close();

        canvas.drawPath(path, paint);

        canvas.restore();

    }

    private ArrayList<Point> calculatePoints() {
        //List of Points
        ArrayList<Point> reference = new ArrayList<Point>();

        return reference;
    }
}
