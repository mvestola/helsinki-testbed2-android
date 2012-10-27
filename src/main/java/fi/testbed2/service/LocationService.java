package fi.testbed2.service;

import android.location.Location;
import com.jhlabs.map.Point2D;

public interface LocationService {

    public static final String LOCATION_PROVIDER_FIXED = "fixed";

    public Point2D.Double getUserLocationXY();
    public void startListeningLocationChanges();
    public void stopListeningLocationChanges();
    public Location getUserLastKnownLocation();

}
