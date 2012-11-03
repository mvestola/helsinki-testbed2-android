package fi.testbed2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import fi.testbed2.R;
import fi.testbed2.app.MainApplication;
import fi.testbed2.task.ParseAndInitTask;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.download)
public class ParsingActivity extends AbstractActivity {

    public static final int ANIMATION_SUB_ACTIVITY = 2;

    private ParseAndInitTask task;

    @InjectView(R.id.progressbar)
    ProgressBar progressBar;

    @InjectView(R.id.progresstext)
    TextView progressTextView;

    private boolean parsingFinished;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	protected void onPause() {
        super.onPause();
        task.kill();
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
            task.kill();
        }
        task = new ParseAndInitTask(this);
        task.setActivity(this);
        task.execute();
    }

    public void onParsingFinished() {
        parsingFinished = true;
        startActivityForResult(new Intent(this, AnimationActivity.class), ANIMATION_SUB_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case ANIMATION_SUB_ACTIVITY:
                handleAnimationResult(resultCode, data);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    private void handleAnimationResult(int resultCode, Intent data) {

        switch(resultCode) {
            case MainApplication.RESULT_REFRESH:
                startParsingTask();
                break;
            case Activity.RESULT_CANCELED:
                this.setResult(resultCode, data);
                finish();
                break;
            case MainApplication.RESULT_ERROR:
                this.setResult(resultCode, data);
                finish();
                break;
            default:
                break;
        }

    }


    @Override
    public void onRefreshButtonSelected() {
        startParsingTask();
    }

    public void publishProgress(final int progress, final String text) {
        runOnUiThread(new Runnable() {
            public void run() {
                progressTextView.setText(text);
                progressBar.setProgress(progress);
            }
        });
    }

}
