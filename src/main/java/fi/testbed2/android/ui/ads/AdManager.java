package fi.testbed2.android.ui.ads;

import com.google.android.gms.ads.AdRequest;

public interface AdManager {

    public AdRequest getAdRequest();
    public void initInterstitialAd();
    public void showInterstitialAdIfLoaded();

}
