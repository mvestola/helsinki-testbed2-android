package fi.testbed2;

import com.xtremelabs.robolectric.Robolectric;
import fi.testbed2.app.MainApplication;
import org.junit.Before;

/**
 * Base class for all test classes.
 */
public abstract class AbstractTestCase  {

    protected static final String TEST_DATA_PATH = "src/test/data/";

    @Before
    public void setUp() throws Exception {
        MainApplication.setContext(Robolectric.application);
    }

}
