package fi.testbed2.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import org.androidannotations.annotations.*;

import fi.testbed2.BuildConfig;
import fi.testbed2.R;
import fi.testbed2.android.ui.dialog.DialogBuilder;
import fi.testbed2.service.SettingsService;


/**
 * Base activity class for all activities.
 * Handles options menu, for instance.
 */
@EActivity
public abstract class AbstractActivity extends Activity {

    @Inject
    DialogBuilder dialogBuilder;

    @Inject
    SettingsService settingsService;

    @ViewById(R.id.adView)
    AdView adView;

    private String currentErrorMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (settingsService.isShowWhatsNewDialog()) {
            dialogBuilder.getWhatsNewDialog().show();
        }
    }

    /** Called when resuming, BUT after onActivityResult! */
    @Override
    protected void onResume() {
        super.onResume();

        AdView adView = (AdView)this.findViewById(R.id.adView);

        if (adView!=null) {
            if (settingsService.showAds()) {
                adView.setVisibility(AdView.VISIBLE);
            } else {
                adView.setVisibility(AdView.GONE);
            }
        }

    }

    @AfterViews
    void initializeAds() {
        adView.loadAd(getAdRequest());
    }

    private AdRequest getAdRequest() {
        if(BuildConfig.ENVIRONMENT.equals("TEST")) {
            return new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("TEST_DEVICE_ID")
                    .build();
        } else {
            return new AdRequest.Builder().build();
        }
    }

    public abstract void onRefreshFromMenuSelected();

    @OptionsItem(R.id.main_menu_refresh)
    public void onRefreshMenuItemSelected() {
        onRefreshFromMenuSelected();
    }

    @OptionsItem(R.id.main_menu_preferences)
    public void onPreferencesMenuItemSelected() {
        startActivity(new Intent(this, TestbedPreferenceActivity.class));
    }

    @OptionsItem(R.id.main_menu_about)
    public void onAboutMenuItemSelected() {
        dialogBuilder.getAboutDialog().show();
    }

    protected void showErrorDialog(String errorMsg) {
        currentErrorMsg = errorMsg;
        dialogBuilder.getErrorDialog(currentErrorMsg).show();
    }

    protected void showShortMessage(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Possibly helps freeing up memory
     * @param view
     */
    protected void unbindDrawables(View view) {
        if (view==null) {
            return;
        }
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

}
