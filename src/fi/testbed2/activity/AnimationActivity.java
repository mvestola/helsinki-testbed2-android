package fi.testbed2.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import fi.testbed2.task.DownloadImagesTask;
import fi.testbed2.view.AnimationView;
import fi.testbed2.R;

public class AnimationActivity extends Activity implements OnClickListener {

	private AnimationView animationView;
	private ImageButton playpauseButton;
	private boolean play = true;
	private TextView timestampView;
	private int orientation;

    private DownloadImagesTask task;

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
        //

    }

    public void reload() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean startAutomatically = sharedPreferences.getBoolean("PREF_ANIM_AUTOSTART", true);

        this.animationView.refresh(getApplicationContext());

        if (!startAutomatically) {
            this.animationView.previous();
            this.animationView.stop();
        }

    }
	
	@Override
	protected void onResume() {
		super.onResume();

        task = new DownloadImagesTask(this);
        task.execute();

        // restore animation bounds if saved before
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();  
		orientation = display.getOrientation();  
		// left:top:right:bottom
		String frameBoundsPref = sharedPreferences.getString("PREF_ANIM_BOUNDS_ORIENTATION_" + orientation, null);

		if(frameBoundsPref == null)
		{
			animationView.post(new Runnable() {
				@Override
				public void run() {
					animationView.start(timestampView);
				}
			});
		}
		else
		{
			String[] parts = frameBoundsPref.split(":");
			final Rect bounds = new Rect(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
			animationView.post(new Runnable() {
				@Override
				public void run() {
					animationView.start(timestampView, bounds);
				}
			});
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		animationView.stop();
		// save bounds to persistent storage
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = sharedPreferences.edit();
		Rect bounds = animationView.getBounds();
		// bounds String format is left:top:right:bottom
		editor.putString("PREF_ANIM_BOUNDS_ORIENTATION_" + orientation, "" + bounds.left + ":" + bounds.top + ":" + bounds.right + ":" + bounds.bottom);
		editor.commit();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.previous_button:
			animationView.previous();
			break;
		case R.id.playpause_button:
			play = !play;
			animationView.playpause();
			if(play)
				playpauseButton.setImageResource(R.drawable.ic_media_play);
			else
				playpauseButton.setImageResource(R.drawable.ic_media_pause);
			break;
		case R.id.forward_button:
			animationView.forward();
			break;
		default:
			return;
		}
	}

}
