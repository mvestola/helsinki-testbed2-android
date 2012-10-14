package fi.testbed2.service;

import android.support.v4.util.LruCache;
import fi.testbed2.data.TestbedMapImage;
import fi.testbed2.data.TestbedParsedPage;

import java.util.ArrayList;
import java.util.List;

public interface PageService {

    public TestbedParsedPage getTestbedParsedPage();
    public void setTestbedParsedPage(TestbedParsedPage testbedParsedPage);
    public int getNotDownloadedCount();
    public List<TestbedMapImage> getDownloadedTestbedImages();
    public void evictAll();

}
