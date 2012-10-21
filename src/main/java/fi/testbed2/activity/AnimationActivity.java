package fi.testbed2.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.inject.Inject;
import fi.testbed2.R;
import fi.testbed2.app.MainApplication;
import fi.testbed2.data.TestbedParsedPage;
import fi.testbed2.service.*;
import fi.testbed2.task.DownloadImagesTask;
import fi.testbed2.util.SeekBarUtil;
import fi.testbed2.view.AnimationView;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Activity handling the main map animation view.
 */
@ContentView(R.layout.animation)
public class AnimationActivity extends AbstractActivity implements OnClickListener, SeekBar.OnSeekBarChangeListener {

    @Inject
    MunicipalityService municipalityService;

    @Inject
    LocationService locationService;

    @Inject
    CoordinateService coordinateService;

    @Inject
    PreferenceService preferenceService;

    @Inject
    BitmapService bitmapService;

    @Inject
    PageService pageService;

    @InjectView(R.id.animation_view)
    AnimationView animationView;

    @InjectView(R.id.playpause_button)
    ImageButton playPauseButton;

    @InjectView(R.id.timestamp_view)
    TextView timestampView;

    @InjectView(R.id.seek)
    SeekBar seekBar;

    boolean isPlaying = true;
    private int orientation;
    private boolean allImagesDownloaded;

    private DownloadImagesTask task;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!compulsoryDataIsAvailable()) {
            reloadAllAndReturnToMainActivity();
            return;
        }

        orientation = getResources().getConfiguration().orientation;

        initListeners();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!compulsoryDataIsAvailable()) {
            reloadAllAndReturnToMainActivity();
            return;
        }

        if (preferenceService.showUserLocation()) {
            locationService.startListeningLocationChanges();
        }

        updatePreferencesToView();

        if (!allImagesDownloaded) {
            task = new DownloadImagesTask(this);
            task.setActivity(this);
            task.execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (task!=null) {
            task.kill();
        }
        pauseAnimation();
        saveMapBoundsAndScaleFactor();
        locationService.stopListeningLocationChanges();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.AnimationRootView));
    }

    @Override
    public void onRefreshButtonSelected() {
        pauseAnimation();
        allImagesDownloaded = false;
        Intent intent = new Intent();
        setResult(MainApplication.RESULT_REFRESH, intent);
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        saveMapBoundsAndScaleFactor();
        orientation = newConfig.orientation;
        updatePreferencesToView();
    }

    /**
     * Updates the Downloading text in top left corner
     * @param text
     */
    public void updateDownloadProgressInfo(final String text) {
        runOnUiThread(new Runnable() {
            public void run() {
                animationView.setDownloadProgressText(text);
                timestampView.invalidate();
            }
        });
    }

    public void onAllImagesDownloaded() {
        allImagesDownloaded = true;
        animationView.setAllImagesDownloaded(true);
        animationView.setDownloadProgressText(null);
        animationView.initView(getApplicationContext());
        animationView.getPlayer().previous();
        updatePlayingState(false);
        if (preferenceService.isStartAnimationAutomatically()) {
            playAnimation();
        }
    }

    private void initListeners() {
        playPauseButton.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    private void initView() {
        animationView.setAllImagesDownloaded(false);
        animationView.municipalities = preferenceService.getSavedMunicipalities();
        animationView.userLocationService = locationService;
        animationView.bitmapService = bitmapService;
        animationView.pageService = pageService;
        animationView.preferenceService = preferenceService;
        animationView.initView(getApplicationContext());
        initAnimation();
    }

    private void initAnimation() {
        animationView.post(new Runnable() {
            @Override
            public void run() {
                final Rect bounds = preferenceService.getSavedMapBounds(orientation);
                final float scale = preferenceService.getSavedScaleFactor(orientation);
                updatePlayingState(true);
                animationView.startAnimation(timestampView, seekBar, bounds, scale);
            }
        });
    }

    private void saveMapBoundsAndScaleFactor() {
        preferenceService.saveMapBoundsAndScaleFactor(animationView.getBounds(),
                animationView.getScaleFactor(), orientation);
    }

    private void updatePreferencesToView() {
        animationView.setScaleFactor(preferenceService.getSavedScaleFactor(orientation));
        animationView.updateBounds(preferenceService.getSavedMapBounds(orientation));
        animationView.municipalities = preferenceService.getSavedMunicipalities();
        animationView.getPlayer().setFrameDelay(preferenceService.getSavedFrameDelay());
        animationView.resetMarkerImageCache();
    }

    /**
     * Check that we have all compulsory data before starting the activity.
     * Android system might kill the app after onPause() and when resuming the
     * app after it has been killed, all static data is null.
     *
     * @return
     */
    private boolean compulsoryDataIsAvailable() {

        TestbedParsedPage page = pageService.getTestbedParsedPage();

        /*
        * Parsed page should always be non-null.
        */
        if (page==null) {
            return false;
        }

        boolean noImagesExist = page.getAllTestbedImages()==null || page.getAllTestbedImages().isEmpty();
        boolean allImagesDownloadedButSomeAreMissing = allImagesDownloaded && pageService.getNotDownloadedImagesCount()>0;

        if (noImagesExist || allImagesDownloadedButSomeAreMissing) {
            return false;
        }

        return true;

    }

    private void reloadAllAndReturnToMainActivity() {
        allImagesDownloaded = false;
        pageService.evictPage();
        bitmapService.evictAll();
        System.gc();
        Intent intent = new Intent();
        setResult(MainApplication.RESULT_OK, intent);
        finish();
    }


    private void playAnimation() {
        updatePlayingState(true);
        animationView.getPlayer().play();
    }

    private void pauseAnimation() {
        updatePlayingState(false);
        animationView.getPlayer().pause();
    }

    @Override
	public void onClick(View v) {

        if (!allImagesDownloaded) {
            return;
        }

		switch(v.getId()) {
            case R.id.playpause_button:
                animationView.getPlayer().playOrPause();
                updatePlayingState(!isPlaying);
                break;
            default:
                return;
		}

	}

    private void updatePlayingState(boolean animationIsPlaying) {

        isPlaying = animationIsPlaying;

        if (isPlaying) {
            playPauseButton.setImageResource(R.drawable.ic_media_pause);
        } else {
            playPauseButton.setImageResource(R.drawable.ic_media_play);
        }

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            pauseAnimation();
            int totalFrames = pageService.getTestbedParsedPage().getAllTestbedImages().size();
            animationView.getPlayer().goToFrame(SeekBarUtil.
                    getFrameIndexFromSeekBarValue(progress, totalFrames));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        pauseAnimation();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Don't do anything
    }

}
