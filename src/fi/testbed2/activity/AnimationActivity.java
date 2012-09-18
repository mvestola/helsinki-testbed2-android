package fi.testbed2.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import fi.testbed2.task.DownloadImagesTask;
import fi.testbed2.view.AnimationView;
import fi.testbed2.R;

public class AnimationActivity extends AbstractActivity implements OnClickListener {

	private AnimationView animationView;
	private ImageButton playpauseButton;
	private boolean isPlaying = true;
	private TextView timestampView;

    private DownloadImagesTask task;

    private int orientation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          
        // we want more space for the animation
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.animation);
        
        final ImageButton previousButton = (ImageButton) findViewById(R.id.previous_button);
        previousButton.setOnClickListener(this);
        
        playpauseButton = (ImageButton) findViewById(R.id.playpause_button);
        playpauseButton.setOnClickListener(this);
        
        final ImageButton forwardButton = (ImageButton) findViewById(R.id.forward_button);
        forwardButton.setOnClickListener(this);

        animationView = (AnimationView) findViewById(R.id.animation_view);
        timestampView = (TextView) findViewById(R.id.timestamp_view);

        orientation = this.getResources().getConfiguration().orientation;

    }

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
        saveMapPosition();
        orientation = newConfig.orientation;
        startAnimation();
    }

    /**
     * Saves the position of the map user has selected to persistent storage.
     */
    private void saveMapPosition() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Editor editor = sharedPreferences.edit();
        Rect bounds = animationView.getBounds();

        // bounds String format is left:top:right:bottom
        if (editor!=null && bounds!=null) {
            editor.putString(getMapPositionKey(),
                    "" + bounds.left + ":" + bounds.top + ":" + bounds.right + ":" + bounds.bottom);
            editor.commit();
        }

    }

    /**
     * Returns the saved map position user has previously used
     * @return
     */
    private Rect getSavedMapPosition() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // left:top:right:bottom
        String frameBoundsPref = sharedPreferences.getString(getMapPositionKey(), null);

        if (frameBoundsPref==null) {
            return null;
        }

        String[] parts = frameBoundsPref.split(":");
        final Rect bounds = new Rect(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));

        return bounds;

    }

    private String getMapPositionKey() {
        return "PREF_ANIM_BOUNDS_ORIENTATION_" + orientation;
    }

    public void reload() {
        animationView.setDownloadProgressText(null);
        this.animationView.refresh(getApplicationContext());
        if (!startAutomatically()) {
            this.animationView.previous();
            this.stopAnimation();
        } else {
            playpauseButton.setImageResource(R.drawable.ic_media_pause);
        }

    }

    private boolean startAutomatically() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getBoolean("PREF_ANIM_AUTOSTART", true);
    }

    private void startAnimation() {

        final Rect bounds = this.getSavedMapPosition();

        animationView.post(new Runnable() {
            @Override
            public void run() {
                playpauseButton.setImageResource(R.drawable.ic_media_pause);
                if (bounds==null) {
                    animationView.start(timestampView);
                } else {
                    animationView.start(timestampView, bounds);
                }
                if (!startAutomatically()) {
                    stopAnimation();
                }
            }
        });

    }

    @Override
	protected void onPause() {
        super.onPause();
        task.abort();
        this.stopAnimation();
        this.saveMapPosition();
	}

    @Override
    protected void onResume() {
        super.onResume();
        task = new DownloadImagesTask(this);
        task.execute();
        this.startAnimation();

    }

    private void stopAnimation() {
        isPlaying = false;
        animationView.stop();
        playpauseButton.setImageResource(R.drawable.ic_media_play);
    }

    @Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.previous_button:
            this.stopAnimation();
			animationView.previous();
			break;
		case R.id.playpause_button:
			isPlaying = !isPlaying;
			animationView.playpause();
			if(isPlaying)
                playpauseButton.setImageResource(R.drawable.ic_media_pause);
			else
                playpauseButton.setImageResource(R.drawable.ic_media_play);
            break;
		case R.id.forward_button:
            this.stopAnimation();
            animationView.forward();
			break;
		default:
			return;
		}
	}

}
