package fi.testbed2.util;

import fi.testbed2.AbstractTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColorUtilTest extends AbstractTestCase {

    @Test
    public void testGetOpacityFromARGB() {

        // ff (255) = no transparency,  0.0 is completely transparent

        // With alpha
        assertEquals(1.0, ColorUtil.getOpacityFromARGB("#ff000000"), 0.0);
        assertEquals(0.0, ColorUtil.getOpacityFromARGB("#00000000"), 0.0);
        assertEquals(0.5, ColorUtil.getOpacityFromARGB("#80000000"), 0.01);
        assertEquals(0.196, ColorUtil.getOpacityFromARGB("#32000000"), 0.01);

        // No alpha
        assertEquals(1.0, ColorUtil.getOpacityFromARGB("#000000"), 0.0);
        assertEquals(1.0, ColorUtil.getOpacityFromARGB("#cccccc"), 0.0);

    }

    @Test
    public void testGetColorWithoutAlpha() {

        // With alpha
        assertEquals("#000000", ColorUtil.getColorWithoutAlpha("#ff000000"));
        assertEquals("#cecece", ColorUtil.getColorWithoutAlpha("#84cecece"));

        // No alpha
        assertEquals("#000000", ColorUtil.getColorWithoutAlpha("#000000"));
        assertEquals("#cccccc", ColorUtil.getColorWithoutAlpha("#cccccc"));

    }

    @Test
    public void testGetColorWithInvertedAplha() {

        // With alpha
        assertEquals("#00000000", ColorUtil.getColorWithInvertedAplha("#ff000000"));
        assertEquals("#ff000000", ColorUtil.getColorWithInvertedAplha("#00000000"));
        assertEquals("#7b000000", ColorUtil.getColorWithInvertedAplha("#84000000"));

        // No alpha
        assertEquals("#00000000", ColorUtil.getColorWithInvertedAplha("#000000"));
        assertEquals("#00cccccc", ColorUtil.getColorWithInvertedAplha("#cccccc"));

    }
}
