package fi.testbed2.data;

import com.jhlabs.map.Point2D;

/**
 * Represents a municipality ("Kunta" in Finnish)
 */
public class Municipality {

    private String name;
    private double lat;
    private double lon;
    private Point2D.Double xyPos; // Position (x,y) in testbed map image

    public Municipality(String name, double lat, double lon, Point2D.Double xyPos) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.xyPos = xyPos;
    }

    public Point2D.Double getXyPos() {
        return xyPos;
    }

}
