package fi.testbed2.android.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.google.android.gms.ads.MobileAds;
import com.google.inject.Inject;

import org.androidannotations.annotations.EApplication;

import fi.testbed2.service.BitmapService;
import fi.testbed2.service.PageService;
import roboguice.RoboGuice;

@EApplication
public class MainApplication extends Application {

    static {
        RoboGuice.setUseAnnotationDatabases(false);
    }

    public static final int RESULT_ERROR = Activity.RESULT_FIRST_USER;
    public static final int RESULT_OK = Activity.RESULT_OK;
    public static final int RESULT_REFRESH = 10;

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    @SuppressLint("StaticFieldLeak")
    private static MainApplication app;

    @Inject
    PageService pageService;

    @Inject
    BitmapService bitmapService;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        app = this;
        MobileAds.initialize(getApplicationContext());
        Logger.debug("MainApplication started");
    }

    public static Context getContext() {
        return context;
    }

    public static MainApplication getApplication() {
        return app;
    }

    public void onTerminate() {
        super.onTerminate();
        pageService.evictPage();
        bitmapService.evictAll();
    }

    /**
     * Returns the version name (number), e.g. 2.0.3
     *
     * @return
     */
    public static String getVersionName() {
        String versionName;
        try {
            versionName = getContext().getPackageManager().
                    getPackageInfo(getContext().getPackageName(), 0).versionName;
        } catch (Exception e) {
            versionName = "Unknown";
        }
        return versionName;
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
