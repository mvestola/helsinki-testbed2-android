package fi.testbed2.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.ImageButton;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowAlertDialog;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import com.xtremelabs.robolectric.tester.android.view.TestMenuItem;
import fi.testbed2.AbstractTestCase;
import fi.testbed2.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest extends AbstractTestCase {

    private MainActivity activity;
    private ImageButton refreshButton;

    @Before
    public void setUp() throws Exception {
        activity = new MainActivity();
        activity.onCreate(null);
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

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(ParsingActivity.class.getName()));

    }

    @Test
    public void shouldOpenParsingActivityWhenRefreshButtonFromMainMenuSelected() {

        MenuItem item = new TestMenuItem(R.id.main_menu_refresh);
        activity.onOptionsItemSelected(item);

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(ParsingActivity.class.getName()));

    }

    @Test
    public void shouldOpenAboutBoxWhenAboutButtonFromMainMenuSelected() {

        MenuItem item = new TestMenuItem(R.id.main_menu_about);
        activity.onOptionsItemSelected(item);

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertNull(startedIntent);

        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        ShadowAlertDialog shadowDialog = Robolectric.shadowOf(dialog);
        assertEquals("Helsinki Testbed Viewer Unknown", shadowDialog.getTitle());

    }

    @Test
    public void shouldOpenPreferencesWhenPreferenceButtonFromMainMenuSelected() {

        MenuItem item = new TestMenuItem(R.id.main_menu_preferences);
        activity.onOptionsItemSelected(item);

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(),
                equalTo(TestbedPreferenceActivity.class.getName()));

    }

}
