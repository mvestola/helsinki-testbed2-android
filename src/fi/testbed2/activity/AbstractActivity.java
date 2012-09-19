package fi.testbed2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import fi.testbed2.R;

/**
 * Base activity class for all activities.
 * Handles options menu.
 */
public abstract class AbstractActivity extends Activity {

    public static final int ABOUT_DIALOG = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_preferences:
                Intent intent = new Intent(this, MyPreferenceActivity.class);
                startActivity(intent);
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
                TextView messageBoxText = new TextView(this);
                messageBoxText.setTextSize(16);
                messageBoxText.setPadding(10,5,5,5);
                final SpannableString s = new SpannableString(this.getText(R.string.about_text));
                Linkify.addLinks(s, Linkify.WEB_URLS);
                messageBoxText.setText(s);
                messageBoxText.setMovementMethod(LinkMovementMethod.getInstance());

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(messageBoxText)
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
                alertDialog = builder.create();
                alertDialog.setTitle(this.getString(R.string.about_title, this.getVersionName()));
                break;
            default:
                alertDialog = null;
        }

        return alertDialog;
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

}
