package jgoguette.twigzolupolus.ca.mvptest.Main.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jgoguette.twigzolupolus.ca.mvptest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendMessageFragment extends Fragment {


    public SendMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_message, container, false);
    }

}
