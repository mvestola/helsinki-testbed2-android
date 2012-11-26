package fi.testbed2;

import com.xtremelabs.robolectric.Robolectric;
import fi.testbed2.app.MainApplication;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * Base class for all test classes.
 */
public abstract class AbstractTestCase  {

    protected static final String TEST_DATA_PATH = "src/test/resources/";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        MainApplication.setContext(Robolectric.application);
    }

    protected void initClassForMocks(Object obj) {
        ((MainApplication) Robolectric.application).getInjector().injectMembers(obj);
    }

    protected <T> T getInjectedMock(Class<T> clazz) {
        return ((MainApplication) Robolectric.application).getInjector().getInstance(clazz);
    }

}
