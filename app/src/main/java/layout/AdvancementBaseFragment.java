package layout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alduran.doranwalsten.flapp.AdvancementFlap;
import com.alduran.doranwalsten.flapp.R;

import java.util.ArrayList;


public class AdvancementBaseFragment extends Fragment {

    SeekBar[] parameters = new SeekBar[2];
    String[] names = new String[2];
    TextView[] titles = new TextView[2];
    ArrayList<double[]> bounds = new ArrayList<double[]>();
    AdvancementFlap curr_flap;

    public AdvancementBaseFragment() {

    }

    public static AdvancementBaseFragment newInstance() {
        AdvancementBaseFragment fragment = new AdvancementBaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final RelativeLayout mLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_advancement_base, container, false);
        final RelativeLayout parentLayout = (RelativeLayout) getActivity().findViewById(R.id.designLayout);

        //Find the flap of interest
        int count = parentLayout.getChildCount(); //Number of children
        for (int i = 0; i < count; i++) {
            View v = parentLayout.getChildAt(i);
            if (v instanceof AdvancementFlap) {
                curr_flap = (AdvancementFlap) v;
            }
        }

        parameters[0] = (SeekBar) mLayout.findViewById(R.id.advSeekBar_1); //RATIO
        parameters[0].setMax(100);
        //Assume that the user wants complete freedom over the dimension of the defect

        names[0] = "Ratio";
        names[1] = "Angle";

        titles[0] = (TextView) mLayout.findViewById(R.id.advTextView1);
        titles[0].setText(String.format("%s: 1.0",names[(curr_flap.isActivated() ? 1 : 0)]));

        //TODO - Set the Triangle buttons here! Need to adjust the design

