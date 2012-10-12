package fi.testbed2.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v4.util.LruCache;
import com.jhlabs.map.Point2D;
import fi.testbed2.data.Municipality;
import fi.testbed2.data.TestbedParsedPage;

public class MainApplication extends Application {

    private static boolean debug = true;

    public static final String LOG_IDENTIFIER = "TestbedViewer2";

	public static final int RESULT_ERROR = Activity.RESULT_FIRST_USER;
    public static final int RESULT_OK = Activity.RESULT_OK;
    public static final int RESULT_REFRESH = 10;

    private static Context context;

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
        context = this;
        initImageCache();
        initPageCache();
    }

    public static Context getContext(){
        return context;
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

        if (debug) {
            return Municipality.getMunicipality("Helsinki").getPositionInMapPx();
        } else {
            return userLocationInMapPx;
        }

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

    public static boolean showUserLocation() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getBoolean(Preference.PREF_LOCATION_SHOW_USER_LOCATION, true);
    }

    /**
     * Returns the version name (number), e.g. 2.0.3
     * @return
     */
    public static String getVersionName() {
        String versionName;
        try {
            versionName = getContext().getPackageManager().
                    getPackageInfo(getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "Unknown";
        }
        return versionName;
    }

    public static boolean isDebug() {
        return debug;
    }

    /**
     * Only for testing.
     *
     * @param context
     */
    public static void setContext(Context context) {
        MainApplication.context = context;
    }

}
