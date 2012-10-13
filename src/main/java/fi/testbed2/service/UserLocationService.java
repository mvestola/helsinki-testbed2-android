package fi.testbed2.service;

import com.jhlabs.map.Point2D;

public interface UserLocationService {

    public Point2D.Double getUserLocationInMapPixels();
    public void setUserLocationInMapPixels(Point2D.Double pos);

}
