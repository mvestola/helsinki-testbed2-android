package fi.testbed2.service;

import fi.testbed2.exception.DownloadTaskException;
import org.apache.http.HttpEntity;

public interface HTTPService {

    public static final int HTTP_VALID_STATUS_CODE = 200;

    public HttpEntity getHttpEntityForUrl(final String url) throws DownloadTaskException;

}