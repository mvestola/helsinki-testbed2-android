package fi.testbed2.android.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.inject.Inject;
import fi.testbed2.R;
import fi.testbed2.android.activity.AnimationActivity;
import fi.testbed2.android.app.Logging;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.android.task.exception.DownloadTaskException;
import fi.testbed2.android.task.result.TaskResult;
import fi.testbed2.android.task.result.TaskResultType;
import fi.testbed2.domain.TestbedMapImage;
import fi.testbed2.service.BitmapService;
import fi.testbed2.service.PageService;
import roboguice.inject.InjectResource;

import java.util.List;

/**
 * Task which downloads all map images and reloads the animation view.
 */
public class DownloadImagesTask extends AbstractTask<TaskResult> {

    @InjectResource(R.string.error_msg_parsed_map_image_null)
    String errorMapImageNull;

    @Inject
    BitmapService bitmapService;

    @Inject
    PageService pageService;

    AnimationActivity activity;

    public DownloadImagesTask(Context context) {
        super(context);
    }

    public void setActivity(AnimationActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Activity getActivity() {
        return this.activity;
    }

    @Override
    protected void onSuccess(TaskResult result) {

        Intent intent = new Intent();
        intent.putExtra(TaskResult.MSG_CODE, result.getMessage());

        Logging.debug("DownloadImagesTask succeeded");

        if (result.isCancelled()) {
            Logging.debug("DownloadImagesTask cancelled");
            activity.setResult(Activity.RESULT_CANCELED);
            activity.finish();
        } else {
            Logging.debug("DownloadImagesTask result OK");
            activity.setResult(MainApplication.RESULT_OK, intent);
            activity.onAllImagesDownloaded();
        }
    }

    @Override
    public TaskResult call() throws DownloadTaskException {

        Logging.debug("DownloadImagesTask call()");

        List<TestbedMapImage> testbedMapImages = pageService.getTestbedParsedPage().getAllTestbedImages();
        int totalImagesNotDownloaded = pageService.getNotDownloadedImagesCount();
        int i= 1;

        for(TestbedMapImage image : testbedMapImages) {

            if (image==null) {
                throw new DownloadTaskException(errorMapImageNull);
            }

            if (!bitmapService.bitmapIsDownloaded(image)) {

                String progressText = getContext().getString(R.string.progress_anim_downloading,
                        i, totalImagesNotDownloaded);
                this.activity.updateDownloadProgressInfo(progressText);

                if (isAbort()) {
                    return new TaskResult(TaskResultType.CANCELLED, "Cancelled");
                }
                bitmapService.downloadBitmap(image);

                i++;
            }

        }

        return new TaskResult(TaskResultType.OK, "All images downloaded");

    }

}
