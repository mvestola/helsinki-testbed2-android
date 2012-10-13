package fi.testbed2.data;

import android.location.Location;
import com.jhlabs.map.Point2D;
import fi.testbed2.util.CoordinateUtil;

/**
 * Represents a municipality ("Kunta" in Finnish)
 */
public class Municipality {

    private String name;
    private double lat;
    private double lon;
    private Point2D.Double posInMapPx;

    public Municipality(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public Point2D.Double getPositionInMapPx() {
        if (posInMapPx == null) {
            Location tempLocation = new Location(CoordinateUtil.PROVIDER_NAME);
            tempLocation.setLatitude(lat);
            tempLocation.setLongitude(lon);
            posInMapPx = CoordinateUtil.convertLocationToTestbedImageXY(tempLocation);
        }
        return posInMapPx;
    }

}
