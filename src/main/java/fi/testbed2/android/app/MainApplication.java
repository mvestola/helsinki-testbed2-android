package fi.testbed2.android.app;

import android.app.Activity;
import android.content.Context;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.googlecode.androidannotations.annotations.EApplication;
import fi.testbed2.MainModule;
import fi.testbed2.service.BitmapService;
import fi.testbed2.service.PageService;
import roboguice.application.RoboApplication;

import java.util.List;

@EApplication
public class MainApplication extends RoboApplication {

    public static final String LOG_IDENTIFIER = "TestbedViewer2";

	public static final int RESULT_ERROR = Activity.RESULT_FIRST_USER;
    public static final int RESULT_OK = Activity.RESULT_OK;
    public static final int RESULT_REFRESH = 10;

    private Module module = new MainModule();

    private static Context context;
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
        Logger.debug("MainApplication started");
    }

    public static Context getContext(){
        return context;
    }

    public static MainApplication getApplication(){
        return app;
    }

    public void onTerminate() {
        super.onTerminate();
        pageService.evictPage();
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

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    protected void addApplicationModules(List<Module> modules) {
        modules.add(module);
    }

}
