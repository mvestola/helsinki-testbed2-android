package fi.testbed2.robotium;

import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;
import fi.testbed2.R;
import fi.testbed2.activity.MainActivity;
import junit.framework.Assert;

/**
 * Robotium test for the main activity.
 */
public class MainActivityRobotiumTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public MainActivityRobotiumTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testPreferenceIsSaved() throws Exception {

        solo.sendKey(Solo.MENU);
        solo.clickOnText(getLocalizedString(R.string.main_menu_preferences));
        solo.clickOnText(getLocalizedString(R.string.preference_map_type_title));
        solo.clickOnText("Kosteus");
        solo.goBack();

        Assert.assertTrue(solo.searchText("Helsinki Testbed Viewer"));

    }

    private String getLocalizedString(int id) {
        return getActivity().getResources().getString(id);
    }

}
