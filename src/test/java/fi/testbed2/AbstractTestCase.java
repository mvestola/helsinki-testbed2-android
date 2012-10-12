package fi.testbed2;

import com.xtremelabs.robolectric.Robolectric;
import fi.testbed2.app.MainApplication;
import fi.testbed2.task.Task;
import org.junit.Before;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Base class for all test classes.
 */
public class AbstractTestCase  {

    protected static final String TEST_DATA_PATH = "src/test/data/";

    @Before
    public void setUp() throws Exception {
        MainApplication.setContext(Robolectric.application);
    }

}
