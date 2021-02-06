package fi.testbed2.service;

import fi.testbed2.android.task.Task;
import fi.testbed2.android.task.exception.DownloadTaskException;
import fi.testbed2.domain.TestbedParsedPage;

public interface PageService {

    /**
     * Use this to initialize the page object for the service.
     * @param url
     * @param task
     * @return
     * @throws fi.testbed2.android.task.exception.DownloadTaskException
     */
    TestbedParsedPage downloadAndParseTestbedPage(final String url, Task task) throws DownloadTaskException;

    TestbedParsedPage getTestbedParsedPage();

    int getNotDownloadedImagesCount();
    void evictPage();

}
