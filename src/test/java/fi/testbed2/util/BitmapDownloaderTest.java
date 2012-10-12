package fi.testbed2.util;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import fi.testbed2.AbstractTestCase;
import fi.testbed2.exception.DownloadTaskException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class BitmapDownloaderTest extends AbstractTestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testDownloadTestbedBitmap() throws Exception {

        File mapFileExists = new File(TEST_DATA_PATH + "map.png");
        assertNotNull(BitmapDownloader.downloadTestbedBitmap(mapFileExists.toURL().toString()));

        thrown.expect(DownloadTaskException.class);
        thrown.expectMessage("Could not download map image. Please try again later.");
        assertNull(BitmapDownloader.downloadTestbedBitmap(mapFileExists.toURL().toString() + ".invalid"));

    }

}