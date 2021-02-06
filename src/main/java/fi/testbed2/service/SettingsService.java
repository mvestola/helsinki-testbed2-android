package fi.testbed2.service;

import android.graphics.Rect;
import fi.testbed2.android.ui.view.MapScaleInfo;
import fi.testbed2.domain.MapLocationGPS;
import fi.testbed2.domain.Municipality;

import java.util.List;

public interface SettingsService {

    String PREF_ANIM_FRAME_DELAY = "PREF_ANIM_FRAME_DELAY_INT";
    String PREF_ANIM_AUTOSTART = "PREF_ANIM_AUTOSTART";

    String PREF_MAP_TYPE = "PREF_MAP_TYPE";
    String PREF_MAP_TIME_STEP = "PREF_MAP_TIME_STEP";
    String PREF_MAP_NUMBER_OF_IMAGES = "PREF_MAP_NUMBER_OF_IMAGES";

    String PREF_BOUNDS_PREFERENCE_KEY_PREFIX = "PREFERENCE_ANIM_BOUNDS_ORIENTATION_";
    String PREF_SCALE_PREFERENCE_KEY_PREFIX = "PREFERENCE_ANIM_SCALE_ORIENTATION_";
    String PREF_SCALE_PIVOT_X_PREFERENCE_KEY_PREFIX = "PREFERENCE_ANIM_SCALE_PIVOT_X_ORIENTATION_";
    String PREF_SCALE_PIVOT_Y_PREFERENCE_KEY_PREFIX = "PREFERENCE_ANIM_SCALE_PIVOT_Y_ORIENTATION_";

    String PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION = "PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION";
    String PREF_HW_ACCEL_DIALOG_SHOWN = "PREF_HW_ACCEL_DIALOG_SHOWN";

    String PREF_LOCATION_SHOW_USER_LOCATION = "PREF_LOCATION_SHOW_USER_LOCATION";
    String PREF_LOCATION_PROVIDER = "PREF_LOCATION_PROVIDER";
    String PREF_LOCATION_FIXED_LAT = "PREF_LOCATION_FIXED_LAT";
    String PREF_LOCATION_FIXED_LON = "PREF_LOCATION_FIXED_LON";

    String PREF_LOCATION_MAP_MARKER_SIZE_DP = "PREF_LOCATION_MAP_MARKER_SIZE_DP_INT";
    String PREF_LOCATION_MAP_MARKER_COLOR = "PREF_LOCATION_MAP_MARKER_COLOR_HEX";

    String PREF_LOCATION_MAP_POINT_COLOR = "PREF_LOCATION_MAP_POINT_COLOR_HEX";
    String PREF_LOCATION_MAP_POINT_SIZE_DP = "PREF_LOCATION_MAP_POINT_SIZE_DP_INT";

    String PREF_LOCATION_SHOW_MUNICIPALITIES_LIST = "PREF_LOCATION_MUNICIPALITIES_LIST";

    String PREF_SHOW_ADS = "PREF_SHOW_ADS";


    void setMapType(String mapType);
    String getMapType();

    /**
     * Saves the bounds of the map user has previously viewed to persistent storage.
     */
    void saveMapBoundsAndScaleFactor(Rect bounds, MapScaleInfo scaleInfo, int orientation);

    Rect getSavedMapBounds(int orientation);

    MapScaleInfo getSavedScaleInfo(int orientation);

    List<Municipality> getSavedMunicipalities();

    int getSavedFrameDelay();

    boolean isStartAnimationAutomatically();

    boolean isShowWhatsNewDialog();
    boolean isShowHardwareAccelerationDialog();

    void saveWhatsNewDialogShownForCurrentVersion();
    void saveHardwareAccelerationDialogShown();

    boolean showUserLocation();

    String getTestbedPageURL();

    String getMapMarkerColor();
    String getMapPointColor();

    int getMapMarkerSizePx();
    int getMapPointSizePx();

    String getLocationProvider();
    void setLocationProvider(String provider);

    MapLocationGPS getSavedFixedLocation();
    MapLocationGPS saveCurrentLocationAsFixedLocation();

    boolean showAds();

}
