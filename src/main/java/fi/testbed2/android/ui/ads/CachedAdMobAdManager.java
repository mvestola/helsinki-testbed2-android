package fi.testbed2.android.ui.ads;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import fi.testbed2.BuildConfig;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.service.SettingsService;


@Singleton
public class CachedAdMobAdManager implements AdManager {

    private InterstitialAd interstitialAd;
    private boolean useInterstitialAds;

    @Inject
    SettingsService settingsService;

    public CachedAdMobAdManager() {
        useInterstitialAds = false; // Disabled because does not currently fit to user flow
        Logger.debug("CachedAdMobAdManager instantiated");
    }

    @Override
    public AdRequest getAdRequest() {
        if(BuildConfig.ENVIRONMENT.equals("TEST")) {
            return new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("TEST_DEVICE_ID")
                    .build();
        } else {
            return new AdRequest.Builder().build();
        }
    }

    @Override
    public void initInterstitialAd() {
        if (settingsService.showAds() && useInterstitialAds && interstitialAd == null) {
            interstitialAd = new InterstitialAd(MainApplication.getContext());
            interstitialAd.setAdUnitId("ca-app-pub-0260854390576047/3107319706");
            requestNewInterstitialAd();

            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitialAd();
                }
            });
        }
    }

    @Override
    public void showInterstitialAdIfLoaded() {
        if (settingsService.showAds() && interstitialAd!=null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    private void requestNewInterstitialAd() {
        interstitialAd.loadAd(getAdRequest());
    }

}
