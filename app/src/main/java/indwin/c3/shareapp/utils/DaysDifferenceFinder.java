package indwin.c3.shareapp.utils;

import java.util.Calendar;

/**
 * Created by shubhang on 04/04/16.
 */
public class DaysDifferenceFinder {
    public static int getDifferenceBetweenDatesInMonths(Calendar startCalendar, Calendar endCalendar) {
        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        return diffMonth;
    }

    public static int getDifferenceBetweenDatesInYears(Calendar startCalendar, Calendar endCalendar) {
        int age = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        if (endCalendar.get(Calendar.MONTH) < startCalendar.get(Calendar.MONTH)) {
            age--;
        } else if (endCalendar.get(Calendar.MONTH) == startCalendar.get(Calendar.MONTH)
                && endCalendar.get(Calendar.DAY_OF_MONTH) < startCalendar.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }
        return age;
    }
}

