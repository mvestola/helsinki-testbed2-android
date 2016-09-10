package fi.testbed2;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import fi.testbed2.android.app.MainApplication;
import roboguice.RoboGuice;

/**
 * Base class for all test classes.
 */
public abstract class AbstractTestCase  {

    protected static final String TEST_DATA_PATH = "src/test/resources/";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        MainApplication.setContext(RuntimeEnvironment.application);
    }

    protected void initClassForMocks(Object obj) {
        RoboGuice.getInjector(MainApplication.getContext()).injectMembers(obj);
    }

    protected <T> T getInjectedMock(Class<T> clazz) {
        return RoboGuice.getInjector(MainApplication.getContext()).getInstance(clazz);
    }

}
