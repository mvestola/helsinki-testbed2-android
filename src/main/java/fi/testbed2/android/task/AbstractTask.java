package fi.testbed2.android.task;

import android.app.Activity;
import android.content.Intent;
import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.*;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.android.task.exception.DownloadTaskException;
import fi.testbed2.service.BitmapService;
import fi.testbed2.service.PageService;
import fi.testbed2.service.SettingsService;
import lombok.Getter;
import lombok.Setter;

@EBean
public abstract class AbstractTask implements Task {

    @App
    MainApplication mainApplication;

    @Inject
    protected SettingsService settingsService;

    @Inject
    protected BitmapService bitmapService;

    @Inject
    protected PageService pageService;

    @Getter @Setter
    private boolean abort;

    @Getter @Setter
    private boolean running;

    protected abstract Activity getActivity();
    protected abstract String getTaskName();
    protected abstract void runOnBackground() throws DownloadTaskException;

    @Background
    @Override
    public void execute() {
        Logger.debug(getTaskName()+" execute()");
        running = true;
        abort = false;
        try {
            runOnBackground();
        } catch (DownloadTaskException e) {
            onError(e.getMessage());
        }
    }

    /**
     * Should be overridden and call super.onSuccess()
     */
    protected void onSuccess() {
        Logger.debug(getTaskName()+" onSuccess()");
        running = false;
    }

    @UiThread
    public void onError(String errorMsg) {
        Logger.debug(getTaskName()+" onError()");
        running = false;
        Intent intent = new Intent();
        intent.putExtra(Task.ERROR_MSG_CODE, errorMsg);
        getActivity().setResult(MainApplication.RESULT_ERROR, intent);
        getActivity().finish();
    }

    @UiThread
    public void onCancel() {
        Logger.debug(getTaskName()+" onCancel()");
        running = false;
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    @AfterInject
    public void injectRoboGuice() {
        mainApplication.getInjector().injectMembers(this);
    }

}