        //TODO - Set the Accept, Cancel buttons for defect
        FloatingActionButton defAccept = (FloatingActionButton) mLayout.findViewById(R.id.advAcceptButton);
        FloatingActionButton defDelete = (FloatingActionButton) mLayout.findViewById(R.id.advQuitButton);
        //Buttons to help define where to point the flap
        final FloatingActionButton up = (FloatingActionButton) mLayout.findViewById(R.id.upArrowButton);
        final FloatingActionButton left = (FloatingActionButton) mLayout.findViewById(R.id.leftArrowButton);
        final FloatingActionButton right = (FloatingActionButton) mLayout.findViewById(R.id.rightArrowButton);
        final FloatingActionButton down = (FloatingActionButton) mLayout.findViewById(R.id.downArrowButton);


        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curr_flap.setPedicle(1);
                curr_flap.setFlapActivated(true);
                curr_flap.invalidate();
                parentLayout.findViewById(R.id.acceptButton).setVisibility(View.VISIBLE);
                parentLayout.findViewById(R.id.quitButton).setVisibility(View.VISIBLE);
                parentLayout.findViewById(R.id.forwardButton).setVisibility(View.VISIBLE);
                titles[0].setText(String.format("%s: 45.0", names[(curr_flap.isActivated() ? 1 : 0)]));
                up.setVisibility(View.GONE);
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
                down.setVisibility(View.GONE);

            }

        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curr_flap.setPedicle(2);
                curr_flap.setFlapActivated(true);
                curr_flap.invalidate();
                parentLayout.findViewById(R.id.acceptButton).setVisibility(View.VISIBLE);
                parentLayout.findViewById(R.id.quitButton).setVisibility(View.VISIBLE);
                parentLayout.findViewById(R.id.forwardButton).setVisibility(View.VISIBLE);
                titles[0].setText(String.format("%s: 45.0", names[(curr_flap.isActivated() ? 1 : 0)]));
                up.setVisibility(View.GONE);
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
                down.setVisibility(View.GONE);
            }

        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curr_flap.setPedicle(3);
                curr_flap.setFlapActivated(true);
                curr_flap.invalidate();
                parentLayout.findViewById(R.id.acceptButton).setVisibility(View.VISIBLE);
                parentLayout.findViewById(R.id.quitButton).setVisibility(View.VISIBLE);
                parentLayout.findViewById(R.id.forwardButton).setVisibility(View.VISIBLE);
                titles[0].setText(String.format("%s: 45.0", names[(curr_flap.isActivated() ? 1 : 0)]));
                up.setVisibility(View.GONE);
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
                down.setVisibility(View.GONE);
            }

        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curr_flap.setPedicle(4);
                curr_flap.setFlapActivated(true);
                curr_flap.invalidate();
                parentLayout.findViewById(R.id.acceptButton).setVisibility(View.VISIBLE);
                parentLayout.findViewById(R.id.quitButton).setVisibility(View.VISIBLE);
                parentLayout.findViewById(R.id.forwardButton).setVisibility(View.VISIBLE);
                titles[0].setText(String.format("%s: 45.0", names[(curr_flap.isActivated() ? 1 : 0)]));
                up.setVisibility(View.GONE);
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
                down.setVisibility(View.GONE);
            }

        });

        defAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //First need to firgure out which edge the
                up.setVisibility(View.VISIBLE);
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.VISIBLE);
                down.setVisibility(View.VISIBLE);

                up.setY(curr_flap.getY() + 445 - (float) (curr_flap.getH() * Math.cos(curr_flap.getRot())));
                up.setX(curr_flap.getX() + 445 - (float) (curr_flap.getH() * Math.sin(curr_flap.getRot())));
                up.setRotation((float) (-curr_flap.getRot() * 180 / Math.PI));
                up.setScaleX(0.75f);
                up.setScaleY(0.75f);

                left.setY(curr_flap.getY() + 445 + (float) (curr_flap.getW() * Math.sin(curr_flap.getRot())));
                left.setX(curr_flap.getX() + 445 - (float) (curr_flap.getW() * Math.cos(curr_flap.getRot())));
                left.setRotation((float) (-curr_flap.getRot() * 180 / Math.PI));
                left.setScaleX(0.75f);
                left.setScaleY(0.75f);

                right.setY(curr_flap.getY() + 445 - (float) (curr_flap.getW() * Math.sin(curr_flap.getRot())));
                right.setX(curr_flap.getX() + 445 + (float) (curr_flap.getW() * Math.cos(curr_flap.getRot())));
                right.setRotation((float) (-curr_flap.getRot() * 180 / Math.PI));
                right.setScaleX(0.75f);
                right.setScaleY(0.75f);

                down.setY(curr_flap.getY() + 445 + (float) (curr_flap.getH() * Math.cos(curr_flap.getRot())));
                down.setX(curr_flap.getX() + 445 + (float) (curr_flap.getH() * Math.sin(curr_flap.getRot())));
                down.setRotation((float) (-curr_flap.getRot() * 180 / Math.PI));
                down.setScaleX(0.75f);
                down.setScaleY(0.75f);
            }
        });

        defDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentLayout.removeView(curr_flap);
                parentLayout.findViewById(R.id.drag_drop).setEnabled(true);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                Fragment fragment = new PlainBaseFragment();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.fragmentContainer, fragment);
                ft.commit();
            }
        });


        final int[] bound = {0,2,30,60}; //Only one set of bound in this case that we need to worry about

        final TextView specific_text = titles[0];
        final String name = names[0];
        parameters[0].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //edited = true;
                int progressChanged = seekBar.getProgress();
                int specific_range = bound[2*(curr_flap.isActivated() ? 1: 0) + 1] - bound[2*(curr_flap.isActivated() ? 1: 0)];
                int specific_bottom = bound[2*(curr_flap.isActivated() ? 1: 0)];
                double display_value = (progressChanged * specific_range)/100. + specific_bottom;
                if (!curr_flap.isFlapActivated()) {
                    curr_flap.setDefectRatio(display_value);
                } else {
                    curr_flap.setBurrowAngle(display_value);
                }
                curr_flap.invalidate();//Force a redraw
                specific_text.setText(String.format("%s : %.2f",name, display_value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return mLayout;
    }

}
