package fi.testbed2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.*;
import fi.testbed2.R;
import fi.testbed2.app.MainApplication;
import fi.testbed2.app.Preference;
import fi.testbed2.dialog.DefaultDialogBuilder;
import fi.testbed2.dialog.DialogBuilder;

/**
 * Base activity class for all activities.
 * Handles options menu, for instance.
 */
public abstract class AbstractActivity extends Activity {

    public static final int ABOUT_DIALOG = 0;
    public static final int WHATS_NEW_DIALOG = 1;

    private DialogBuilder dialogBuilder = new DefaultDialogBuilder();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWhatsNewDialogIfNecessary();
    }

    private void showWhatsNewDialogIfNecessary() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String dialogShownForVersion = sharedPreferences.
                getString(Preference.PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION, "");
        if (!dialogShownForVersion.equals(MainApplication.getVersionName())) {
            showDialog(WHATS_NEW_DIALOG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public abstract void onRefreshButtonSelected();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_refresh:
                onRefreshButtonSelected();
                return true;
            case R.id.main_menu_preferences:
                startActivity(new Intent(this, MyPreferenceActivity.class));
                return true;
            case R.id.main_menu_about:
                showDialog(ABOUT_DIALOG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog alertDialog;
        switch(id) {
            case ABOUT_DIALOG:
                alertDialog = dialogBuilder.getAboutAlertDialog(this);
                break;
            case WHATS_NEW_DIALOG:
                alertDialog = dialogBuilder.getWhatsNewAlertDialog(this);
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
