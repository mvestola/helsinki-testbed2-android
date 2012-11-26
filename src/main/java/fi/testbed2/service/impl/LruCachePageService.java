package fi.testbed2.service.impl;

import android.app.ActivityManager;
import android.support.v4.util.LruCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fi.testbed2.R;
import fi.testbed2.android.app.Logging;
import fi.testbed2.android.task.Task;
import fi.testbed2.android.task.exception.DownloadTaskException;
import fi.testbed2.domain.TestbedMapImage;
import fi.testbed2.domain.TestbedParsedPage;
import fi.testbed2.service.BitmapService;
import fi.testbed2.service.HttpUrlService;
import fi.testbed2.service.PageService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Singleton
public class LruCachePageService implements PageService {

    private static String PAGE_CACHE_KEY = "TESTBED_PAGE";

    private static final String HTML_TIMESTAMP_PREFIX = "var anim_timestamps = new Array(\"";
    private static final String HTML_TIMESTAMP_SUFFIX = "\");";
    private static final String HTML_IMAGE_PREFIX = "var anim_images_anim_anim = new Array(\"";
    private static final String HTML_IMAGE_SUFFIX = "\");";

    @Inject
    ActivityManager activityManager;

    @Inject
    BitmapService bitmapService;

    @Inject
    HttpUrlService httpUrlService;

    private int cacheSizeInBytes = -1;

    private LruCache<String, TestbedParsedPage> pageCache;

    public LruCachePageService() {
        Logging.debug("LruCachePageService instantiated");
    }

    private LruCache<String, TestbedParsedPage> getCache() {
        if (pageCache==null) {
            initPageCache();
        }
        return pageCache;
    }

    public void setCacheSizeInBytes(int bytes) {
        cacheSizeInBytes = bytes;
    }

    private int getCacheSizeInBytes() {
        if (cacheSizeInBytes == -1) {
            /*
            * Get memory class of this device, exceeding this amount
            * will throw an OutOfMemory exception.
            */
            final int memClass = activityManager.getMemoryClass();
            cacheSizeInBytes = 1024 * 1024 * memClass / 8;
        }
        return cacheSizeInBytes;
    }

    private void initPageCache() {
        pageCache = new LruCache<String, TestbedParsedPage>(getCacheSizeInBytes());
    }

    public TestbedParsedPage getTestbedParsedPage() {
        return getCache().get(PAGE_CACHE_KEY);
    }

    private void setTestbedParsedPage(TestbedParsedPage newTestbedParsedPage) {

        clearPreviousOldData(getTestbedParsedPage(), newTestbedParsedPage);

        if (newTestbedParsedPage==null) {
            getCache().remove(PAGE_CACHE_KEY);
            System.gc();
        } else {
            getCache().put(PAGE_CACHE_KEY, newTestbedParsedPage);
        }
    }

    @Override
    public void evictPage() {
        getCache().evictAll();
    }

    public int getNotDownloadedImagesCount() {

        List<TestbedMapImage> testbedMapImages = getTestbedParsedPage().getAllTestbedImages();

        if (testbedMapImages==null || testbedMapImages.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        int notDownloaded = 0;
        for (TestbedMapImage mapImage : testbedMapImages) {
            if (mapImage==null || !bitmapService.bitmapIsDownloaded(mapImage)) {
                notDownloaded++;
            }
        }

        return notDownloaded;

    }

    private void clearPreviousOldData(TestbedParsedPage oldPage, TestbedParsedPage newPage) {

        if (oldPage==null) {
            return;
        }

        for (TestbedMapImage mapImg : oldPage.getAllTestbedImages()) {

            if (mapImg!=null && !newPage.getAllTestbedImages().contains(mapImg)) {
                bitmapService.evictBitmap(mapImg);
            }

        }

    }

    /**
     *
     * @param url HTML page url
     * @param task
     * @return
     * @throws DownloadTaskException
     */
    public TestbedParsedPage downloadAndParseTestbedPage(final String url, Task task) throws DownloadTaskException {

        Logging.debug("Downloading testbed page from url: " + url);

        TestbedParsedPage testbedParsedPage = new TestbedParsedPage();

        try {

            InputStream in = httpUrlService.getInputStreamForHttpUrl(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String[] timestamps = null;
            String[] imageUrls = null;

            while(!task.isAbort())
            {
                String line = reader.readLine();

                if (line == null)
                    break;

                if(line.startsWith(HTML_TIMESTAMP_PREFIX) && line.endsWith(HTML_TIMESTAMP_SUFFIX)) {
                    String tmp = line.substring(HTML_TIMESTAMP_PREFIX.length(), line.length() - HTML_TIMESTAMP_SUFFIX.length());
                    tmp = tmp.replaceAll("\"", "");
                    timestamps = tmp.split(",");
                }

                else if(line.startsWith(HTML_IMAGE_PREFIX) && line.endsWith(HTML_IMAGE_SUFFIX)) {
                    String tmp = line.substring(HTML_IMAGE_PREFIX.length(), line.length() - HTML_IMAGE_SUFFIX.length());
                    tmp = tmp.replaceAll("\"", "");
                    imageUrls = tmp.split(",");
                    break;
                }
            }

            in.close();

            if (task.isAbort()) {
                return null;
            }

            // validate timestamps and imageUrls
            if(timestamps == null) {
                throw new DownloadTaskException(R.string.error_msg_parsing_timestamp);
            }
            if(imageUrls == null) {
                throw new DownloadTaskException(R.string.error_msg_parsing_urls);
            }
            if(timestamps.length != imageUrls.length) {
                throw new DownloadTaskException(R.string.error_msg_parsing_length);
            }

            int i = 0;
            for(String imageUrl : imageUrls) {
                TestbedMapImage image = new TestbedMapImage(imageUrl, timestamps[i]);
                i++;
                testbedParsedPage.addTestbedMapImage(image);
            }

        } catch (IOException e) {
            throw new DownloadTaskException(R.string.error_msg_parsing_page);
        } catch(IndexOutOfBoundsException e) {
            throw new DownloadTaskException(R.string.error_msg_parsing_page_changed);
        }

        this.setTestbedParsedPage(testbedParsedPage);


        return testbedParsedPage;
    }

}
