package fi.testbed2.activity;

import android.content.Intent;
import android.os.Bundle;
import fi.testbed2.R;
import fi.testbed2.app.MainApplication;
import fi.testbed2.task.ParseAndInitTask;


public class ParsingActivity extends AbstractActivity {
    private ParseAndInitTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);
    }

	@Override
	protected void onPause() {
        super.onPause();
        task.abort();
	}

	@Override
	protected void onResume() {
        super.onResume();
		task = new ParseAndInitTask(getApplicationContext(), this);
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
        task.abort();
        Intent intent = new Intent();
        this.setResult(MainApplication.RESULT_REFRESH, intent);
        this.finish();
    }
}
