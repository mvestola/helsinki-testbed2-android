package fi.testbed2.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeUtil {

    /**
     * Converts the given timestamp in GMT format to local format.
     *
     * @param gmtTimestamp
     * @return
     */
    public static String getLocalTimestampFromGMTTimestamp(String gmtTimestamp) {

        int year = Integer.parseInt(gmtTimestamp.substring(0, 4));
        int month = Integer.parseInt(gmtTimestamp.substring(4, 6));
        int day = Integer.parseInt(gmtTimestamp.substring(6, 8));
        int hour = Integer.parseInt(gmtTimestamp.substring(8, 10));
        int minute = Integer.parseInt(gmtTimestamp.substring(10, 12));
        int second = 0;

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Helsinki"));
        cal.set(year + 1900, month, day, hour, minute, second);
        cal.setTimeInMillis(cal.getTimeInMillis());

        return String.format("%1$tH:%2$tM", cal.getTime(), cal.getTime());

    }

}
