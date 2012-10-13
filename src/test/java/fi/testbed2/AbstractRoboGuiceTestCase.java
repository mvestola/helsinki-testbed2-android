package fi.testbed2;

import android.content.Context;
import com.google.inject.AbstractModule;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import fi.testbed2.service.CoordinateService;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;
import roboguice.test.RobolectricRoboTestRunner;

import static org.mockito.Mockito.mock;

/**
 * Base class for all test classes needing the RoboGuice Ioc framework.
 */
@RunWith(RobolectricRoboTestRunner.class)
public abstract class AbstractRoboGuiceTestCase extends AbstractTestCase {

    protected CoordinateService coordinateServiceMock = mock(CoordinateService.class);

    protected static Context context = new RoboActivity();

    @Before
    public void setUp() throws Exception {

        super.setUp();

        RoboGuice.setBaseApplicationInjector(Robolectric.application,
                RoboGuice.DEFAULT_STAGE,
                Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application)).with(new TestModule()));
    }

    @After
    public void tearDown() {
        // Don't forget to tear down our custom injector to avoid polluting other test classes
        RoboGuice.util.reset();
    }

    private class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(CoordinateService.class).toInstance(coordinateServiceMock);
        }
    }

}
