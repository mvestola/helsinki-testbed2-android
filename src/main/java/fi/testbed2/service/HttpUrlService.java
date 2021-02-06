package fi.testbed2.service;

import java.io.InputStream;

import fi.testbed2.android.task.exception.DownloadTaskException;

public interface HttpUrlService {

    int HTTP_VALID_STATUS_CODE = 200;

    InputStream getInputStreamForHttpUrl(final String url) throws DownloadTaskException;

}