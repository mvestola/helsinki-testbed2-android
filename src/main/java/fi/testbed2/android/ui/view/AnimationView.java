package fi.testbed2.android.ui.view;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.*;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EView;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jhlabs.map.Point2D;
import fi.testbed2.R;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.domain.Municipality;
import fi.testbed2.domain.TestbedMapImage;
import fi.testbed2.service.BitmapService;
import fi.testbed2.service.LocationService;
import fi.testbed2.service.PageService;
import fi.testbed2.service.SettingsService;
import fi.testbed2.util.SeekBarUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * View which shows the map animation.
 */
@EView
public class AnimationView extends View {

    /**
     * Used for bounds calculation
     */
    private static final float GESTURE_THRESHOLD_DIP = 16.0f;

    @Getter
    private List<Municipality> municipalities;

    @Setter
    private boolean allImagesDownloaded;

    @Getter
    private AnimationViewPlayer player;

    // Utils
    @Bean @Getter
    AnimationViewCanvasUtil canvasUtil;
    @Bean @Getter
    AnimationViewScaleAndGestureUtil scaleAndGestureUtil;

    // Scaling related
    private ScaleGestureDetector scaleDetector;
    private GestureDetector gestureDetector;
    @Getter @Setter
    private MapScaleInfo scaleInfo = new MapScaleInfo();
    private boolean doNotMoveMap;

    // Animation frame related
    private int frameWidth;
    private int frameHeight;

    // Bounds calculation related
    @Getter
    private Rect bounds;
    private float boundsStartY;
    private float boundsStartX;
    private float boundsDistanceY;
	private float boundsDistanceX;
	private boolean boundsMoveMap;

    // Views and texts
    @ViewById(R.id.timestamp_view)
    TextView timestampView;

    @ViewById(R.id.seek)
    SeekBar seekBar;

    @Setter
    private String downloadProgressText;


    // Services

    @Inject
    public LocationService userLocationService;

    @Inject
    public BitmapService bitmapService;

    @Inject
    public PageService pageService;

    @Inject
    public SettingsService settingsService;

    /**
     * All three constructors are needed!!
     */

	public AnimationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AnimationView(Context context) {
		super(context);
	}

    public void initView(Context context) {

        Logger.debug("Initializing AnimationView");

        player = new AnimationViewPlayer(this);
        scaleAndGestureUtil.setView(this);

        gestureDetector = new GestureDetector(context, scaleAndGestureUtil.getGestureListener());
        scaleDetector = new ScaleGestureDetector(context, scaleAndGestureUtil.getScaleListener());

        BitmapDrawable firstMap = new BitmapDrawable(bitmapService.getBitmap(getMapImagesToBeDrawn().get(0)));

        frameWidth = firstMap.getMinimumWidth();
        frameHeight = firstMap.getMinimumHeight();

        player.setFrameDelay(settingsService.getSavedFrameDelay());
        player.setCurrentFrame(0);
        player.setFrames(getMapImagesToBeDrawn().size() - 1);

    }

    public void startAnimation(TextView timestampView, SeekBar seekBar, Rect bounds, MapScaleInfo scaleInfo) {
        Logger.debug("Start animation");
        player.play();
        this.scaleInfo = scaleInfo;
        this.timestampView = timestampView;
        this.seekBar = seekBar;
        if (bounds==null) {
            initializeBounds();
        } else {
            this.bounds = bounds;
        }
		next();
	}

