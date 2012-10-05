package fi.testbed2.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.threefiftynice.android.preference.ListPreferenceMultiSelect;
import fi.testbed2.R;
import fi.testbed2.app.MainApplication;
import fi.testbed2.data.Municipality;

public class MyPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        String[] entries = Municipality.getFinlandMunicipalityNamesShownInTestbedMap();
        String[] entryValues = entries;
        ListPreferenceMultiSelect lp =
                (ListPreferenceMultiSelect)getPreferenceManager().
                        findPreference(MainApplication.PREF_LOCATION_SHOW_MUNICIPALITIES);
        lp.setEntries(entries);
        lp.setEntryValues(entryValues);

    }

}
