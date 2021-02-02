package fi.testbed2;

import com.google.inject.AbstractModule;

import fi.testbed2.android.ui.ads.AdManager;
import fi.testbed2.android.ui.ads.CachedAdMobAdManager;
import fi.testbed2.android.ui.dialog.AlertDialogBuilder;
import fi.testbed2.android.ui.dialog.DialogBuilder;
import fi.testbed2.service.*;
import fi.testbed2.service.impl.*;
import roboguice.inject.SharedPreferencesName;

/**
 * Module used by the RoboGuice IoC framework.
 */
public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DialogBuilder.class).to(AlertDialogBuilder.class);
        bind(MunicipalityService.class).to(InlineMunicipalityService.class);
        bind(LocationService.class).to(PreferenceBasedLocationService.class);
        bind(CoordinateService.class).to(MercatorCoordinateService.class);
        bind(SettingsService.class).to(SharedPreferenceSettingsService.class);
        bind(AdManager.class).to(CachedAdMobAdManager.class);
        bind(BitmapService.class).to(LruCacheBitmapService.class);
        bind(PageService.class).to(LruCachePageService.class);
        bind(HttpUrlService.class).to(URLConnectionHttpUrlService.class);
        bindConstant().annotatedWith(SharedPreferencesName.class)
                .to(SharedPreferenceSettingsService.SHARED_PREFERENCE_FILE_NAME);
    }

}
