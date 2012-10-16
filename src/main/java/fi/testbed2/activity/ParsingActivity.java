package fi.testbed2.activity;

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

    private ParseAndInitTask task;

    @InjectView(R.id.progressbar)
    ProgressBar progressBar;

    @InjectView(R.id.progresstext)
    TextView progressTextView;

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
		task = new ParseAndInitTask(this);
        task.setActivity(this);
        task.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.DownloadRootView));
        System.gc();
    }

    @Override
    public void onRefreshButtonSelected() {
        if (task!=null) {
            task.kill();
        }
        Intent intent = new Intent();
        this.setResult(MainApplication.RESULT_REFRESH, intent);
        this.finish();
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
