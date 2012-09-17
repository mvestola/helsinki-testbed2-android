package fi.testbed2.task;

import java.util.*;

import android.view.View;
import android.view.ViewGroup;
import fi.testbed2.activity.AnimationActivity;
import fi.testbed2.app.MainApplication;
import fi.testbed2.data.MapImage;
import fi.testbed2.data.TestbedMapImage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import fi.testbed2.exception.DownloadTaskException;
import fi.testbed2.result.AbstractTaskResult;
import fi.testbed2.result.DownloadImagesTaskResult;
import fi.testbed2.result.TaskResultType;
import fi.testbed2.R;

/**
 * Task which downloads all map images and reloads the animation view.
 */
public class DownloadImagesTask extends AbstractTask<DownloadImagesTaskResult> {

    AnimationActivity activity;

	public DownloadImagesTask(final AnimationActivity activity) {
        super(activity);
        this.activity = activity;
	}

    @Override
    protected void onTaskEnd() {
        activity.reload();
    }

    @Override
	protected DownloadImagesTaskResult doInBackground(Void... params) {

        try {
            return processImages();
        } catch(DownloadTaskException e) {
            return new DownloadImagesTaskResult(TaskResultType.ERROR, e.getMessage());
        }

    }

    private DownloadImagesTaskResult processImages() throws DownloadTaskException {

        List<MapImage> mapImageList = new ArrayList<MapImage>();
        for(TestbedMapImage image : MainApplication.getParsedHTML().getTestbedImages()) {
            if (isAbort()) {
                doCancel();
                return null;
            }
            Bitmap bitmap = image.getDownloadedBitmapImage();
            String localTimestamp = image.getLocalTimestamp();
            MapImage mapImage = new MapImage(localTimestamp, new BitmapDrawable(bitmap));
            mapImageList.add(mapImage);
        }

        DownloadImagesTaskResult result = new DownloadImagesTaskResult(TaskResultType.OK, "All images downloaded");
        result.setMapImages(mapImageList);
        return result;

    }

    @Override
    protected void saveResultToApplication(DownloadImagesTaskResult result) {
        MainApplication.setMapImageList(result.getMapImages());
    }
}
