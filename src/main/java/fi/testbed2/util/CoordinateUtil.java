package fi.testbed2.util;

import android.location.Location;
import com.jhlabs.map.Point2D;
import com.jhlabs.map.proj.MercatorProjection;


/**
 * Utility class which is used to convert GPS coordinates to
 * x,y positions in the testbed map image.
 */
public class CoordinateUtil {

    public static final String PROVIDER_NAME = "dummy_provider";

    private static Location knownPointInHumppila = new Location(PROVIDER_NAME);
    private static Location knownPointInPorvoo = new Location(PROVIDER_NAME);

    private static Point2D.Double knownPointInHumppilaXY;

    /**
     * Manually calculated known point in the testbed map image
     */
    private static Point2D.Double knownPointInHumppilaXYInTestbedMap =
            new Point2D.Double(99d, 16d);

    /*
    * These are calculated manually and scale the x,y coordinates
    * which are given from Mercator projection to the x,y coordinates
    * of the testbed map still image.
    */
    private static double xScale = 8314.6379;
    private static double yScale = -8525.3994;

    static {

        knownPointInHumppila.setLatitude(60.95357);
        knownPointInHumppila.setLongitude(23.33907);

        knownPointInPorvoo.setLatitude(60.39069);
        knownPointInPorvoo.setLongitude(25.61653);

        knownPointInHumppilaXY = convertLocationToMercatorXY(knownPointInHumppila);

    }

    /**
     * Converts given location coordinates to the x,y coordinates in the testbed map image.
     * @param location
     * @return
     */
    public static Point2D.Double convertLocationToTestbedImageXY(Location location) {
        Point2D.Double mercatorPoint = convertLocationToMercatorXY(location);
        return convertToXYInTestbedMap(mercatorPoint);
    }

    /**
     * Converts the given point in Mercator x,y coordinates to the x,y coordinates
     * in the testbed map image.
     *
     * @param point
     * @return
     */
    private static Point2D.Double convertToXYInTestbedMap(Point2D.Double point) {

        double distanceInTestbedImagePxX = (point.x - knownPointInHumppilaXY.x) * xScale;
        double distanceInTestbedImagePxY = (point.y - knownPointInHumppilaXY.y) * yScale;

        double distanceInMercatorPxX = knownPointInHumppilaXYInTestbedMap.x+distanceInTestbedImagePxX;
        double finalYPos = knownPointInHumppilaXYInTestbedMap.y+distanceInTestbedImagePxY;

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
    private static Point2D.Double convertLocationToMercatorXY(Location coordinate) {

        MercatorProjection projection = new MercatorProjection();

        double lat = coordinate.getLatitude();
        double lon = coordinate.getLongitude();

        // convert to radian
        lat = lat * Math.PI / 180d;
        lon = lon * Math.PI / 180d;

        return projection.project(lon, lat, new Point2D.Double());

    }

}
