package com.alduran.doranwalsten.flapp;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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
        RhomboidFlap flap = new RhomboidFlap(getBaseContext());
        flap.setActivated(false);//Want it to be black
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT );
        params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        View new_view = (View) flap;
        new_view.setLayoutParams(params);
        //Apparently need a really large region
        new_view.getLayoutParams().height = 1000;
        new_view.getLayoutParams().width = 1000;
        mLayout.addView(new_view);
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
