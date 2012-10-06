package fi.testbed2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.*;
import android.widget.TextView;
import fi.testbed2.R;
import fi.testbed2.app.MainApplication;

/**
 * Base activity class for all activities.
 * Handles options menu, for instance.
 */
public abstract class AbstractActivity extends Activity {

    public static final int ABOUT_DIALOG = 0;
    public static final int WHATS_NEW_DIALOG = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWhatsNewDialogIfNecessary();
    }

    private void showWhatsNewDialogIfNecessary() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String dialogShownForVersion = sharedPreferences.
                getString(MainApplication.PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION, "");
        if (!dialogShownForVersion.equals(getVersionName())) {
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
                alertDialog = getAboutAlertDialog();
                break;
            case WHATS_NEW_DIALOG:
                alertDialog = getWhatsNewAlertDialog();
                break;
            default:
                alertDialog = null;
        }

        return alertDialog;
    }

    private AlertDialog getWhatsNewAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getWhatsNewDialogContents())
                .setPositiveButton(this.getText(R.string.close_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(MainApplication.PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION, getVersionName());
                        editor.commit();
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(this.getString(R.string.whats_new_title, this.getVersionName()));
        return  alertDialog;
    }

    private TextView getWhatsNewDialogContents() {

        TextView messageBoxText = new TextView(this);
        messageBoxText.setTextSize(18);
        messageBoxText.setPadding(10,5,5,5);
        final SpannableString s = new SpannableString(" "+this.getText(R.string.whats_new_text));
        messageBoxText.setText(s);
        messageBoxText.setMovementMethod(LinkMovementMethod.getInstance());

        return messageBoxText;

    }

    private AlertDialog getAboutAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getAboutDialogContents())
                .setPositiveButton(this.getText(R.string.about_visit_website), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Open app homepage
                        Uri url = Uri.parse(getString(R.string.app_homepage));
                        Intent intent = new Intent(Intent.ACTION_VIEW, url);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(this.getText(R.string.close_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(this.getString(R.string.about_title, this.getVersionName()));
        return  alertDialog;
    }

    private TextView getAboutDialogContents() {

        TextView messageBoxText = new TextView(this);
        messageBoxText.setTextSize(16);
        messageBoxText.setPadding(10,5,5,5);
        final SpannableString s1 = new SpannableString(this.getText(R.string.about_text));
        final SpannableString s2 = new SpannableString(this.getText(R.string.extra_license_text));
        Linkify.addLinks(s1, Linkify.WEB_URLS);
        Linkify.addLinks(s2, Linkify.WEB_URLS);
        messageBoxText.append(s1);
        messageBoxText.append(s2);
        messageBoxText.setMovementMethod(LinkMovementMethod.getInstance());

        return messageBoxText;

    }

    /**
     * Returns the version name (number), e.g. 2.0.3
     * @return
     */
    private String getVersionName() {
        String versionName;
        try {
            versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "Unknown";
        }
        return versionName;
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
