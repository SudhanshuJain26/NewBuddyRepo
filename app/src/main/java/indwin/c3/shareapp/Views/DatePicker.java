package indwin.c3.shareapp.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.fragments.ProfileFormStep2Fragment1;
import indwin.c3.shareapp.fragments.ProfileFormStep2Fragment3;

/**
 * Created by shubhang on 04/04/16.
 */
public class DatePicker {
    private static final int MIN_YEAR = 1970;

    private static final int MAX_YEAR = 2099;

    private static final String[] PICKER_DISPLAY_MONTHS_NAMES = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
            "Nov", "Dec"};

    private static final String[] MONTHS = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};

    private View view;
    private Activity activity;
    private AlertDialog.Builder builder;
    private AlertDialog pickerDialog;
    private boolean build = false;
    private NumberPicker monthNumberPicker;
    private NumberPicker yearNumberPicker;
    private NumberPicker dateNumberPicker;
    int height, width;
    String dateType;
    private ImageButton closeDatePciker;
    private TextView header;

    public DatePicker(Activity activity, String dateType) {
        this.activity = activity;
        this.dateType = dateType;
        this.view = activity.getLayoutInflater().inflate(R.layout.date_picker_view, new RelativeLayout(activity), false);
    }

    public void build(DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener) {
        this.build(-1, -1, -1, positiveButtonListener, negativeButtonListener);
    }

    private int currentYear;
    private int currentMonth;
    private int currentDate;

    public void build(int selectedDate, int selectedMonth, int selectedYear, DialogInterface.OnClickListener positiveButtonListener,
                      DialogInterface.OnClickListener negativeButtonListener) {

        final Calendar instance = Calendar.getInstance();
        currentDate = instance.get(Calendar.DATE);
        currentMonth = instance.get(Calendar.MONTH);
        currentYear = instance.get(Calendar.YEAR);

        if (selectedMonth > 11 || selectedMonth < -1) {
            selectedMonth = currentMonth;
        }

        if (selectedYear < MIN_YEAR || selectedYear > MAX_YEAR) {
            selectedYear = currentYear;
        }

        if (selectedDate == -1) {
            selectedDate = currentDate;
        }
        if (selectedMonth == -1) {
            selectedMonth = currentMonth;
        }

        if (selectedYear == -1) {
            selectedYear = currentYear;
        }

        builder = new AlertDialog.Builder(activity, R.style.Theme_Window_NoMinWidth);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        height = displaymetrics.heightPixels - statusBarHeight;
        width = displaymetrics.widthPixels;
        builder.setView(view);

        header = (TextView) view.findViewById(R.id.heading);
        closeDatePciker = (ImageButton) view.findViewById(R.id.close_date_picker);
        dateNumberPicker = (NumberPicker) view.findViewById(R.id.dateNumberPicker);
        dateNumberPicker.setMinValue(1);
        dateNumberPicker.setMaxValue(31);

        monthNumberPicker = (NumberPicker) view.findViewById(R.id.monthNumberPicker);
        monthNumberPicker.setDisplayedValues(PICKER_DISPLAY_MONTHS_NAMES);

        monthNumberPicker.setMinValue(0);
        monthNumberPicker.setMaxValue(MONTHS.length - 1);

        yearNumberPicker = (NumberPicker) view.findViewById(R.id.yearNumberPicker);
        if ("VerificationDate".equals(dateType)) {
            yearNumberPicker.setMinValue(currentYear);
            yearNumberPicker.setMaxValue(MAX_YEAR);
        } else {
            yearNumberPicker.setMinValue(MIN_YEAR);
            //change to currentYear - 18 for only above 18
            yearNumberPicker.setMaxValue(currentYear);
        }

        dateNumberPicker.setValue(selectedDate);
        monthNumberPicker.setValue(selectedMonth);
        yearNumberPicker.setValue(selectedYear);

        dateNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        monthNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        yearNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        build = true;
        pickerDialog = builder.create();
        Button confirmCollegeEndDateButton = (Button) view.findViewById(R.id.confirm_end_date);
        confirmCollegeEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("VerificationDate".equals(dateType)) {
                    long dayMillisec = 86400 * 1000;

                    try {
                        Calendar c = Calendar.getInstance();
                        c.set(getSelectedYear(), getSelectedMonth(), getSelectedDate(), 0, 0);
                        Date currentDate = new Date();
                        long currentTime = currentDate.getTime();
                        long selectedTime = c.getTime().getTime();
                        if ((selectedTime < (3 * dayMillisec + currentTime)) || (selectedTime > (30 * dayMillisec) + currentTime)) {
                            Toast.makeText(activity, "Sorry, can't do it then!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {


                    }
                    //if (getSelectedYear() == getCurrentYear()) {
                    //    if ((getSelectedMonth() == getCurrentMonth() && getSelectedDate() <= (currentDate + 2)) || ((getSelectedMonth() == (getCurrentMonth() + 1) && getSelectedDate() > currentDate)) || (getSelectedMonth() >= (getCurrentMonth() + 2))) {
                    //        Toast.makeText(activity, "Sorry, can't do it then!", Toast.LENGTH_SHORT).show();
                    //        return;
                    //    } else if (getSelectedMonth() < getCurrentMonth()) {
                    //        Toast.makeText(activity, "Sorry, can't do it then!", Toast.LENGTH_SHORT).show();
                    //        return;
                    //    }
                    //}
                    pickerDialog.dismiss();
                    ProfileFormStep2Fragment3.confirmVerificationDate();
                } else {
                    ProfileFormStep2Fragment1.confirmDOB();
                    pickerDialog.dismiss();
                }
            }
        });
        closeDatePciker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDialog.dismiss();
            }
        });
        if ("VerificationDate".equals(dateType)) {
            confirmCollegeEndDateButton.setText("Submit");
            header.setText("Schedule Verification Date");
        } else {
            confirmCollegeEndDateButton.setText("Save");
            header.setText("Enter date of birth");
        }
    }

    public void show() {
        if (build) {
            pickerDialog.show();
            pickerDialog.getWindow().setLayout(width, height);
        } else {
            throw new IllegalStateException("Build picker before use");
        }
    }

    public int getSelectedMonth() {
        return monthNumberPicker.getValue();
    }

    public String getSelectedMonthName() {
        return MONTHS[monthNumberPicker.getValue()];
    }

    public String getSelectedMonthShortName() {
        return PICKER_DISPLAY_MONTHS_NAMES[monthNumberPicker.getValue()];
    }

    public int getSelectedDate() {
        return dateNumberPicker.getValue();
    }

    public int getSelectedYear() {
        return yearNumberPicker.getValue();
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setMonthValueChangedListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        monthNumberPicker.setOnValueChangedListener(valueChangeListener);
    }

    public void setYearValueChangedListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        yearNumberPicker.setOnValueChangedListener(valueChangeListener);
    }

    public void setMonthWrapSelectorWheel(boolean wrapSelectorWheel) {
        monthNumberPicker.setWrapSelectorWheel(wrapSelectorWheel);
    }

    public void setYearWrapSelectorWheel(boolean wrapSelectorWheel) {
        yearNumberPicker.setWrapSelectorWheel(wrapSelectorWheel);
    }
}