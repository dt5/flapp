package com.alduran.doranwalsten.flapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.opengl.GLU;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.rajawali3d.surface.IRajawaliSurfaceRenderer;
import org.rajawali3d.util.RajLog;


/**
 * Created by ron on 2/17/16.
 */

public class Renderer extends RajawaliRenderer implements IRajawaliSurfaceRenderer {



    public Context context;
    public int ID;


    Point dude = new Point();
    int width;
    int height;
    Object3D mObject;
    public Renderer(Context context, int ID) {
        super(context);
        this.context = context;
        setFrameRate(60);
        WindowManager wm = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        this.ID = ID;
        display.getSize(dude);
        width = dude.x;
        height = dude.y;
    }


    private DirectionalLight directionalLight1;
    private DirectionalLight directionalLight2;
    private Sphere earthSphere;


    public void initScene(){

        directionalLight1 = new DirectionalLight(1f, .2f, -1.0f);
        directionalLight1.setColor(1.0f, 1.0f, 1.0f);
        directionalLight1.setPower(0.001f);
        //directionalLight2 = new DirectionalLight(-5f, 2f, 1.0f);
        //directionalLight2.setColor(1.0f, 1.0f, 1.0f);
        //directionalLight2.setPower(2);
        //getCurrentScene().addLight(directionalLight1);
        getCurrentScene().addLight(directionalLight1);

        LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.patrick_obj);


        try {
            objParser.parse();
        }catch (ParsingException e) {

        }

        Object3D mObject = objParser.getParsedObject();
        //mObject.setColor(Color.TRANSPARENT);
        mObject.setScale(10.f);//Patrick's model is kind of small, I think
        Material material = new Material();
        Texture texture = new Texture("Patrick",R.drawable.patrick_texture);
        try {
            material.addTexture(texture);
        } catch (ATexture.TextureException error){
            Log.d("DEBUG", "TEXTURE ERROR");
        }

        mObject.setMaterial(material);
        getCurrentScene().addChild(mObject);

        //Sample code that this was based on - HOLD ON AS A REFERENCE
        //mObject.getModelMatrix();
        /*
        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColor(0);

        Texture earthTexture = new Texture("Earth", R.drawable.earthtruecolor_nasa_big);
        try{
            material.addTexture(earthTexture);

        } catch (ATexture.TextureException error){
            Log.d("DEBUG", "TEXTURE ERROR");
        }


        earthSphere = new Sphere(1, 24, 24);
        earthSphere.setMaterial(material);
        getCurrentScene().addChild(earthSphere);
        getCurrentCamera().setZ(4.2f);
        */
        ArcballCamera arcball = new ArcballCamera(this.context, ((Activity) this.context).findViewById(ID));
        arcball.setTarget(mObject); //your 3D Object

        arcball.setPosition(0,0,4); //optional

        getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), arcball);

    }

    @Override
    public void onTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float winX = event.getRawX();
            float winY = event.getRawY();
            Log.v("test", "HERE");
            Vector3 model_point = screenToWorld(winX, winY, width, height, 1);
            RajLog.i("touch", "TOUCH!!!!!");
            Log.v("locate", String.format("%.2f %.2f %.2f", model_point.x, model_point.y, model_point.z));
        }
    }


    public Vector3 screenToWorld(float x, float y, int viewportWidth, int viewportHeight, float projectionDepth)
    {
        float[] r1 = new float[16];
        int[] viewport = new int[] { 0, 0, viewportWidth, viewportHeight };

        GLU.gluUnProject(x, viewportHeight - y, 0.0f, getCurrentCamera().getViewMatrix().getFloatValues(), 0, getCurrentCamera().getProjectionMatrix().getFloatValues(), 0, viewport, 0, r1, 0);

        //take the normalized vector from the resultant projection and the camera, and then project by the desired distance from the camera.
        Vector3 result = new Vector3(-r1[0], r1[1], r1[2]);
        result.subtract(getCurrentCamera().getPosition());
        result.normalize();
        result.multiply(projectionDepth);
        result.add(getCurrentCamera().getPosition());
        return result;
    }
    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j){
    }

//    @Override
//    public void onRender(final long elapsedTime, final double deltaTime) {
//        super.onRender(elapsedTime, deltaTime);
//        earthSphere.rotate(Vector3.Axis.Y, 1.0);
//    }

}
