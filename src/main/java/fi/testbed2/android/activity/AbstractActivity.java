package fi.testbed2.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import fi.testbed2.R;
import fi.testbed2.android.ui.ads.AdManager;
import fi.testbed2.android.ui.dialog.DialogBuilder;
import fi.testbed2.service.SettingsService;


/**
 * Base activity class for all activities.
 * Handles options menu, for instance.
 */
@EActivity
public abstract class AbstractActivity extends AppCompatActivity {

    @Inject
    DialogBuilder dialogBuilder;

    @Inject
    SettingsService settingsService;

    @Inject
    AdManager adManager;

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.adView)
    AdView adView;

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

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

        AdView adView = this.findViewById(R.id.adView);

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
        AdRequest adRequest = adManager.getAdRequest();
        if (adRequest != null) {
            adView.loadAd(adRequest);
        }
    }

    @AfterViews
    void bindActionBar() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.toolbar_logo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public abstract void onRefreshFromMenuSelected();

    @SuppressLint("NonConstantResourceId")
    @OptionsItem(R.id.main_menu_refresh)
    public void onRefreshMenuItemSelected() {
        onRefreshFromMenuSelected();
    }

    @SuppressLint("NonConstantResourceId")
    @OptionsItem(R.id.main_menu_preferences)
    public void onPreferencesMenuItemSelected() {
        startActivity(new Intent(this, TestbedPreferenceActivity_.class));
    }

    @SuppressLint("NonConstantResourceId")
    @OptionsItem(R.id.main_menu_about)
    public void onAboutMenuItemSelected() {
        dialogBuilder.getAboutDialog().show();
    }

    protected void showErrorDialog(String errorMsg) {
        dialogBuilder.getErrorDialog(errorMsg).show();
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
