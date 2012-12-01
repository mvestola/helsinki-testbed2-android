package fi.testbed2.android.task;

import android.app.Activity;
import fi.testbed2.R;
import fi.testbed2.android.activity.ParsingActivity;
import fi.testbed2.android.task.exception.DownloadTaskException;
import fi.testbed2.android.task.exception.TaskCancelledException;
import fi.testbed2.domain.TestbedMapImage;
import fi.testbed2.domain.TestbedParsedPage;
import roboguice.inject.InjectResource;

/**
 * Task which parses the testbed HTML page and initializes the animation view
 * with the latest map image. Should be called from ParsingActivity.
 */
public class ParseAndInitTask extends AbstractTask implements Task {

    private ParsingActivity activity;

    @InjectResource(R.string.progress_parsing)
    String progressParsing;

    @InjectResource(R.string.progress_downloading)
    String progressDownloading;

    @InjectResource(R.string.progress_done)
    String progressDone;

    public ParseAndInitTask(ParsingActivity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected Activity getActivity() {
        return activity;
    }

    @Override
    protected String getTaskName() {
        return "ParseAndInitTask";
    }

    @Override
    protected void runOnBackground() throws DownloadTaskException,
            TaskCancelledException {
        parseAndInit();
    }

    @Override
    public void doOnSuccess() {
        activity.onParsingFinished();
    }

    private void parseAndInit() throws DownloadTaskException,
            TaskCancelledException {

        activity.publishProgress(0, progressParsing);

        TestbedParsedPage testbedParsedPage =
                pageService.downloadAndParseTestbedPage(settingsService.getTestbedPageURL(), this);

        if (testbedParsedPage == null) {

            if (isCancelled()) {
                throw new TaskCancelledException();
            }

        }

        activity.publishProgress(50, progressDownloading);

        TestbedMapImage mapImage = testbedParsedPage.getLatestTestbedImage();

        if (!bitmapService.bitmapIsDownloaded(mapImage)) {
            bitmapService.downloadBitmap(mapImage);
        }

        activity.publishProgress(100, progressDone);

    }


}
