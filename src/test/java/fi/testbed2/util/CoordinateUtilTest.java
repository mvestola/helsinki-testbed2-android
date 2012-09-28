package fi.testbed2.util;

import android.location.Location;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class CoordinateUtilTest {

    @Test
    public void testGetCurrentPixelY() throws Exception {

        Location upperLeft = new Location("dummyprovider");
        upperLeft.setLatitude(61.005744);
        upperLeft.setLongitude(22.676468);

        Location lowerRight = new Location("dummyprovider");
        lowerRight.setLatitude(59.316375);
        lowerRight.setLongitude(26.786728);

        Location humppilanRisteys = new Location("dummyprovider");
        humppilanRisteys.setLatitude(60.953558);
        humppilanRisteys.setLongitude(23.339242);

        double pixelY = CoordinateUtil.getCurrentPixelY(upperLeft, lowerRight, humppilanRisteys);
        double pixelX = CoordinateUtil.getCurrentPixelX(upperLeft, lowerRight, humppilanRisteys);

        System.out.println("pixelY: "+pixelY);
        System.out.println("pixelX: "+pixelX);


    }

    @Test
    public void testGetCurrentPixelX() throws Exception {

    }
}
