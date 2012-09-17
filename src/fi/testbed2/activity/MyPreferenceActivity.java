package fi.testbed2.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import fi.testbed2.R;

public class MyPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

}
