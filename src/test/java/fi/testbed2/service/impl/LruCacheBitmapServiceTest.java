package fi.testbed2.service.impl;

import fi.testbed2.AbstractTestCase;
import fi.testbed2.BuildConfig;
import fi.testbed2.InjectedTestRunner;
import fi.testbed2.android.task.exception.DownloadTaskException;
import fi.testbed2.domain.TestbedMapImage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(InjectedTestRunner.class)
@Config(constants = BuildConfig.class, sdk = AbstractTestCase.ROBOLECTRIC_API_LEVEL)
public class LruCacheBitmapServiceTest extends AbstractTestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private LruCacheBitmapService lruCacheBitmapService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        lruCacheBitmapService = new LruCacheBitmapService();
        initClassForMocks(lruCacheBitmapService);
        lruCacheBitmapService.setCacheSizeInBytes(1024 * 1024 * 100);
    }

    @Test
    public void testDownloadBitmap() throws Exception {

        File mapFileExists = new File(TEST_DATA_PATH + "map.png");
        TestbedMapImage image = new TestbedMapImage(mapFileExists.toURI().toURL().toString(),
                "201210141130");

        assertNotNull(lruCacheBitmapService.downloadBitmap(image));

        thrown.expect(DownloadTaskException.class);
        thrown.expectMessage("Could not download map image. Please try again later.");
        TestbedMapImage imageNotExists = new TestbedMapImage(mapFileExists.toURI().toURL().toString()+".invalid",
                "201210141130");

        assertNull(lruCacheBitmapService.downloadBitmap(imageNotExists));

    }

}