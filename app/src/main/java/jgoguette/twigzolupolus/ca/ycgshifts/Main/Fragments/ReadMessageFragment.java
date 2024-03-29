package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.MainActivity;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReadMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadMessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_NOTIFICATION = "Messages";

    private Message message;

    private Context context;

    public ReadMessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param messages Parameter 1.
     * @return A new instance of fragment ReadMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReadMessageFragment newInstance(Message messages) {
        ReadMessageFragment fragment = new ReadMessageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTIFICATION, messages);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            message = (Message) getArguments().getSerializable(ARG_NOTIFICATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_read_message, container, false);

        setTitle();

        ((MainActivity)context).setTitle(message.getSubject());

        TextView sender = (TextView)rootView.findViewById(R.id.readNotificationSender);
        sender.setText(message.getSender());

        TextView to = (TextView)rootView.findViewById(R.id.readNotificationTo);
        to.setText(to.getText() + " " + message.getReceiver());

        TextView timeStamp = (TextView)rootView.findViewById(R.id.readNotificationTimeStamp);
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                message.getTimeStamp(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timeStamp.setText(timeAgo);

        TextView msg = (TextView)rootView.findViewById(R.id.readNotificationMessage);
        msg.setText(message.getMessage());

        ((MainActivity)context).toolbar.setTitle(message.getSubject());

        return rootView;
    }

    public void setTitle() {
        ((MainActivity)context).setTitle(message.getSubject());
    }

}
