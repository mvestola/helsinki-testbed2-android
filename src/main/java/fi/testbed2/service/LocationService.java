package fi.testbed2.service;

import com.jhlabs.map.Point2D;

public interface LocationService {

    public Point2D.Double getUserLocationXY();
    public void startListeningLocationChanges();
    public void stopListeningLocationChanges();

}
