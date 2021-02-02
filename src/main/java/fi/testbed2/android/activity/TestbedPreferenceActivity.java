package fi.testbed2.android.activity;

import android.os.Bundle;

import org.androidannotations.annotations.EActivity;

import androidx.appcompat.app.AppCompatActivity;
import fi.testbed2.R;

@EActivity(R.layout.settings)
public class TestbedPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
    }

}
