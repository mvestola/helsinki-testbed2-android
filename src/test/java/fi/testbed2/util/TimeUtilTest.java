package fi.testbed2.util;

import fi.testbed2.AbstractTestCase;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TimeUtilTest extends AbstractTestCase {

    @Test
    public void testGetLocalTimestampFromGMTTimestamp() throws Exception {

        // Daylight savings time used
        assertEquals("20:35", TimeUtil.getLocalTimestampFromGMTTimestamp("201210121735"));
        assertEquals("21:45", TimeUtil.getLocalTimestampFromGMTTimestamp("201108121845"));

        // No daylight savings time used
        assertEquals("21:15", TimeUtil.getLocalTimestampFromGMTTimestamp("201210281915"));
        assertEquals("22:25", TimeUtil.getLocalTimestampFromGMTTimestamp("201111282025"));

        // Errors
        assertNull(TimeUtil.getLocalTimestampFromGMTTimestamp(""));
        assertNull(TimeUtil.getLocalTimestampFromGMTTimestamp(null));
        assertNull(TimeUtil.getLocalTimestampFromGMTTimestamp("123456789"));
        assertNull(TimeUtil.getLocalTimestampFromGMTTimestamp("2011112820"));

    }

}
