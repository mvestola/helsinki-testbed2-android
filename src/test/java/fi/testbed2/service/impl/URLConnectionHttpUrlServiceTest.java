package fi.testbed2.service.impl;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.io.InputStream;

import fi.testbed2.AbstractTestCase;
import fi.testbed2.InjectedTestRunner;
import fi.testbed2.android.task.exception.DownloadTaskException;

import static org.junit.Assert.assertNotNull;

@RunWith(InjectedTestRunner.class)
public class URLConnectionHttpUrlServiceTest extends AbstractTestCase {

    private static final String TEST_URL = "/testing";

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);
    private MockServerClient mockServerClient;

    private URLConnectionHttpUrlService httpUrlService;

    @SuppressWarnings("deprecation")
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        httpUrlService = new URLConnectionHttpUrlService();
        initClassForMocks(httpUrlService);
    }

    private String getFullTestUrl() {
        return "http://localhost:" + mockServerClient.getPort() + TEST_URL;
    }

    @Test
    public void testResponse200() throws Exception {
        mockServerClient.when(HttpRequest.request(TEST_URL)).respond(HttpResponse.response().withStatusCode(HttpStatus.SC_OK));
        InputStream stream = httpUrlService.getInputStreamForHttpUrl(getFullTestUrl());
        assertNotNull(stream);
    }

    @Test
    public void testResponse404() throws Exception {
        mockServerClient.when(HttpRequest.request(TEST_URL)).respond(HttpResponse.response().withStatusCode(HttpStatus.SC_NOT_FOUND));
        thrown.expect(DownloadTaskException.class);
        thrown.expectMessage("Could not connect to the server (HTTP 404). Please try again later.");
        httpUrlService.getInputStreamForHttpUrl(getFullTestUrl());
    }

    @Test
    public void testResponse500() throws Exception {
        mockServerClient.when(HttpRequest.request(TEST_URL)).respond(HttpResponse.response().withStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        thrown.expect(DownloadTaskException.class);
        thrown.expectMessage("Could not connect to the server (HTTP 500). Please try again later.");
        httpUrlService.getInputStreamForHttpUrl(getFullTestUrl());
    }
}
