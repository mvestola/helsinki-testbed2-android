package fi.testbed2.task;

import android.content.Context;
import fi.testbed2.result.TaskResult;
import roboguice.activity.event.OnDestroyEvent;
import roboguice.event.Observes;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;

public abstract class AbstractTask<T extends TaskResult> extends RoboAsyncTask<T> implements Task {

    private boolean abort;

    protected AbstractTask(Context context) {
        super(context);
    }

    public void abort() {
        this.abort = true;
    }

    public boolean isAbort() {
        return abort;
    }

    @Override
    protected void onInterrupted(Exception e) {
        Ln.d("Interrupting background task %s", this);
    }

    protected void onActivityDestroy(@Observes OnDestroyEvent ignored ) {
        Ln.d("Killing background thread %s", this);
        kill();
    }

    public void kill() {
        abort();
        cancel(true);
    }


/*    @Override
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
            this.onSuccessTaskEnd();
        }
		else {
			Log.e(MainApplication.LOG_IDENTIFIER, result.getMessage());
			activity.setResult(MainApplication.RESULT_ERROR, intent);
            this.onErrorTaskEnd();
		}

	}
*/

}
