package fi.testbed2.util;

import android.location.Location;
import com.jhlabs.map.Point2D;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

/**
 * This class contains some tests to test that GPS coordinates are correctly
 * mapped to the right x,y coordinates in the testbed map image.
 * The tests contain some known points (e.g. road intersections) whose
 * GPS coordinates are manually fetched from Eniro maps (http://www.eniro.fi/kartta/)
 * and the corresponding x,y coordinates are manually calculated from the testbed map image
 * with GIMP.
 */
@RunWith(RobolectricTestRunner.class)
public class CoordinateUtilTest {

    /**
     * Allow some small variance when converting to (x,y) coordinates.
     * Specify value in pixels
     */
    private static final double PRECISION_STRICT = 3.0;

    /**
     * Some points are probably not so accurate in testbed map, so allow
     * more loose precision.
     */
    private static final double PRECISION_LOOSE = 7.0;

    /**
     * Some points seem to be way-off the actual GPS coordinates.
     */
    private static final double PRECISION_VERY_LOOSE = 13.0;


    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void testGetRoadIntersectionNearHumppila() throws Exception {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(60.95357);
        point.setLongitude(23.33907);

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(99.0, pos.x, PRECISION_STRICT);
        assertEquals(16.0, pos.y, PRECISION_STRICT);

    }

    @Test
    public void testGetRoadIntersectionNearPorvoo() throws Exception {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(60.39069);
        point.setLongitude(25.61653);

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(429.5, pos.x, PRECISION_STRICT);
        assertEquals(187.0, pos.y, PRECISION_STRICT);

    }

    @Test
    public void testGetRoadIntersectionNearTammela() throws Exception {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(60.82826);
        point.setLongitude(24.01678);

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(197.5, pos.x, PRECISION_STRICT);
        assertEquals(55.0, pos.y, PRECISION_STRICT);

    }

    @Test
    public void getRoadIntersectionNearTammela() {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(60.82826);
        point.setLongitude(24.01678);

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(197.5, pos.x, PRECISION_STRICT);
        assertEquals(55.0, pos.y, PRECISION_STRICT);

    }

    @Test
    public void testGetRoadIntersectionOfKeha3AndLansivayla() throws Exception {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(60.13773);
        point.setLongitude(24.53475);

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(273.8, pos.x, PRECISION_STRICT);
        assertEquals(262.6, pos.y, PRECISION_STRICT);

    }

    @Test
    public void testGetPointInVihdintieKeha3Intersection() throws Exception {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(60.27414);
        point.setLongitude(24.80443);

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(311.0, pos.x, PRECISION_STRICT);
        assertEquals(222.4, pos.y, PRECISION_STRICT);

    }

    @Test
    public void testGetRoadIntersectionNearKouvola() throws Exception {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(60.88512);
        point.setLongitude(26.76588);

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(596.9, pos.x, PRECISION_STRICT);
        assertEquals(35.5, pos.y, PRECISION_STRICT);

    }

    @Test
    public void testGetPointNearLoviisa() throws Exception {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(60.49151);
        point.setLongitude(25.95929);

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(480.5, pos.x, PRECISION_STRICT);
        assertEquals(155.4, pos.y, PRECISION_STRICT);

    }

    @Test
    public void testGetPointNearVahvala() throws Exception {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(60.95451);
        point.setLongitude(22.86604);

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(29.6, pos.x, PRECISION_STRICT);
        assertEquals(13.5, pos.y, PRECISION_STRICT);

    }

    @Test
    public void testGetPointNearRaasepori() throws Exception {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(60.39320);
        point.setLongitude(23.13106);

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(62.2, pos.x, PRECISION_LOOSE);
        assertEquals(185, pos.y, PRECISION_STRICT);

    }

    @Test
    public void testGetPointNearKarjalohja() throws Exception {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(60.21003);
        point.setLongitude(23.66773);

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(141.0, pos.x, PRECISION_LOOSE);
        assertEquals(241.0, pos.y, PRECISION_STRICT);

    }

    @Test
    public void testGetPointInRussaro() throws Exception {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(59.77339);
        point.setLongitude(22.94221);

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(28.5, pos.x, PRECISION_VERY_LOOSE);
        assertEquals(366.2, pos.y, PRECISION_LOOSE);

    }

    @Test
    public void testTopLeftPointOfTestbedImage() throws Exception {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(61.005);  // y
        point.setLongitude(22.657);   // x

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(0.0, pos.x, PRECISION_STRICT);
        assertEquals(0.0, pos.y, PRECISION_STRICT);

    }

    @Test
    public void testTopRightPointOfTestbedImage() throws Exception {

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        point.setLatitude(61.005);  // y
        point.setLongitude(26.792);   // x

        Point2D.Double pos = CoordinateUtil.convertLocationToTestbedImageXY(point);

        assertEquals(600.0, pos.x, PRECISION_STRICT);
        assertEquals(0.0, pos.y, PRECISION_STRICT);

    }

    @Test
    public void testErrorValues() throws Exception {

        Point2D.Double nullPos = CoordinateUtil.convertLocationToTestbedImageXY(null);
        assertNull(nullPos);

        Location point = new Location(CoordinateUtil.PROVIDER_NAME);
        Point2D.Double emptyPos = CoordinateUtil.convertLocationToTestbedImageXY(point);
        assertNotNull(emptyPos);

    }

}