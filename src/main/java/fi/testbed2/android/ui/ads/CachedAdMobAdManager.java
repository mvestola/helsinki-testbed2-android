package fi.testbed2.android.ui.ads;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

import fi.testbed2.BuildConfig;
import fi.testbed2.android.app.Logger;
import fi.testbed2.service.SettingsService;


@Singleton
public class CachedAdMobAdManager implements AdManager {

    @Inject
    SettingsService settingsService;

    public CachedAdMobAdManager() {
        Logger.debug("CachedAdMobAdManager instantiated");
    }

    @Override
    public AdRequest getAdRequest() {
        if (BuildConfig.ENVIRONMENT.equals("TEST")) {
            List<String> testDevices = new ArrayList<>();
            testDevices.add(AdRequest.DEVICE_ID_EMULATOR);

            RequestConfiguration requestConfiguration = new RequestConfiguration.Builder()
                    .setTestDeviceIds(testDevices)
                    .build();

            MobileAds.setRequestConfiguration(requestConfiguration);
        }
        return new AdRequest.Builder().build();
    }
}
