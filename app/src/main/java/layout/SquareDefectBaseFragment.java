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
import com.alduran.doranwalsten.flapp.SquareDefect;

import java.util.ArrayList;


public class SquareDefectBaseFragment extends Fragment {

    SeekBar[] parameters = new SeekBar[2];
    String[] names = new String[2];
    TextView[] titles = new TextView[2];
    ArrayList<double[]> bounds = new ArrayList<double[]>();
    SquareDefect curr_defect;
    boolean lock_dimensions = false;//Give the user freedom to lock the dimensions of the advancement flap

    public SquareDefectBaseFragment() {

    }

    public static SquareDefectBaseFragment newInstance() {
        SquareDefectBaseFragment fragment = new SquareDefectBaseFragment();
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

        RelativeLayout mLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_square_defect_base, container, false);
        parameters[0] = (SeekBar) mLayout.findViewById(R.id.squareSeekBar_1); //RATIO
        parameters[0].setMax(100);
        parameters[1] = (SeekBar) mLayout.findViewById(R.id.squareSeekBar_2); //ALPHA
        parameters[1].setMax(100);
        //Assume that the user wants complete freedom over the dimension of the defect

        names[0] = "Width";
        names[1] = "Height";

        titles[0] = (TextView) mLayout.findViewById(R.id.squareTextView1);
        titles[0].setText(String.format("%s: 200",names[0]));
        titles[1] = (TextView) mLayout.findViewById(R.id.squareTextView2);
        titles[1].setText(String.format("%s: 200", names[1]));

        Button lockButton = (Button) mLayout.findViewById(R.id.lockButton);
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lock_dimensions) {
                    lock_dimensions = true;
                    curr_defect.setH(curr_defect.getW());
                    curr_defect.invalidate();
                    parameters[1].setVisibility(View.GONE);
                    titles[1].setVisibility(View.GONE);
                    //Change the properties of the button
                    v.setBackground(getResources().getDrawable(R.drawable.ic_lock_outline_white_24dp));
                } else {
                    lock_dimensions = false;
                    parameters[1].setVisibility(View.VISIBLE);
                    titles[1].setVisibility(View.VISIBLE);
                    v.setBackground(getResources().getDrawable(R.drawable.ic_lock_open_black_24dp));
                }
            }
        });

        int[] bound = {50,300}; //Only one set of bound in this case that we need to worry about

        RelativeLayout parentLayout = (RelativeLayout) getActivity().findViewById(R.id.designLayout);
        int count = parentLayout.getChildCount(); //Number of children
        for (int i = 0; i < count; i++) {
            View v = parentLayout.getChildAt(i);
            if (v instanceof SquareDefect) {
                curr_defect = (SquareDefect) v;
            }
        }

        for (int i = 0; i < 2; i++) {
            final int specific_range = bound[1] - bound[0];
            final int specific_bottom = bound[0];
            final TextView specific_text = titles[i];
            final String name = names[i];
            final int id = i;
            parameters[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //edited = true;
                    int progressChanged = seekBar.getProgress();
                    int display_value = (progressChanged * specific_range)/100 + specific_bottom;
                    switch(id) {
                        case 0:
                            curr_defect.setW(display_value);
                            if (lock_dimensions) { //If we want to lock the dimensions
                                curr_defect.setH(display_value);
                            }
                            break;
                        case 1:
                            curr_defect.setH(display_value);
                            break;
                    }
                    curr_defect.invalidate();//Force a redraw
                    specific_text.setText(String.format("%s : %d",name, display_value));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        return mLayout;
    }

    //Define the function for the lock button - determine whether the dimension of the
    public void lockButtonPressed(View v) {
        if (!lock_dimensions) {
            lock_dimensions = true;
            curr_defect.setH(curr_defect.getW());
            parameters[1].setVisibility(View.GONE);
            titles[1].setVisibility(View.GONE);
            //Change the properties of the button
            v.setBackground(getResources().getDrawable(R.drawable.ic_lock_outline_white_24dp));
            v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            lock_dimensions = false;
            parameters[1].setVisibility(View.VISIBLE);
            titles[1].setVisibility(View.VISIBLE);
            v.setBackground(getResources().getDrawable(R.drawable.ic_lock_open_black_24dp));
            v.setBackgroundColor(getResources().getColor(R.color.gray));
        }

    }

}
