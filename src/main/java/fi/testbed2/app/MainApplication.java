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
import com.google.inject.Inject;
import fi.testbed2.data.TestbedParsedPage;
import fi.testbed2.service.BitmapService;
import fi.testbed2.service.PageService;
import fi.testbed2.service.PreferenceService;

public class MainApplication extends Application {

    private static boolean debug = true;

    public static final String LOG_IDENTIFIER = "TestbedViewer2";

	public static final int RESULT_ERROR = Activity.RESULT_FIRST_USER;
    public static final int RESULT_OK = Activity.RESULT_OK;
    public static final int RESULT_REFRESH = 10;

    private static Context context;

    @Inject
    PageService pageService;

    @Inject
    BitmapService bitmapService;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext(){
        return context;
    }

    public void onTerminate() {
        super.onTerminate();
        pageService.evictAll();
        bitmapService.evictAll();
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
