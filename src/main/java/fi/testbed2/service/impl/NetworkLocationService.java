package fi.testbed2.service.impl;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jhlabs.map.Point2D;
import fi.testbed2.app.MainApplication;
import fi.testbed2.service.CoordinateService;
import fi.testbed2.service.LocationService;
import fi.testbed2.service.MunicipalityService;
import fi.testbed2.service.PreferenceService;

@Singleton
public class NetworkLocationService implements LocationService, LocationListener {

    private static int LOCATION_UPDATE_INTERVAL_MINUTES = 1;
    private static int LOCATION_UPDATE_ACCURACY_METERS = 1000;

    @Inject
    LocationManager locationManager;

    @Inject
    MunicipalityService municipalityService;

    @Inject
    PreferenceService preferenceService;

    @Inject
    CoordinateService coordinateService;

    private Point2D.Double userLocationXY;

    public NetworkLocationService() {

    }

    @Override
    public Point2D.Double getUserLocationXY() {

        if (!preferenceService.showUserLocation()) {
            return null;
        }

        if (MainApplication.isDebug()) {
            return municipalityService.getMunicipality("Helsinki").getXyPos();
        } else {
            return userLocationXY;
        }

    }

    @Override
    public void startListeningLocationChanges() {

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastKnownLocation!=null) {
            userLocationXY = coordinateService.convertLocationToXyPos(lastKnownLocation);
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                LOCATION_UPDATE_INTERVAL_MINUTES * 60 * 1000, LOCATION_UPDATE_ACCURACY_METERS, this);

    }

    @Override
    public void stopListeningLocationChanges() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location!=null) {
            userLocationXY = coordinateService.convertLocationToXyPos(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

}
