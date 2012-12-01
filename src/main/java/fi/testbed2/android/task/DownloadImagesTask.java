package fi.testbed2.android.task;

import android.app.Activity;
import fi.testbed2.R;
import fi.testbed2.android.activity.AnimationActivity;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.android.task.exception.DownloadTaskException;
import fi.testbed2.android.task.exception.TaskCancelledException;
import fi.testbed2.domain.TestbedMapImage;
import roboguice.inject.InjectResource;

import java.util.List;

/**
 * Task which downloads all map images and reloads the animation view.
 */
public class DownloadImagesTask extends AbstractTask {

    private AnimationActivity activity;

    @InjectResource(R.string.error_msg_parsed_map_image_null)
    String errorMapImageNull;

    public DownloadImagesTask(AnimationActivity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected Activity getActivity() {
        return activity;
    }

    @Override
    protected String getTaskName() {
        return "DownloadImagesTask";
    }

    @Override
    protected void runOnBackground() throws DownloadTaskException,
            TaskCancelledException {
        downloadImages();
    }

    @Override
    public void doOnSuccess() {
        activity.setResult(MainApplication.RESULT_OK);
        activity.onAllImagesDownloaded();
    }

    public void updateDownloadProgress(int currentImageIndex, int totalImagesNotDownloaded) {
        String progressText = activity.getString(R.string.progress_anim_downloading,
                currentImageIndex, totalImagesNotDownloaded);
        activity.updateDownloadProgressInfo(progressText);
    }

    private void downloadImages() throws DownloadTaskException,
            TaskCancelledException {

        Logger.debug("DownloadImagesTask downloadImages()");

        List<TestbedMapImage> testbedMapImages = pageService.getTestbedParsedPage().getAllTestbedImages();
        int totalImagesNotDownloaded = pageService.getNotDownloadedImagesCount();
        int i= 1;

        for(TestbedMapImage image : testbedMapImages) {

            if (image==null) {
                throw new DownloadTaskException(errorMapImageNull);
            }

            if (!bitmapService.bitmapIsDownloaded(image)) {

                updateDownloadProgress(i, totalImagesNotDownloaded);

                if (isCancelled()) {
                    throw new TaskCancelledException();
                }

                bitmapService.downloadBitmap(image);

                i++;
            }

        }

    }

}
