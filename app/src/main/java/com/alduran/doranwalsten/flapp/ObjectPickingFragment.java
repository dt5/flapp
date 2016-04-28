package com.alduran.doranwalsten.flapp;

import android.app.Activity;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.util.OnObjectPickedListener;

public class ObjectPickingFragment extends BaseFragment implements
    OnTouchListener {

    ObjectPickingRenderer specific_render;
    ArcballCamera myCamera;
    RhomboidFlap new_flap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ((View) mRajawaliSurface).setOnTouchListener(this);
        ((View) mRajawaliSurface).setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:

                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            return true;
                        } else {
                            return false;
                        }

                    case DragEvent.ACTION_DROP:
                        //Dumb switch here, couldn't find a way to get Flap to inherit View
                        Flap curr_flap = (Flap) event.getLocalState();
                        curr_flap.setActivated(true);
                        View curr_view = (View) curr_flap;
                        curr_view.invalidate();
                        curr_view.setX(event.getX() - curr_flap.getWidth() / 2 - curr_flap.getDisplacement()[0]);
                        curr_view.setY(event.getY() - curr_flap.getHeight() / 2 - curr_flap.getDisplacement()[1]);
                        curr_view.setVisibility(View.VISIBLE);
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;
                }
                return false;
            }
        });



        //mRenderer = createRenderer();
        specific_render = createRenderer();
        return mLayout;
    }

    @Override
    public ObjectPickingRenderer createRenderer() {
        return new ObjectPickingRenderer(getActivity());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ObjectPickingRenderer renderer = (ObjectPickingRenderer) mRenderer;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float winX = event.getRawX();
            float winY = event.getRawY();
            /* Here is my attempt to get world coordinates
            Log.v("test", "HERE");
            Vector3 model_point = renderer.screenToWorld(winX, winY, renderer.width, renderer.height, (float) 0.5);
            RajLog.i("touch", "TOUCH!!!!!");
            Log.v("locate", String.format("%.2f %.2f %.2f", model_point.x, model_point.y, model_point.z));
            */
        }

        return getActivity().onTouchEvent(event);
    }



    public void switchCameraMode() { //Switch between the on and off states
        myCamera.switchMode();
    }

    private final class ObjectPickingRenderer extends BaseRenderer
        implements OnObjectPickedListener {

        public Context context;


        Point dude = new Point();
        int width;
        int height;
        Object3D mObject;
        private DirectionalLight directionalLight;

        public ObjectPickingRenderer(Context context) {
            super(context);
            this.context = context;
            setFrameRate(60);
            WindowManager wm = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            display.getSize(dude);
            width = dude.x;
            height = dude.y;
        }

        @Override
        protected void initScene() {
            directionalLight = new DirectionalLight(0f, .2f, -1.0f);
            //directionalLight = new DirectionalLight(-5f, .2f, -1.0f);
            directionalLight.setColor(1.0f, 1.0f, 1.0f);
            directionalLight.setPower(1.5f);
            getCurrentScene().addLight(directionalLight);

            LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.test_obj);
            try {
                objParser.parse();
            }catch (ParsingException e) {

            }

            mObject = objParser.getParsedObject();
            mObject.setScale(.01f); //Need to make the test small (0.01) Patrick large (5)
            // Only need to work with Materials for Patrick
            /*
            Material material = new Material();
            Texture texture = new Texture("Patrick",R.drawable.patrick_texture);
            try {
                material.addTexture(texture);
            } catch (ATexture.TextureException error){
                Log.d("DEBUG", "TEXTURE ERROR");
            }
            material.setColor(0);
            mObject.setMaterial(material);
            */
            getCurrentScene().addChild(mObject);

            myCamera = new ArcballCamera(this.context, ((Activity) this.context).findViewById(R.id.rajwali_surface));
            myCamera.setTarget(mObject); //your 3D Object

            myCamera.setPosition(0, 0, 4); //optional

            getCurrentScene().replaceAndSwitchCamera(myCamera,0);

        }

        @Override
        protected void onRender(long ellapsedRealtime, double deltaTime) {
            super.onRender(ellapsedRealtime, deltaTime);

        }

        /* This is my attempt at doing 3D projection. Don't need to worry about this for now
        public Vector3 screenToWorld(float x, float y, int viewportWidth, int viewportHeight, float projectionDepth)
        {
            float[] r1 = new float[16];
            int[] viewport = new int[] { 0, 0, viewportWidth, viewportHeight };

            float[] a = new float[16];
            float[] b = new float[16];
            float[] c = new float[16];
            getCurrentCamera().getModelMatrix().toFloatArray(a);
            getCurrentCamera().getProjectionMatrix().toFloatArray(b);

            GLU.gluUnProject(x, y, 0.5f,a, 0,b, 0,viewport, 0, r1, 0);

            //take the normalized vector from the resultant projection and the camera, and then project by the desired distance from the camera.
            Vector3 result = new Vector3(-r1[0], r1[1], r1[2]);
            result.subtract(getCurrentCamera().getPosition());
            result.normalize();
            result.multiply(projectionDepth);
            result.add(getCurrentCamera().getPosition());
            return result;
        }
        */

        public void onObjectPicked(Object3D object) {
            object.setZ(object.getZ() == 0 ? -2 : 0);
        }


    }

}
