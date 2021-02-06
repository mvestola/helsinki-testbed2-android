package fi.testbed2.android.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.roboguice.annotations.RoboGuice;

import fi.testbed2.R;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.android.task.ParseAndInitTask;

@SuppressLint("NonConstantResourceId")
@EActivity(R.layout.download)
@OptionsMenu(R.menu.main_menu)
@RoboGuice
public class ParsingActivity extends AbstractActivity {

    public static final int ANIMATION_SUB_ACTIVITY = 2;

    private ParseAndInitTask task;

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.progressbar)
    ProgressBar progressBar;

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.progresstext)
    TextView progressTextView;

    private boolean parsingFinished;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        task.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (parsingFinished) {
            // When back button from ready AnimationActivity is pressed
            this.setResult(MainApplication.RESULT_OK, new Intent());
            finish();
        } else {
            startParsingTask();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.DownloadRootView));
        System.gc();
    }

    private void startParsingTask() {
        parsingFinished = false;
        if (task!=null) {
            task.cancel();
        }
        task = new ParseAndInitTask(this);
        task.execute();
    }

    public void onParsingFinished() {
        parsingFinished = true;
        AnimationActivity_.intent(this).startForResult(ANIMATION_SUB_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ANIMATION_SUB_ACTIVITY) {
            handleAnimationResult(resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleAnimationResult(int resultCode, Intent data) {

        switch(resultCode) {
            case MainApplication.RESULT_REFRESH:
                startParsingTask();
                break;
            case Activity.RESULT_CANCELED:
            case MainApplication.RESULT_ERROR:
                this.setResult(resultCode, data);
                finish();
                break;
            default:
                break;
        }

    }


    @Override
    public void onRefreshFromMenuSelected() {
        startParsingTask();
    }

    @UiThread
    public void publishProgress(final int progress, final String text) {
        progressTextView.setText(text);
        progressBar.setProgress(progress);
    }

}
