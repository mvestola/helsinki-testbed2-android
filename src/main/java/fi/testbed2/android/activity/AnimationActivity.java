package fi.testbed2.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.inject.Inject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.SeekBarTouchStart;
import org.androidannotations.annotations.SeekBarTouchStop;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.roboguice.annotations.RoboGuice;

import java.lang.reflect.Method;

import fi.testbed2.R;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.android.task.DownloadImagesTask;
import fi.testbed2.android.ui.view.AnimationView;
import fi.testbed2.android.ui.view.MapScaleInfo;
import fi.testbed2.domain.TestbedParsedPage;
import fi.testbed2.service.BitmapService;
import fi.testbed2.service.LocationService;
import fi.testbed2.service.PageService;
import fi.testbed2.util.SeekBarUtil;

/**
 * Activity handling the main map animation view.
 */
@SuppressLint("NonConstantResourceId")
@EActivity(R.layout.animation)
@OptionsMenu(R.menu.animation_menu)
@RoboGuice
public class AnimationActivity extends AbstractActivity {

    @Inject
    LocationService locationService;

    @Inject
    BitmapService bitmapService;

    @Inject
    PageService pageService;

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.animation_view)
    AnimationView animationView;

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.playpause_button)
    ImageButton playPauseButton;

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.timestamp_view)
    TextView timestampView;

    @SuppressLint("NonConstantResourceId")
    @ViewById(R.id.seek)
    SeekBar seekBar;

    boolean isPlaying = true;
    private int orientation;
    private boolean allImagesDownloaded;

    private DownloadImagesTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (compulsoryDataIsNotAvailable()) {
            reloadAllAndReturnToMainActivity();
            return;
        }

        orientation = getResources().getConfiguration().orientation;

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (compulsoryDataIsNotAvailable()) {
            reloadAllAndReturnToMainActivity();
            return;
        }

        if (settingsService.showUserLocation()) {
            locationService.startListeningLocationChanges();
        }

        updateSettingsToView();

        if (!allImagesDownloaded) {
            task = new DownloadImagesTask(this);
            task.execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (task != null) {
            task.cancel();
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
    public void onRefreshFromMenuSelected() {
        pauseAnimation();
        allImagesDownloaded = false;
        Intent intent = new Intent();
        setResult(MainApplication.RESULT_REFRESH, intent);
        finish();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        saveMapBoundsAndScaleFactor();
        orientation = newConfig.orientation;
        updateSettingsToView();
    }

    /**
     * Updates the Downloading text in top left corner
     *
     * @param text
     */
    @UiThread
    public void updateDownloadProgressInfo(final String text) {
        animationView.setDownloadProgressText(text);
        timestampView.invalidate();
    }

    public void onAllImagesDownloaded() {
        allImagesDownloaded = true;
        animationView.setAllImagesDownloaded(true);
        animationView.setDownloadProgressText(null);
        animationView.initView(getApplicationContext());
        animationView.getPlayer().previous();
        updatePlayingState(false);
        if (settingsService.isStartAnimationAutomatically()) {
            playAnimation();
        }
        showHardwareAccelerationWarningIfNeeded();
    }

    @AfterViews
    protected void initView() {

        if (compulsoryDataIsNotAvailable()) {
            reloadAllAndReturnToMainActivity();
            return;
        }

        animationView.setAllImagesDownloaded(false);
        animationView.setMunicipalities(settingsService.getSavedMunicipalities());
        animationView.initView(getApplicationContext());
        initAnimation();
    }

    private void initAnimation() {
        animationView.post(new Runnable() {
            @Override
            public void run() {
                final Rect bounds = settingsService.getSavedMapBounds(orientation);
                final MapScaleInfo scaleInfo = settingsService.getSavedScaleInfo(orientation);
                updatePlayingState(true);
                animationView.startAnimation(timestampView, seekBar, bounds, scaleInfo);
            }
        });
    }

    private void saveMapBoundsAndScaleFactor() {
        settingsService.saveMapBoundsAndScaleFactor(animationView.getBoundsUtil().getBounds(),
                animationView.getScaleInfo(), orientation);
    }

    private void updateSettingsToView() {
        setTitle(this.getResources().getIdentifier("map_type_" + settingsService.getMapType(), "string", this.getPackageName()));
        animationView.setScaleInfo(settingsService.getSavedScaleInfo(orientation));
        animationView.updateBounds(settingsService.getSavedMapBounds(orientation));
        animationView.setMunicipalities(settingsService.getSavedMunicipalities());
        animationView.getPlayer().setFrameDelay(settingsService.getSavedFrameDelay());
        animationView.getCanvasUtil().resetMarkerAndPointImageCache();
    }

    /**
     * Check that we have all compulsory data before starting the activity.
     * Android system might kill the app after onPause() and when resuming the
     * app after it has been killed, all static data is null.
     *
     * @return
     */
    private boolean compulsoryDataIsNotAvailable() {

        TestbedParsedPage page = pageService.getTestbedParsedPage();

        /*
         * Parsed page should always be non-null.
         */
        if (page == null) {
            return true;
        }

        boolean noImagesExist = page.getAllTestbedImages() == null || page.getAllTestbedImages().isEmpty();
        boolean allImagesDownloadedButSomeAreMissing = allImagesDownloaded && pageService.getNotDownloadedImagesCount() > 0;

        return noImagesExist || allImagesDownloadedButSomeAreMissing;

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

    private void showHardwareAccelerationWarningIfNeeded() {

        try {
            // isHardwareAccelerated exists in API level >=11
            Method m = View.class.getMethod("isHardwareAccelerated");
            boolean isHardwareAccelerated = (Boolean) m.invoke(this.findViewById(android.R.id.content));

            Logger.debug("Hardware acceleration status: " + isHardwareAccelerated);

            if (settingsService.isShowHardwareAccelerationDialog() &&
                    isHardwareAccelerated) {
                dialogBuilder.getHardwareAccelerationDialog().show();
            }

        } catch (Exception e) {
            // Ignore
            Logger.debug("OK. No hardware acceleration because API level < 11");
        }

    }

    private void playAnimation() {
        updatePlayingState(true);
        animationView.getPlayer().play();
    }

    private void pauseAnimation() {
        updatePlayingState(false);
        animationView.getPlayer().pause();
    }


    @SuppressLint("NonConstantResourceId")
    @Click(R.id.playpause_button)
    public void onPlayPauseButtonClick() {

        if (!allImagesDownloaded) {
            return;
        }

        animationView.getPlayer().playOrPause();
        updatePlayingState(!isPlaying);

    }

    private void updatePlayingState(boolean animationIsPlaying) {

        isPlaying = animationIsPlaying;

        if (isPlaying) {
            playPauseButton.setImageResource(R.drawable.ic_media_pause);
        } else {
            playPauseButton.setImageResource(R.drawable.ic_media_play);
        }

    }

    @SuppressLint("NonConstantResourceId")
    @OptionsItem(R.id.main_menu_reset_zoom)
    public void onResetZoomSelected() {
        animationView.setScaleInfo(new MapScaleInfo());
        animationView.updateBounds(null);
        animationView.invalidate();
    }

    @SuppressLint("NonConstantResourceId")
    @SeekBarProgressChange(R.id.seek)
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            pauseAnimation();
            int totalFrames = pageService.getTestbedParsedPage().getAllTestbedImages().size();
            animationView.getPlayer().goToFrame(SeekBarUtil.
                    getFrameIndexFromSeekBarValue(progress, totalFrames));
        }
    }

    @SuppressLint("NonConstantResourceId")
    @SeekBarTouchStart(R.id.seek)
    public void onStartTrackingTouch(SeekBar seekBar) {
        pauseAnimation();
    }

    @SuppressWarnings("EmptyMethod")
    @SuppressLint("NonConstantResourceId")
    @SeekBarTouchStop(R.id.seek)
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Don't do anything
    }

}
