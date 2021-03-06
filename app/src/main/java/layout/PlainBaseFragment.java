package layout;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alduran.doranwalsten.flapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlainBaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlainBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlainBaseFragment extends Fragment {

    public PlainBaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlainBaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlainBaseFragment newInstance() {
        PlainBaseFragment fragment = new PlainBaseFragment();
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
        return inflater.inflate(R.layout.fragment_plain_base, container, false);
    }

}
