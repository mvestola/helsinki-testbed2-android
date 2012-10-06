package fi.testbed2.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import com.jhlabs.map.Point2D;
import fi.testbed2.data.TestbedParsedPage;

public class MainApplication extends Application {

    public static final String LOG_IDENTIFIER = "TestbedViewer2";

	public static final int RESULT_ERROR = Activity.RESULT_FIRST_USER;
    public static final int RESULT_OK = Activity.RESULT_OK;
    public static final int RESULT_REFRESH = 10;

    public static final String PREF_ANIM_FRAME_DELAY = "PREF_ANIM_FRAME_DELAY";
    public static final String PREF_ANIM_AUTOSTART = "PREF_ANIM_AUTOSTART";
    public static final String PREF_MAP_TYPE = "PREF_MAP_TYPE";
    public static final String PREF_MAP_TIME_STEP = "PREF_MAP_TIME_STEP";
    public static final String PREF_MAP_NUMBER_OF_IMAGES = "PREF_MAP_NUMBER_OF_IMAGES";
    public static final String PREF_ORIENTATION_PREFERENCE_KEY_PREFIX = "PREFERENCE_ANIM_BOUNDS_ORIENTATION_";
    public static final String PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION = "PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION";
    public static final String PREF_LOCATION_SHOW_USER_LOCATION = "PREF_LOCATION_SHOW_USER_LOCATION";
    public static final String PREF_LOCATION_SHOW_MUNICIPALITIES = "PREF_LOCATION_MUNICIPALITIES";
    public static final String PREF_LOCATION_SHOW_MUNICIPALITIES_SPLIT = "===";

    private static Context mContext;

    private static LruCache<String, Bitmap> imageCache;
    private static LruCache<String, TestbedParsedPage> pageCache;

    private static String PAGE_CACHE_KEY = "TESTBED_PAGE";

    private static Point2D.Double userLocationInMapPx;

    public static TestbedParsedPage getTestbedParsedPage() {
        return pageCache.get(PAGE_CACHE_KEY);
    }

    public static void setTestbedParsedPage(TestbedParsedPage testbedParsedPage) {
        if (testbedParsedPage==null) {
            pageCache.remove(PAGE_CACHE_KEY);
            System.gc();
        } else {
            pageCache.put(PAGE_CACHE_KEY, testbedParsedPage);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initImageCache();
        initPageCache();
    }

    public static Context getContext(){
        return mContext;
    }

    private void initImageCache() {

        /*
         * Get memory class of this device, exceeding this amount
         * will throw an OutOfMemory exception.
         */
        final int memClass = ((ActivityManager) this.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();

        final int cacheSizeInBytes = 1024 * 1024 * memClass;

        imageCache = new LruCache<String, Bitmap>(cacheSizeInBytes) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight();
            }
            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                if (evicted) {
                    oldValue.recycle();
                }
            }
        };

    }

    private void initPageCache() {

        final int memClass = ((ActivityManager) this.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();

        final int cacheSizeInBytes = 1024 * 1024 * memClass / 8;

        pageCache = new LruCache<String, TestbedParsedPage>(cacheSizeInBytes);

    }

    public static Point2D.Double getUserLocationInMapPixels() {
        return userLocationInMapPx;
    }

    public static void setUserLocationInMapPixels(Point2D.Double pos) {
        userLocationInMapPx = pos;
    }

    public static void clearData() {
        clearPageCache();
        clearImageCache();
        System.gc();
    }

    public void onTerminate() {
        super.onTerminate();
        clearData();
    }

    public static void deleteBitmapCacheEntry(String key) {
        imageCache.remove(key);
        System.gc();
    }

    private static void clearImageCache() {
        imageCache.evictAll();
    }

    private static void clearPageCache() {
        pageCache.evictAll();
    }

    public static void addBitmapToImageCache(String key, Bitmap bitmap) {
        if (getBitmapFromImageCache(key) == null) {
            imageCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromImageCache(String key) {
        return imageCache.get(key);
    }
}
