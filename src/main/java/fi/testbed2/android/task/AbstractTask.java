package fi.testbed2.android.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.inject.Inject;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.android.task.exception.DownloadTaskException;
import fi.testbed2.android.task.exception.TaskCancelledException;
import fi.testbed2.service.BitmapService;
import fi.testbed2.service.PageService;
import fi.testbed2.service.SettingsService;
import lombok.Getter;
import roboguice.activity.event.OnDestroyEvent;
import roboguice.event.Observes;
import roboguice.inject.InjectorProvider;
import roboguice.util.RoboAsyncTask;

public abstract class AbstractTask extends RoboAsyncTask<Void> implements Task {

    @Inject
    protected SettingsService settingsService;

    @Inject
    protected BitmapService bitmapService;

    @Inject
    protected PageService pageService;

    @Getter
    protected boolean cancelled;

    protected AbstractTask(Context context) {
        super();

        /**
         * In RoboGuice 1.1, RoboAsyncTask does not do injection although it should,
         * so do it manually. See more:
         * http://code.google.com/p/roboguice/issues/detail?id=93
         */
        ((InjectorProvider)context).getInjector().injectMembers(this);
    }

    /**
     * Return the activity which started this task
     * @return
     */
    protected abstract Activity getActivity();

    /**
     * Return task name for logging.
     * @return
     */
    protected abstract String getTaskName();

    /**
     * Executes this method on background.
     * @throws DownloadTaskException Thrown e.g. if network connection fails
     * @throws TaskCancelledException Thrown if task is cancelled
     */
    protected abstract void runOnBackground() throws DownloadTaskException,
            TaskCancelledException;

    /**
     * Should be overridden and call super.onSuccess()
     */
    protected abstract void doOnSuccess();

    /**
     * This method is run on background in different thread, NOT in UI thread
     * @return
     * @throws Exception
     */
    @Override
    public Void call() throws Exception {
        Logger.debug(getTaskName()+" execute()");
        runOnBackground();
        return null;
    }

    @Override
    public void cancel() {
        cancelled = true;
        cancel(true);
    }

    public void onException(Exception e) {

        if (e instanceof DownloadTaskException) {
            onError(e.getMessage());
        } else if (e instanceof TaskCancelledException) {
            onCancel();
        } else {
            Logger.debug(getTaskName()+" onException(): "+e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    protected void onSuccess(Void result) {
        Logger.debug(getTaskName()+" onSuccess()");
        doOnSuccess();
    }

    @Override
    protected void onInterrupted(Exception e) {
        Logger.debug(getTaskName()+" onInterrupted()");
    }

    protected void onActivityDestroy(@Observes OnDestroyEvent ignored ) {
        Logger.debug(getTaskName() + " onActivityDestroy()");
        cancel();
    }

    private void onError(String errorMsg) {
        Logger.debug(getTaskName()+" onError()");
        Intent intent = new Intent();
        intent.putExtra(fi.testbed2.android.task.Task.ERROR_MSG_CODE, errorMsg);
        getActivity().setResult(MainApplication.RESULT_ERROR, intent);
        getActivity().finish();
    }

    private void onCancel() {
        Logger.debug(getTaskName()+" onCancel()");
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

}
