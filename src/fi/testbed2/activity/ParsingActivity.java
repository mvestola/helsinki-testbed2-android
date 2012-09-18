package fi.testbed2.activity;

import android.app.Activity;
import android.os.Bundle;
import fi.testbed2.R;
import fi.testbed2.task.ParseAndInitTask;


public class ParsingActivity extends AbstractActivity {
    private ParseAndInitTask task;

	/** Called when the activity is first created. */
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
    
    
}
