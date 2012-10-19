package fi.testbed2.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.inject.Inject;
import fi.testbed2.R;
import fi.testbed2.activity.ParsingActivity;
import fi.testbed2.app.Logging;
import fi.testbed2.app.MainApplication;
import fi.testbed2.data.TestbedMapImage;
import fi.testbed2.data.TestbedParsedPage;
import fi.testbed2.exception.DownloadTaskException;
import fi.testbed2.result.TaskResult;
import fi.testbed2.result.TaskResultType;
import fi.testbed2.service.BitmapService;
import fi.testbed2.service.PageService;
import fi.testbed2.service.PreferenceService;
import roboguice.inject.InjectResource;

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
    protected Activity getActivity() {
        return this.activity;
    }

    @Override
    protected void onSuccess(TaskResult result) {

        Logging.debug("ParseAndInitTask succeeded");

        Intent intent = new Intent();
        intent.putExtra(TaskResult.MSG_CODE, result.getMessage());

        if (result.isCancelled()) {
            Logging.debug("ParseAndInitTask cancelled");
            activity.setResult(Activity.RESULT_CANCELED);
        } else {
            Logging.debug("ParseAndInitTask result OK");
            activity.setResult(MainApplication.RESULT_OK, intent);
        }
        activity.finish();

    }

    @Override
    public TaskResult call() throws DownloadTaskException {

        Logging.debug("ParseAndInitTask call()");

        TaskResult result = new TaskResult(TaskResultType.OK, "Parsing and initialization OK");

        activity.publishProgress(0, progressParsing);
        TestbedParsedPage testbedParsedPage =
                pageService.downloadAndParseTestbedPage(preferenceService.getTestbedPageURL(), this);

        if (testbedParsedPage == null || isAbort()) {
            return new TaskResult(TaskResultType.CANCELLED, "Cancelled");
        }

        activity.publishProgress(50, progressDownloading);

        TestbedMapImage mapImage = testbedParsedPage.getLatestTestbedImage();

        if (!bitmapService.bitmapIsDownloaded(mapImage)) {
            bitmapService.downloadBitmap(mapImage);
        }

        activity.publishProgress(100, progressDone);

        return result;

    }

}
