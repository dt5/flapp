package com.alduran.doranwalsten.flapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by doranwalsten on 4/5/16.
 */
public class MainFeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_main);

        //Declare what each of the buttons is doing
        FloatingActionButton accept = (FloatingActionButton) findViewById(R.id.acceptButton_F);
        FloatingActionButton edit = (FloatingActionButton) findViewById(R.id.editButton);
        FloatingActionButton cancel = (FloatingActionButton) findViewById(R.id.cancelButton_F);

        Bundle extras = getIntent().getExtras();
        ImageView face = (ImageView) findViewById(R.id.face_view);
        face.setImageResource(extras.getInt("image_res"));

        accept.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent m) {
                Intent getFeedback = new Intent(MainFeedbackActivity.this,MainActivity.class);
                startActivity(getFeedback);
                return true;
            }
        });

        //For the edit button, we want to go back to the Rhomboid Activity
        edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent m) {
                Intent addDesign = new Intent(MainFeedbackActivity.this, MainDesign.class);
                startActivity(addDesign);
                return true;
            }
        });

        //For the quit  button, want to go back to the flap selection page
        cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent m) {
                Intent quit = new Intent(MainFeedbackActivity.this, MainActivity.class);
                startActivity(quit);
                return true;
            }
        });
    }

    /*
    public void stressPressed(View v) {
        Button strain_button = (Button) findViewById(R.id.strainButton);
        Button stress_button = (Button) findViewById(R.id.stressButton);
        GifImageView feedback = (GifImageView) findViewById(R.id.flap_gif);

        //Since stress initially disabled, this occurs when strain is visible
        strain_button.setEnabled(true); //
        strain_button.setBackgroundColor(getResources().getColor(R.color.gray));
        strain_button.setTextColor(getResources().getColor(R.color.black));
        feedback.setImageResource(R.drawable.adv_45_right_21_stress);//Replace with stress

        stress_button.setEnabled(false);
        stress_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        stress_button.setTextColor(getResources().getColor(R.color.white));

    }

    public void strainPressed(View v) {
        Button strain_button = (Button) findViewById(R.id.strainButton);
        Button stress_button = (Button) findViewById(R.id.stressButton);
        GifImageView feedback = (GifImageView) findViewById(R.id.flap_gif);

        //Since strain initially disabled, this occurs when strain is visible
        strain_button.setEnabled(false); //
        strain_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        strain_button.setTextColor(getResources().getColor(R.color.white));
        feedback.setImageResource(R.drawable.adv_45_right_21_strain);//Replace with stress

        stress_button.setEnabled(true);
        stress_button.setBackgroundColor(getResources().getColor(R.color.gray));
        stress_button.setTextColor(getResources().getColor(R.color.black));

    }
    */
}
