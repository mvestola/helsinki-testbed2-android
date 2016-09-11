package fi.testbed2.android.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.ImageButton;
import fi.testbed2.AbstractTestCase;
import fi.testbed2.BuildConfig;
import fi.testbed2.InjectedTestRunner;
import fi.testbed2.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowIntent;

import static org.robolectric.Shadows.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(InjectedTestRunner.class)
@Config(constants = BuildConfig.class, sdk = AbstractTestCase.ROBOLECTRIC_API_LEVEL)
public class MainActivityTest extends AbstractTestCase {

    private MainActivity_ activity;

    private ImageButton refreshButton;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.setupActivity(MainActivity_.class);
        refreshButton = (ImageButton) activity.findViewById(R.id.button_refresh);
    }

    @Test
    public void shouldReturnCorrectName() throws Exception {
        String appName = activity.getResources().getString(R.string.app_name);
        assertThat(appName, equalTo("Helsinki Testbed Viewer 2.0"));
    }

    @Test
    public void shouldOpenParsingActivityWhenLargeRefreshButtonSelected() {

        refreshButton.performClick();

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
