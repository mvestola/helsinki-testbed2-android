package fi.testbed2.service.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.httpclient.FakeHttp;

import java.io.InputStream;

import fi.testbed2.AbstractTestCase;
import fi.testbed2.BuildConfig;
import fi.testbed2.InjectedTestRunner;
import fi.testbed2.android.task.exception.DownloadTaskException;

import static org.junit.Assert.assertNotNull;

@RunWith(InjectedTestRunner.class)
@Config(constants = BuildConfig.class, sdk = AbstractTestCase.ROBOLECTRIC_API_LEVEL)
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

        FakeHttp.setDefaultHttpResponse(200, "OK");
        InputStream stream = httpUrlService.getInputStreamForHttpUrl("fi.testbed2/test");
        assertNotNull(stream);

    }

    @Test
    public void testResponse404() throws Exception {

        FakeHttp.setDefaultHttpResponse(404, "Not Found");

        thrown.expect(DownloadTaskException.class);
        thrown.expectMessage("Could not connect to the server (HTTP 404). Please try again later.");
        httpUrlService.getInputStreamForHttpUrl("fi.testbed2/test");

    }

    @Test
    public void testResponse500() throws Exception {

        FakeHttp.setDefaultHttpResponse(500, "Server error");

        thrown.expect(DownloadTaskException.class);
        thrown.expectMessage("Could not connect to the server (HTTP 500). Please try again later.");
        httpUrlService.getInputStreamForHttpUrl("fi.testbed2/test");

    }

}
