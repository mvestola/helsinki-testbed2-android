package fi.testbed2.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import fi.testbed2.R;
import fi.testbed2.app.MainApplication;
import fi.testbed2.app.Preference;
import fi.testbed2.data.TestbedParsedPage;
import fi.testbed2.task.DownloadImagesTask;
import fi.testbed2.util.CoordinateUtil;
import fi.testbed2.util.SeekBarUtil;
import fi.testbed2.view.AnimationView;

/**
 * TODO: this class has grown quite large. Refactor it to smaller classes
 */
public class AnimationActivity extends AbstractActivity implements OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static int LOCATION_UPDATE_INTERVAL_MINUTES = 1;
    private static int LOCATION_UPDATE_ACCURACY_METERS = 1000;

    private AnimationView animationView;
	private ImageButton playPauseButton;
	private boolean isPlaying = true;
	private TextView timestampView;
    private SeekBar seekBar;

    private DownloadImagesTask task;

    private int orientation;

    private boolean allImagesDownloaded;

    private LocationListener locationListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!compulsoryDataIsAvailable()) {
            returnToMainActivity();
            return;
        }

        // we want more space for the animation
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.animation);

        initButtons();
        initViews();
        initAnimation();

        orientation = this.getResources().getConfiguration().orientation;

    }

    private void initButtons() {

        playPauseButton = (ImageButton) findViewById(R.id.playpause_button);
        playPauseButton.setOnClickListener(this);

        seekBar = (SeekBar)findViewById(R.id.seek);
        seekBar.setOnSeekBarChangeListener(this);

    }

    private void initViews() {
        animationView = (AnimationView) findViewById(R.id.animation_view);
        animationView.setAllImagesDownloaded(false);
        timestampView = (TextView) findViewById(R.id.timestamp_view);
    }

    private void initAnimation() {

        animationView.post(new Runnable() {
            @Override
            public void run() {
                final Rect bounds = getSavedMapBounds();
                final float scale = getSavedScaleFactor();
                updatePlayingState(true);
                if (bounds==null) {
                    animationView.start(timestampView, seekBar, scale);
                } else {
                    animationView.start(timestampView, seekBar, bounds, scale);
                }

            }
        });

    }

    private void initLocationListener() {

        if (MainApplication.showUserLocation()) {

            LocationManager locationManager =
                    (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocation!=null) {
                MainApplication.setUserLocationInMapPixels(CoordinateUtil.convertLocationToTestbedImageXY(lastKnownLocation));
            }

            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    if (location!=null) {
                        MainApplication.setUserLocationInMapPixels(
                                CoordinateUtil.convertLocationToTestbedImageXY(location));
                    }
                }
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                public void onProviderEnabled(String provider) {}
                public void onProviderDisabled(String provider) {}
            };

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    LOCATION_UPDATE_INTERVAL_MINUTES * 60 * 1000, LOCATION_UPDATE_ACCURACY_METERS, locationListener);

        }

    }

    private void removeLocationListener() {
        if (locationListener!=null) {
            LocationManager locationManager =
                    (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.removeUpdates(locationListener);
        }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        saveMapBoundsAndScaleFactor();
        orientation = newConfig.orientation;
        updateBoundsToView();
    }

    private String getMapBoundsPreferenceKey() {
        return Preference.PREF_BOUNDS_PREFERENCE_KEY_PREFIX + orientation;
    }

    private String getMapScalePreferenceKey() {
        return Preference.PREF_SCALE_PREFERENCE_KEY_PREFIX + orientation;
    }

    /**
     * Saves the bounds of the map user has previously viewed to persistent storage.
     */
    private void saveMapBoundsAndScaleFactor() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Editor editor = sharedPreferences.edit();
        Rect bounds = animationView.getBounds();
        float scale = animationView.getScaleFactor();

        if (editor!=null) {
            if (bounds!=null) {
                // bounds String format is left:top:right:bottom
                editor.putString(getMapBoundsPreferenceKey(),
                        "" + bounds.left + ":" + bounds.top + ":" + bounds.right + ":" + bounds.bottom);
            }
            editor.putFloat(getMapScalePreferenceKey(), scale);
            editor.commit();
        }

    }

    /**
     * Returns the saved map bounds user has previously used
     * @return
     */
    private Rect getSavedMapBounds() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // left:top:right:bottom
        String frameBoundsPref = sharedPreferences.getString(getMapBoundsPreferenceKey(), null);

        if (frameBoundsPref==null) {
            return null;
        }

        String[] parts = frameBoundsPref.split(":");
        final Rect bounds = new Rect(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));

        return bounds;

    }

    private float getSavedScaleFactor() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getFloat(getMapScalePreferenceKey(), 1.0f);
    }

    private void updateBoundsToView() {
        animationView.setScaleFactor(getSavedScaleFactor());
        animationView.updateBounds(getSavedMapBounds());
    }

    private void updateFrameDelayToView() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        animationView.setFrameDelay(Integer.parseInt(
                sharedPreferences.getString(Preference.PREF_ANIM_FRAME_DELAY, "1000")));
    }

    private boolean startAnimationAutomatically() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getBoolean(Preference.PREF_ANIM_AUTOSTART, true);
    }

    @Override
	protected void onPause() {
        super.onPause();
        if (task!=null) {
            task.abort();
        }
        this.pauseAnimation();
        this.saveMapBoundsAndScaleFactor();
        this.removeLocationListener();
	}

    @Override
    protected void onResume() {
        super.onResume();

        if (!compulsoryDataIsAvailable()) {
            returnToMainActivity();
            return;
        }

        this.initLocationListener();

        updateBoundsToView();
        updateFrameDelayToView();
        if (!allImagesDownloaded) {
            task = new DownloadImagesTask(this);
            task.execute();
        }
    }

    /**
     * Check that we have all compulsory data before starting the activity.
     * Android system might kill the app after onPause() and when resuming the
     * app after it has been killed, all static data is null.
     *
     * @return
     */
    private boolean compulsoryDataIsAvailable() {

        TestbedParsedPage page = MainApplication.getTestbedParsedPage();

        /*
        * Parsed page should always be non-null.
        */
        if (page==null) {
            return false;
        }

        boolean noImagesExist = page.getAllTestbedImages()==null || page.getAllTestbedImages().isEmpty();
        boolean allImagesDownloadedButSomeAreMissing = allImagesDownloaded && page.getNotDownloadedCount()>0;

        if (noImagesExist || allImagesDownloadedButSomeAreMissing) {
            return false;
        }

        return true;

    }

    private void returnToMainActivity() {
        this.allImagesDownloaded = false;
        MainApplication.clearData();
        Intent intent = new Intent();
        this.setResult(MainApplication.RESULT_OK, intent);
        this.finish();
    }

    public void onAllImagesDownloaded() {
        allImagesDownloaded = true;
        animationView.setAllImagesDownloaded(true);
        animationView.setDownloadProgressText(null);
        animationView.refresh(getApplicationContext());
        animationView.previous();
        updatePlayingState(false);
        if (startAnimationAutomatically()) {
            playAnimation();
        }
    }

    private void playAnimation() {
        updatePlayingState(true);
        animationView.play();
    }

    private void pauseAnimation() {
        updatePlayingState(false);
        animationView.pause();
    }

    @Override
	public void onClick(View v) {
        if (!allImagesDownloaded) {
            return;
        }
		switch(v.getId()) {
            case R.id.playpause_button:
                animationView.playpause();
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
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.AnimationRootView));
    }

    @Override
    public void onRefreshButtonSelected() {
        this.pauseAnimation();
        this.allImagesDownloaded = false;
        Intent intent = new Intent();
        this.setResult(MainApplication.RESULT_REFRESH, intent);
        this.finish();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            this.pauseAnimation();
            animationView.goToFrame(SeekBarUtil.getFrameIndexFromSeekBarValue(progress,
                    MainApplication.getTestbedParsedPage().getAllTestbedImages().size()));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        this.pauseAnimation();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Don't do anything
    }
}
