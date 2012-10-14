package fi.testbed2.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.inject.Inject;
import fi.testbed2.R;
import fi.testbed2.activity.ParsingActivity;
import fi.testbed2.app.MainApplication;
import fi.testbed2.result.TaskResult;
import fi.testbed2.service.BitmapService;
import fi.testbed2.service.PageService;
import fi.testbed2.service.PreferenceService;
import fi.testbed2.data.TestbedParsedPage;
import fi.testbed2.exception.DownloadTaskException;
import fi.testbed2.result.TaskResultType;
import fi.testbed2.util.HTMLUtil;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

/**
 * Task which parses the testbed HTML page and initializes the animation view
 * with the latest map image.
 */
public class ParseAndInitTask extends AbstractTask<TaskResult> implements Task {

    ParsingActivity activity;

    @Inject
    PreferenceService preferenceService;

    @Inject
    BitmapService bitmapService;

    @Inject
    PageService pageService;

    @InjectResource(R.string.progress_parsing)
    String progressParsing;

    @InjectResource(R.string.progress_downloading)
    String progressDownloading;

    @InjectResource(R.string.progress_done)
    String progressDone;


    public ParseAndInitTask(Context context) {
        super(context);
    }

    public void setActivity(ParsingActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onSuccess(TaskResult result) {

        Intent intent = new Intent();
        intent.putExtra(TaskResult.MSG_CODE, result.getMessage());

        if (result.isCancelled()) {
            activity.setResult(Activity.RESULT_CANCELED);
        } else {
            activity.setResult(MainApplication.RESULT_OK, intent);
        }
        activity.finish();

    }

    @Override
    protected void onException(Exception e) {

        e.printStackTrace();

        Intent intent = new Intent();
        intent.putExtra(TaskResult.MSG_CODE, e.getMessage());
        activity.setResult(MainApplication.RESULT_ERROR, intent);
        activity.finish();
    }

    @Override
    public TaskResult call() throws DownloadTaskException {

        TaskResult result = new TaskResult(TaskResultType.OK, "Parsing and initialization OK");

        activity.publishProgress(0, progressParsing);
        TestbedParsedPage testbedParsedPage = HTMLUtil.parseTestbedPage(preferenceService.getTestbedPageURL(), this);

        if (testbedParsedPage == null || isAbort()) {
            return new TaskResult(TaskResultType.CANCELLED, "Cancelled");
        }

        pageService.setTestbedParsedPage(testbedParsedPage);

        activity.publishProgress(50, progressDownloading);
        bitmapService.downloadBitmap(testbedParsedPage.getLatestTestbedImage());
        activity.publishProgress(100, progressDone);

        return result;

    }

}
