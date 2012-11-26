package fi.testbed2.service.impl;

import com.xtremelabs.robolectric.Robolectric;
import fi.testbed2.AbstractTestCase;
import fi.testbed2.InjectedTestRunner;
import fi.testbed2.exception.DownloadTaskException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;

@RunWith(InjectedTestRunner.class)
public class ApacheHttpUrlServiceTest extends AbstractTestCase {

    private ApacheHttpUrlService httpUrlService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        httpUrlService = new ApacheHttpUrlService();
        initClassForMocks(httpUrlService);
    }

    @Test
    public void testResponse200() throws Exception {

        Robolectric.addPendingHttpResponse(200, "OK");
        InputStream stream = httpUrlService.getInputStreamForHttpUrl("fi.testbed2/test");
        assertNotNull(stream);

    }

    @Test
    public void testResponse404() throws Exception {

        Robolectric.addPendingHttpResponse(404, "Not Found");

        thrown.expect(DownloadTaskException.class);
        thrown.expectMessage("Could not connect to the server (HTTP 404). Please try again later.");
        httpUrlService.getInputStreamForHttpUrl("fi.testbed2/test");

    }

    @Test
    public void testResponse500() throws Exception {

        Robolectric.addPendingHttpResponse(500, "Server error");

        thrown.expect(DownloadTaskException.class);
        thrown.expectMessage("Could not connect to the server (HTTP 500). Please try again later.");
        httpUrlService.getInputStreamForHttpUrl("fi.testbed2/test");

    }

}
