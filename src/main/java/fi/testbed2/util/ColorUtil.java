package fi.testbed2.util;

/**
 * Utility class for color manipulation.
 */
public class ColorUtil {

    /**
     * Returns the opacity component of the ARGB color as a decimal format.
     *
     * @param argb Color in HEX format (either with or without alpha, e.g.
     *             #CCCCCC or #ffCCCCCC.
     * @return Value between 0.0...1.0 where 0.0 is completely transparent
     * and 1.0 means no transparency.
     */
    public static double getOpacityFromARGB(String argb) {
        int[] components = getARGBComponents(argb);
        double opacity = components[0] / 255.0;

        if (opacity > 1.0) {
            opacity = 1.0;
        } else if (opacity < 0.0) {
            opacity = 0.0;
        }

        return opacity;
    }

    /**
     * Returns the color without alpha channel.
     *
     * @param argb Color in HEX format (either with or without alpha, e.g.
     *             #CCCCCC or #ffCCCCCC.
     * @return
     */
    public static String getColorWithoutAlpha(String argb) {
        int[] components = getARGBComponents(argb);
        return "#" + getTwoDigitHexString(components[1]) +
                getTwoDigitHexString(components[2]) +
                getTwoDigitHexString(components[3]);
    }

    /**
     * Returns the ARGB color with inverted alpha channel.
     *
     * @param argb Color in HEX format (either with or without alpha, e.g.
     *             #CCCCCC or #ffCCCCCC.
     * @return
     */
    public static String getColorWithInvertedAplha(String argb) {

        int[] components = getARGBComponents(argb);
        components[0] = 255 - components[0];

        return "#" + getTwoDigitHexString(components[0]) +
                getTwoDigitHexString(components[1]) +
                getTwoDigitHexString(components[2]) +
                getTwoDigitHexString(components[3]);

    }

    /**
     * Return ARGB values in int array
     *
     * @param argb Color in HEX format (either with or without alpha, e.g.
     *             #CCCCCC or #ffCCCCCC.
     * @return
     */
    private static int[] getARGBComponents(String argb) {

        if (argb.startsWith("#")) {
            argb = argb.replace("#", "");
        }

        int alpha = -1, red = -1, green = -1, blue = -1;

        if (argb.length() == 8) {
            alpha = Integer.parseInt(argb.substring(0, 2), 16);
            red = Integer.parseInt(argb.substring(2, 4), 16);
            green = Integer.parseInt(argb.substring(4, 6), 16);
            blue = Integer.parseInt(argb.substring(6, 8), 16);
        } else if (argb.length() == 6) {
            alpha = 255;
            red = Integer.parseInt(argb.substring(0, 2), 16);
            green = Integer.parseInt(argb.substring(2, 4), 16);
            blue = Integer.parseInt(argb.substring(4, 6), 16);
        }

        return new int[]{alpha, red, green, blue};

    }

    private static String getTwoDigitHexString(int integer) {

        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(integer));
        if (sb.length() < 2) {
            sb.insert(0, '0'); // pad with leading zero if needed
        }

        return sb.toString();

    }

}
