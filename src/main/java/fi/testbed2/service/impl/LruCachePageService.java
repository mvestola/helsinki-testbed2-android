package fi.testbed2.service.impl;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fi.testbed2.data.TestbedMapImage;
import fi.testbed2.data.TestbedParsedPage;
import fi.testbed2.service.BitmapService;
import fi.testbed2.service.PageService;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class LruCachePageService implements PageService {

    private static String PAGE_CACHE_KEY = "TESTBED_PAGE";

    @Inject
    ActivityManager activityManager;

    @Inject
    BitmapService bitmapService;

    private int cacheSizeInBytes = -1;

    private LruCache<String, TestbedParsedPage> pageCache;

    public LruCachePageService() {
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

    public void setTestbedParsedPage(TestbedParsedPage newTestbedParsedPage) {

        clearPreviousOldData(getTestbedParsedPage(), newTestbedParsedPage);

        if (newTestbedParsedPage==null) {
            getCache().remove(PAGE_CACHE_KEY);
            System.gc();
        } else {
            getCache().put(PAGE_CACHE_KEY, newTestbedParsedPage);
        }
    }

    @Override
    public void evictAll() {
        getCache().evictAll();
    }

    public int getNotDownloadedCount() {

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

    public List<TestbedMapImage> getDownloadedTestbedImages() {

        List<TestbedMapImage> downloaded = new ArrayList<TestbedMapImage>();
        for (TestbedMapImage mapImage : getTestbedParsedPage().getAllTestbedImages()) {
            if (bitmapService.bitmapIsDownloaded(mapImage)) {
                downloaded.add(mapImage);
            }
        }

        return downloaded;
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

}
