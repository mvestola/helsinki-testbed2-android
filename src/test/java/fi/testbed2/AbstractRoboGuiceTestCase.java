package fi.testbed2;

import android.content.Context;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;
import roboguice.test.RobolectricRoboTestRunner;

/**
 * Base class for all test classes needing the RoboGuice Ioc framework.
 */
@RunWith(RobolectricRoboTestRunner.class)
public abstract class AbstractRoboGuiceTestCase extends AbstractTestCase {

    protected static Context context = new RoboActivity();
    protected TestModule testModule;

    @Before
    public void setUp() throws Exception {

        super.setUp();

        testModule = new TestModule();
        RoboGuice.setBaseApplicationInjector(Robolectric.application,
                RoboGuice.DEFAULT_STAGE,
                Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with(testModule));
    }

    @After
    public void tearDown() {
        // Don't forget to tear down our custom injector to avoid polluting other test classes
        RoboGuice.util.reset();
    }


}
