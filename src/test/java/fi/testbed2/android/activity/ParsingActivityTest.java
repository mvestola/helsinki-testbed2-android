package fi.testbed2.android.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.MenuItem;
import fi.testbed2.AbstractTestCase;
import fi.testbed2.BuildConfig;
import fi.testbed2.InjectedTestRunner;
import fi.testbed2.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowIntent;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

@RunWith(InjectedTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ParsingActivityTest extends AbstractTestCase {

    private ParsingActivity_ activity;

    @Before
    public void setUp() throws Exception {
        activity = new ParsingActivity_();
        activity.onCreate(null);
    }

    @Test
    public void shouldOpenAboutBoxWhenAboutButtonFromMainMenuSelected() {

        MenuItem item = new RoboMenuItem(R.id.main_menu_about);
        activity.onOptionsItemSelected(item);

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertNull(startedIntent);

        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        ShadowAlertDialog shadowDialog = shadowOf(dialog);
        assertEquals("Helsinki Testbed Viewer 1.0", shadowDialog.getTitle());

    }

    @Test
    public void shouldOpenPreferencesWhenPreferenceButtonFromMainMenuSelected() {

        MenuItem item = new RoboMenuItem(R.id.main_menu_preferences);
        activity.onOptionsItemSelected(item);

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(),
                equalTo(TestbedPreferenceActivity.class.getName()));

    }


}
