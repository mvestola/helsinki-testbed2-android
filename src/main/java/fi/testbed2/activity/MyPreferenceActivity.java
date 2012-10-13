package fi.testbed2.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.google.inject.Inject;
import com.threefiftynice.android.preference.ListPreferenceMultiSelect;
import fi.testbed2.R;
import fi.testbed2.app.Preference;
import fi.testbed2.service.MunicipalityService;

public class MyPreferenceActivity extends PreferenceActivity {

    @Inject
    MunicipalityService municipalityService;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        String[] entries = municipalityService.getFinlandMunicipalityNamesShownInTestbedMap();
        String[] entryValues = entries;
        ListPreferenceMultiSelect lp =
                (ListPreferenceMultiSelect)getPreferenceManager().
                        findPreference(Preference.PREF_LOCATION_SHOW_MUNICIPALITIES);
        lp.setEntries(entries);
        lp.setEntryValues(entryValues);

    }

}
