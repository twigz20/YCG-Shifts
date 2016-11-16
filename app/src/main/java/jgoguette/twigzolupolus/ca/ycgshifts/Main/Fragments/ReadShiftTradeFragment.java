package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTrade.ReadShiftTradeView;
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
    private Message messages;

    @Override
    public void setRead() {

    }

    @Override
    public void loadProfilePic() {

    }

    public enum Day {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

    Day[] dayValues = Day.values();

    List<User> usersList = new ArrayList<>();

    Context context;

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
        if (getArguments() != null) {
            messages = (Message) getArguments().getSerializable(ARG_NOTIFICATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_shift_trade, container, false);
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

}
