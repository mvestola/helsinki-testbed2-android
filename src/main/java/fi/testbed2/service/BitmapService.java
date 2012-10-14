package fi.testbed2.service;

import android.graphics.Bitmap;
import fi.testbed2.data.TestbedMapImage;
import fi.testbed2.exception.DownloadTaskException;

public interface BitmapService {

    public boolean bitmapIsDownloaded(TestbedMapImage image);
    public Bitmap downloadBitmap(TestbedMapImage image) throws DownloadTaskException;
    public Bitmap getBitmap(TestbedMapImage image);
    public void evictBitmap(TestbedMapImage image);
    public void evictAll();

}
