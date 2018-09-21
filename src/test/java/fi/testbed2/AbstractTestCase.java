package fi.testbed2;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.robolectric.RoboSettings;
import org.robolectric.RuntimeEnvironment;

import fi.testbed2.android.app.MainApplication;
import roboguice.RoboGuice;

/**
 * Base class for all test classes.
 */
public abstract class AbstractTestCase  {

    /**
     * API level supported by Robolectric
     */
    public static final int ROBOLECTRIC_API_LEVEL = 27;

    protected static final String TEST_DATA_PATH = "src/test/resources/";

    static {
        // The default MavenRepositoryId and MavenRepositoryUrl point at Sonatype
        // which is missing crucial components for Robolectric to work properly.
        RoboSettings.setMavenRepositoryId("mavenCentral");
        RoboSettings.setMavenRepositoryUrl("https://repo1.maven.org/maven2");
    }

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
