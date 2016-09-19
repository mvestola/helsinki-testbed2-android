package fi.testbed2.android.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;

import fi.testbed2.AbstractTestCase;
import fi.testbed2.BuildConfig;
import fi.testbed2.InjectedTestRunner;
import fi.testbed2.R;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(InjectedTestRunner.class)
@Config(constants = BuildConfig.class, sdk = AbstractTestCase.ROBOLECTRIC_API_LEVEL)
public class MainActivityTest extends AbstractTestCase {

    private MainActivity_ activity;

    private View rainAndTemperatureButton;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.setupActivity(MainActivity_.class);
        rainAndTemperatureButton = activity.findViewById(R.id.button_rain_temperature);
    }

    @Test
    public void shouldReturnCorrectName() throws Exception {
        String appName = activity.getResources().getString(R.string.app_name);
        assertThat(appName, equalTo("Helsinki Testbed Viewer 2.0"));
    }

    @Test
    public void shouldOpenParsingActivityWhenLargeRefreshButtonSelected() {

        rainAndTemperatureButton.performClick();

        Intent expectedIntent = new Intent(activity, ParsingActivity_.class);
        ShadowActivity shadowActivity = shadowOf(activity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));

    }

    @Test
    public void shouldOpenParsingActivityWhenRefreshButtonFromMainMenuSelected() {

        MenuItem item = new RoboMenuItem(R.id.main_menu_refresh);
        activity.onOptionsItemSelected(item);

        Intent expectedIntent = new Intent(activity, ParsingActivity_.class);
        ShadowActivity shadowActivity = shadowOf(activity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));

    }

    @Test
    @Ignore("Robolectric does not work with appcompact AlertDialogs yet")
    public void shouldOpenAboutBoxWhenAboutButtonFromMainMenuSelected() {

        MenuItem item = new RoboMenuItem(R.id.main_menu_about);
        activity.onOptionsItemSelected(item);

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertNull(startedIntent);

        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        ShadowAlertDialog shadowDialog = shadowOf(dialog);
        assertEquals("Helsinki Testbed Viewer 2.0.13", shadowDialog.getTitle());

    }

    @Test
    public void shouldOpenPreferencesWhenPreferenceButtonFromMainMenuSelected() {

        MenuItem item = new RoboMenuItem(R.id.main_menu_preferences);
        activity.onOptionsItemSelected(item);

        Intent expectedIntent = new Intent(activity, TestbedPreferenceActivity.class);
        ShadowActivity shadowActivity = shadowOf(activity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));

    }

}
