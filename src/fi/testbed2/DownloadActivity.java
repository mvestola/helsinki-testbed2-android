package fi.testbed2;

import android.app.Activity;
import android.os.Bundle;


public class DownloadActivity extends Activity {
    private DownloadTask dl;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);
    }

	@Override
	protected void onPause() {
		super.onPause();
		dl.abort();
	}

	@Override
	protected void onResume() {
		super.onResume();
		dl = new DownloadTask(getApplicationContext(), this);
		dl.execute();
	}
    
    
}
