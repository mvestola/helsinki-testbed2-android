package fi.testbed2.service;

import fi.testbed2.domain.MapLocationGPS;
import fi.testbed2.domain.MapLocationXY;

public interface LocationService {

    public static final String LOCATION_PROVIDER_FIXED = "fixed";

    public MapLocationGPS getUserLastLocation();
    public MapLocationXY getUserLocationXY();

    public void startListeningLocationChanges();
    public void stopListeningLocationChanges();

}
