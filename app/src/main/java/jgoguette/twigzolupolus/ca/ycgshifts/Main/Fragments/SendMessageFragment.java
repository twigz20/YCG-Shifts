package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import java.util.ArrayList;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendMessage.SendMessagePresenter;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendMessage.SendMessagePresenterImpl;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendMessage.SendMessageView;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendMessageFragment extends Fragment implements SendMessageView {

    Context context;

    private MultiAutoCompleteTextView to;
    private EditText subject;
    private EditText message;

    private SendMessagePresenter presenter;

    public SendMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        presenter = new SendMessagePresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_message, container, false);

        to = (MultiAutoCompleteTextView) view.findViewById(R.id.editTextTo);

        getUserNames();

        Button send = (Button)view.findViewById(R.id.bSend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subject = (EditText)getView().findViewById(R.id.editTextSubject);
                message = (EditText)getView().findViewById(R.id.etStatusMessage);
                presenter.sendMessage(to.getText().toString(),subject.getText().toString(), message.getText().toString());
            }
        });

        Button cancel = (Button)view.findViewById(R.id.bCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateBackToMessages();
            }
        });
        return view;
    }

    @Override
    public void setToError() {
        to.setError(getString(R.string.to_Error_message));
    }

    @Override
    public void setSubjectError() {
        subject.setError(getString(R.string.subject_Error_message));
    }

    @Override
    public void setMessageError() {
        message.setError(getString(R.string.message_Error_message));
    }

    @Override
    public void onSuccess() {
        navigateBackToMessages();
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void navigateBackToMessages() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void getUserNames() {
        presenter.getUserNames();
    }

    @Override
    public void onUserNamesFetchedSuccessful(ArrayList<String> userNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, userNames);

        to.setAdapter(adapter);
        to.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer() );
    }
}
