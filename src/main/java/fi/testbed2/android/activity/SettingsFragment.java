package fi.testbed2.android.activity;

import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.google.inject.Inject;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceFragmentCompat;
import fi.testbed2.R;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.android.ui.dialog.DialogBuilder;
import fi.testbed2.domain.MapLocationGPS;
import fi.testbed2.service.LocationService;
import fi.testbed2.service.MunicipalityService;
import fi.testbed2.service.SettingsService;
import roboguice.RoboGuice;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    MunicipalityService municipalityService;

    @Inject
    SettingsService settingsService;

    @Inject
    DialogBuilder dialogBuilder;

    private ListPreference locationProviderList;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        RoboGuice.getInjector(MainApplication.getContext()).injectMembersWithoutViews(this);
        setPreferencesFromResource(R.xml.preferences, rootKey);

        initMunicipalityList();
        locationProviderList = (ListPreference) getPreferenceScreen().findPreference(SettingsService.PREF_LOCATION_PROVIDER);
    }

    private void initMunicipalityList() {

        String[] entries = municipalityService.getFinlandMunicipalityNamesShownInTestbedMap();
        MultiSelectListPreference multiSelectListPreference =
                (MultiSelectListPreference) getPreferenceManager().
                        findPreference(SettingsService.PREF_LOCATION_SHOW_MUNICIPALITIES_LIST);
        multiSelectListPreference.setEntries(entries);
        multiSelectListPreference.setEntryValues(entries);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Logger.debug("PreferenceActivity.onSharedPreferenceChanged: " + key);

        if (key.equals(SettingsService.PREF_LOCATION_PROVIDER)) {

            Logger.debug("Updating location provider...");

            String newProvider = sharedPreferences.getString(key, LocationManager.NETWORK_PROVIDER);

            if (newProvider.equals(LocationService.LOCATION_PROVIDER_FIXED)) {

                MapLocationGPS loc = settingsService.saveCurrentLocationAsFixedLocation();
                Logger.debug("Using fixed provider: " + loc);

                /*
                  If no location is available, revert to GPS provider.
                 */
                if (loc == null) {
                    newProvider = LocationManager.GPS_PROVIDER;
                    settingsService.setLocationProvider(newProvider);
                    dialogBuilder.getErrorDialog(
                            this.getString(R.string.preference_location_provider_fixed_error_dialog)).show();
                }

            }

            locationProviderList.setSummary(getStringForCurrentlySelectedLocationProvider(newProvider));

        }

    }

    private String getStringForCurrentlySelectedLocationProvider(String newProvider) {

        String currentValue = "";

        switch (newProvider) {
            case LocationService.LOCATION_PROVIDER_FIXED:
                MapLocationGPS loc = settingsService.getSavedFixedLocation();
                String coordinates = "lat: " + loc.getLatitude() + ", lon: " + loc.getLongitude();
                currentValue = this.getString(R.string.preference_location_provider_fixed, coordinates);
                break;
            case LocationManager.NETWORK_PROVIDER:
                currentValue = this.getString(R.string.preference_location_provider_network);
                break;
            case LocationManager.GPS_PROVIDER:
                currentValue = this.getString(R.string.preference_location_provider_gps);
                break;
        }

        return this.getString(R.string.preference_location_provider_summary, currentValue);

    }

    @Override
    public void onResume() {
        super.onResume();

        String provider = settingsService.getLocationProvider();
        locationProviderList.setSummary(getStringForCurrentlySelectedLocationProvider(provider));

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoboGuice.getInjector(MainApplication.getContext()).injectMembersWithoutViews(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RoboGuice.getInjector(MainApplication.getContext()).injectViewMembers(this);
    }
}
