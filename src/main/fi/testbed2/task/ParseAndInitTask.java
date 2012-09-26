package fi.testbed2.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import fi.testbed2.R;
import fi.testbed2.app.MainApplication;
import fi.testbed2.data.TestbedMapImage;
import fi.testbed2.data.TestbedParsedPage;
import fi.testbed2.exception.DownloadTaskException;
import fi.testbed2.result.ParseAndInitTaskResult;
import fi.testbed2.result.TaskResultType;
import fi.testbed2.util.HTMLUtil;

/**
 * Task which parses the testbed HTML page and initializes the animation view
 * with the latest map image.
 */
public class ParseAndInitTask extends AbstractTask<ParseAndInitTaskResult> {

    private String url;

	public ParseAndInitTask(final Context context, final Activity activity) {
        super(activity);
        initURL(context);
	}

    private void initURL(final Context context) {
        // construct URL where to download content
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mapType = sharedPreferences.getString(MainApplication.PREF_MAP_TYPE, "radar");
        String mapTimeStep = sharedPreferences.getString(MainApplication.PREF_MAP_TIME_STEP, "5");
        String mapNumberOfImages = sharedPreferences.getString(MainApplication.PREF_MAP_NUMBER_OF_IMAGES, "10");
        url = activity.getString(R.string.testbed_base_url, mapType, mapTimeStep, mapNumberOfImages);
    }

    @Override
    protected void saveResultToApplication(ParseAndInitTaskResult result) {
        MainApplication.setTestbedParsedPage(result.getTestbedParsedPage());
    }

    private void clearPreviousOldData(TestbedParsedPage oldPage, TestbedParsedPage newPage) {

        if (oldPage==null) {
            return;
        }

        for (TestbedMapImage mapImg : oldPage.getAllTestbedImages()) {

            if (mapImg!=null && !newPage.getAllTestbedImages().contains(mapImg)) {
                MainApplication.deleteBitmapCacheEntry(mapImg.getBitmapCacheKey());
            }

        }

    }

    @Override
	protected ParseAndInitTaskResult doInBackground(Void... params) {

        try {
            ParseAndInitTaskResult result = new ParseAndInitTaskResult(TaskResultType.OK, "Parsing and initialization OK");

            publishProgress(new DownloadTaskProgress(0, 0, true, activity.getString(R.string.progress_parsing)));
            TestbedParsedPage testbedParsedPage = HTMLUtil.parseTestbedPage(url, this);
            result.setTestbedParsedPage(testbedParsedPage);

            if (testbedParsedPage ==null || isAbort()) {
                doCancel();
                return null;
            }

            clearPreviousOldData(MainApplication.getTestbedParsedPage(), result.getTestbedParsedPage());

            publishProgress(new DownloadTaskProgress(50, 0, false, activity.getString(R.string.progress_downloading)));
            testbedParsedPage.getLatestTestbedImage().downloadAndCacheImage();
            publishProgress(new DownloadTaskProgress(100, 0, false, activity.getString(R.string.progress_done)));

            return result;
        } catch(DownloadTaskException e) {
            return new ParseAndInitTaskResult(TaskResultType.ERROR, e.getMessage());
        }

    }

    @Override
    protected void onSuccessTaskEnd() {
        activity.finish();
    }

    @Override
    protected void onErrorTaskEnd() {
        activity.finish();
    }

}
