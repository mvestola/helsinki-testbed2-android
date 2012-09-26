package fi.testbed2.util;

import fi.testbed2.R;
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
                throw new DownloadTaskException(R.string.error_msg_http_status, "-1");
            }

            int statusCode = statusLine.getStatusCode();

            if(statusCode != HTTP_VALID_STATUS_CODE) {
                throw new DownloadTaskException(R.string.error_msg_http_status, ""+statusCode);
            }

            HttpEntity entity = response.getEntity();

            if(entity == null) {
                throw new DownloadTaskException(R.string.error_msg_http_error);
            }

            return entity;
        } catch(IllegalStateException e) {
            e.printStackTrace();
            throw new DownloadTaskException(R.string.error_msg_invalid_url, url);
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
            throw new DownloadTaskException(R.string.error_msg_invalid_url, url);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new DownloadTaskException(R.string.error_msg_http_error);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DownloadTaskException(R.string.error_msg_http_error);
        }

    }

}
