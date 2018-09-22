package fi.testbed2.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.LocationManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fi.testbed2.R;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.android.ui.view.MapScaleInfo;
import fi.testbed2.domain.MapLocationGPS;
import fi.testbed2.domain.Municipality;
import fi.testbed2.service.LocationService;
import fi.testbed2.service.MunicipalityService;
import fi.testbed2.service.SettingsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class SharedPreferenceSettingsService implements SettingsService {

    public static final String SHARED_PREFERENCE_FILE_NAME = "fi.testbed2_preferences";

    @Inject
    MunicipalityService municipalityService;

    @Inject
    LocationService locationService;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    Context context;

    public SharedPreferenceSettingsService() {
        Logger.debug("SharedPreferenceSettingsService instantiated");
    }

    @Override
    public void setMapType(String mapType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsService.PREF_MAP_TYPE, mapType);
        editor.apply();
    }

    @Override
    public String getMapType() {
        return sharedPreferences.getString(SettingsService.PREF_MAP_TYPE, "radar");
    }

    /**
     * Saves the bounds of the map user has previously viewed to persistent storage.
     */
    @Override
    public void saveMapBoundsAndScaleFactor(Rect bounds, MapScaleInfo scaleInfo, int orientation) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (editor!=null) {
            if (bounds!=null) {
                // bounds String format is left:top:right:bottom
                editor.putString(getMapBoundsPreferenceKey(orientation),
                        "" + bounds.left + ":" + bounds.top + ":" + bounds.right + ":" + bounds.bottom);
            }
            editor.putFloat(getMapScalePreferenceKey(orientation), scaleInfo.getScaleFactor());
            editor.putFloat(getMapScalePivotXPreferenceKey(orientation), scaleInfo.getPivotX());
            editor.putFloat(getMapScalePivotYPreferenceKey(orientation), scaleInfo.getPivotY());
            editor.apply();
        }

    }

    @Override
    public Rect getSavedMapBounds(int orientation) {

        // left:top:right:bottom
        String frameBoundsPref = sharedPreferences.getString(getMapBoundsPreferenceKey(orientation), null);

        if (frameBoundsPref==null) {
            return null;
        }

        String[] parts = frameBoundsPref.split(":");
        final Rect bounds = new Rect(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));

        return bounds;

    }


    @Override
    public MapScaleInfo getSavedScaleInfo(int orientation) {

        float scaleFactor = sharedPreferences.getFloat(getMapScalePreferenceKey(orientation), 1.0f);
        float pivotX = sharedPreferences.getFloat(getMapScalePivotXPreferenceKey(orientation), 0.0f);
        float pivotY = sharedPreferences.getFloat(getMapScalePivotYPreferenceKey(orientation), 0.0f);

        return new MapScaleInfo(scaleFactor, pivotX, pivotY);
    }

    @Override
    public List<Municipality> getSavedMunicipalities() {
        List<Municipality> municipalities = new ArrayList<Municipality>();

        Set<String> municipalitiesSet = sharedPreferences.getStringSet(PREF_LOCATION_SHOW_MUNICIPALITIES_LIST, Collections.<String>emptySet());
        List<String> municipalityList = new ArrayList(municipalitiesSet);
        Collections.sort(municipalityList);

        for (String municipalityName : municipalityList) {
            Municipality municipality = municipalityService.getMunicipality(municipalityName);
            if (municipality!=null) {
                municipalities.add(municipality);
            }
        }

        return municipalities;
    }

    @Override
    public void saveWhatsNewDialogShownForCurrentVersion() {
        Logger.debug("Saving that what's new dialog is shown for version: " + MainApplication.getVersionName());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsService.PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION,
                MainApplication.getVersionName());
        editor.apply();
    }

    @Override
    public void saveHardwareAccelerationDialogShown() {
        Logger.debug("Saving hardware acceleration dialog is shown");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SettingsService.PREF_HW_ACCEL_DIALOG_SHOWN, true);
        editor.apply();
    }

    @Override
    public int getSavedFrameDelay() {
        return Integer.parseInt(sharedPreferences.getString(SettingsService.PREF_ANIM_FRAME_DELAY, "1000"));
    }

    @Override
    public boolean isStartAnimationAutomatically() {
        return sharedPreferences.getBoolean(SettingsService.PREF_ANIM_AUTOSTART, true);
    }

    @Override
    public boolean isShowWhatsNewDialog() {

        String dialogShownForVersion = sharedPreferences.
                getString(SettingsService.PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION, "");

        Logger.debug("What's new dialog is shown for version: " + dialogShownForVersion);

        if (!dialogShownForVersion.equals(MainApplication.getVersionName())) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isShowHardwareAccelerationDialog() {
        return !sharedPreferences.getBoolean(
                SettingsService.PREF_HW_ACCEL_DIALOG_SHOWN, false);
    }

    public boolean showUserLocation() {
        return sharedPreferences.getBoolean(SettingsService.PREF_LOCATION_SHOW_USER_LOCATION, true);
    }

    public String getTestbedPageURL() {

        String mapType = sharedPreferences.getString(PREF_MAP_TYPE, "radar");
        String mapTimeStep = sharedPreferences.getString(PREF_MAP_TIME_STEP, "5");
        String mapNumberOfImages = sharedPreferences.getString(PREF_MAP_NUMBER_OF_IMAGES, "10");
        return context.getString(R.string.testbed_base_url, mapType, mapTimeStep, mapNumberOfImages);

    }

    @Override
    public String getMapMarkerColor() {
        int color = sharedPreferences.getInt(PREF_LOCATION_MAP_MARKER_COLOR,
                convertToColorInt(context.getString(R.string.preference_map_marker_color_default)));
        return convertToARGB(color);
    }

    @Override
    public String getMapPointColor() {
        int color = sharedPreferences.getInt(PREF_LOCATION_MAP_POINT_COLOR,
                convertToColorInt(context.getString(R.string.preference_map_point_color_default)));
        return convertToARGB(color);
    }

    private int convertToColorInt(String argb) throws NumberFormatException {

        if (argb.startsWith("#")) {
            argb = argb.replace("#", "");
        }

        int alpha = 0, red = 0, green = 0, blue = 0;

        if (argb.length() == 8) {
            alpha = Integer.parseInt(argb.substring(0, 2), 16);
            red = Integer.parseInt(argb.substring(2, 4), 16);
            green = Integer.parseInt(argb.substring(4, 6), 16);
            blue = Integer.parseInt(argb.substring(6, 8), 16);
        }
        else if (argb.length() == 6) {
            alpha = 255;
            red = Integer.parseInt(argb.substring(0, 2), 16);
            green = Integer.parseInt(argb.substring(2, 4), 16);
            blue = Integer.parseInt(argb.substring(4, 6), 16);
        }

        return Color.argb(alpha, red, green, blue);
    }

    private String convertToARGB(int color) {
        String alpha = Integer.toHexString(Color.alpha(color));
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));

        if (alpha.length() == 1) {
            alpha = "0" + alpha;
        }

        if (red.length() == 1) {
            red = "0" + red;
        }

        if (green.length() == 1) {
            green = "0" + green;
        }

        if (blue.length() == 1) {
            blue = "0" + blue;
        }

        return "#" + alpha + red + green + blue;
    }


    private static String getMapBoundsPreferenceKey(int orientation) {
        return PREF_BOUNDS_PREFERENCE_KEY_PREFIX + orientation;
    }

    private static String getMapScalePreferenceKey(int orientation) {
        return PREF_SCALE_PREFERENCE_KEY_PREFIX + orientation;
    }

    private static String getMapScalePivotXPreferenceKey(int orientation) {
        return PREF_SCALE_PIVOT_X_PREFERENCE_KEY_PREFIX + orientation;
    }

    private static String getMapScalePivotYPreferenceKey(int orientation) {
        return PREF_SCALE_PIVOT_Y_PREFERENCE_KEY_PREFIX + orientation;
    }

    @Override
    public int getMapMarkerSizePx() {
        int dp = Integer.valueOf(sharedPreferences.getString(PREF_LOCATION_MAP_MARKER_SIZE_DP, "25"));
        return dpToPixels(dp);
    }

    @Override
    public int getMapPointSizePx() {
        int dp = Integer.valueOf(sharedPreferences.getString(PREF_LOCATION_MAP_POINT_SIZE_DP, "10"));
        return dpToPixels(dp);
    }

    @Override
    public String getLocationProvider() {
        return sharedPreferences.getString(PREF_LOCATION_PROVIDER, LocationManager.NETWORK_PROVIDER);
    }

    public void setLocationProvider(String provider) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsService.PREF_LOCATION_PROVIDER, provider);
        editor.apply();
    }

    public MapLocationGPS getSavedFixedLocation() {

        double lat = Double.parseDouble(sharedPreferences.getString(PREF_LOCATION_FIXED_LAT, "-1"));
        double lon = Double.parseDouble(sharedPreferences.getString(PREF_LOCATION_FIXED_LON, "-1"));

        if (lat<0 || lon<0) {
            return null;
        }

        return new MapLocationGPS(lat, lon);

    }

    /**
     * Saves the last known location as fixed location to the preferences.
     * If the location is not available, does not save it to the preferences.
     *
     * @return Returns the last known location or null if no location was available
     */
    public MapLocationGPS saveCurrentLocationAsFixedLocation() {

        MapLocationGPS lastKnownLocation = locationService.getUserLastLocation();

        if (lastKnownLocation!=null) {
            Logger.debug("Saving last known location to preferences: " + lastKnownLocation);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SettingsService.PREF_LOCATION_FIXED_LAT,""+lastKnownLocation.getLatitude());
            editor.putString(SettingsService.PREF_LOCATION_FIXED_LON,""+lastKnownLocation.getLongitude());
            editor.apply();
        }

        return lastKnownLocation;

    }

    public boolean showAds() {
        return sharedPreferences.getBoolean(SettingsService.PREF_SHOW_ADS, true);
    }

    private int dpToPixels(int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
