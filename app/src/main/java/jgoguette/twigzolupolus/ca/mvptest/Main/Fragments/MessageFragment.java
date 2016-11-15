package jgoguette.twigzolupolus.ca.mvptest.Main.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jgoguette.twigzolupolus.ca.mvptest.Main.Fragments.Messages.MessageView;
import jgoguette.twigzolupolus.ca.mvptest.Main.MainActivity;
import jgoguette.twigzolupolus.ca.mvptest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements MessageView {

    private Context context;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        setTitle();

        return view;
    }

    @Override
    public void setTitle() {
        ((MainActivity)context).toolbar.setTitle(R.string.Messages_Title);
    }
}
