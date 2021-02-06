package fi.testbed2.util;

import org.junit.Test;

import fi.testbed2.AbstractTestCase;

import static junit.framework.Assert.assertEquals;

public class SeekBarUtilTest extends AbstractTestCase {

    @Test
    public void testGetFrameNumberFromSeekBarValue() {

        // NOTE! Returned frame number starts from 0, NOT 1

        assertEquals(0, SeekBarUtil.getFrameIndexFromSeekBarValue(0, 10));
        assertEquals(9, SeekBarUtil.getFrameIndexFromSeekBarValue(100, 10));

        assertEquals(1, SeekBarUtil.getFrameIndexFromSeekBarValue(50, 3));
        assertEquals(0, SeekBarUtil.getFrameIndexFromSeekBarValue(25, 3));
        assertEquals(8, SeekBarUtil.getFrameIndexFromSeekBarValue(90, 10));
        assertEquals(4, SeekBarUtil.getFrameIndexFromSeekBarValue(50, 10));

        assertEquals(0, SeekBarUtil.getFrameIndexFromSeekBarValue(0, 1));
        assertEquals(0, SeekBarUtil.getFrameIndexFromSeekBarValue(50, 1));
        assertEquals(0, SeekBarUtil.getFrameIndexFromSeekBarValue(100, 1));

        assertEquals(10000 - 1, SeekBarUtil.getFrameIndexFromSeekBarValue(100, 10000));

        // Error values
        assertEquals(-1, SeekBarUtil.getFrameIndexFromSeekBarValue(100, 0));
        assertEquals(-1, SeekBarUtil.getFrameIndexFromSeekBarValue(-100, 10));
        assertEquals(-1, SeekBarUtil.getFrameIndexFromSeekBarValue(200, 10));

    }

    @Test
    public void testGetSeekBarValueFromFrameNumber() {

        assertEquals(0, SeekBarUtil.getSeekBarValueFromFrameNumber(0, 2));
        assertEquals(100, SeekBarUtil.getSeekBarValueFromFrameNumber(1, 2));

        assertEquals(0, SeekBarUtil.getSeekBarValueFromFrameNumber(0, 3));
        assertEquals(50, SeekBarUtil.getSeekBarValueFromFrameNumber(1, 3));
        assertEquals(100, SeekBarUtil.getSeekBarValueFromFrameNumber(2, 3));

        assertEquals(0, SeekBarUtil.getSeekBarValueFromFrameNumber(0, 5));
        assertEquals(25, SeekBarUtil.getSeekBarValueFromFrameNumber(1, 5));
        assertEquals(50, SeekBarUtil.getSeekBarValueFromFrameNumber(2, 5));
        assertEquals(75, SeekBarUtil.getSeekBarValueFromFrameNumber(3, 5));
        assertEquals(100, SeekBarUtil.getSeekBarValueFromFrameNumber(4, 5));

        assertEquals(0, SeekBarUtil.getSeekBarValueFromFrameNumber(0, 1));

        assertEquals(100, SeekBarUtil.getSeekBarValueFromFrameNumber(10, 11));
        assertEquals(90, SeekBarUtil.getSeekBarValueFromFrameNumber(9, 11));
        assertEquals(80, SeekBarUtil.getSeekBarValueFromFrameNumber(8, 11));
        assertEquals(70, SeekBarUtil.getSeekBarValueFromFrameNumber(7, 11));
        assertEquals(60, SeekBarUtil.getSeekBarValueFromFrameNumber(6, 11));
        assertEquals(50, SeekBarUtil.getSeekBarValueFromFrameNumber(5, 11));
        assertEquals(40, SeekBarUtil.getSeekBarValueFromFrameNumber(4, 11));
        assertEquals(30, SeekBarUtil.getSeekBarValueFromFrameNumber(3, 11));
        assertEquals(20, SeekBarUtil.getSeekBarValueFromFrameNumber(2, 11));
        assertEquals(10, SeekBarUtil.getSeekBarValueFromFrameNumber(1, 11));
        assertEquals(0, SeekBarUtil.getSeekBarValueFromFrameNumber(0, 11));

        // Error values
        assertEquals(-1, SeekBarUtil.getSeekBarValueFromFrameNumber(0, 0));
        assertEquals(-1, SeekBarUtil.getSeekBarValueFromFrameNumber(10, 10));

    }
}
