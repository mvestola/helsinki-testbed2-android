package fi.testbed2.task;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import fi.testbed2.app.MainApplication;
import fi.testbed2.R;
import fi.testbed2.result.AbstractTaskResult;

public abstract class AbstractTask<T extends AbstractTaskResult> extends AsyncTask<Void, DownloadTaskProgress, T> {

	private ProgressBar progressBar;
	private TextView progressTextView;
	protected Activity activity;
    private boolean abort;

	public AbstractTask(final Activity activity) {
		progressBar = (ProgressBar) activity.findViewById(R.id.progressbar);
		progressTextView = (TextView) activity.findViewById(R.id.progresstext);
		this.activity = activity;
	}

    public void abort() {
        this.abort = true;
    }

    public boolean isAbort() {
        return abort;
    }

    protected void doCancel() {
        activity.setResult(Activity.RESULT_CANCELED);
        //activity.finish();
        this.cancel(true);
    }

    @Override
	protected void onCancelled() {
        super.onCancelled();
//		activity.setResult(Activity.RESULT_CANCELED);
//		activity.finish();
	}

	@Override
	protected void onPostExecute(T result) {
		super.onPostExecute(result);

        if (isCancelled()) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(AbstractTaskResult.MSG_CODE, result.getMessage());

        if(!result.isError()) {
			Log.i(MainApplication.LOG_IDENTIFIER, result.getMessage());
			saveResultToApplication(result);
			activity.setResult(MainApplication.RESULT_OK, intent);
		}
		else {
			Log.e(MainApplication.LOG_IDENTIFIER, result.getMessage());
			activity.setResult(MainApplication.RESULT_ERROR, intent);
		}
        this.onTaskEnd();
		
	}

    protected abstract void onTaskEnd();

    protected abstract void saveResultToApplication(T result);

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(DownloadTaskProgress... values) {
		super.onProgressUpdate(values);
		DownloadTaskProgress progress = values[0];
			
		progressTextView.setText(progress.message);
		
		if(progress.intermediate != progressBar.isIndeterminate()) {
			progressBar.setIndeterminate(progress.intermediate);
		}

		progressBar.setProgress(progress.m_progress);
		progressBar.setSecondaryProgress(progress.s_progress);
	}

}
