package fi.testbed2.service.impl;

import com.google.inject.Singleton;

import java.io.BufferedInputStream;
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
public class URLConnectionHttpUrlService implements HttpUrlService {

    public URLConnectionHttpUrlService() {
        Logger.debug("URLConnectionHttpUrlService instantiated");
    }

    public InputStream getInputStreamForHttpUrl(final String url) throws DownloadTaskException {

        try {

            Logger.debug("getInputStreamForHttpUrl: " + url);

            URL urlObj = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
            try {
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HTTP_VALID_STATUS_CODE) {
                    throw new DownloadTaskException(R.string.error_msg_http_status, "" + statusCode);
                }
                return new BufferedInputStream(urlConnection.getInputStream());
            } finally {
                urlConnection.disconnect();
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new DownloadTaskException(R.string.error_msg_invalid_url, url);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DownloadTaskException(R.string.error_msg_http_error);
        }

    }

}
