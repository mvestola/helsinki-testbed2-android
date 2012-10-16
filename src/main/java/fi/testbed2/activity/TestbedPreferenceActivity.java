package fi.testbed2.activity;

import android.os.Bundle;
import com.google.inject.Inject;
import com.threefiftynice.android.preference.ListPreferenceMultiSelect;
import fi.testbed2.R;
import fi.testbed2.service.MunicipalityService;
import fi.testbed2.service.PreferenceService;
import roboguice.activity.RoboPreferenceActivity;

public class TestbedPreferenceActivity extends RoboPreferenceActivity {

    @Inject
    MunicipalityService municipalityService;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        initMunicipalityList();

    }

    private void initMunicipalityList() {

        String[] entries = municipalityService.getFinlandMunicipalityNamesShownInTestbedMap();
        String[] entryValues = entries;
        ListPreferenceMultiSelect lp =
                (ListPreferenceMultiSelect)getPreferenceManager().
                        findPreference(PreferenceService.PREF_LOCATION_SHOW_MUNICIPALITIES);
        lp.setEntries(entries);
        lp.setEntryValues(entryValues);

    }

}
