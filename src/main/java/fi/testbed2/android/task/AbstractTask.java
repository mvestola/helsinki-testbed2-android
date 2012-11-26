package fi.testbed2.android.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import fi.testbed2.android.app.Logging;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.android.task.result.TaskResult;
import roboguice.activity.event.OnDestroyEvent;
import roboguice.event.Observes;
import roboguice.inject.InjectorProvider;
import roboguice.util.RoboAsyncTask;

public abstract class AbstractTask<T extends TaskResult> extends RoboAsyncTask<T> implements Task {

    private boolean abort;
    private Context context;

    protected AbstractTask(Context context) {
        super();
        this.context = context;
        ((InjectorProvider)context).getInjector().injectMembers(this);
    }

    public void abort() {
        this.abort = true;
    }

    public boolean isAbort() {
        return abort;
    }

    protected abstract Activity getActivity();

    @Override
    protected void onException(Exception e) {
        e.printStackTrace();
        Intent intent = new Intent();
        intent.putExtra(TaskResult.MSG_CODE, e.getMessage());
        getActivity().setResult(MainApplication.RESULT_ERROR, intent);
        getActivity().finish();
    }

    @Override
    protected void onInterrupted(Exception e) {
        Logging.debug("Interrupting background task");
    }

    protected void onActivityDestroy(@Observes OnDestroyEvent ignored ) {
        Logging.debug("Killing background task");
        kill();
    }

    protected Context getContext() {
        return context;
    }

    public void kill() {
        abort();
        cancel(true);
    }

}
