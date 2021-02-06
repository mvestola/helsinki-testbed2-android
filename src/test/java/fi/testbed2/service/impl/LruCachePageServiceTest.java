package fi.testbed2.service.impl;

import fi.testbed2.AbstractTestCase;
import fi.testbed2.InjectedTestRunner;
import fi.testbed2.android.task.Task;
import fi.testbed2.android.task.exception.DownloadTaskException;
import fi.testbed2.domain.TestbedParsedPage;
import fi.testbed2.service.HttpUrlService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(InjectedTestRunner.class)
public class LruCachePageServiceTest extends AbstractTestCase {

    @Mock
    private Task task;

    private static LruCachePageService lruCachePageService;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        lruCachePageService = new LruCachePageService();
        initClassForMocks(lruCachePageService);
        lruCachePageService.setCacheSizeInBytes(1024 * 1024 * 100);

        when(task.isCancelled()).thenReturn(false);
    }

    @Test
    public void testParseTestbedPageWithRainRadar() throws Exception {

        initHTMLPage("testbed_rain_15.html");
        TestbedParsedPage page = lruCachePageService.downloadAndParseTestbedPage("http://url.is.irrelevant.here", task);

        assertEquals(15, page.getAllTestbedImages().size());

        assertEquals("12:40", page.getAllTestbedImages().get(0).getLocalTimestamp());
        assertEquals("12:45",page.getAllTestbedImages().get(1).getLocalTimestamp());
        assertEquals("12:50",page.getAllTestbedImages().get(2).getLocalTimestamp());
        assertEquals("12:55",page.getAllTestbedImages().get(3).getLocalTimestamp());
        assertEquals("13:00",page.getAllTestbedImages().get(4).getLocalTimestamp());
        assertEquals("13:05",page.getAllTestbedImages().get(5).getLocalTimestamp());
        assertEquals("13:10",page.getAllTestbedImages().get(6).getLocalTimestamp());
        assertEquals("13:15",page.getAllTestbedImages().get(7).getLocalTimestamp());
        assertEquals("13:20",page.getAllTestbedImages().get(8).getLocalTimestamp());
        assertEquals("13:25",page.getAllTestbedImages().get(9).getLocalTimestamp());
        assertEquals("13:30",page.getAllTestbedImages().get(10).getLocalTimestamp());
        assertEquals("13:35",page.getAllTestbedImages().get(11).getLocalTimestamp());
        assertEquals("13:40",page.getAllTestbedImages().get(12).getLocalTimestamp());
        assertEquals("13:45",page.getAllTestbedImages().get(13).getLocalTimestamp());
        assertEquals("13:50",page.getAllTestbedImages().get(14).getLocalTimestamp());

        assertEquals("http://2.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMJYzJ.r/D", page.getAllTestbedImages().get(0).getImageURL());
        assertEquals("http://2.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMJYzl.r/D", page.getAllTestbedImages().get(1).getImageURL());
        assertEquals("http://1.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMJYlJ.r/D", page.getAllTestbedImages().get(2).getImageURL());
        assertEquals("http://1.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMJYll.r/D", page.getAllTestbedImages().get(3).getImageURL());
        assertEquals("http://3.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMNJJJ.r/D", page.getAllTestbedImages().get(4).getImageURL());
        assertEquals("http://3.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMNJJl.r/D", page.getAllTestbedImages().get(5).getImageURL());
        assertEquals("http://4.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMNJNJ.r/D", page.getAllTestbedImages().get(6).getImageURL());
        assertEquals("http://4.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMNJNl.r/D", page.getAllTestbedImages().get(7).getImageURL());
        assertEquals("http://2.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMNJvJ.r/D", page.getAllTestbedImages().get(8).getImageURL());
        assertEquals("http://2.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMNJvl.r/D", page.getAllTestbedImages().get(9).getImageURL());
        assertEquals("http://1.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMNJMJ.r/D", page.getAllTestbedImages().get(10).getImageURL());
        assertEquals("http://1.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMNJMl.r/D", page.getAllTestbedImages().get(11).getImageURL());
        assertEquals("http://1.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMNJzJ.r/D", page.getAllTestbedImages().get(12).getImageURL());
        assertEquals("http://1.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMNJzl.r/D", page.getAllTestbedImages().get(13).getImageURL());
        assertEquals("http://2.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMNJlJ.r/D", page.getAllTestbedImages().get(14).getImageURL());

    }

    @Test
    public void testParseTestbedPageWithWindMapOnlyOneImage() throws Exception {

        initHTMLPage("testbed_wind_1.html");
        TestbedParsedPage page = lruCachePageService.downloadAndParseTestbedPage("http://url.is.irrelevant.here", task);

        assertEquals(1, page.getAllTestbedImages().size());

        assertEquals("14:30",page.getAllTestbedImages().get(0).getLocalTimestamp());
        assertEquals("http://3.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd9n/WdbHhvJNvJYvMNNMJ.r/D",page.getAllTestbedImages().get(0).getImageURL());

    }

    @Test
    public void testParseTestbedPageWithAirPressureMapOnlyThreeHoursTimeStep() throws Exception {

        initHTMLPage("testbed_pressure_5.html");
        TestbedParsedPage page = lruCachePageService.downloadAndParseTestbedPage("http://url.is.irrelevant.here", task);

        assertEquals(5, page.getAllTestbedImages().size());

        assertEquals("00:00",page.getAllTestbedImages().get(0).getLocalTimestamp());
        assertEquals("03:00",page.getAllTestbedImages().get(1).getLocalTimestamp());
        assertEquals("06:00",page.getAllTestbedImages().get(2).getLocalTimestamp());
        assertEquals("09:00",page.getAllTestbedImages().get(3).getLocalTimestamp());
        assertEquals("12:00",page.getAllTestbedImages().get(4).getLocalTimestamp());

        assertEquals("http://1.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqdr1UXX71UdbHhvJNvJYvvvNJJ.r/D",page.getAllTestbedImages().get(0).getImageURL());
        assertEquals("http://3.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqdr1UXX71UdbHhvJNvJYvMJJJJ.r/D",page.getAllTestbedImages().get(1).getImageURL());
        assertEquals("http://4.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqdr1UXX71UdbHhvJNvJYvMJMJJ.r/D",page.getAllTestbedImages().get(2).getImageURL());
        assertEquals("http://1.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqdr1UXX71UdbHhvJNvJYvMJ_JJ.r/D",page.getAllTestbedImages().get(3).getImageURL());
        assertEquals("http://2.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqdr1UXX71UdbHhvJNvJYvMJYJJ.r/D",page.getAllTestbedImages().get(4).getImageURL());

    }

    @Test(expected = DownloadTaskException.class)
    public void testParseTestbedPageWithErrors() throws Exception {

        initHTMLPage("testbed_invalid.html");
        TestbedParsedPage page = lruCachePageService.downloadAndParseTestbedPage("http://url.is.irrelevant.here", task);

    }

    @Test(expected = DownloadTaskException.class)
    public void testParseTestbedPageWithOneTimestampMissing() throws Exception {

        initHTMLPage("testbed_timestamps_length_error.html");
        TestbedParsedPage page = lruCachePageService.downloadAndParseTestbedPage("http://url.is.irrelevant.here", task);

    }

    @Test(expected = DownloadTaskException.class)
    public void testParseTestbedPageWithAddedNewlines() throws Exception {

        initHTMLPage("testbed_changed_newlines.html");
        TestbedParsedPage page = lruCachePageService.downloadAndParseTestbedPage("http://url.is.irrelevant.here", task);
        assertEquals(5, page.getAllTestbedImages().size());

        // These test are not currently executed since there will be exception

        assertEquals("06:00",page.getAllTestbedImages().get(0).getLocalTimestamp());
        assertEquals("07:00",page.getAllTestbedImages().get(1).getLocalTimestamp());
        // There was a time gap in the servers while saving test data
        assertEquals("12:00",page.getAllTestbedImages().get(2).getLocalTimestamp());
        assertEquals("13:00",page.getAllTestbedImages().get(3).getLocalTimestamp());
        assertEquals("14:00",page.getAllTestbedImages().get(4).getLocalTimestamp());

        assertEquals("http://2.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMJMJJ.r/D",page.getAllTestbedImages().get(0).getImageURL());
        assertEquals("http://4.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMJzJJ.r/D",page.getAllTestbedImages().get(1).getImageURL());
        assertEquals("http://4.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMJYJJ.r/D",page.getAllTestbedImages().get(2).getImageURL());
        assertEquals("http://3.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMNJJJ.r/D",page.getAllTestbedImages().get(3).getImageURL());
        assertEquals("http://3.img.fmi.fi/php/img.php?A=dA4ndr1aWd17/dRUXRLUWda7Rd9ULdq1Uqd1qWq1dRU4rU1qR71UdbHhvJNvJYvMNNJJ.r/D",page.getAllTestbedImages().get(4).getImageURL());


    }

    @Test
    public void testParseTestbedPageTaskAborted() throws Exception {

        initHTMLPage("testbed_rain_15.html");
        when(task.isCancelled()).thenReturn(true);

        TestbedParsedPage page = lruCachePageService.downloadAndParseTestbedPage("http://url.is.irrelevant.here", task);
        assertNull(page);

    }

    private void initHTMLPage(String pageName) {

        try {

            InputStream in = new FileInputStream(TEST_DATA_PATH+pageName);
            when(getInjectedMock(HttpUrlService.class).
                    getInputStreamForHttpUrl(any(String.class))).thenReturn(in);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
