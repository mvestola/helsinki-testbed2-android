package fi.testbed2.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(value = JUnit4.class)
public class MathUtilTest {

    @Test
    public void testGetClosestValue() throws Exception {

        List<Integer> allowedValues = new ArrayList<Integer>();
        Collections.addAll(allowedValues, 1, 2, 3, 5, 10, 15, 30, 45, 100);

        assertEquals(1, MathUtil.getClosestValue(-1, allowedValues));
        assertEquals(1, MathUtil.getClosestValue(0, allowedValues));
        assertEquals(1, MathUtil.getClosestValue(1, allowedValues));
        assertEquals(2, MathUtil.getClosestValue(2, allowedValues));
        assertEquals(3, MathUtil.getClosestValue(4, allowedValues));
        assertEquals(5, MathUtil.getClosestValue(7, allowedValues));
        assertEquals(10, MathUtil.getClosestValue(8, allowedValues));
        assertEquals(45, MathUtil.getClosestValue(70, allowedValues));
        assertEquals(100, MathUtil.getClosestValue(101, allowedValues));
        assertEquals(100, MathUtil.getClosestValue(Integer.MAX_VALUE, allowedValues));

    }
}
