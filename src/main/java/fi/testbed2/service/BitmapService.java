package fi.testbed2.service;

import android.graphics.Bitmap;
import fi.testbed2.android.task.exception.DownloadTaskException;
import fi.testbed2.domain.TestbedMapImage;

/**
 * Service which provides downloaded Bitmap images
 * for testbed images.
 */
public interface BitmapService {

    boolean bitmapIsNotDownloaded(TestbedMapImage image);
    Bitmap downloadBitmap(TestbedMapImage image) throws DownloadTaskException;
    Bitmap getBitmap(TestbedMapImage image);
    void evictBitmap(TestbedMapImage image);
    void evictAll();

}
