package com.alduran.doranwalsten.flapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import layout.AdvancementBaseFragment;
import layout.PlainBaseFragment;
import layout.RhomboidBaseFragment;

/**
 * Created by doranwalsten on 4/4/16.
 */
public class MainDesign extends AppCompatActivity {

    ListView drag_options;
    private boolean open;//Whether the drag and drop menu is currently open
    private boolean adding; //Whether we are now adding a flap design
    ObjectPickingFragment curr_face; //This is our Rajawali object that we want to mess with
    Flap curr_flap;//Have access to the current flap fragment in use

    Integer[] imageId = {
            R.drawable.rhomboid_logo,
            R.drawable.advancement_logo,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design_main);

        //Setup the Rajawali Surface Fragment
        final FragmentManager manager = getSupportFragmentManager();
        curr_face = (ObjectPickingFragment) manager.findFragmentById(R.id.face);
        //Setup the Plain Base Fragment
        Fragment baseFrag = new PlainBaseFragment();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.fragmentContainer, baseFrag);
        ft.commit();

        //Setup the FloatingActionButton to handle turning on and off the rotation action of the
        //Object Picking Fragment
        final FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.moveButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                curr_face.switchCameraMode();
                if (curr_face.myCamera.getMode()) {
                    myFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
                } else {
                    myFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                }
            }
        });
        final FloatingActionButton forward = (FloatingActionButton) findViewById(R.id.forwardButton);
        final FloatingActionButton accept = (FloatingActionButton) findViewById(R.id.acceptButton);
        final FloatingActionButton edit = (FloatingActionButton) findViewById(R.id.editFlapButton);
        final FloatingActionButton cancel = (FloatingActionButton) findViewById(R.id.quitButton);
        final Button ddb = (Button) findViewById(R.id.drag_drop);

        forward.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent next = new Intent(MainDesign.this,MainFeedbackActivity.class);
                forward.setVisibility(View.GONE);
                accept.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                ImageView feedback = (ImageView) findViewById(R.id.face_view);
                if (curr_flap instanceof RhomboidFlap) {
                    next.putExtra("image_res",R.drawable.rhomboid_feedback_bad);
                } else if (curr_flap instanceof AdvancementFlap) {
                    next.putExtra("image_res",R.drawable.adv_feedback_bad);
                }
                startActivity(next);
            }

        });


        accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                forward.setVisibility(View.GONE);
                accept.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);

                curr_flap.setActivated(false);
                ((View) curr_flap).invalidate();

                Fragment fragment = new PlainBaseFragment();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.fragmentContainer, fragment);
                ft.commit();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curr_flap instanceof RhomboidFlap) {
                    RhomboidBaseFragment fragment = new RhomboidBaseFragment();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.fragmentContainer, fragment);
                    ft.commit();
                } else if (curr_flap instanceof AdvancementFlap) {
                    AdvancementBaseFragment fragment = new AdvancementBaseFragment();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.fragmentContainer, fragment);
                    ft.commit();
                }

                edit.setVisibility(View.GONE);
                accept.setVisibility(View.VISIBLE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                forward.setVisibility(View.GONE);
                accept.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                Fragment fragment = new PlainBaseFragment();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.fragmentContainer, fragment);
                ft.commit();

                //In addition, need to remove the current design
                deleteFlap(curr_flap);
                ddb.setEnabled(true);//Allowed to use that button again

            }
        });
        //Setup the usual selection stuff (Table View)
        this.open = false;
        this.adding = false;

        FlappList menu = new FlappList(MainDesign.this,R.id.list,imageId);
        drag_options = (ListView) findViewById(R.id.list);
        drag_options.setAdapter(menu);
        drag_options.setVisibility(View.GONE);

        drag_options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                adding = true;
                open = false;

                RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.designLayout);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT );
                params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
                params.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
                //NOW! Need to initialize the specific fragment and flap for design
                Flap new_flap;
                Fragment fragment;
                FloatingActionButton forward = (FloatingActionButton) findViewById(R.id.forwardButton);
                FloatingActionButton accept = (FloatingActionButton) findViewById(R.id.acceptButton);
                FloatingActionButton cancel = (FloatingActionButton) findViewById(R.id.quitButton);
                //Determine which Flap needs to be added
                if (position == 0) { //Rhomboid Flap
                    new_flap = new RhomboidFlap(getBaseContext());
                    //Need to initiate the new Fragment
                    fragment = new RhomboidBaseFragment();
                    forward.setVisibility(View.VISIBLE);
                    accept.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.VISIBLE);
                } else if (position == 1) { //Advancement Flap
                    new_flap = new AdvancementFlap(getBaseContext());
                    //Need to initiate the new Fragment
                    fragment = new AdvancementBaseFragment();
                    //Only want accept and cancel visible initially
                } else {
                    new_flap = null;
                    fragment = null;
                }
                curr_flap = new_flap;
                    //Need to make a new Flap interface so that way we can save space!!!!
                View new_view = (View) new_flap;
                new_view.setLayoutParams(params);
                //Apparently need a really large region
                new_view.getLayoutParams().height = 1000;
                new_view.getLayoutParams().width = 1000;
                myLayout.addView(new_view);
                //Trying to do this here so above the flap
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.fragmentContainer, fragment);
                ft.commit();
                findViewById(R.id.fragmentContainer).bringToFront(); //WOO! This worked!
                drag_options.setVisibility(View.GONE);



                //Need to make the design button options visible

                Button ddb = (Button) findViewById(R.id.drag_drop);
                ddb.setEnabled(false);//Need to disable the button to prevent other flaps from being added
            }
        });
    }

    //Will need to modify such that we can pass a specific flap on the screent to this method
    public void deleteFlap(Flap flap) {
        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.designLayout);
        parentLayout.removeView((View) flap);
    }
    private class FlappList extends ArrayAdapter<Integer> {

        private Integer[] imageId;


        public FlappList(Context context, int textViewResourceId, Integer[] items) {
            super(context, textViewResourceId, items);
            this.imageId = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.flap_single, null);
            }

            Integer it = imageId[position];
            if (it != null) {
                ImageView iv = (ImageView) v.findViewById(R.id.img);
                if (iv != null) {
                    iv.setImageResource(it);
                }
            }

            return v;
        }
    }

    //Now want to implement functions that allow us to simply add images onto the surface view
    public void dragDropPressed(View v) {
        if(this.open) {
            drag_options.setVisibility(View.GONE);
            this.open = false;
        } else {
            drag_options.setVisibility(View.VISIBLE);
            this.open = true;
        }
    }
}
