package fi.testbed2.service.impl;

import com.google.inject.Singleton;
import com.jhlabs.map.Point2D;
import com.jhlabs.map.proj.MercatorProjection;

import fi.testbed2.android.app.Logger;
import fi.testbed2.domain.MapLocationGPS;
import fi.testbed2.domain.MapLocationXY;
import fi.testbed2.service.CoordinateService;

/**
 * Coordinate service using the Mercator projection to convert
 * GPS coordinates to x,y coordinates in the testbed map image.
 */
@Singleton
public class MercatorCoordinateService implements CoordinateService {

    public static final String STATIC_PROVIDER_NAME = "dummy_provider";

    private static final MapLocationGPS humppila = new MapLocationGPS(60.95357, 23.33907);
    private static final MapLocationGPS porvoo = new MapLocationGPS(60.39069, 25.61653);

    private final Point2D.Double humppilaMercatorXY;

    /**
     * Manually calculated known point in the testbed map image
     */
    private static final MapLocationXY humppilaTestbedXY = new MapLocationXY(99d, 16d);

    /*
     * These are calculated manually and scale the x,y coordinates
     * which are given from Mercator projection to the x,y coordinates
     * of the testbed map still image.
     */
    private static final double xScale = 8314.6379;
    private static final double yScale = -8525.3994;

    public MercatorCoordinateService() {
        Logger.debug("MercatorCoordinateService instantiated");
        humppilaMercatorXY = convertLocationToMercatorXY(humppila);
    }

    /**
     * Returns a known point directly at the road intersection near Humppila.
     * This point can be used to check that the x,y coordinates are calculated correctly.
     * The x,y coordinates correspond to the testbed map image x,y coordinates in pixels.
     *
     * @return
     */
    @Override
    public MapLocationXY getKnownPositionForTesting() {
        return humppilaTestbedXY;
    }

    /**
     * Converts given location coordinates to the x,y coordinates in the testbed map image.
     *
     * @param location
     * @return
     */
    @Override
    public MapLocationXY convertLocationToXyPos(MapLocationGPS location) {
        if (location == null) {
            return null;
        }
        Point2D.Double mercatorPoint = convertLocationToMercatorXY(location);
        Point2D.Double converted = convertToXYInTestbedMap(mercatorPoint);

        return new MapLocationXY(converted.x, converted.y);

    }

    /**
     * Converts the given point in Mercator x,y coordinates to the x,y coordinates
     * in the testbed map image.
     *
     * @param point
     * @return
     */
    private Point2D.Double convertToXYInTestbedMap(Point2D.Double point) {

        double distanceInTestbedImagePxX = (point.x - humppilaMercatorXY.x) * xScale;
        double distanceInTestbedImagePxY = (point.y - humppilaMercatorXY.y) * yScale;

        double distanceInMercatorPxX = humppilaTestbedXY.getX() + distanceInTestbedImagePxX;
        double finalYPos = humppilaTestbedXY.getY() + distanceInTestbedImagePxY;

        return new Point2D.Double(distanceInMercatorPxX, finalYPos);
    }


    /**
     * Converts given location coordinates to the x,y coordinates given by the Mercator
     * projection. These are not the final testbed map image coordinates but they need
     * to be scaled.
     *
     * @param coordinate
     * @return
     */
    private Point2D.Double convertLocationToMercatorXY(MapLocationGPS coordinate) {

        if (coordinate == null) {
            return null;
        }

        MercatorProjection projection = new MercatorProjection();

        double lat = coordinate.getLatitude();
        double lon = coordinate.getLongitude();

        // convert to radian
        lat = lat * Math.PI / 180d;
        lon = lon * Math.PI / 180d;

        return projection.project(lon, lat, new Point2D.Double());

    }

}
