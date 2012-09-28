package fi.testbed2.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import fi.testbed2.R;
import fi.testbed2.app.MainApplication;
import fi.testbed2.exception.DownloadTaskException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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

            InputStream stream = null;

            try {

                //Log.e(MainApplication.LOG_IDENTIFIER, "downloading image: " + getLocalTimestamp());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inTempStorage =  new byte[16 * 1024];
                options.inPurgeable = true;
                options.inInputShareable = true;
                options.inDither=false;

                stream = new URL(imageURL).openStream();
                Bitmap downloadedBitmapImage = BitmapFactory.decodeStream(stream, new Rect(-1,-1,-1,-1), options);

                if (downloadedBitmapImage==null) {
                    throw new DownloadTaskException(R.string.error_msg_map_image_could_not_download);
                }

                MainApplication.addBitmapToImageCache(bitmapCacheKey, downloadedBitmapImage);

            } catch (IOException e) {
                e.printStackTrace();
                throw new DownloadTaskException(R.string.error_msg_io_exception);
            } finally {
                if (stream!=null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

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

            String gmtTimestamp = this.timestamp;
            int year = Integer.parseInt(gmtTimestamp.substring(0, 4));
            int month = Integer.parseInt(gmtTimestamp.substring(4, 6));
            int day = Integer.parseInt(gmtTimestamp.substring(6, 8));
            int hour = Integer.parseInt(gmtTimestamp.substring(8, 10));
            int minute = Integer.parseInt(gmtTimestamp.substring(10, 12));
            int second = 0;

            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Helsinki"));
            cal.set(year + 1900, month, day, hour, minute, second);
            cal.setTimeInMillis(cal.getTimeInMillis()); // XXX + Calendar.getInstance().get(Calendar.DST_OFFSET));

            localTimestamp = String.format("%1$tH:%2$tM", cal.getTime(), cal.getTime());

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
