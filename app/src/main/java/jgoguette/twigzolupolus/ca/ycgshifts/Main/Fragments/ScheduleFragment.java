package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Schedule.SchedulePresenter;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Schedule.SchedulePresenterImpl;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Schedule.ScheduleView;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.MainActivity;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Shift;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment
        implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener,
        WeekView.EmptyViewLongPressListener, ScheduleView, ShiftTradeDialogFragment.ScheduleMessageDialogFragmentEvents{

    private final int NULL_HOUR = -1;
    private final int DEFAULT_HOUR = 6;
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;

    Context context;
    private ArrayList<Shift> shifts;
    Message message = new Message();

    private SchedulePresenter presenter;

    private ProgressDialog progressDialog;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity();
        presenter = new SchedulePresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        setTitle();

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) view.findViewById(R.id.weekView);

        // Default hour to go to when opened
        mWeekView.goToHour(DEFAULT_HOUR);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);

        shifts = new ArrayList<>();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getSchedule();
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                String timeDisplay;
                if(hour > 11)
                    timeDisplay = (hour - 12) == 0 ?  "12 PM" : (hour - 12) + "PM";
                else if(hour == 0)
                    timeDisplay = "12 AM";
                else
                    timeDisplay = hour + " AM";

                return timeDisplay;
            }
        });
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        Calendar mainCalendar = Calendar.getInstance();
        if (newMonth == mainCalendar.get(Calendar.MONTH)) {
            List<WeekViewEvent> events = new ArrayList<>();
            DateTimeFormatter dt = DateTimeFormat.forPattern(getResources().getString(R.string.date_format));
            SimpleDateFormat ft = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.CANADA);

            Date date;
            try {
                for (int i = 0; i < shifts.size(); i++) {
                    date = ft.parse(shifts.get(i).getStart_time());
                    Calendar startTime = Calendar.getInstance();
                    startTime.setTime(date);

                    date = ft.parse(shifts.get(i).getEnd_time());
                    Calendar endTime = (Calendar) startTime.clone();
                    endTime.setTime(date);

                    WeekViewEvent event = new WeekViewEvent(i + 1, "Busy", startTime, endTime);
                    event.setColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));
                    events.add(event);
                }
            } catch (ParseException e) {
                System.out.println("Unparseable using " + ft);
            }

            return events;
        } else
            return new ArrayList<>();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_schedule, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    protected String getEventTitle(Calendar time) {
        return String.format(Locale.CANADA,"Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(getActivity(), "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(getActivity(), "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.CANADA);
        try {
            Date date = dateFormat.parse(dateFormat.format(event.getStartTime().getTime()));
            LocalDateTime localDate = new LocalDateTime(date);

            DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy (z)");
            Date d = dateFormat.parse(dateFormat.format(event.getStartTime().getTime()));
            String formattedDate = targetFormat.format(date);

            DateFormat targetFormat2 = new SimpleDateFormat("hh:mm aaa");
            Date d2 = dateFormat.parse(dateFormat.format(event.getStartTime().getTime()));
            String formattedTime = targetFormat2.format(date);

            date = dateFormat.parse(dateFormat.format(event.getEndTime().getTime()));
            DateFormat targetFormat3 = new SimpleDateFormat("hh:mm aaa");
            Date d3 = dateFormat.parse(dateFormat.format(event.getEndTime().getTime()));
            String formattedTime2 = targetFormat3.format(date);

            String formattedTime3 = formattedTime + " - " + formattedTime2;

            String startDate = localDate.toString(getResources().getString(R.string.date_format));
            for(Shift shift : shifts) {
                if(shift.getStart_time().contains(startDate)) {
                    if (!shift.getShiftTradeRequestSent()) {
                        shift.setShiftTradeRequestSent(true);
                        message.setDate(formattedDate);
                        message.setTime(formattedTime3);

                        message.setShift(shift);

                        Toast.makeText(getActivity(), "Long pressed event: \n" + formattedDate
                                + "\n" + formattedTime3, Toast.LENGTH_SHORT).show();

                        ShiftTradeDialogFragment shiftTradeDialogFragment = new ShiftTradeDialogFragment();

                        shiftTradeDialogFragment.setScheduleMessageFragmentEvents(ScheduleFragment.this);
                        // Show Alert DialogFragment
                        shiftTradeDialogFragment
                                .show(((MainActivity) context).getSupportFragmentManager(), "Alert Dialog Fragment");
                    } else {
                       showMessageDialog("Can't Process Request", "Already sent shift trade request for this shift. Please wait for approval.");
                    }
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTitle() {
        ((MainActivity)context).toolbar.setTitle(R.string.Schedule_Title);
    }

    @Override
    public void getSchedule() {
        presenter.getSchedule();
    }

    @Override
    public void sendShiftTradeRequest() {
        presenter.sendShiftTradeRequest(message);
    }

    @Override
    public void onScheduleLoaded(ArrayList<Shift> shifts, int hour) {
        this.shifts = shifts;

        if(hour != NULL_HOUR)
            mWeekView.goToHour(hour);
        else
            mWeekView.goToHour(DEFAULT_HOUR);
        mWeekView.notifyDatasetChanged();

        Toast.makeText(context, "Schedule Updated", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onTradeRequestSent(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTradeRequestNotSent(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessageDialog(String title, String message) {
        AlertDialog ad = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .create();
        ad.show();
    }

    @Override
    public void onMessageEntered(String msg) {
        message.setMessage(msg);
        sendShiftTradeRequest();
    }

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(getString(R.string.schedule_progressDialog_message));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
