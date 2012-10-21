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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Municipality that = (Municipality) o;

        if (Double.compare(that.lat, lat) != 0) return false;
        if (Double.compare(that.lon, lon) != 0) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (xyPos != null ? !xyPos.equals(that.xyPos) : that.xyPos != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        temp = lat != +0.0d ? Double.doubleToLongBits(lat) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = lon != +0.0d ? Double.doubleToLongBits(lon) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (xyPos != null ? xyPos.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

}
