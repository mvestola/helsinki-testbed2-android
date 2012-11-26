package fi.testbed2.service;

import fi.testbed2.android.task.exception.DownloadTaskException;

import java.io.InputStream;

public interface HttpUrlService {

    public static final int HTTP_VALID_STATUS_CODE = 200;

    public InputStream getInputStreamForHttpUrl(final String url) throws DownloadTaskException;

}