package fi.testbed2.service.impl;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fi.testbed2.R;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.task.exception.DownloadTaskException;
import fi.testbed2.domain.TestbedMapImage;
import fi.testbed2.service.BitmapService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Singleton
public class LruCacheBitmapService implements BitmapService {

    @Inject
    ActivityManager activityManager;

    private LruCache<String, Bitmap> imageCache;

    private int cacheSizeInBytes = -1;

    public LruCacheBitmapService() {
        Logger.debug("LruCacheBitmapService instantiated");
    }

    private LruCache<String, Bitmap> getCache() {
        if (imageCache==null) {
            initImageCache();
        }
        return imageCache;
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
            cacheSizeInBytes = 1024 * 1024 * memClass;
        }
        return cacheSizeInBytes;
    }

    private void initImageCache() {

        imageCache = new LruCache<String, Bitmap>(getCacheSizeInBytes()) {
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight();
            }
            @Override
            protected void entryRemoved(boolean evicted, @NonNull String key, @NonNull Bitmap oldValue, Bitmap newValue) {
                if (evicted) {
                    oldValue.recycle();
                }
            }
        };

    }

    public void evictBitmap(TestbedMapImage image) {
        getCache().remove(image.getImageURL());
        System.gc();
    }

    @Override
    public void evictAll() {
        getCache().evictAll();
    }

    @Override
    public Bitmap getBitmap(TestbedMapImage image) {
        return getCache().get(image.getImageURL());
    }

    public boolean bitmapIsNotDownloaded(TestbedMapImage image) {
        return getCache().get(image.getImageURL()) == null;
    }

    public Bitmap downloadBitmap(TestbedMapImage image) throws DownloadTaskException {

        InputStream stream = null;

        String imageURL = image.getImageURL();

        try {

            Logger.debug("Downloading bitmap from url: " + imageURL);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inTempStorage =  new byte[16 * 1024];

            stream = new URL(imageURL).openStream();
            Bitmap downloadedBitmapImage = BitmapFactory.decodeStream(stream, new Rect(-1,-1,-1,-1), options);

            if (downloadedBitmapImage==null) {
                throw new DownloadTaskException(R.string.error_msg_map_image_could_not_download);
            }

            getCache().put(imageURL, downloadedBitmapImage);

            return downloadedBitmapImage;

        } catch (IOException e) {
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
