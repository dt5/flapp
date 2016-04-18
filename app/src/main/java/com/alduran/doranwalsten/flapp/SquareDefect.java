package com.alduran.doranwalsten.flapp;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by doranwalsten on 4/12/16.
 * This class will be used to allow the user to define the square defect that they
 * will be working with for an advancement flap
 */

public class SquareDefect extends View implements Flap { //Still a View

    private int w; //width
    private int h; //height
    private Point center;
    private float[] touchpoint = new float[2];
    private float[] displacement = {0,0};
    private GestureDetectorCompat mDetector;

    public SquareDefect(Context context) {
        super(context);
        w = 200;
        h = 200;
        this.center = new Point(300,300);
        mDetector = new GestureDetectorCompat(context,new MoveListener(this));

    }

    public int getW() { return this.w; }
    public int getH() { return this.h; }
    public void setW(int w) {
        this.w = w;
    }
    public void setH(int h) {
        this.h = h;
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
        path.moveTo(ref.get(0).x,ref.get(0).y);
        path.lineTo(ref.get(1).x, ref.get(1).y);
        path.lineTo(ref.get(2).x, ref.get(2).y);
        path.lineTo(ref.get(3).x, ref.get(3).y);
        path.lineTo(ref.get(0).x,ref.get(0).y);
        path.close();

        canvas.drawPath(path, paint);
        canvas.restore();

    }

    public ArrayList<Point> calculatePoints() {
        //List of Points
        ArrayList<Point> reference = new ArrayList<Point>();
        Point pt1 = new Point(this.center.x - this.w/2, this.center.y+this.h/2);
        Point pt2 = new Point(this.center.x + this.w/2, this.center.y+this.h/2);
        Point pt3 = new Point(this.center.x + this.w/2, this.center.y-this.h/2);
        Point pt4 = new Point(this.center.x - this.w/2, this.center.y-this.h/2);
        reference.add(pt1);
        reference.add(pt2);
        reference.add(pt3);
        reference.add(pt4);
        return reference;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mDetector.onTouchEvent(ev);
        return true;
    }


    //This class should hopefully detect a long press in order to run
    //We want to keep this a private class in case we need special listeners for different types of
    //apps
    private class MoveListener extends GestureDetector.SimpleOnGestureListener  {

        View parent;
        public MoveListener(View v) {
            parent = v;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent ev) {
            RelativeLayout parent_layout = (RelativeLayout) parent.getParent();
            parent_layout.findViewById(R.id.forwardButton).setVisibility(View.VISIBLE);
            parent_layout.findViewById(R.id.acceptButton).setVisibility(View.VISIBLE);
            parent_layout.findViewById(R.id.quitButton).setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent ev) {
            setTouchpoint(ev.getRawX(), ev.getRawY());
            ClipData clipData = ClipData.newPlainText("", "");

            //Use Bitmap to create Shadow
            setDrawingCacheEnabled(true);
            Bitmap viewCapture = getDrawingCache();
            FlapDragShadowBuilder shadowBuilder = new FlapDragShadowBuilder(viewCapture);
            shadowBuilder.setDisplacement(getDisplacement()[0], getDisplacement()[1]);
            startDrag(clipData, shadowBuilder, parent, 0);
            setVisibility(View.GONE);
        }
    }
}