    public void next() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if(player.isPlaying()) {
                    player.goToNextFrame();
                }
            }
        }, player.getFrameDelay());
    }

    private void initializeBounds() {

        // scale bounds
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();

        Logger.debug("Initializing bounds...");
        Logger.debug("measuredHeight: " + measuredHeight);
        Logger.debug("measuredWidth: " + measuredWidth);

        if(measuredHeight > measuredWidth) {
            int scaleBy = measuredHeight-frameHeight;
            if(scaleBy > 0)
                bounds = new Rect(0, 0, frameWidth + scaleBy, frameHeight + scaleBy);
            else
                bounds = new Rect(0, 0, frameWidth, frameHeight);
        } else {
            int scaleBy = measuredWidth-frameWidth;
            if(scaleBy > 0)
                bounds = new Rect(0, 0, frameWidth + scaleBy, frameHeight + scaleBy);
            else
                bounds = new Rect(0, 0, frameWidth, frameHeight);
        }
    }

    private List<TestbedMapImage> getMapImagesToBeDrawn() {

        if (allImagesDownloaded) {
            return pageService.getTestbedParsedPage().getAllTestbedImages();
        } else {
            List<TestbedMapImage> list = new ArrayList<TestbedMapImage>();
            list.add(pageService.getTestbedParsedPage().getLatestTestbedImage());
            return list;
        }

    }

    @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

        canvas.save();
        canvas.scale(scaleInfo.getScaleFactor(), scaleInfo.getScaleFactor(),
                scaleInfo.getPivotX(), scaleInfo.getPivotY());

        TestbedMapImage currentMap = getMapImagesToBeDrawn().get(player.getCurrentFrame());

        updateTimestampView(currentMap);
        updateSeekBar();

        BitmapDrawable frame = new BitmapDrawable(bitmapService.getBitmap(currentMap));
        frame.setBounds(bounds);
        frame.draw(canvas);

        canvasUtil.drawMunicipalities(canvas, bounds, municipalities);
        canvasUtil.drawUserLocation(canvas, bounds);

        canvas.restore();

    }

    private void updateTimestampView(TestbedMapImage currentMap) {

        String timestamp = currentMap.getLocalTimestamp();
        String text = String.format("%1$02d/%2$02d @ ",
                player.getCurrentFrame() + 1 , player.getFrames() + 1) + timestamp;

        if (downloadProgressText!=null) {
            text="@ "+timestamp+"  "+downloadProgressText;
        }

        timestampView.setText(text);
        timestampView.invalidate();

    }

    private void updateSeekBar() {
        seekBar.setProgress(SeekBarUtil.getSeekBarValueFromFrameNumber(player.getCurrentFrame(),
                pageService.getTestbedParsedPage().getAllTestbedImages().size()));
    }


	@Override
	public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);
        scaleDetector.onTouchEvent(event);
        calculateNewBounds(event);

        return true;
	}


    /**
     * This method is quite a mess. It is hard to understand how the bounds
     * are calculated. Do not touch this if you don't know what you are doing.
     *
     * @param event
     * @return
     */
    private boolean calculateNewBounds(MotionEvent event) {

        int action = event.getAction();
        boolean isMultiTouchEvent = event.getPointerCount()>1;

        // Do not move the map in multi-touch
        if (isMultiTouchEvent) {
            doNotMoveMap = true;
        }

        // Allow map movement again when new non-multi-touch down event occurs
        if (!isMultiTouchEvent && action==MotionEvent.ACTION_DOWN) {
            doNotMoveMap = false;
        }

        if (doNotMoveMap) {
            return true;
        }

        switch(action) {
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                boundsMoveMap = false;
                break;
            case MotionEvent.ACTION_DOWN:
                boundsStartY = event.getY();
                boundsDistanceY = 0.0f;
                boundsStartX = event.getX();
                boundsDistanceX = 0.0f;
                break;
            case MotionEvent.ACTION_MOVE:
                boundsDistanceY = -(boundsStartY - event.getY());
                boundsDistanceX = -(boundsStartX - event.getX());

                Rect savedBounds = new Rect(bounds);

                Logger.debug("Calculating new bounds...");

                // Convert the dips to pixels
                final float scale = getContext().getResources().getDisplayMetrics().density;
                int mGestureThreshold = (int) (GESTURE_THRESHOLD_DIP * scale / scaleInfo.getScaleFactor() + 0.5f);

                if(!boundsMoveMap && (Math.abs(boundsDistanceY) > mGestureThreshold || Math.abs(boundsDistanceX) > mGestureThreshold)) {
                    boundsMoveMap = true;
                }

                if(boundsMoveMap) {

                    scaleAndGestureUtil.hideMunicipalityToast();

                    float mDistance_y_dip = boundsDistanceY * scale / scaleInfo.getScaleFactor() + 0.5f;
                    float mDistance_x_dip = boundsDistanceX * scale / scaleInfo.getScaleFactor() + 0.5f;

                    bounds.offset((int)mDistance_x_dip, (int)mDistance_y_dip);

                    Logger.debug("New bounds left: " + bounds.left);
                    Logger.debug("New bounds top: " + bounds.top);
                    Logger.debug("New bounds right: " + bounds.right);
                    Logger.debug("New bounds bottom: " + bounds.bottom);

                    // restart measuring distances
                    boundsStartY = event.getY();
                    boundsDistanceY = 0.0f;
                    boundsStartX = event.getX();
                    boundsDistanceX = 0.0f;

                    invalidate();
                }


                break;
            default:
                return super.onTouchEvent(event);

        }

        return true;
    }



    /*
    * ============
    * Setters and getters
    * ============
    */

    public void updateBounds(Rect bounds) {
        if (bounds==null) {
            initializeBounds();
        } else {
            this.bounds = bounds;
        }
    }

    public void setMunicipalities(List<Municipality> municipalities) {
        this.municipalities = municipalities;
        canvasUtil.setMunicipalitiesOnScreen(new HashMap<Municipality, Point2D.Double>());
    }

    @AfterInject
    void injectRoboGuiceDependencies() {
        MainApplication.getApplication().getInjector().injectMembers(this);
    }

}
