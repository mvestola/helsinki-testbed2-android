package fi.testbed2.service;

import android.graphics.Rect;
import android.location.Location;
import fi.testbed2.data.Municipality;

import java.util.List;

public interface PreferenceService {

    public static final String PREF_ANIM_FRAME_DELAY = "PREF_ANIM_FRAME_DELAY";
    public static final String PREF_ANIM_AUTOSTART = "PREF_ANIM_AUTOSTART";

    public static final String PREF_MAP_TYPE = "PREF_MAP_TYPE";
    public static final String PREF_MAP_TIME_STEP = "PREF_MAP_TIME_STEP";
    public static final String PREF_MAP_NUMBER_OF_IMAGES = "PREF_MAP_NUMBER_OF_IMAGES";

    public static final String PREF_BOUNDS_PREFERENCE_KEY_PREFIX = "PREFERENCE_ANIM_BOUNDS_ORIENTATION_";
    public static final String PREF_SCALE_PREFERENCE_KEY_PREFIX = "PREFERENCE_ANIM_SCALE_ORIENTATION_";

    public static final String PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION = "PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION";

    public static final String PREF_LOCATION_SHOW_USER_LOCATION = "PREF_LOCATION_SHOW_USER_LOCATION";
    public static final String PREF_LOCATION_PROVIDER = "PREF_LOCATION_PROVIDER";
    public static final String PREF_LOCATION_FIXED_LAT = "PREF_LOCATION_FIXED_LAT";
    public static final String PREF_LOCATION_FIXED_LON = "PREF_LOCATION_FIXED_LON";

    public static final String PREF_LOCATION_MAP_MARKER_SIZE = "PREF_LOCATION_MAP_MARKER_SIZE";
    public static final String PREF_LOCATION_MAP_MARKER_COLOR = "PREF_LOCATION_MAP_MARKER_COLOR";

    public static final String PREF_LOCATION_MAP_POINT_COLOR = "PREF_LOCATION_MAP_POINT_COLOR";
    public static final String PREF_LOCATION_MAP_POINT_SIZE = "PREF_LOCATION_MAP_POINT_SIZE";

    public static final String PREF_LOCATION_SHOW_MUNICIPALITIES = "PREF_LOCATION_MUNICIPALITIES";
    public static final String PREF_LOCATION_SHOW_MUNICIPALITIES_SPLIT = "===";

    /**
     * Saves the bounds of the map user has previously viewed to persistent storage.
     */
    public void saveMapBoundsAndScaleFactor(Rect bounds, float scale, int orientation);

    public Rect getSavedMapBounds(int orientation);

    public float getSavedScaleFactor(int orientation);

    public List<Municipality> getSavedMunicipalities();

    public int getSavedFrameDelay();

    public boolean isStartAnimationAutomatically();

    public boolean isShowWhatsNewDialog();

    public void saveWhatsNewDialogShownForCurrentVersion();

    public boolean showUserLocation();

    public String getTestbedPageURL();

    public String getMapMarkerColor();
    public String getMapPointColor();

    public int getMapMarkerSize();
    public int getMapPointSize();

    public String getLocationProvider();
    public void setLocationProvider(String provider);

    public Location getSavedFixedLocation();
    public Location saveCurrentLocationAsFixedLocation();

}
