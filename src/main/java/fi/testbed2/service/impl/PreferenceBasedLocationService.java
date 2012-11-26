package fi.testbed2.service.impl;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fi.testbed2.Environment;
import fi.testbed2.app.Logging;
import fi.testbed2.data.MapLocationGPS;
import fi.testbed2.data.MapLocationXY;
import fi.testbed2.service.CoordinateService;
import fi.testbed2.service.LocationService;
import fi.testbed2.service.MunicipalityService;
import fi.testbed2.service.SettingsService;

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
    SettingsService settingsService;

    @Inject
    CoordinateService coordinateService;

    private MapLocationXY userLocationXY;
    private MapLocationGPS userLocation;

    public PreferenceBasedLocationService() {
        Logging.debug("PreferenceBasedLocationService instantiated");
    }

    @Override
    public MapLocationXY getUserLocationXY() {

        if (!settingsService.showUserLocation()) {
            return null;
        }

        if (Environment.TEST_ENVIRONMENT && !getProvider().equals(LOCATION_PROVIDER_FIXED)) {
            return municipalityService.getMunicipality("Helsinki").getXyPos();
        } else {
            return userLocationXY;
        }

    }

    public MapLocationGPS getUserLastLocation() {

        if (!settingsService.showUserLocation()) {
            return null;
        }

        if (Environment.TEST_ENVIRONMENT) {
            return municipalityService.getMunicipality("Kouvola").getGpsPos();
        } else {
            return userLocation;
        }
    }

    @Override
    public void startListeningLocationChanges() {

        Logging.debug("Started listening location changes");

        String provider = getProvider();

        if (provider.equals(LOCATION_PROVIDER_FIXED)) {
            userLocation = settingsService.getSavedFixedLocation();
            userLocationXY = coordinateService.convertLocationToXyPos(userLocation);
            return;
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
        if (lastKnownLocation!=null) {
            userLocation = new MapLocationGPS(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            userLocationXY = coordinateService.convertLocationToXyPos(userLocation);
        }

        try {
            locationManager.requestLocationUpdates(provider,
                    LOCATION_UPDATE_INTERVAL_MINUTES * 60 * 1000, LOCATION_UPDATE_ACCURACY_METERS, this);
        } catch (Exception e) {
            /*
             * Might throw exception if location provider not found.
             * Seems to only occur in Android emulator, see bug description:
             * http://code.google.com/p/android/issues/detail?id=19857
             */
            Logging.debug("RequestLocationUpdates failed: "+e.getMessage());
        }

    }

    @Override
    public void stopListeningLocationChanges() {
        Logging.debug("Stopped listening location changes");
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location!=null) {
            userLocation = new MapLocationGPS(location.getLatitude(), location.getLongitude());;
            userLocationXY = coordinateService.convertLocationToXyPos(userLocation);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    private String getProvider() {
        return settingsService.getLocationProvider();
    }

}
