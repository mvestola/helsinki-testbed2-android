package fi.testbed2.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import com.googlecode.androidannotations.annotations.*;
import fi.testbed2.R;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.android.task.Task;

@EActivity(R.layout.main)
@OptionsMenu(R.menu.main_menu)
@RoboGuice
public class MainActivity extends AbstractActivity {

    public static final int PARSING_SUB_ACTIVITY = 1;

    @ViewById(R.id.button_refresh)
    ImageButton refreshButton;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /** Called when resuming, BUT after onActivityResult! */
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Click(R.id.button_refresh)
    public void refreshButtonClicked() {
        startMainParsingActivity();
	}

    private void startMainParsingActivity() {
        Logger.debug("startMainParsingActivity");
        ParsingActivity_.intent(this).startForResult(PARSING_SUB_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PARSING_SUB_ACTIVITY:
                handleParsingResult(resultCode, data);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    private void handleParsingResult(int resultCode, Intent data) {

        switch(resultCode) {
            case Activity.RESULT_CANCELED:
                showShortMessage(this.getString(R.string.notice_cancelled));
                break;
            case MainApplication.RESULT_ERROR:
                String errorMsg = this.getString(R.string.error_message_detailed,
                    data.getStringExtra(Task.ERROR_MSG_CODE));
                showErrorDialog(errorMsg);
                break;
        }

    }

    @Override
    public void onRefreshFromMenuSelected() {
        startMainParsingActivity();
    }

}