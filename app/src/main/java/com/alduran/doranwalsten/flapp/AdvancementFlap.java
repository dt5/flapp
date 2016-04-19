package com.alduran.doranwalsten.flapp;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by doranwalsten on 4/12/16.
 * This class will be used to allow the user to define the square defect that they
 * will be working with for an advancement flap
 */

public class AdvancementFlap extends View implements Flap, RotationGestureDetector.OnRotationGestureListener { //Still a View

    private int w; //width
    private int h; //height
    private double defect_ratio; //Ratio of defect width to height, provided by the user
    private double flap_ratio; //Ratio of flap length to defect width. used to define the length of flap
    private double burrow_angle = 45;//Angle of the Burrow triangles
    private Point center;
    private float[] touchpoint = new float[2];
    private float[] displacement = {0,0};
    private boolean activated = false;//Variable to indicate whether the full advancement flap needs to be shwon
    private RotationGestureDetector mRotationDetector;
    private double rot = 0.0; //Store the current global rotation
    private GestureDetectorCompat mDetector;
    private float mScaleFactor = 1.f;
    private ScaleGestureDetector mScaleDetector;


    public AdvancementFlap(Context context) {
        super(context);
        w = 100;
        h = 100;
        this.center = new Point(500,500);
        this.defect_ratio = 1; //Initially square
        this.flap_ratio = 3; //Initially 3:1
        mDetector = new GestureDetectorCompat(context,new MoveListener(this));
        this.mRotationDetector = new RotationGestureDetector(this);
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());


    }

    public int getW() { return this.w; }
    public int getH() { return this.h; }
    public void setW(int w) {
        this.w = w;
    }
    public void setH(int h) {
        this.h = h;
    }
    public void setDefectRatio(double r) {
        this.defect_ratio = r;
        setH((int) (w*defect_ratio));
    }
    public void setFlapRatio(double r) {this.flap_ratio = r;}
    public void setBurrowAngle(double a) {this.burrow_angle = a;}
    public void setActivated(boolean f) {this.activated = f;}
    public boolean isActivated() {return this.activated;}


    public void setTouchpoint(float x, float y) {
        touchpoint[0] = x;
        touchpoint[1] = y;
        displacement[0] = x - this.getX() - 500;
        displacement[1] = y - this.getY() - 700;
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
        Path path1 = new Path();
        //Define my path here given my ArrayList
        path1.moveTo(ref.get(0).x, ref.get(0).y);
        path1.lineTo(ref.get(1).x, ref.get(1).y);
        path1.lineTo(ref.get(2).x, ref.get(2).y);
        path1.lineTo(ref.get(3).x, ref.get(3).y);
        path1.lineTo(ref.get(0).x, ref.get(0).y);

        if (activated) { //Add the wings of the Flap to the View
            path1.moveTo(ref.get(0).x,ref.get(0).y);
            path1.lineTo(ref.get(4).x,ref.get(4).y);
            path1.moveTo(ref.get(1).x,ref.get(1).y);
            path1.lineTo(ref.get(5).x,ref.get(5).y);
        }
        path1.close();
        canvas.drawPath(path1, paint);
        //Add the two triangles
        if (activated) {
            Path path2 = new Path();
            path2.moveTo(ref.get(6).x,ref.get(6).y);
            path2.lineTo(ref.get(7).x, ref.get(7).y);
            path2.lineTo(ref.get(4).x, ref.get(4).y);
            path2.close();
            canvas.drawPath(path2, paint);

            Path path3 = new Path();
            path3.moveTo(ref.get(8).x, ref.get(8).y);
            path3.lineTo(ref.get(9).x, ref.get(9).y);
            path3.lineTo(ref.get(5).x, ref.get(5).y);
            path3.close();
            canvas.drawPath(path3, paint);
        }
        canvas.restore();

    }

    public ArrayList<Point> calculatePoints() {
        //List of Points
        ArrayList<Point> reference = new ArrayList<Point>();
        double d = Math.sqrt(Math.pow(w/2.,2) + Math.pow(h/2.,2));
        double theta = Math.atan2(h,w);
        //These points define the defect
        Point pt1 = new Point(this.center.x + (int) (d*Math.cos(theta + rot)), this.center.y - (int) (d*Math.sin(theta + rot)));
        Point pt2 = new Point(this.center.x + (int) (d*Math.cos(theta - rot)), this.center.y + (int) (d*Math.sin(theta - rot)));
        Point pt3 = new Point(this.center.x - (int) (d*Math.cos(theta + rot)), this.center.y +(int) (d*Math.sin(theta + rot)));
        Point pt4 = new Point(this.center.x - (int) (d*Math.cos(theta - rot)), this.center.y- (int) (d*Math.sin(theta - rot)));
        reference.add(pt1);
        reference.add(pt2);
        reference.add(pt3);
        reference.add(pt4);

        //These points define the flap
        if (activated) {//
            //Body of the Flap
            Point pt5 = new Point(pt1.x + (int) (mScaleFactor*flap_ratio*w*Math.cos(rot)),pt1.y - (int) (mScaleFactor*flap_ratio*w*Math.sin(rot)));
            Point pt6 = new Point(pt2.x + (int) (mScaleFactor*flap_ratio*w*Math.cos(rot)),pt2.y - (int) (mScaleFactor*flap_ratio*w*Math.sin(rot)));
            reference.add(pt5);
            reference.add(pt6);
            //Triangles
            double side = w/Math.tan(Math.toRadians(burrow_angle))/2.;
            Point pt7 = new Point(pt5.x - (int) (w*Math.cos(rot)/2.), pt5.y + (int) (w*Math.sin(rot)/2.));
            Point pt8 = new Point(pt5.x - (int) (side*Math.sin(rot)), pt5.y - (int) (side*Math.cos(rot)));
            reference.add(pt7);
            reference.add(pt8);
            Point pt9 = new Point(pt6.x - (int) (w*Math.cos(rot)/2.), pt6.y + (int) (w*Math.sin(rot)/2.));
            Point pt10 = new Point(pt6.x + (int) (side*Math.sin(rot)), pt6.y + (int) (side*Math.cos(rot)));
            reference.add(pt9);
            reference.add(pt10);
        }

        return reference;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mDetector.onTouchEvent(ev);
        mRotationDetector.onTouchEvent(ev);
        mScaleDetector.onTouchEvent(ev);
        return true;
    }


    //This class should hopefully just detect the scale events
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
            invalidate();
            return true;
        }

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

    public void OnRotation(RotationGestureDetector rotationDetector) {
        //Use this method to set the "rotation angle" of the rhomboid flap
        this.rot = rotationDetector.getAngle();
        Log.i("Test", String.format("Angle Rotated: %.2f", rotationDetector.getAngle()));
    }
}
