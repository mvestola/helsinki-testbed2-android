package fi.testbed2.data;

import android.graphics.Bitmap;
import fi.testbed2.app.MainApplication;
import fi.testbed2.exception.DownloadTaskException;
import fi.testbed2.util.BitmapDownloader;
import fi.testbed2.util.TimeUtil;

/**
 * Represents the image object read from the testbed website.
 */
public class TestbedMapImage {

    public static final String CACHE_KEY_PREFIX = "bitmap_";

    private String imageURL;
    private String timestamp;
    private String localTimestamp;
    private String bitmapCacheKey;

    public TestbedMapImage(String imageURL, String timestamp, int index) {

        this.imageURL = imageURL;
        this.timestamp = timestamp;
        // Should be unique key
        this.bitmapCacheKey = CACHE_KEY_PREFIX + this.getImageURL();

    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTimestamp() {
        return getLocalTimestamp();
    }

    public boolean hasBitmapDataDownloaded() {
        return getDownloadedBitmapImage()!=null;
    }

    /**
     * Downloads the image data and caches it.
     *
     * @throws DownloadTaskException
     */
    public void downloadAndCacheImage() throws DownloadTaskException {

        if (!hasBitmapDataDownloaded()) {
            Bitmap bitmap = BitmapDownloader.downloadTestbedBitmap(imageURL);
            MainApplication.addBitmapToImageCache(bitmapCacheKey, bitmap);
        }

    }

    /**
     * Returns the downloaded bitmap image. If image not yet downloaded, returns null.
     * @return
     */
    public Bitmap getDownloadedBitmapImage() {
        return MainApplication.getBitmapFromImageCache(bitmapCacheKey);
    }

    private String getLocalTimestamp() {
        if (localTimestamp==null) {
            localTimestamp = TimeUtil.getLocalTimestampFromGMTTimestamp(this.timestamp);
        }
        return localTimestamp;
    }

    public String getBitmapCacheKey() {
        return bitmapCacheKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestbedMapImage that = (TestbedMapImage) o;

        if (bitmapCacheKey != null ? !bitmapCacheKey.equals(that.bitmapCacheKey) : that.bitmapCacheKey != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return bitmapCacheKey != null ? bitmapCacheKey.hashCode() : 0;
    }
}
