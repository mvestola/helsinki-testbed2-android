package fi.testbed2.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fi.testbed2.R;
import fi.testbed2.app.MainApplication;
import fi.testbed2.data.Municipality;
import fi.testbed2.dialog.DialogType;
import fi.testbed2.service.MunicipalityService;
import fi.testbed2.service.PreferenceService;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class DefaultPreferenceService implements PreferenceService {

    @Inject
    MunicipalityService municipalityService;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    Context context;

    /**
     * Saves the bounds of the map user has previously viewed to persistent storage.
     */
    @Override
    public void saveMapBoundsAndScaleFactor(Rect bounds, float scale, int orientation) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (editor!=null) {
            if (bounds!=null) {
                // bounds String format is left:top:right:bottom
                editor.putString(getMapBoundsPreferenceKey(orientation),
                        "" + bounds.left + ":" + bounds.top + ":" + bounds.right + ":" + bounds.bottom);
            }
            editor.putFloat(getMapScalePreferenceKey(orientation), scale);
            editor.commit();
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
    public float getSavedScaleFactor(int orientation) {
        return sharedPreferences.getFloat(getMapScalePreferenceKey(orientation), 1.0f);
    }

    @Override
    public List<Municipality> getSavedMunicipalities() {

        List<Municipality> municipalities = new ArrayList<Municipality>();

        String municipalitiesString = sharedPreferences.getString(PREF_LOCATION_SHOW_MUNICIPALITIES, "");

        String[] municipalityArray = municipalitiesString.split(
                PreferenceService.PREF_LOCATION_SHOW_MUNICIPALITIES_SPLIT);

        if (municipalityArray.length<1 || municipalityArray[0].length()==0) {
            return municipalities;
        }

        for (String municipalityName : municipalityArray) {
            Municipality municipality = municipalityService.getMunicipality(municipalityName);
            if (municipality!=null) {
                municipalities.add(municipality);
            }
        }

        return municipalities;

    }

    @Override
    public void saveWhatsNewDialogShownForCurrentVersion() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PreferenceService.PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION,
                MainApplication.getVersionName());
        editor.commit();
    }

    @Override
    public int getSavedFrameDelay() {
        return Integer.parseInt(sharedPreferences.getString(PreferenceService.PREF_ANIM_FRAME_DELAY, "1000"));
    }

    @Override
    public boolean isStartAnimationAutomatically() {
        return sharedPreferences.getBoolean(PreferenceService.PREF_ANIM_AUTOSTART, true);
    }

    @Override
    public boolean isShowWhatsNewDialog() {

        String dialogShownForVersion = sharedPreferences.
                getString(PreferenceService.PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION, "");
        if (!dialogShownForVersion.equals(MainApplication.getVersionName())) {
            return true;
        }

        return false;
    }

    public boolean showUserLocation() {
        return sharedPreferences.getBoolean(PreferenceService.PREF_LOCATION_SHOW_USER_LOCATION, true);
    }

    public String getTestbedPageURL() {

        String mapType = sharedPreferences.getString(PREF_MAP_TYPE, "radar");
        String mapTimeStep = sharedPreferences.getString(PREF_MAP_TIME_STEP, "5");
        String mapNumberOfImages = sharedPreferences.getString(PREF_MAP_NUMBER_OF_IMAGES, "10");
        return context.getString(R.string.testbed_base_url, mapType, mapTimeStep, mapNumberOfImages);

    }


    private static String getMapBoundsPreferenceKey(int orientation) {
        return PREF_BOUNDS_PREFERENCE_KEY_PREFIX + orientation;
    }

    private static String getMapScalePreferenceKey(int orientation) {
        return PREF_SCALE_PREFERENCE_KEY_PREFIX + orientation;
    }

}
