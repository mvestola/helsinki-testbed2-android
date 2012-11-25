package fi.testbed2.service.impl;

import com.xtremelabs.robolectric.Robolectric;
import fi.testbed2.AbstractTestCase;
import fi.testbed2.InjectedTestRunner;
import fi.testbed2.exception.DownloadTaskException;
import org.apache.http.HttpEntity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(InjectedTestRunner.class)
public class DefaultHTTPServiceTest extends AbstractTestCase {

    private DefaultHTTPService httpService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        httpService = new DefaultHTTPService();
    }

    @Test
    public void testResponse200() throws Exception {

        Robolectric.addPendingHttpResponse(200, "OK");
        HttpEntity httpEntity = httpService.getHttpEntityForUrl("fi.testbed2/test");

        assertNotNull(httpEntity);

    }

    @Test
    public void testResponse404() throws Exception {

        Robolectric.addPendingHttpResponse(404, "Not Found");

        thrown.expect(DownloadTaskException.class);
        thrown.expectMessage("Could not connect to the server (HTTP 404). Please try again later.");
        httpService.getHttpEntityForUrl("fi.testbed2/test");

    }

    @Test
    public void testResponse500() throws Exception {

        Robolectric.addPendingHttpResponse(500, "Server error");

        thrown.expect(DownloadTaskException.class);
        thrown.expectMessage("Could not connect to the server (HTTP 500). Please try again later.");
        httpService.getHttpEntityForUrl("fi.testbed2/test");

    }

}
