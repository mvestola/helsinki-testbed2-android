package fi.testbed2;

import android.app.ActivityManager;
import com.xtremelabs.robolectric.Robolectric;
import fi.testbed2.app.MainApplication;
import fi.testbed2.dialog.DialogBuilder;
import fi.testbed2.service.*;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Base class for all test classes.
 */
public abstract class AbstractTestCase  {

    protected static final String TEST_DATA_PATH = "src/test/data/";

    @Mock
    protected DialogBuilder mockDialogBuilder;

    @Mock
    protected MunicipalityService mockMunicipalityService;

    @Mock
    protected LocationService mockLocationService;

    @Mock
    protected CoordinateService mockCoordinateService;

    @Mock
    protected PreferenceService mockPreferenceService;

    @Mock
    protected BitmapService mockBitmapService;

    @Mock
    protected PageService mockPageService;

    @Mock
    protected HTTPService mockHttpService;

    @Mock
    protected ActivityManager mockActivityManager;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        MainApplication.setContext(Robolectric.application);
    }

}
