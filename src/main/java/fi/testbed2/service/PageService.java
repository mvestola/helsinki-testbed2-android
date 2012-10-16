package fi.testbed2.service;

import fi.testbed2.data.TestbedMapImage;
import fi.testbed2.data.TestbedParsedPage;
import fi.testbed2.exception.DownloadTaskException;
import fi.testbed2.task.Task;

import java.util.List;

public interface PageService {

    /**
     * Use this to initialize the page object for the service.
     * @param url
     * @param task
     * @return
     * @throws DownloadTaskException
     */
    public TestbedParsedPage downloadAndParseTestbedPage(final String url, Task task) throws DownloadTaskException;

    public TestbedParsedPage getTestbedParsedPage();

    public int getNotDownloadedImagesCount();
    public void evictPage();

}
