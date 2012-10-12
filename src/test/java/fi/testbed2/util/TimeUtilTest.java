package fi.testbed2.util;

import fi.testbed2.AbstractTestCase;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class TimeUtilTest extends AbstractTestCase {

    @Test
    public void testGetLocalTimestampFromGMTTimestamp() throws Exception {

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Helsinki"));
        if (cal.getTimeZone().useDaylightTime()) {
            assertEquals("20:35", TimeUtil.getLocalTimestampFromGMTTimestamp("201210121735"));
            assertEquals("21:45", TimeUtil.getLocalTimestampFromGMTTimestamp("201210121845"));
        } else {
            assertEquals("19:35", TimeUtil.getLocalTimestampFromGMTTimestamp("201210121735"));
            assertEquals("20:45", TimeUtil.getLocalTimestampFromGMTTimestamp("201210121845"));
        }

    }

}
