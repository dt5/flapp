package com.alduran.doranwalsten.flapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.rajawali3d.IRajawaliDisplay;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.IRajawaliSurfaceRenderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class BaseFragment extends Fragment implements IRajawaliDisplay, OnClickListener {


    protected RelativeLayout mLayout;
    protected IRajawaliSurface mRajawaliSurface;
    protected IRajawaliSurfaceRenderer mRenderer;
    //protected RajawaliRenderer mRenderer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the view
        mLayout = (RelativeLayout) inflater.inflate(getLayoutID(), container, false);

        // Find the TextureView
        mRajawaliSurface = (IRajawaliSurface) mLayout.findViewById(R.id.rajwali_surface);


        // Create the renderer
        mRenderer = createRenderer();
        onBeforeApplyRenderer();
        applyRenderer();
        return mLayout;
    }

    public BaseRenderer createRenderer() {
        return new BaseRenderer(getActivity()) {
            @Override
            protected void initScene() {

            }
        };
    }

    protected void onBeforeApplyRenderer() {

    }

    protected void applyRenderer() {
        mRajawaliSurface.setSurfaceRenderer(mRenderer);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mLayout != null)
            mLayout.removeView((View) mRajawaliSurface);
    }

    @Override
    public int getLayoutID() {
        return R.layout.rajawali_textureview_fragment;
    }


    protected abstract class BaseRenderer extends RajawaliRenderer {

        public BaseRenderer(Context context) {
            super(context);
        }

        @Override
        public void onOffsetsChanged(float v, float v2, float v3, float v4, int i, int i2) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }

        @Override
        public void onRenderSurfaceCreated(EGLConfig config, GL10 gl, int width, int height) {
            super.onRenderSurfaceCreated(config, gl, width, height);
        }

        @Override
        protected void onRender(long ellapsedRealtime, double deltaTime) {
            super.onRender(ellapsedRealtime, deltaTime);
        }
    }

}
