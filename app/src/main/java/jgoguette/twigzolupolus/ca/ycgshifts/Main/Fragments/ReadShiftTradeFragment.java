package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTrade.ReadShiftTradePresenter;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTrade.ReadShiftTradePresenterImpl;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTrade.ReadShiftTradeView;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.MainActivity;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReadShiftTradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadShiftTradeFragment extends Fragment implements ReadShiftTradeView{
    private static final String ARG_NOTIFICATION = "Messages";

    // TODO: Rename and change types of parameters
    private Message message;

    public enum Day {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

    Day[] dayValues = Day.values();

    List<User> usersList = new ArrayList<>();

    Context context;

    ReadShiftTradePresenter presenter;

    public ReadShiftTradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReadShiftTradeMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReadShiftTradeFragment newInstance(Message message) {
        ReadShiftTradeFragment fragment = new ReadShiftTradeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTIFICATION, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        presenter = new ReadShiftTradePresenterImpl(this);
        if (getArguments() != null) {
            message = (Message) getArguments().getSerializable(ARG_NOTIFICATION);

            presenter.setRead(message.getKey());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_read_shift_trade, container, false);

        setTitle();

        TextView sender = (TextView)view.findViewById(R.id.shiftSwapName);
        sender.setText(message.getSender());

        TextView date = (TextView)view.findViewById(R.id.shiftSwapDate);
        date.setText(message.getDate());

        TextView msg = (TextView)view.findViewById(R.id.shiftSwapMessage);
        msg.setText(message.getMessage());

        TextView time = (TextView)view.findViewById(R.id.shiftSwapTime);
        time.setText(message.getTime());

        TextView day = (TextView)view.findViewById(R.id.shiftSwapDay);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy (z)", Locale.CANADA);
        try {
            Date d = dateFormat.parse(message.getDate());
            LocalDateTime localDate = LocalDateTime.fromDateFields(d);
            day.setText(convertIntToDay(localDate.getDayOfWeek()-1));
        } catch (ParseException e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        Button accept = (Button)view.findViewById(R.id.shiftSwapAccept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.acceptShiftTrade(message.getKey(), message.getShift());
            }
        });

        Button reject = (Button)view.findViewById(R.id.shiftSwapReject);
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.rejectShiftTrade(message.getKey());
                navigateBackToMessages();
            }
        });

        loadProfilePic();

        return view;
    }

    @Override
    public void onShiftTradedSuccess() {
        navigateBackToMessages();
    }

    @Override
    public void onShiftTradedFailed() {

    }

    @Override
    public void loadProfilePic() {
        presenter.loadProfilePic(message.getShift().getOwner());
    }

    @Override
    public void onProfilePicLoadedFailure() {

    }

    @Override
    public void setProfilePic(Uri uri) {
        ImageView profilePic = (ImageView) getView().findViewById(R.id.shiftSwapImage);
        Picasso.with(context)
                .load(uri)
                .fit()
                .into(profilePic);
    }

    @Override
    public void navigateBackToMessages() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public String convertDayToString(Day day) {
        String[] days = getResources().getStringArray(R.array.days);
        if(day == Day.MON)
            return days[0];
        else if(day == Day.TUE)
            return days[1];
        else if(day == Day.WED)
            return days[2];
        else if(day == Day.THU)
            return days[3];
        else if(day == Day.FRI)
            return days[4];
        else if(day == Day.SAT)
            return days[5];
        else
            return days[6];
    }

    public String convertIntToDay(int day) {
        String d = null;
        try {
            d = convertDayToString(dayValues[day]);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return d;
    }

    @Override
    public void setTitle() {
        ((MainActivity)context).setTitle(((MainActivity)context).user.getName());
    }

}
