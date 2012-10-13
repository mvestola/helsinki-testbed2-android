package fi.testbed2;

import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import fi.testbed2.app.MainModule;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import roboguice.RoboGuice;
import roboguice.test.RobolectricRoboTestRunner;

/**
 * Base class for all test classes needing the RoboGuice Ioc framework.
 */
@RunWith(RobolectricRoboTestRunner.class)
public abstract class AbstractRoboGuiceTestCase extends AbstractTestCase {

    @Before
    public void setUp() throws Exception {

        super.setUp();

        RoboGuice.setBaseApplicationInjector(Robolectric.application,
                RoboGuice.DEFAULT_STAGE,
                Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).
                        with(new MainModule()));

    }

    @After
    public void tearDown() {
        // Don't forget to tear down our custom injector to avoid polluting other test classes
        RoboGuice.util.reset();
    }

}
