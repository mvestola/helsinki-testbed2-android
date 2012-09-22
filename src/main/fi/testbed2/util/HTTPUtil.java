package fi.testbed2.util;

import fi.testbed2.exception.DownloadTaskException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Utility class for making HTTP requests.
 */
public class HTTPUtil {

    private static final int HTTP_VALID_STATUS_CODE = 200;

    public static HttpEntity getHttpEntityForUrl(final String url) throws DownloadTaskException {

        try
        {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();

            if(statusLine == null) {
                String message = "Invalid HTTP status line (init)";
                throw new DownloadTaskException(message);
            }

            int statusCode = statusLine.getStatusCode();

            if(statusCode != HTTP_VALID_STATUS_CODE) {
                String message = "Invalid HTTP status code (init): " + statusCode;
                throw new DownloadTaskException(message);
            }

            HttpEntity entity = response.getEntity();

            if(entity == null) {
                throw new DownloadTaskException("Unable to retrieve HttpEntity for url: " + url);
            }

            return entity;
        }
        catch(IllegalArgumentException e)
        {
            throw new DownloadTaskException("IllegalArgumentException. Invalid url? url=" + url, e);
        } catch (ClientProtocolException e) {
            throw new DownloadTaskException("ClientProtocolException: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new DownloadTaskException("IOException: " + e.getMessage(), e);
        }

    }

}
