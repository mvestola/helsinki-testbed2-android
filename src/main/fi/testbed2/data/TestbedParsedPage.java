package fi.testbed2.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the results (images) parsed from the testbed's HTML page.
 */
public class TestbedParsedPage {

    private List<TestbedMapImage> testbedMapImages;

    public TestbedMapImage getLatestTestbedImage() {
        return testbedMapImages.get(testbedMapImages.size()-1);
    }

    public List<TestbedMapImage> getAllTestbedImages() {
        return testbedMapImages;
    }

    public int getNotDownloadedCount() {

        int notDownloaded = 0;
        for (TestbedMapImage mapImage : testbedMapImages) {
            if (!mapImage.hasBitmapDataDownloaded()) {
                notDownloaded++;
            }
        }

        return notDownloaded;

    }

    public List<TestbedMapImage> getDownloadedTestbedImages() {

        List<TestbedMapImage> downloaded = new ArrayList<TestbedMapImage>();
        for (TestbedMapImage mapImage : testbedMapImages) {
            if (mapImage.hasBitmapDataDownloaded()) {
                downloaded.add(mapImage);
            }
        }

        return downloaded;
    }

    public void addTestbedMapImage(TestbedMapImage image) {
        if (testbedMapImages==null) {
            testbedMapImages = new ArrayList<TestbedMapImage>();
        }
        testbedMapImages.add(image);
    }

}