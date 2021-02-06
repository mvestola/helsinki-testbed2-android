package fi.testbed2.service;

import fi.testbed2.domain.MapLocationGPS;
import fi.testbed2.domain.MapLocationXY;

public interface LocationService {

    String LOCATION_PROVIDER_FIXED = "fixed";
    String LOCATION_PROVIDER_NETWORK = "network";
    String LOCATION_PROVIDER_GPS = "gps";

    MapLocationGPS getUserLastLocation();
    MapLocationXY getUserLocationXY();

    void startListeningLocationChanges();
    void stopListeningLocationChanges();

}
