package fi.testbed2.service.impl;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import fi.testbed2.AbstractRoboGuiceTestCase;
import fi.testbed2.data.TestbedMapImage;
import fi.testbed2.exception.DownloadTaskException;
import fi.testbed2.service.BitmapService;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import roboguice.RoboGuice;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class LruCacheBitmapServiceTest extends AbstractRoboGuiceTestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static LruCacheBitmapService bitmapService;

    @BeforeClass
    public static void setUpClass() {
        bitmapService = RoboGuice.getInjector(context).getInstance(LruCacheBitmapService.class);
        bitmapService.setCacheSizeInBytes(1024*1024*100);
    }

    @Test
    public void testDownloadBitmap() throws Exception {

        File mapFileExists = new File(TEST_DATA_PATH + "map.png");
        TestbedMapImage image = new TestbedMapImage(mapFileExists.toURL().toString(),
                "201210141130");

        assertNotNull(bitmapService.downloadBitmap(image));

        thrown.expect(DownloadTaskException.class);
        thrown.expectMessage("Could not download map image. Please try again later.");
        TestbedMapImage imageNotExists = new TestbedMapImage(mapFileExists.toURL().toString()+".invalid",
                "201210141130");

        assertNull(bitmapService.downloadBitmap(imageNotExists));

    }

}