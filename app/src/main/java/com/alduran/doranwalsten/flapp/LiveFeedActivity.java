package com.alduran.doranwalsten.flapp;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class LiveFeedActivity extends AppCompatActivity {

    Camera mCamera; //Need a camera
    Preview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_feed);

        if (safeCameraOpen(0)) {
            Log.i("Test","Camera Opened");
        }

        mPreview = new Preview(this.getBaseContext(),mCamera);


        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        RelativeLayout mLayout = (RelativeLayout) findViewById(R.id.cameraLayout);
        preview.addView(mPreview);

        ImageView face = (ImageView) findViewById(R.id.face_outline);

        //Add a flap to the View
        Bundle extras = getIntent().getExtras();
        Log.i("Pass", String.format("%d", extras.getInt("flap_type")));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT );
        View new_view;
        switch(extras.getInt("flap_type")) {
            case 0: //Rhomboid
                Log.i("Pass", String.format("%d", extras.getInt("flap_type")));
                RhomboidFlap rflap = new RhomboidFlap(this.getBaseContext());
                rflap.setMotionActivated(false);
                rflap.setActivated(false); //Want it to be black
                rflap.invalidate();
                new_view = (View) rflap;

                break;
            case 1://Advancement Flap
                Log.i("Pass",String.format("%d",extras.getInt("flap_type")));
                AdvancementFlap aflap = new AdvancementFlap(this.getBaseContext());
                aflap.setMotionActivated(false);
                aflap.setActivated(false); //Want it to be black
                aflap.setFlapActivated(true);
                aflap.invalidate();
                new_view = (View) aflap;
                break;
            default:
                Log.i("Pass",String.format("%d",extras.getInt("flap_type")));
                new_view = null;
                break;
        }
        new_view.setLayoutParams(params);
        new_view.getLayoutParams().height = 1000;
        new_view.getLayoutParams().width = 1000;


        switch(extras.getInt("flap_type")) {
            case 0:
                new_view.setX(face.getX() + 950);//random, just to get the idea
                new_view.setY(face.getY() + 300); //random, just to get the idea
                break;
            case 1:
                new_view.setX(face.getX()+200);//random, just to get the idea
                new_view.setY(face.getY() - 400); //random, just to get the idea

        }

        //Apparently need a really large region

        mLayout.addView(new_view);

        //Add the face to the View
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCameraAndPreview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCameraAndPreview();
    }

    //Private helper methods for the camera
    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            //releaseCameraAndPreview();
            mCamera = Camera.open(id);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }

    private void releaseCameraAndPreview() {
        mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

}
