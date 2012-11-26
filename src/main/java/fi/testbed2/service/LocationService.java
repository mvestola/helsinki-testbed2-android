package fi.testbed2.service;

import android.location.Location;
import com.jhlabs.map.Point2D;
import fi.testbed2.data.MapLocationGPS;
import fi.testbed2.data.MapLocationXY;

public interface LocationService {

    public static final String LOCATION_PROVIDER_FIXED = "fixed";

    public MapLocationGPS getUserLastLocation();
    public MapLocationXY getUserLocationXY();

    public void startListeningLocationChanges();
    public void stopListeningLocationChanges();

}
