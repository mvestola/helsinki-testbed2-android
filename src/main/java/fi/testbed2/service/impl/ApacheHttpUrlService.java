package fi.testbed2.service.impl;

import com.google.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fi.testbed2.R;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.task.exception.DownloadTaskException;
import fi.testbed2.service.HttpUrlService;

/**
 * Service class for making HTTP requests.
 */
@Singleton
public class ApacheHttpUrlService implements HttpUrlService {

    public ApacheHttpUrlService() {
        Logger.debug("ApacheHttpUrlService instantiated");
    }

    public InputStream getInputStreamForHttpUrl(final String url) throws DownloadTaskException {

        try
        {

            Logger.debug("getInputStreamForHttpUrl: " + url);

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int statusCode = conn.getResponseCode();

            if(statusCode != HTTP_VALID_STATUS_CODE) {
                throw new DownloadTaskException(R.string.error_msg_http_status, ""+statusCode);
            }

            return conn.getInputStream();
        } catch(IllegalStateException e) {
            e.printStackTrace();
            throw new DownloadTaskException(R.string.error_msg_invalid_url, url);
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
            throw new DownloadTaskException(R.string.error_msg_invalid_url, url);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DownloadTaskException(R.string.error_msg_http_error);
        }

    }

}
