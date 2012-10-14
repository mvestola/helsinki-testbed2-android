package fi.testbed2.data;

import fi.testbed2.util.TimeUtil;

/**
 * Represents the image object read from the testbed website.
 */
public class TestbedMapImage {

    private String imageURL;
    private String timestamp;
    private String localTimestamp;

    public TestbedMapImage(String imageURL, String timestamp) {
        this.imageURL = imageURL;
        this.timestamp = timestamp;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTimestamp() {
        return getLocalTimestamp();
    }

    private String getLocalTimestamp() {
        if (localTimestamp==null) {
            localTimestamp = TimeUtil.getLocalTimestampFromGMTTimestamp(this.timestamp);
        }
        return localTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestbedMapImage that = (TestbedMapImage) o;

        if (imageURL != null ? !imageURL.equals(that.imageURL) : that.imageURL != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return imageURL != null ? imageURL.hashCode() : 0;
    }

}
