package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendBlast.SendBlastPresenter;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendBlast.SendBlastPresenterImpl;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendBlast.SendBlastView;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.MainActivity;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendBlastFragment extends Fragment implements SendBlastView {

    private EditText message;

    private Context context;

    private User user;

    private SendBlastPresenter presenter;

    public SendBlastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        user = new User(((MainActivity)context).user);
        presenter = new SendBlastPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_blast, container, false);

        setTitle();

        message = (EditText)view.findViewById(R.id.blastMessage);

        Button send = (Button)view.findViewById(R.id.sendBlast);
        Button cancel = (Button)view.findViewById(R.id.cancelBlast);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.sendBlast(user.getName(), message.getText().toString());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToHome();
            }
        });

        return view;
    }

    @Override
    public void setTitle() {
        ((MainActivity)context).setTitle(R.string.Send_Blast_Title);
    }

    @Override
    public void setMessageError() {
        message.setError(getString(R.string.message_error));
    }

    @Override
    public void onSuccess() {
        navigateToHome();
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void navigateToHome() {
        ((MainActivity)context).navigateToHome();
    }
}
