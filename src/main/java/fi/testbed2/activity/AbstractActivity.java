package fi.testbed2.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import com.google.ads.AdView;
import com.google.inject.Inject;
import fi.testbed2.R;
import fi.testbed2.dialog.DialogBuilder;
import fi.testbed2.dialog.DialogType;
import fi.testbed2.service.PreferenceService;
import roboguice.activity.RoboActivity;


/**
 * Base activity class for all activities.
 * Handles options menu, for instance.
 */
public abstract class AbstractActivity extends RoboActivity {

    @Inject
    DialogBuilder dialogBuilder;

    @Inject
    PreferenceService preferenceService;

    private String currentErrorMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (preferenceService.isShowWhatsNewDialog()) {
            showDialog(DialogType.WHATS_NEW);
        }
    }

    /** Called when resuming, BUT after onActivityResult! */
    @Override
    protected void onResume() {
        super.onResume();

        AdView adView = (AdView)this.findViewById(R.id.adView);

        if (adView!=null) {
            if (preferenceService.showAds()) {
                adView.setVisibility(AdView.VISIBLE);
            } else {
                adView.setVisibility(AdView.GONE);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        onSuperCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Used for sub-classes for overriding the onCreateOptionsMenu
     * which needs to call the super's method (can't call just
     * super.onCreateOptionsMenu() because it will create duplicate
     * menu items).
     * @param menu
     * @return
     */
    protected boolean onSuperCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public abstract void onRefreshButtonSelected();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_refresh:
                onRefreshButtonSelected();
                return true;
            case R.id.main_menu_preferences:
                startActivity(new Intent(this, TestbedPreferenceActivity.class));
                return true;
            case R.id.main_menu_about:
                showDialog(DialogType.ABOUT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void showErrorDialog(String errorMsg) {
        currentErrorMsg = errorMsg;
        showDialog(DialogType.ERROR);
    }

    protected void showDialog(DialogType type) {
        showDialog(type.ordinal());
    }

    protected void showShortMessage(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog alertDialog;

        DialogType dialogType = DialogType.getById(id);

        switch(dialogType) {
            case ABOUT:
                alertDialog = dialogBuilder.getAboutAlertDialog();
                break;
            case WHATS_NEW:
                alertDialog = dialogBuilder.getWhatsNewAlertDialog();
                break;
            case ERROR:
                alertDialog = dialogBuilder.getErrorDialog(currentErrorMsg);
                break;
            default:
                alertDialog = null;
        }

        return alertDialog;
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
