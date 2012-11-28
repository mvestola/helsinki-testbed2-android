package fi.testbed2.android.task;

import android.app.Activity;
import android.content.Intent;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.UiThread;
import fi.testbed2.R;
import fi.testbed2.android.activity.ParsingActivity;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.android.task.exception.DownloadTaskException;
import fi.testbed2.domain.TestbedMapImage;
import fi.testbed2.domain.TestbedParsedPage;
import roboguice.inject.InjectResource;

/**
 * Task which parses the testbed HTML page and initializes the animation view
 * with the latest map image. Should be called from ParsingActivity.
 */
@EBean
public class ParseAndInitTask extends AbstractTask implements Task {

    @RootContext
    ParsingActivity activity;

    @InjectResource(R.string.progress_parsing)
    String progressParsing;

    @InjectResource(R.string.progress_downloading)
    String progressDownloading;

    @InjectResource(R.string.progress_done)
    String progressDone;

    @Override
    protected Activity getActivity() {
        return activity;
    }

    @Override
    protected String getTaskName() {
        return "ParseAndInitTask";
    }

    @Override
    protected void runOnBackground() throws DownloadTaskException {
        parseAndInit();
    }

    @UiThread
    @Override
    public void onSuccess() {
        super.onSuccess();
        activity.onParsingFinished();
    }

    @UiThread
    public void publishProgress(int progress, String progressText) {
        activity.publishProgress(progress, progressText);
    }

    private void parseAndInit() throws DownloadTaskException {

        publishProgress(0, progressParsing);

        TestbedParsedPage testbedParsedPage =
                pageService.downloadAndParseTestbedPage(settingsService.getTestbedPageURL(), this);

        if (testbedParsedPage == null || isAbort()) {
            onCancel();
            return;
        }

        publishProgress(50, progressDownloading);

        TestbedMapImage mapImage = testbedParsedPage.getLatestTestbedImage();

        if (!bitmapService.bitmapIsDownloaded(mapImage)) {
            bitmapService.downloadBitmap(mapImage);
        }

        publishProgress(100, progressDone);
        onSuccess();

    }


}
