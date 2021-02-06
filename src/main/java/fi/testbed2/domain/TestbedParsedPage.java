package fi.testbed2.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the results (images) parsed from the testbed's HTML page.
 */
public class TestbedParsedPage {

    private List<TestbedMapImage> testbedMapImages;

    public TestbedMapImage getLatestTestbedImage() {
        return testbedMapImages.get(testbedMapImages.size() - 1);
    }

    public List<TestbedMapImage> getAllTestbedImages() {
        return testbedMapImages;
    }

    public void addTestbedMapImage(TestbedMapImage image) {
        if (testbedMapImages == null) {
            testbedMapImages = new ArrayList<>();
        }
        testbedMapImages.add(image);
    }

}