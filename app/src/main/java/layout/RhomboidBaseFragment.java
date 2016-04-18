package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alduran.doranwalsten.flapp.R;
import com.alduran.doranwalsten.flapp.RhomboidFlap;

import java.util.ArrayList;

/**
 * This fragment will allow me to edit the parameters on a Rhomboid Flap View placed in the main view
 * of the current activity (MainDesign)
 */
public class RhomboidBaseFragment extends Fragment {

    SeekBar[] parameters = new SeekBar[3];
    String[] names = new String[3];
    TextView[] titles = new TextView[3];
    ArrayList<double[]> bounds = new ArrayList<double[]>();
    RhomboidFlap curr_flap;

    //Don't need to define the drag listener for this fragment, but for the ObjectPicking Fragment
    public RhomboidBaseFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RhomboidBaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RhomboidBaseFragment newInstance() {
        RhomboidBaseFragment fragment = new RhomboidBaseFragment();
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
        RelativeLayout mLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_rhomboid_base, container, false);
        parameters[0] = (SeekBar) mLayout.findViewById(R.id.seekBar_1); //RATIO
        parameters[1] = (SeekBar) mLayout.findViewById(R.id.seekBar_2); //ALPHA
        parameters[2] = (SeekBar) mLayout.findViewById(R.id.seekBar_3); //BETA

        names[0] = "Ratio";
        names[1] = "Alpha";
        names[2] = "Beta";

        titles[0] = (TextView) mLayout.findViewById(R.id.textView1);
        titles[0].setText(String.format("%s: 1.73",names[0]));
        titles[1] = (TextView) mLayout.findViewById(R.id.textView2);
        titles[1].setText(String.format("%s: 0.0",names[1]));
        titles[2] = (TextView) mLayout.findViewById(R.id.textView3);
        titles[2].setText(String.format("%s: 60.0", names[2]));

        double[] ratio_bound = {1.0,2.0};
        bounds.add(ratio_bound);
        double[] alpha_bound = {0.0,90.0};
        bounds.add(alpha_bound);
        double[] beta_bound = {0.0,90.0};
        bounds.add(beta_bound);

        //Add the buttons which allow the user to flip the pedicle up vs. down
        final Button upButton = (Button) mLayout.findViewById(R.id.upButton);
        final Button downButton = (Button) mLayout.findViewById(R.id.downButton);

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upButton.setEnabled(false); //
                upButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                upButton.setTextColor(getResources().getColor(R.color.white));

                downButton.setEnabled(true);
                downButton.setBackgroundColor(getResources().getColor(R.color.gray));
                downButton.setTextColor(getResources().getColor(R.color.black));

                //
                //Need to switch the way the beta angle is defined
                curr_flap.switchUp();
                curr_flap.invalidate();

            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downButton.setEnabled(false); //
                downButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                downButton.setTextColor(getResources().getColor(R.color.white));

                upButton.setEnabled(true);
                upButton.setBackgroundColor(getResources().getColor(R.color.gray));
                upButton.setTextColor(getResources().getColor(R.color.black));

                //
                //Need to switch the way the beta angle is defined
                curr_flap.switchUp();
                curr_flap.invalidate();

            }
        });

        RelativeLayout parentLayout = (RelativeLayout) getActivity().findViewById(R.id.designLayout);
        int count = parentLayout.getChildCount(); //Number of children
        for (int i = 0; i < count; i++) {
            View v = parentLayout.getChildAt(i);
            if (v instanceof RhomboidFlap) {
                curr_flap = (RhomboidFlap) v;
            }
        }

        for (int i = 0; i < 3; i++) {
            final double specific_range = bounds.get(i)[1] - bounds.get(i)[0];
            final double specific_bottom = bounds.get(i)[0];
            final TextView specific_text = titles[i];
            final String name = names[i];
            final int id = i;
            parameters[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //edited = true;
                    int progressChanged = seekBar.getProgress();
                    double display_value = progressChanged / 100. * specific_range + specific_bottom;
                    switch(id) {
                        case 0:
                            curr_flap.setHeight(display_value);
                            break;
                        case 1:
                            curr_flap.setAlpha(display_value*Math.PI/180.);
                            break;
                        case 2:
                            curr_flap.setBeta(display_value*Math.PI/180.);
                            break;
                    }
                    curr_flap.invalidate();
                    specific_text.setText(String.format("%s : %.2f",name, display_value));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        return mLayout;
    }

}
