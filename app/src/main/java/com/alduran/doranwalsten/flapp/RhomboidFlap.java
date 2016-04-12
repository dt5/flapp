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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by doranwalsten on 1/23/16.
 * This class creates a rhombioid flap shape to display in the app
 */
public class RhomboidFlap extends View {

    //Default Constructor
    private double alpha; //Inclination angle
    private double beta; //Spacing angle
    private int height;
    private int width;
    private Point center;
    private float[] touchpoint = new float[2];
    private float[] displacement = {0,0};
    private ScaleGestureDetector mScaleDetector;
    private GestureDetectorCompat mDetector;
    private float mScaleFactor = 1.f;

    public RhomboidFlap(Context context) {
        super(context);
        //Set default alpha, beta, height, and width
        this.alpha = 0;
        this.beta = Math.PI/3;
        this.width = 120;
        this.height = (int) Math.floor(this.width*Math.sqrt(3));
        this.center = new Point(300,300);
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        this.mDetector = new GestureDetectorCompat(context,new MoveListener(this));

    }

    public void setAlpha(double a) {
        this.alpha = a;
    }
    public void setBeta(double b) {
        this.beta = b;
    }
    public void setHeight(double ratio) {
        this.height = (int) Math.floor(this.width*ratio);
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

    public void setCenter(int x, int y) {
        this.center.set(x,y);
    }

    public Point getCenter() {
        return this.center;
    }

    @Override
    public void onDraw(Canvas canvas) {
        //Get the points
        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor,300,300);
        Log.i("Scaling", String.format("Current Scale Value: %.2f", mScaleFactor));
        ArrayList<Point> ref = calculatePoints();
        //Set the style
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(android.graphics.Color.BLACK);

        //Design the outline
        Path path = new Path();
        path.moveTo(ref.get(0).x, ref.get(0).y);
        path.lineTo(ref.get(1).x, ref.get(1).y);
        path.lineTo(ref.get(2).x, ref.get(2).y);
        path.moveTo(ref.get(0).x, ref.get(0).y);
        path.lineTo(ref.get(3).x, ref.get(3).y);
        path.lineTo(ref.get(4).x, ref.get(4).y);
        path.lineTo(ref.get(5).x, ref.get(5).y);
        path.lineTo(ref.get(0).x, ref.get(0).y);
        path.close();

        canvas.drawPath(path, paint);

        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);
        mDetector.onTouchEvent(ev);
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
    private class MoveListener extends GestureDetector.SimpleOnGestureListener {

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
    //

    private ArrayList<Point> calculatePoints() {
        //List of Points
        ArrayList<Point> reference = new ArrayList<Point>();
        //Important values
        double l = Math.sqrt(Math.pow(this.height / 2, 2) + Math.pow(this.width / 2, 2));
        double gamma = (this.alpha + this.beta) - Math.PI/2;

        //Find the points
        Point pt1 = new Point();
        pt1.set(center.x, center.y);
        reference.add(pt1);

        int x = 0;
        int y = 0;
        Point pt2 = new Point();
        x = (int) (pt1.x + l * Math.cos(this.alpha));
        y = (int) (pt1.y + l * Math.sin(this.alpha));
        pt2.set(x, y);
        reference.add(pt2);

        Point pt3 = new Point();
        x = (int) (pt2.x + l * Math.sin(gamma));
        y = (int) (pt2.y - l * Math.cos(gamma));
        pt3.set(x, y);
        reference.add(pt3);

        //Now, the body of the Rhombus
        Point pt4 = new Point();
        x = pt1.x - this.width / 2;
        y = pt1.y + this.height / 2;
        pt4.set(x, y);
        reference.add(pt4);

        Point pt5 = new Point();
        x = pt1.x - this.width;
        y = pt1.y;
        pt5.set(x, y);
        reference.add(pt5);

        Point pt6 = new Point();
        x = pt1.x - this.width / 2;
        y = pt1.y - this.height / 2;
        pt6.set(x, y);
        reference.add(pt6);

        return reference;
    }


    public ImageView returnImage() {
        Bitmap viewCapture = null;
        this.setDrawingCacheEnabled(true);
        viewCapture = Bitmap.createBitmap(this.getDrawingCache());
        ImageView viewSeen = new ImageView(this.getContext());
        viewSeen.setImageBitmap(viewCapture);
        return viewSeen;
    }


}
