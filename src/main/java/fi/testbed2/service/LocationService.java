package fi.testbed2.service;

import fi.testbed2.domain.MapLocationGPS;
import fi.testbed2.domain.MapLocationXY;

public interface LocationService {

    public static final String LOCATION_PROVIDER_FIXED = "fixed";
    public static final String LOCATION_PROVIDER_NETWORK = "network";
    public static final String LOCATION_PROVIDER_GPS = "gps";

    public MapLocationGPS getUserLastLocation();
    public MapLocationXY getUserLocationXY();

    public void startListeningLocationChanges();
    public void stopListeningLocationChanges();

}
