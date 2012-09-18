package fi.testbed2.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
        Dialog dialog;
        switch(id) {
            case ABOUT_DIALOG:
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.about);
                dialog.setTitle(this.getString(R.string.about_title, this.getVersionName()));
                TextView text = (TextView) dialog.findViewById(R.id.about_text);
                text.setText(R.string.about_text);
                break;
            default:
                dialog = null;
        }

        return dialog;
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
