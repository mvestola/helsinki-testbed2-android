package fi.testbed2.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.MenuItem;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowAlertDialog;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import com.xtremelabs.robolectric.tester.android.view.TestMenuItem;
import fi.testbed2.AbstractRoboGuiceTestCase;
import fi.testbed2.R;
import org.junit.Before;
import org.junit.Test;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class ParsingActivityTest extends AbstractRoboGuiceTestCase {

    private ParsingActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = new ParsingActivity();
        activity.onCreate(null);
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
