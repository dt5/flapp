package com.alduran.doranwalsten.flapp;

import android.view.MotionEvent;

public class RotationGestureDetector {
    private static final int INVALID_POINTER_ID = -1;
    private float fX, fY, sX, sY;
    private int ptrID1, ptrID2;
    private double mAngle;
    private double last_valid = 0.0;

    private OnRotationGestureListener mListener;

    public double getAngle() {
        return mAngle;
    }

    public RotationGestureDetector(OnRotationGestureListener listener){
        mListener = listener;
        ptrID1 = INVALID_POINTER_ID;
        ptrID2 = INVALID_POINTER_ID;
    }

    public boolean onTouchEvent(MotionEvent event){
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                ptrID1 = event.getPointerId(event.getActionIndex());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                 ptrID2 = event.getPointerId(event.getActionIndex());
                 sX = event.getX(event.findPointerIndex(ptrID1));
                 sY = event.getY(event.findPointerIndex(ptrID1));
                 fX = event.getX(event.findPointerIndex(ptrID2));
                 fY = event.getY(event.findPointerIndex(ptrID2));
                 break;
            case MotionEvent.ACTION_MOVE:
                 if(ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID){
                 float nfX, nfY, nsX, nsY;
                     nsX = event.getX(event.findPointerIndex(ptrID1));
                     nsY = event.getY(event.findPointerIndex(ptrID1));
                     nfX = event.getX(event.findPointerIndex(ptrID2));
                     nfY = event.getY(event.findPointerIndex(ptrID2));

                     mAngle = last_valid + angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY);

                     if (mListener != null) {
                        mListener.OnRotation(this);
                     }
                 }
                 break;
            case MotionEvent.ACTION_UP:
                 ptrID1 = INVALID_POINTER_ID;
                last_valid = mAngle;
                 break;
            case MotionEvent.ACTION_POINTER_UP:
                 ptrID2 = INVALID_POINTER_ID;
                last_valid = mAngle;
                 break;
            case MotionEvent.ACTION_CANCEL:
                 ptrID1 = INVALID_POINTER_ID;
                 ptrID2 = INVALID_POINTER_ID;
                last_valid = mAngle;
                 break;
        }
        return true;
    }

    private double angleBetweenLines (float fX, float fY, float sX, float sY, float nfX, float nfY, float nsX, float nsY) {
        float angle1 = (float) Math.atan2( (fY - sY), (fX - sX) );
        float angle2 = (float) Math.atan2( (nfY - nsY), (nfX - nsX) );

        double angle = angle1 - angle2 % (Math.PI*2);
        if (angle < -Math.PI) angle += Math.PI*2;
        if (angle > Math.PI) angle -= Math.PI*2;
        return angle;
    }

    public static interface OnRotationGestureListener {
        public void OnRotation(RotationGestureDetector rotationDetector);
    }
 }
