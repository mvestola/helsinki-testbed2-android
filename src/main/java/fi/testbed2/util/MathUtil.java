package fi.testbed2.util;

import java.util.List;

public class MathUtil {

    /**
     * Returns the closest allowed value (rounds down).
     * Implementation copied from:
     * http://stackoverflow.com/questions/1187352/find-closest-value-in-an-ordererd-list
     *
     * @param value         Some integer
     * @param allowedValues List of allowed values.
     * @return The closest allowed value. For example, if value is 4 and the allowed values are
     * [1,2,3,5,10], returns 3. For value 8, returns 10.
     */
    public static int getClosestValue(int value, List<Integer> allowedValues) {

        int min = Integer.MAX_VALUE;
        int closest = value;

        for (int v : allowedValues) {
            final int diff = Math.abs(v - value);

            if (diff < min) {
                min = diff;
                closest = v;
            }
        }

        return closest;
    }

}
