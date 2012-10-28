package fi.testbed2.service.impl;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jhlabs.map.Point2D;
import fi.testbed2.Environment;
import fi.testbed2.app.Logging;
import fi.testbed2.service.CoordinateService;
import fi.testbed2.service.LocationService;
import fi.testbed2.service.MunicipalityService;
import fi.testbed2.service.PreferenceService;

/**
 * Service class used to provide user location based on the user's settings. The location can be
 * one of the followings: fixed location, coarse network location or fine GPS location
 */
@Singleton
public class PreferenceBasedLocationService implements LocationService, LocationListener {

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
    private Location userLocation;

    public PreferenceBasedLocationService() {
        Logging.debug("PreferenceBasedLocationService instantiated");
    }

    @Override
    public Point2D.Double getUserLocationXY() {

        if (!preferenceService.showUserLocation()) {
            return null;
        }

        if (Environment.TEST_ENVIRONMENT && !getProvider().equals(LOCATION_PROVIDER_FIXED)) {
            return municipalityService.getMunicipality("Helsinki").getXyPos();
        } else {
            return userLocationXY;
        }

    }

    public Location getUserLastLocation() {

        if (!preferenceService.showUserLocation()) {
            return null;
        }

        if (Environment.TEST_ENVIRONMENT) {
            return municipalityService.getMunicipality("Kouvola").getLocation();
        } else {
            return userLocation;
        }
    }

    @Override
    public void startListeningLocationChanges() {

        Logging.debug("Started listening location changes");

        String provider = getProvider();

        if (provider.equals(LOCATION_PROVIDER_FIXED)) {
            userLocation = preferenceService.getSavedFixedLocation();
            userLocationXY = coordinateService.convertLocationToXyPos(userLocation);
            return;
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
        if (lastKnownLocation!=null) {
            userLocation = lastKnownLocation;
            userLocationXY = coordinateService.convertLocationToXyPos(lastKnownLocation);
        }

        locationManager.requestLocationUpdates(provider,
                LOCATION_UPDATE_INTERVAL_MINUTES * 60 * 1000, LOCATION_UPDATE_ACCURACY_METERS, this);

    }

    @Override
    public void stopListeningLocationChanges() {
        Logging.debug("Stopped listening location changes");
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location!=null) {
            userLocation = location;
            userLocationXY = coordinateService.convertLocationToXyPos(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    private String getProvider() {
        return preferenceService.getLocationProvider();
    }

}
