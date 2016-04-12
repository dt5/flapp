package com.alduran.doranwalsten.flapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void designButtonPressed(View v) {
        //Make a new intent and load the design activity
        Intent viewFaceIntent = new Intent(MainActivity.this, MainDesign.class);
        startActivity(viewFaceIntent);
    }

    public void tutorialButtonPressed(View v) {
        //Make a new intent to begin the tutorial
    }
}
