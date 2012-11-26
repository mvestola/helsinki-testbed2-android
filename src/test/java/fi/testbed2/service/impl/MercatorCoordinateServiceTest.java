package fi.testbed2.service.impl;

import fi.testbed2.AbstractTestCase;
import fi.testbed2.InjectedTestRunner;
import fi.testbed2.data.MapLocationGPS;
import fi.testbed2.data.MapLocationXY;
import fi.testbed2.service.CoordinateService;
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
@RunWith(InjectedTestRunner.class)
public class MercatorCoordinateServiceTest extends AbstractTestCase {

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

    private static CoordinateService coordinateService = new MercatorCoordinateService();

    @Test
    public void testGetRoadIntersectionNearHumppila() throws Exception {

        MapLocationGPS point = new MapLocationGPS(60.95357, 23.33907);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(99.0, pos.getX(), PRECISION_STRICT);
        assertEquals(16.0, pos.getY(), PRECISION_STRICT);

    }

    @Test
    public void testGetRoadIntersectionNearPorvoo() throws Exception {

        MapLocationGPS point = new MapLocationGPS(60.39069, 25.61653);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(429.5, pos.getX(), PRECISION_STRICT);
        assertEquals(187.0, pos.getY(), PRECISION_STRICT);

    }

    @Test
    public void testGetRoadIntersectionNearTammela() throws Exception {

        MapLocationGPS point = new MapLocationGPS(60.82826, 24.01678);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(197.5, pos.getX(), PRECISION_STRICT);
        assertEquals(55.0, pos.getY(), PRECISION_STRICT);

    }

    @Test
    public void getRoadIntersectionNearTammela() {

        MapLocationGPS point = new MapLocationGPS(60.82826, 24.01678);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(197.5, pos.getX(), PRECISION_STRICT);
        assertEquals(55.0, pos.getY(), PRECISION_STRICT);

    }

    @Test
    public void testGetRoadIntersectionOfKeha3AndLansivayla() throws Exception {

        MapLocationGPS point = new MapLocationGPS(60.13773, 24.53475);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(273.8, pos.getX(), PRECISION_STRICT);
        assertEquals(262.6, pos.getY(), PRECISION_STRICT);

    }

    @Test
    public void testGetPointInVihdintieKeha3Intersection() throws Exception {

        MapLocationGPS point = new MapLocationGPS(60.27414, 24.80443);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(311.0, pos.getX(), PRECISION_STRICT);
        assertEquals(222.4, pos.getY(), PRECISION_STRICT);

    }

    @Test
    public void testGetRoadIntersectionNearKouvola() throws Exception {

        MapLocationGPS point = new MapLocationGPS(60.88512, 26.76588);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(596.9, pos.getX(), PRECISION_STRICT);
        assertEquals(35.5, pos.getY(), PRECISION_STRICT);

    }

    @Test
    public void testGetPointNearLoviisa() throws Exception {

        MapLocationGPS point = new MapLocationGPS(60.49151, 25.95929);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(480.5, pos.getX(), PRECISION_STRICT);
        assertEquals(155.4, pos.getY(), PRECISION_STRICT);

    }

    @Test
    public void testGetPointNearVahvala() throws Exception {

        MapLocationGPS point = new MapLocationGPS(60.95451, 22.86604);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(29.6, pos.getX(), PRECISION_STRICT);
        assertEquals(13.5, pos.getY(), PRECISION_STRICT);

    }

    @Test
    public void testGetPointNearRaasepori() throws Exception {

        MapLocationGPS point = new MapLocationGPS(60.39320, 23.13106);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(62.2, pos.getX(), PRECISION_LOOSE);
        assertEquals(185, pos.getY(), PRECISION_STRICT);

    }

    @Test
    public void testGetPointNearKarjalohja() throws Exception {

        MapLocationGPS point = new MapLocationGPS(60.21003, 23.66773);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(141.0, pos.getX(), PRECISION_LOOSE);
        assertEquals(241.0, pos.getY(), PRECISION_STRICT);

    }

    @Test
    public void testGetPointInRussaro() throws Exception {

        MapLocationGPS point = new MapLocationGPS(59.77339, 22.94221);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(28.5, pos.getX(), PRECISION_VERY_LOOSE);
        assertEquals(366.2, pos.getY(), PRECISION_LOOSE);

    }

    @Test
    public void testTopLeftPointOfTestbedImage() throws Exception {

        MapLocationGPS point = new MapLocationGPS(61.005, 22.657);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(0.0, pos.getX(), PRECISION_STRICT);
        assertEquals(0.0, pos.getY(), PRECISION_STRICT);

    }

    @Test
    public void testTopRightPointOfTestbedImage() throws Exception {

        MapLocationGPS point = new MapLocationGPS(61.005, 26.792);
        MapLocationXY pos = coordinateService.convertLocationToXyPos(point);

        assertEquals(600.0, pos.getX(), PRECISION_STRICT);
        assertEquals(0.0, pos.getY(), PRECISION_STRICT);

    }

    @Test
    public void testErrorValues() throws Exception {

        MapLocationXY nullPos = coordinateService.convertLocationToXyPos(null);
        assertNull(nullPos);

        MapLocationGPS point = new MapLocationGPS(0,0);
        MapLocationXY emptyPos = coordinateService.convertLocationToXyPos(point);
        assertNotNull(emptyPos);

    }

}